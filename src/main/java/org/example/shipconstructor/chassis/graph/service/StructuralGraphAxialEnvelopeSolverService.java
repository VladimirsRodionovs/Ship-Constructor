package org.example.shipconstructor.chassis.graph.service;

import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoad;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMember;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMemberEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphNode;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanel;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanelEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphSolveResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralMemberRole;
import org.example.shipconstructor.chassis.graph.domain.StructuralNodeType;
import org.example.shipconstructor.chassis.graph.domain.StructuralPanelRole;
import org.example.shipconstructor.chassis.graph.domain.StructuralPanelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructuralGraphAxialEnvelopeSolverService {

    public StructuralGraphSolveResult solve(StructuralGraph graph, StructuralGraphLoadSet loadSet) {
        return solve(null, graph, loadSet);
    }

    public StructuralGraphSolveResult solve(ChassisCalculationInput input, StructuralGraph graph, StructuralGraphLoadSet loadSet) {
        if (graph == null) {
            throw new IllegalArgumentException("graph is required");
        }
        if (loadSet == null) {
            throw new IllegalArgumentException("loadSet is required");
        }

        LoadShareProfile share = resolveLoadShareProfile(input);

        Map<String, StructuralGraphNode> nodeById = new HashMap<String, StructuralGraphNode>();
        for (StructuralGraphNode n : graph.getNodes()) {
            nodeById.put(n.getId(), n);
        }

        List<StructuralGraphMemberEnvelope> memberEnvelopes = new ArrayList<StructuralGraphMemberEnvelope>();
        List<StructuralGraphPanelEnvelope> panelEnvelopes = new ArrayList<StructuralGraphPanelEnvelope>();
        List<String> warnings = new ArrayList<String>();

        double graphSpan = Math.max(graph.getLengthMeters(), Math.max(graph.getWidthMeters(), graph.getHeightMeters()));
        if (graphSpan <= 1e-9d) {
            warnings.add("graph has non-positive span; solver indices may be invalid");
            graphSpan = 1.0d;
        }

        for (StructuralGraphMember m : graph.getMembers()) {
            StructuralGraphNode a = nodeById.get(m.getNodeAId());
            StructuralGraphNode b = nodeById.get(m.getNodeBId());
            if (a == null || b == null) {
                warnings.add("member references missing node: " + m.getId());
                continue;
            }

            Vector3 pa = a.getPositionMeters();
            Vector3 pb = b.getPositionMeters();
            Vector3 axis = new Vector3(pb.getX() - pa.getX(), pb.getY() - pa.getY(), pb.getZ() - pa.getZ());
            double len = magnitude(axis);
            if (len <= 1e-9d) {
                warnings.add("zero-length member: " + m.getId());
                continue;
            }
            Vector3 axisUnit = new Vector3(axis.getX() / len, axis.getY() / len, axis.getZ() / len);
            double longitudinalAlignment = Math.abs(axisUnit.getX());

            double peakTension = 0.0d;
            double peakCompression = 0.0d;
            double peakAbs = 0.0d;
            String governingLoadName = null;
            String governingLoadMode = null;

            for (StructuralGraphLoad load : loadSet.getLoads()) {
                if (load == null || load.getDirectionUnit() == null) {
                    continue;
                }
                Vector3 loadDir = normalize(load.getDirectionUnit());
                double axialProjection = dot(axisUnit, loadDir);
                if (Math.abs(axialProjection) < 1e-6d) {
                    continue;
                }

                double targetWeight = computeTargetWeight(load, m, a, b);
                if (targetWeight <= 0.0d) {
                    continue;
                }
                double roleWeight = roleParticipationWeight(m.getRole(), load);
                double durationWeight = Math.max(0.3d, Math.min(2.5d, Math.sqrt(Math.max(0.01d, load.getDurationSec())) / 5.0d));
                double criticalWeight = load.isCertificationCritical() ? 1.15d : 1.0d;
                double lengthLeverage = 0.75d + 0.5d * Math.min(1.0d, len / graphSpan);
                double loadModeWeight = loadModeInteractionWeight(m.getRole(), load.getPrimaryLoadMode(), longitudinalAlignment);

                double signedIndex = load.getMagnitudeG()
                        * axialProjection
                        * targetWeight
                        * roleWeight
                        * durationWeight
                        * criticalWeight
                        * lengthLeverage
                        * loadModeWeight
                        * share.memberShare;

                if (signedIndex > 0.0d) {
                    peakTension = Math.max(peakTension, signedIndex);
                } else {
                    peakCompression = Math.max(peakCompression, -signedIndex);
                }
                double absIndex = Math.abs(signedIndex);
                if (absIndex > peakAbs) {
                    peakAbs = absIndex;
                    governingLoadName = load.getName();
                    governingLoadMode = load.getPrimaryLoadMode();
                }
            }

            memberEnvelopes.add(new StructuralGraphMemberEnvelope(
                    m.getId(),
                    m.getRole(),
                    len,
                    longitudinalAlignment,
                    peakTension,
                    peakCompression,
                    peakAbs,
                    governingLoadName,
                    governingLoadMode
            ));
        }

        for (StructuralGraphPanel p : graph.getPanels()) {
            double areaProxy = estimatePanelAreaProxy(p, nodeById);
            if (areaProxy <= 1e-9d) {
                warnings.add("panel has near-zero area proxy: " + p.getId());
                continue;
            }

            double peakShear = 0.0d;
            double peakNormal = 0.0d;
            double peakAbs = 0.0d;
            String governingLoadName = null;
            String governingLoadMode = null;
            double panelNormalLong = estimatePanelLongitudinalNormalAlignment(p, nodeById);

            for (StructuralGraphLoad load : loadSet.getLoads()) {
                if (load == null || load.getDirectionUnit() == null) {
                    continue;
                }
                Vector3 dir = normalize(load.getDirectionUnit());
                double shearProxy = Math.sqrt(dir.getY() * dir.getY() + dir.getZ() * dir.getZ());
                double normalProxy = Math.abs(dir.getX()) * panelNormalLong;

                double targetWeight = computePanelTargetWeight(load, p, nodeById);
                if (targetWeight <= 0.0d) {
                    continue;
                }
                double roleWeight = panelRoleParticipationWeight(p.getRole(), p.getType(), load);
                double durationWeight = Math.max(0.3d, Math.min(2.5d, Math.sqrt(Math.max(0.01d, load.getDurationSec())) / 5.0d));
                double criticalWeight = load.isCertificationCritical() ? 1.15d : 1.0d;
                double areaScale = 0.8d + 0.4d * Math.min(1.0d, Math.sqrt(areaProxy) / Math.max(1.0d, graphSpan));
                double modeWeight = panelLoadModeWeight(p.getRole(), load.getPrimaryLoadMode());

                double shearIndex = load.getMagnitudeG() * shearProxy * targetWeight * roleWeight * durationWeight * criticalWeight * areaScale * modeWeight * share.panelShare;
                double normalIndex = load.getMagnitudeG() * normalProxy * targetWeight * roleWeight * durationWeight * criticalWeight * areaScale * modeWeight * share.panelShare;
                peakShear = Math.max(peakShear, shearIndex);
                peakNormal = Math.max(peakNormal, normalIndex);
                double combined = Math.max(shearIndex, normalIndex);
                if (combined > peakAbs) {
                    peakAbs = combined;
                    governingLoadName = load.getName();
                    governingLoadMode = load.getPrimaryLoadMode();
                }
            }

            panelEnvelopes.add(new StructuralGraphPanelEnvelope(
                    p.getId(),
                    p.getRole(),
                    areaProxy,
                    peakShear,
                    peakNormal,
                    peakAbs,
                    governingLoadName,
                    governingLoadMode
            ));
        }

        return new StructuralGraphSolveResult(memberEnvelopes, panelEnvelopes, share.memberShare, share.panelShare, share.label, warnings);
    }

    private LoadShareProfile resolveLoadShareProfile(ChassisCalculationInput input) {
        String structureProfileCode = input == null ? null : input.getStructureProfileCode();
        if (structureProfileCode != null && !structureProfileCode.trim().isEmpty()) {
            StructureTypeLoadSharingCatalog.LoadSharingProfile p = StructureTypeLoadSharingCatalog.resolveByProfileKey(structureProfileCode);
            return new LoadShareProfile(p.getMemberShare(), p.getPanelShare(), p.getLabel());
        }
        EngineeringStackSelection stack = input == null ? null : input.getEngineeringStack();
        Long structureTypeId = stack == null ? null : stack.getStructureTypeId();
        StructureTypeLoadSharingCatalog.LoadSharingProfile p = StructureTypeLoadSharingCatalog.resolve(structureTypeId);
        return new LoadShareProfile(p.getMemberShare(), p.getPanelShare(), p.getLabel());
    }

    private double computeTargetWeight(StructuralGraphLoad load, StructuralGraphMember member, StructuralGraphNode a, StructuralGraphNode b) {
        switch (load.getTargetType()) {
            case WHOLE_GRAPH:
                return 0.35d;
            case NODE:
                if (load.getTargetRef() == null) {
                    return 0.0d;
                }
                if (load.getTargetRef().equals(member.getNodeAId()) || load.getTargetRef().equals(member.getNodeBId())) {
                    return 1.0d;
                }
                return 0.05d;
            case NODE_SET_BY_TYPE:
                if (load.getTargetRef() == null) {
                    return 0.0d;
                }
                StructuralNodeType type = parseNodeType(load.getTargetRef());
                if (type == null) {
                    return 0.0d;
                }
                boolean aMatch = a.getType() == type;
                boolean bMatch = b.getType() == type;
                if (aMatch && bMatch) {
                    return 1.15d;
                }
                if (aMatch || bMatch) {
                    return 0.95d;
                }
                return 0.15d;
            case NODE_SET_BY_PREFIX:
                if (load.getTargetRef() == null) {
                    return 0.0d;
                }
                boolean ap = member.getNodeAId().startsWith(load.getTargetRef());
                boolean bp = member.getNodeBId().startsWith(load.getTargetRef());
                if (ap && bp) {
                    return 1.0d;
                }
                if (ap || bp) {
                    return 0.8d;
                }
                return 0.1d;
            default:
                return 0.1d;
        }
    }

    private double computePanelTargetWeight(StructuralGraphLoad load, StructuralGraphPanel panel, Map<String, StructuralGraphNode> nodeById) {
        switch (load.getTargetType()) {
            case WHOLE_GRAPH:
                return 0.45d;
            case NODE:
                return panelContainsNode(panel, load.getTargetRef()) ? 0.75d : 0.05d;
            case NODE_SET_BY_TYPE:
                StructuralNodeType type = parseNodeType(load.getTargetRef());
                if (type == null) {
                    return 0.0d;
                }
                int matches = 0;
                for (String id : panel.getBoundaryNodeIds()) {
                    StructuralGraphNode n = nodeById.get(id);
                    if (n != null && n.getType() == type) {
                        matches++;
                    }
                }
                if (matches <= 0) {
                    return 0.10d;
                }
                return Math.min(1.0d, 0.35d + 0.20d * matches);
            case NODE_SET_BY_PREFIX:
                if (load.getTargetRef() == null) {
                    return 0.0d;
                }
                for (String id : panel.getBoundaryNodeIds()) {
                    if (id != null && id.startsWith(load.getTargetRef())) {
                        return 0.75d;
                    }
                }
                return 0.10d;
            default:
                return 0.1d;
        }
    }

    private boolean panelContainsNode(StructuralGraphPanel panel, String nodeId) {
        if (panel == null || nodeId == null) {
            return false;
        }
        for (String id : panel.getBoundaryNodeIds()) {
            if (nodeId.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private StructuralNodeType parseNodeType(String ref) {
        try {
            return ref == null ? null : StructuralNodeType.valueOf(ref);
        } catch (Exception ignored) {
            return null;
        }
    }

    private double roleParticipationWeight(StructuralMemberRole role, StructuralGraphLoad load) {
        if (role == null) {
            return 0.6d;
        }
        switch (role) {
            case PRIMARY_LOAD_PATH:
                return 1.0d;
            case SECONDARY_STIFFENING:
                if (load.getType() == null) {
                    return 0.75d;
                }
                switch (load.getType()) {
                    case BODY_INERTIAL:
                    case ENGINE_DIFFERENTIAL_CONTROL:
                        return 0.75d;
                    case LANDING_REACTION:
                    case GROUND_SUPPORT_REACTION:
                        return 0.65d;
                    default:
                        return 0.70d;
                }
            case LOCAL_LOAD_TRANSFER:
                if (load.getType() == null) {
                    return 0.9d;
                }
                switch (load.getType()) {
                    case THRUST_APPLICATION:
                    case LANDING_REACTION:
                    case GROUND_SUPPORT_REACTION:
                        return 1.10d;
                    default:
                        return 0.55d;
                }
            default:
                return 0.75d;
        }
    }

    private double panelRoleParticipationWeight(StructuralPanelRole role, StructuralPanelType type, StructuralGraphLoad load) {
        double base;
        if (role == null) {
            base = 0.6d;
        } else {
            switch (role) {
                case PRIMARY_SHEAR_CLOSURE:
                    base = 1.0d;
                    break;
                case SECONDARY_CLOSURE:
                    base = 0.75d;
                    break;
                case LOCAL_PARTITION:
                    base = 0.45d;
                    break;
                default:
                    base = 0.6d;
            }
        }

        if (load != null && load.getType() != null) {
            switch (load.getType()) {
                case BODY_INERTIAL:
                case ENGINE_DIFFERENTIAL_CONTROL:
                    base *= 1.0d;
                    break;
                case LANDING_REACTION:
                case GROUND_SUPPORT_REACTION:
                    base *= (type == StructuralPanelType.BOTTOM_PANEL || type == StructuralPanelType.BULKHEAD) ? 1.15d : 0.85d;
                    break;
                case THRUST_APPLICATION:
                    base *= (type == StructuralPanelType.BULKHEAD) ? 1.10d : 0.75d;
                    break;
                default:
                    break;
            }
        }
        return base;
    }

    private double loadModeInteractionWeight(StructuralMemberRole role, String primaryLoadMode, double longitudinalAlignment) {
        String mode = primaryLoadMode == null ? "" : primaryLoadMode.trim().toLowerCase(java.util.Locale.US);
        if (mode.isEmpty()) {
            return 1.0d;
        }
        if ("compression".equals(mode) || "tension".equals(mode)) {
            return 0.8d + 0.4d * longitudinalAlignment;
        }
        if ("bending".equals(mode)) {
            if (role == StructuralMemberRole.SECONDARY_STIFFENING) {
                return 1.15d - 0.20d * longitudinalAlignment;
            }
            return 1.05d - 0.15d * longitudinalAlignment;
        }
        if ("mixed".equals(mode)) {
            return 0.95d;
        }
        if ("thrust".equals(mode)) {
            if (role == StructuralMemberRole.LOCAL_LOAD_TRANSFER) {
                return 1.15d;
            }
            return 1.0d;
        }
        return 1.0d;
    }

    private double panelLoadModeWeight(StructuralPanelRole role, String primaryLoadMode) {
        String mode = primaryLoadMode == null ? "" : primaryLoadMode.trim().toLowerCase(java.util.Locale.US);
        if (mode.isEmpty()) {
            return 1.0d;
        }
        if ("bending".equals(mode) || "mixed".equals(mode)) {
            return role == StructuralPanelRole.PRIMARY_SHEAR_CLOSURE ? 1.15d : 1.0d;
        }
        if ("compression".equals(mode) || "tension".equals(mode)) {
            return 0.85d;
        }
        if ("thrust".equals(mode)) {
            return 0.80d;
        }
        return 1.0d;
    }

    private double estimatePanelAreaProxy(StructuralGraphPanel panel, Map<String, StructuralGraphNode> nodeById) {
        if (panel == null || panel.getBoundaryNodeIds().size() < 3) {
            return 0.0d;
        }
        Vector3 a = nodePos(nodeById, panel.getBoundaryNodeIds().get(0));
        Vector3 b = nodePos(nodeById, panel.getBoundaryNodeIds().get(1));
        Vector3 c = nodePos(nodeById, panel.getBoundaryNodeIds().get(2));
        if (a == null || b == null || c == null) {
            return 0.0d;
        }
        Vector3 ab = new Vector3(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ());
        Vector3 ac = new Vector3(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ());
        Vector3 cross = cross(ab, ac);
        double area1 = 0.5d * magnitude(cross);
        if (panel.getBoundaryNodeIds().size() < 4) {
            return area1;
        }
        Vector3 d = nodePos(nodeById, panel.getBoundaryNodeIds().get(3));
        if (d == null) {
            return area1;
        }
        Vector3 ad = new Vector3(d.getX() - a.getX(), d.getY() - a.getY(), d.getZ() - a.getZ());
        double area2 = 0.5d * magnitude(cross(ac, ad));
        return area1 + area2;
    }

    private double estimatePanelLongitudinalNormalAlignment(StructuralGraphPanel panel, Map<String, StructuralGraphNode> nodeById) {
        if (panel == null || panel.getBoundaryNodeIds().size() < 3) {
            return 0.5d;
        }
        Vector3 a = nodePos(nodeById, panel.getBoundaryNodeIds().get(0));
        Vector3 b = nodePos(nodeById, panel.getBoundaryNodeIds().get(1));
        Vector3 c = nodePos(nodeById, panel.getBoundaryNodeIds().get(2));
        if (a == null || b == null || c == null) {
            return 0.5d;
        }
        Vector3 n = cross(new Vector3(b.getX() - a.getX(), b.getY() - a.getY(), b.getZ() - a.getZ()),
                new Vector3(c.getX() - a.getX(), c.getY() - a.getY(), c.getZ() - a.getZ()));
        double m = magnitude(n);
        if (m <= 1e-9d) {
            return 0.5d;
        }
        return Math.abs(n.getX() / m);
    }

    private Vector3 nodePos(Map<String, StructuralGraphNode> nodeById, String id) {
        StructuralGraphNode n = nodeById.get(id);
        return n == null ? null : n.getPositionMeters();
    }

    private Vector3 normalize(Vector3 v) {
        double m = magnitude(v);
        if (m <= 1e-9d) {
            return new Vector3(1.0d, 0.0d, 0.0d);
        }
        return new Vector3(v.getX() / m, v.getY() / m, v.getZ() / m);
    }

    private double magnitude(Vector3 v) {
        return Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
    }

    private double dot(Vector3 a, Vector3 b) {
        return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
    }

    private Vector3 cross(Vector3 a, Vector3 b) {
        return new Vector3(
                a.getY() * b.getZ() - a.getZ() * b.getY(),
                a.getZ() * b.getX() - a.getX() * b.getZ(),
                a.getX() * b.getY() - a.getY() * b.getX()
        );
    }

    private static final class LoadShareProfile {
        private final double memberShare;
        private final double panelShare;
        @SuppressWarnings("unused")
        private final String label;

        private LoadShareProfile(double memberShare, double panelShare, String label) {
            this.memberShare = memberShare;
            this.panelShare = panelShare;
            this.label = label;
        }
    }
}
