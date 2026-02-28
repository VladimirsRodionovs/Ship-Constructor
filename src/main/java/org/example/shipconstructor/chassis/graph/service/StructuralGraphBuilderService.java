package org.example.shipconstructor.chassis.graph.service;

import org.example.shipconstructor.chassis.domain.CapabilityClassSet;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.CubeDimensions;
import org.example.shipconstructor.chassis.domain.EngineLayerConfig;
import org.example.shipconstructor.chassis.domain.EngineLayerPattern;
import org.example.shipconstructor.chassis.domain.EngineLayerSymmetryMode;
import org.example.shipconstructor.chassis.domain.EngineLayoutConfig;
import org.example.shipconstructor.chassis.domain.MassDistributionModel;
import org.example.shipconstructor.chassis.domain.MassNode;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.domain.StructuralOptimizationMode;
import org.example.shipconstructor.chassis.domain.ThrustLayout;
import org.example.shipconstructor.chassis.domain.ThrustNode;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphBuildResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMember;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphNode;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanel;
import org.example.shipconstructor.chassis.graph.domain.StructuralMemberType;
import org.example.shipconstructor.chassis.graph.domain.StructuralMemberRole;
import org.example.shipconstructor.chassis.graph.domain.StructuralNodeType;
import org.example.shipconstructor.chassis.graph.domain.StructuralPanelType;
import org.example.shipconstructor.chassis.graph.domain.StructuralPanelRole;
import org.example.shipconstructor.ui.SizeTypeCatalog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StructuralGraphBuilderService {
    private static final double HULL_FACE_EPS = 1e-6d;

    public StructuralGraphBuildResult build(ChassisCalculationInput input) {
        if (input == null) {
            throw new IllegalArgumentException("input is required");
        }
        if (input.getSizeDimensions() == null) {
            throw new IllegalArgumentException("sizeDimensions is required");
        }
        if (!SizeTypeCatalog.values().containsKey(Integer.valueOf(input.getSizeType()))) {
            throw new IllegalArgumentException("unknown sizeType: " + input.getSizeType());
        }

        double cubeEdge = SizeTypeCatalog.values().get(Integer.valueOf(input.getSizeType())).getCubeEdgeMeters();
        CubeDimensions d = input.getSizeDimensions();
        double length = cubeEdge * d.getX();
        double width = cubeEdge * d.getY();
        double height = cubeEdge * d.getZ();

        List<String> warnings = new ArrayList<String>();
        if (input.getSizeTotal() > d.getBoundingCubeCount()) {
            warnings.add("sizeTotal exceeds bounding volume cubes; structural graph uses bounding dimensions only");
        }

        List<StructuralGraphNode> nodes = new ArrayList<StructuralGraphNode>();
        List<StructuralGraphMember> members = new ArrayList<StructuralGraphMember>();
        List<StructuralGraphPanel> panels = new ArrayList<StructuralGraphPanel>();
        Map<String, StructuralGraphNode> nodeById = new LinkedHashMap<String, StructuralGraphNode>();

        int stations = estimateStationCount(d);
        StructuralOptimizationMode optimizationMode = resolveOptimizationMode(input);
        BracingProfile bracingProfile = resolveBracingProfile(input, optimizationMode);
        buildFrameStations(length, width, height, stations, bracingProfile, nodes, nodeById, members, panels);
        applyOptimizationPruning(members, optimizationMode, warnings);
        addBulkheads(stations, nodeById, panels);
        addMassAnchors(input.getMassDistributionModel(), length, width, height, stations, nodes, nodeById, members, warnings);
        addEngineMounts(input, length, width, height, stations, nodes, nodeById, members, warnings);
        addLandingPoints(input, length, width, height, stations, nodes, nodeById, members);

        StructuralGraph graph = new StructuralGraph(cubeEdge, length, width, height, nodes, members, panels);
        IntrusionMetrics intrusion = estimateIntrusionMetrics(graph, nodeById);
        if (intrusion.intrusiveMemberLengthFraction > 0.35d) {
            warnings.add("high internal structural intrusion: long internal members may obstruct habitable/module volume");
        }
        return new StructuralGraphBuildResult(
                graph,
                warnings,
                intrusion.intrusiveMemberLengthMeters,
                intrusion.totalMemberLengthMeters,
                intrusion.estimatedInternalStructureIntrusionVolumeM3);
    }

    private int estimateStationCount(CubeDimensions d) {
        int byLength = Math.max(3, (int) Math.ceil(d.getX() / 3.0d) + 1);
        return Math.min(9, byLength);
    }

    private void buildFrameStations(
            double length,
            double width,
            double height,
            int stations,
            BracingProfile bracingProfile,
            List<StructuralGraphNode> nodes,
            Map<String, StructuralGraphNode> nodeById,
            List<StructuralGraphMember> members,
            List<StructuralGraphPanel> panels) {
        double x0 = -0.5d * length;
        double dx = stations <= 1 ? 0.0d : length / (stations - 1);
        double yP = -0.5d * width;
        double yS = 0.5d * width;
        double zB = -0.5d * height;
        double zT = 0.5d * height;

        for (int i = 0; i < stations; i++) {
            double x = x0 + i * dx;
            addNode(nodes, nodeById, stationCornerId(i, "B", "P"), "Station " + i + " bottom-port", StructuralNodeType.FRAME_CORNER, new Vector3(x, yP, zB));
            addNode(nodes, nodeById, stationCornerId(i, "B", "S"), "Station " + i + " bottom-starboard", StructuralNodeType.FRAME_CORNER, new Vector3(x, yS, zB));
            addNode(nodes, nodeById, stationCornerId(i, "T", "P"), "Station " + i + " top-port", StructuralNodeType.FRAME_CORNER, new Vector3(x, yP, zT));
            addNode(nodes, nodeById, stationCornerId(i, "T", "S"), "Station " + i + " top-starboard", StructuralNodeType.FRAME_CORNER, new Vector3(x, yS, zT));
            addNode(nodes, nodeById, stationCenterId(i), "Station " + i + " center", StructuralNodeType.FRAME_MIDPOINT, new Vector3(x, 0.0d, 0.0d));

            addMember(members, "MRING-" + i + "-BOTTOM", "Bottom transverse ring " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCornerId(i, "B", "P"), stationCornerId(i, "B", "S"));
            addMember(members, "MRING-" + i + "-TOP", "Top transverse ring " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCornerId(i, "T", "P"), stationCornerId(i, "T", "S"));
            addMember(members, "MRING-" + i + "-PORT", "Port vertical ring " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCornerId(i, "B", "P"), stationCornerId(i, "T", "P"));
            addMember(members, "MRING-" + i + "-STAR", "Starboard vertical ring " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCornerId(i, "B", "S"), stationCornerId(i, "T", "S"));

            if (bracingProfile.addTransverseCrossBraces && i > 0 && i < stations - 1) {
                addMember(members, "MBRACE-" + i + "-FACE1", "Ring diagonal brace face1 " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "P"), stationCornerId(i, "T", "S"));
                addMember(members, "MBRACE-" + i + "-FACE2", "Ring diagonal brace face2 " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "S"), stationCornerId(i, "T", "P"));
            }

            addMember(members, "MCENTER-" + i + "-BP", "Center strut BP " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCenterId(i), stationCornerId(i, "B", "P"));
            addMember(members, "MCENTER-" + i + "-BS", "Center strut BS " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCenterId(i), stationCornerId(i, "B", "S"));
            addMember(members, "MCENTER-" + i + "-TP", "Center strut TP " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCenterId(i), stationCornerId(i, "T", "P"));
            addMember(members, "MCENTER-" + i + "-TS", "Center strut TS " + i, StructuralMemberType.TRANSVERSE_RING,
                    stationCenterId(i), stationCornerId(i, "T", "S"));
        }

        for (int i = 0; i < stations - 1; i++) {
            for (String[] pos : Arrays.asList(
                    new String[]{"B", "P"},
                    new String[]{"B", "S"},
                    new String[]{"T", "P"},
                    new String[]{"T", "S"})) {
                addMember(members, "MLONG-" + i + "-" + pos[0] + pos[1], "Longitudinal rail " + i + " " + pos[0] + pos[1], StructuralMemberType.LONGITUDINAL_RAIL,
                        stationCornerId(i, pos[0], pos[1]), stationCornerId(i + 1, pos[0], pos[1]));
            }
            addMember(members, "MSPINE-" + i, "Center spine " + i, StructuralMemberType.CENTER_SPINE,
                    stationCenterId(i), stationCenterId(i + 1));

            boolean addLongitudinalAtStation = (i % bracingProfile.longitudinalBraceStep) == 0;
            if (bracingProfile.addSideLongitudinalDiagonals && addLongitudinalAtStation) {
                addMember(members, "MBRACE-LONG-PORT-A-" + i, "Port longitudinal diagonal brace A " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "P"), stationCornerId(i + 1, "T", "P"));
                addMember(members, "MBRACE-LONG-PORT-B-" + i, "Port longitudinal diagonal brace B " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "T", "P"), stationCornerId(i + 1, "B", "P"));
                addMember(members, "MBRACE-LONG-STAR-A-" + i, "Starboard longitudinal diagonal brace A " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "S"), stationCornerId(i + 1, "T", "S"));
                addMember(members, "MBRACE-LONG-STAR-B-" + i, "Starboard longitudinal diagonal brace B " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "T", "S"), stationCornerId(i + 1, "B", "S"));
            }
            if (bracingProfile.addTopBottomLongitudinalDiagonals && addLongitudinalAtStation) {
                addMember(members, "MBRACE-LONG-TOP-A-" + i, "Top longitudinal diagonal brace A " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "T", "P"), stationCornerId(i + 1, "T", "S"));
                addMember(members, "MBRACE-LONG-TOP-B-" + i, "Top longitudinal diagonal brace B " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "T", "S"), stationCornerId(i + 1, "T", "P"));
                addMember(members, "MBRACE-LONG-BOT-A-" + i, "Bottom longitudinal diagonal brace A " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "P"), stationCornerId(i + 1, "B", "S"));
                addMember(members, "MBRACE-LONG-BOT-B-" + i, "Bottom longitudinal diagonal brace B " + i, StructuralMemberType.DIAGONAL_BRACE,
                        stationCornerId(i, "B", "S"), stationCornerId(i + 1, "B", "P"));
            }

            addPanel(panels, "PTOP-" + i, "Top panel bay " + i, StructuralPanelType.TOP_PANEL,
                    stationCornerId(i, "T", "P"), stationCornerId(i, "T", "S"), stationCornerId(i + 1, "T", "S"), stationCornerId(i + 1, "T", "P"));
            addPanel(panels, "PBOT-" + i, "Bottom panel bay " + i, StructuralPanelType.BOTTOM_PANEL,
                    stationCornerId(i, "B", "P"), stationCornerId(i, "B", "S"), stationCornerId(i + 1, "B", "S"), stationCornerId(i + 1, "B", "P"));
            addPanel(panels, "PPORT-" + i, "Port side panel bay " + i, StructuralPanelType.PORT_PANEL,
                    stationCornerId(i, "B", "P"), stationCornerId(i, "T", "P"), stationCornerId(i + 1, "T", "P"), stationCornerId(i + 1, "B", "P"));
            addPanel(panels, "PSTAR-" + i, "Starboard side panel bay " + i, StructuralPanelType.STARBOARD_PANEL,
                    stationCornerId(i, "B", "S"), stationCornerId(i, "T", "S"), stationCornerId(i + 1, "T", "S"), stationCornerId(i + 1, "B", "S"));
        }
    }

    private BracingProfile resolveBracingProfile(ChassisCalculationInput input, StructuralOptimizationMode mode) {
        String code = input == null ? null : input.getStructureProfileCode();
        if (code == null) {
            return adaptBracingByMode(BracingProfile.frameLight(), mode);
        }
        String c = code.trim().toUpperCase();
        if (c.contains("ISOGRID") || c.contains("ORTHOGRID") || c.contains("SANDWICH") || c.contains("HONEYCOMB")
                || c.contains("LATTICE") || c.contains("SHELL") || c.contains("CORE_")) {
            return adaptBracingByMode(BracingProfile.shellDominated(), mode);
        }
        if (c.contains("SPACE_FRAME") || c.contains("TRUSS")) {
            return adaptBracingByMode(BracingProfile.spaceFrame(), mode);
        }
        if (c.contains("MONOLITHIC") || c.contains("BOX_BEAM") || c.contains("TUBULAR") || c.contains("RIBBED_FRAME")) {
            return adaptBracingByMode(BracingProfile.frameLight(), mode);
        }
        return adaptBracingByMode(BracingProfile.frameLight(), mode);
    }

    private StructuralOptimizationMode resolveOptimizationMode(ChassisCalculationInput input) {
        if (input == null || input.getStructuralDesignBasis() == null || input.getStructuralDesignBasis().getOptimizationMode() == null) {
            return StructuralOptimizationMode.BALANCED;
        }
        return input.getStructuralDesignBasis().getOptimizationMode();
    }

    private BracingProfile adaptBracingByMode(BracingProfile base, StructuralOptimizationMode mode) {
        if (base == null) {
            base = BracingProfile.frameLight();
        }
        if (mode == StructuralOptimizationMode.HIGH_MARGIN) {
            return new BracingProfile(true, true, true, 1);
        }
        if (mode == StructuralOptimizationMode.MIN_MASS) {
            return new BracingProfile(false, base.addSideLongitudinalDiagonals, false, 2);
        }
        return base;
    }

    private void applyOptimizationPruning(List<StructuralGraphMember> members, StructuralOptimizationMode mode, List<String> warnings) {
        if (members == null || members.isEmpty() || mode == StructuralOptimizationMode.HIGH_MARGIN) {
            return;
        }
        int removed = 0;
        List<StructuralGraphMember> keep = new ArrayList<StructuralGraphMember>(members.size());
        int diagCounter = 0;
        for (StructuralGraphMember m : members) {
            if (m.getType() != StructuralMemberType.DIAGONAL_BRACE) {
                keep.add(m);
                continue;
            }
            boolean remove = false;
            if (mode == StructuralOptimizationMode.MIN_MASS) {
                remove = (diagCounter % 2) == 1;
            } else if (mode == StructuralOptimizationMode.BALANCED) {
                remove = false;
            }
            diagCounter++;
            if (remove) {
                removed++;
            } else {
                keep.add(m);
            }
        }
        if (removed > 0) {
            members.clear();
            members.addAll(keep);
            warnings.add("optimization pruning removed " + removed + " secondary diagonal braces (mode=" + mode + ")");
        }
    }

    private IntrusionMetrics estimateIntrusionMetrics(StructuralGraph graph, Map<String, StructuralGraphNode> nodeById) {
        double totalLength = 0.0d;
        double intrusiveLength = 0.0d;
        for (StructuralGraphMember member : graph.getMembers()) {
            StructuralGraphNode a = nodeById.get(member.getNodeAId());
            StructuralGraphNode b = nodeById.get(member.getNodeBId());
            if (a == null || b == null || a.getPositionMeters() == null || b.getPositionMeters() == null) {
                continue;
            }
            double len = distance(a.getPositionMeters(), b.getPositionMeters());
            totalLength += len;
            if (isIntrusiveMember(member, a, b, graph)) {
                intrusiveLength += len;
            }
        }
        double proxyArea = Math.max(0.25d, 0.06d * graph.getCubeEdgeMeters() * graph.getCubeEdgeMeters());
        double intrusionVolume = intrusiveLength * proxyArea;
        return new IntrusionMetrics(totalLength, intrusiveLength, intrusionVolume);
    }

    private boolean isIntrusiveMember(StructuralGraphMember member, StructuralGraphNode a, StructuralGraphNode b, StructuralGraph graph) {
        if (member.getType() == StructuralMemberType.ENGINE_SUPPORT
                || member.getType() == StructuralMemberType.MASS_SUPPORT
                || member.getType() == StructuralMemberType.LANDING_SUPPORT) {
            return true;
        }
        if (member.getType() == StructuralMemberType.CENTER_SPINE) {
            return true;
        }
        if (member.getType() == StructuralMemberType.DIAGONAL_BRACE) {
            return !(isOnHullSurface(a.getPositionMeters(), graph) && isOnHullSurface(b.getPositionMeters(), graph));
        }
        if (member.getType() == StructuralMemberType.TRANSVERSE_RING && isInteriorNode(a) && isInteriorNode(b)) {
            return true;
        }
        return false;
    }

    private boolean isInteriorNode(StructuralGraphNode n) {
        return n.getType() == StructuralNodeType.FRAME_MIDPOINT
                || n.getType() == StructuralNodeType.MASS_ANCHOR
                || n.getType() == StructuralNodeType.ENGINE_MOUNT
                || n.getType() == StructuralNodeType.THRUST_LOAD_POINT;
    }

    private boolean isOnHullSurface(Vector3 p, StructuralGraph graph) {
        if (p == null) {
            return false;
        }
        double hx = 0.5d * graph.getLengthMeters();
        double hy = 0.5d * graph.getWidthMeters();
        double hz = 0.5d * graph.getHeightMeters();
        return near(Math.abs(p.getX()), hx) || near(Math.abs(p.getY()), hy) || near(Math.abs(p.getZ()), hz);
    }

    private boolean near(double a, double b) {
        return Math.abs(a - b) <= HULL_FACE_EPS;
    }

    private double distance(Vector3 a, Vector3 b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    private void addBulkheads(int stations, Map<String, StructuralGraphNode> nodeById, List<StructuralGraphPanel> panels) {
        if (stations < 2) {
            return;
        }
        int[] indices = stations <= 3 ? new int[]{0, stations - 1} : new int[]{0, stations / 2, stations - 1};
        for (int idx : indices) {
            if (!nodeById.containsKey(stationCornerId(idx, "B", "P"))) {
                continue;
            }
            addPanel(panels, "PBULK-" + idx, "Bulkhead " + idx, StructuralPanelType.BULKHEAD,
                    stationCornerId(idx, "B", "P"), stationCornerId(idx, "B", "S"), stationCornerId(idx, "T", "S"), stationCornerId(idx, "T", "P"));
        }
    }

    private void addMassAnchors(
            MassDistributionModel massDistribution,
            double length,
            double width,
            double height,
            int stations,
            List<StructuralGraphNode> nodes,
            Map<String, StructuralGraphNode> nodeById,
            List<StructuralGraphMember> members,
            List<String> warnings) {
        if (massDistribution == null) {
            return;
        }
        int idx = 0;
        for (MassNode massNode : massDistribution.getMajorMassNodes()) {
            if (massNode == null || massNode.getPosition() == null) {
                continue;
            }
            String id = "N-MASS-" + idx;
            addNode(nodes, nodeById, id, "Mass anchor " + safeName(massNode.getName(), idx), StructuralNodeType.MASS_ANCHOR,
                    clampToEnvelope(massNode.getPosition(), length, width, height));
            addNearestCenterMember(members, id, massNode.getPosition(), length, stations);
            idx++;
        }
        if (idx == 0) {
            Vector3 com = massDistribution.getCenterOfMassLoaded() != null
                    ? massDistribution.getCenterOfMassLoaded()
                    : (massDistribution.getCenterOfMassFueled() != null ? massDistribution.getCenterOfMassFueled() : massDistribution.getCenterOfMassDry());
            if (com != null) {
                addNode(nodes, nodeById, "N-MASS-COM", "Mass anchor COM", StructuralNodeType.MASS_ANCHOR, clampToEnvelope(com, length, width, height));
                addNearestCenterMember(members, "N-MASS-COM", com, length, stations);
            } else {
                warnings.add("massDistributionModel present but no COM/majorMassNodes provided");
            }
        }
    }

    private void addNearestCenterMember(List<StructuralGraphMember> members, String anchorNodeId, Vector3 position, double length, int stations) {
        String stationId = nearestStationCenterId(position == null ? 0.0d : position.getX(), length, stations);
        addMember(members, "MMASS-" + anchorNodeId, "Mass support " + anchorNodeId, StructuralMemberType.MASS_SUPPORT, anchorNodeId, stationId);
    }

    private void addEngineMounts(
            ChassisCalculationInput input,
            double length,
            double width,
            double height,
            int stations,
            List<StructuralGraphNode> nodes,
            Map<String, StructuralGraphNode> nodeById,
            List<StructuralGraphMember> members,
            List<String> warnings) {
        ThrustLayout thrustLayout = input.getThrustLayout();
        int count = 0;
        if (thrustLayout != null && !thrustLayout.getThrustNodes().isEmpty()) {
            for (ThrustNode t : thrustLayout.getThrustNodes()) {
                if (t == null || t.getPosition() == null) {
                    continue;
                }
                String mountId = "N-ENG-" + count;
                Vector3 pos = clampToEnvelope(t.getPosition(), length, width, height);
                addNode(nodes, nodeById, mountId, "Engine mount " + safeName(t.getName(), count), StructuralNodeType.ENGINE_MOUNT, pos);
                addNode(nodes, nodeById, "N-THR-" + count, "Thrust load point " + safeName(t.getName(), count), StructuralNodeType.THRUST_LOAD_POINT, pos);
                addMember(members, "MENG-" + count, "Engine support " + count, StructuralMemberType.ENGINE_SUPPORT, mountId, nearestCenterId(pos, length, stations));
                count++;
            }
        }
        if (count > 0) {
            return;
        }

        EngineLayoutConfig layoutConfig = input.getStructuralDesignBasis() == null ? null : input.getStructuralDesignBasis().getEngineLayoutConfig();
        List<Vector3> configured = buildEngineNodesFromLayoutConfig(layoutConfig, length, width, height);
        if (!configured.isEmpty()) {
            for (Vector3 p : configured) {
                String mountId = "N-ENG-CFG-" + count;
                addNode(nodes, nodeById, mountId, "Engine mount config " + count, StructuralNodeType.ENGINE_MOUNT, p);
                addNode(nodes, nodeById, "N-THR-CFG-" + count, "Thrust load point config " + count, StructuralNodeType.THRUST_LOAD_POINT, p);
                addMember(members, "MENG-CFG-" + count, "Engine support config " + count, StructuralMemberType.ENGINE_SUPPORT, mountId, nearestCenterId(p, length, stations));
                count++;
            }
            return;
        }

        int profile = resolveEnginePlacementProfileClass(input);
        for (Vector3 p : buildProfileEngineNodes(profile, length, width, height)) {
            String mountId = "N-ENG-P-" + count;
            addNode(nodes, nodeById, mountId, "Engine mount profile " + count, StructuralNodeType.ENGINE_MOUNT, p);
            addNode(nodes, nodeById, "N-THR-P-" + count, "Thrust load point profile " + count, StructuralNodeType.THRUST_LOAD_POINT, p);
            addMember(members, "MENG-P-" + count, "Engine support profile " + count, StructuralMemberType.ENGINE_SUPPORT, mountId, nearestCenterId(p, length, stations));
            count++;
        }

        if (count == 0 && profile > 0) {
            warnings.add("engine placement profile set but no synthetic engine nodes generated for profile=" + profile);
        }
    }

    private void addLandingPoints(
            ChassisCalculationInput input,
            double length,
            double width,
            double height,
            int stations,
            List<StructuralGraphNode> nodes,
            Map<String, StructuralGraphNode> nodeById,
            List<StructuralGraphMember> members) {
        StructuralDesignBasis basis = input.getStructuralDesignBasis();
        CapabilityClassSet caps = basis == null ? null : basis.getCapabilityClassSet();
        int landing = caps == null ? 0 : caps.getPlanetLandingCapabilityClass();
        if (landing <= 0) {
            return;
        }

        double xFore = -0.30d * length;
        double xAft = 0.30d * length;
        double y = 0.35d * width;
        double z = -0.50d * height;
        Vector3[] points = new Vector3[]{
                new Vector3(xFore, -y, z),
                new Vector3(xFore, y, z),
                new Vector3(xAft, -y, z),
                new Vector3(xAft, y, z)
        };
        for (int i = 0; i < points.length; i++) {
            String id = "N-LAND-" + i;
            addNode(nodes, nodeById, id, "Landing load point " + i, StructuralNodeType.LANDING_LOAD_POINT, points[i]);
            addMember(members, "MLAND-" + i, "Landing support member " + i, StructuralMemberType.LANDING_SUPPORT, id, nearestCenterId(points[i], length, stations));
        }
    }

    private List<Vector3> buildProfileEngineNodes(int profile, double length, double width, double height) {
        List<Vector3> nodes = new ArrayList<Vector3>();
        if (profile <= 0) {
            return nodes;
        }
        double ySpread = 0.25d * width;
        double zSpread = 0.20d * height;
        double xFore = -0.35d * length;
        double xAft = 0.35d * length;
        double xMid = 0.0d;
        double xForeEnd = -0.45d * length;
        double xAftEnd = 0.45d * length;

        switch (profile) {
            case 1: // aft axial
                nodes.add(new Vector3(xAftEnd, 0.0d, 0.0d));
                break;
            case 2: // aft distributed
                nodes.add(new Vector3(xAftEnd, -ySpread, 0.0d));
                nodes.add(new Vector3(xAftEnd, ySpread, 0.0d));
                nodes.add(new Vector3(xAft, 0.0d, zSpread));
                break;
            case 3: // fore axial
                nodes.add(new Vector3(xForeEnd, 0.0d, 0.0d));
                break;
            case 4: // fore distributed
                nodes.add(new Vector3(xForeEnd, -ySpread, 0.0d));
                nodes.add(new Vector3(xForeEnd, ySpread, 0.0d));
                nodes.add(new Vector3(xFore, 0.0d, zSpread));
                break;
            case 5: // mid axial single-line
                nodes.add(new Vector3(xMid, 0.0d, 0.0d));
                break;
            case 6: // mid distributed
                nodes.add(new Vector3(xMid, -ySpread, 0.0d));
                nodes.add(new Vector3(xMid, ySpread, 0.0d));
                nodes.add(new Vector3(xMid, 0.0d, zSpread));
                nodes.add(new Vector3(xMid, 0.0d, -zSpread));
                break;
            case 7: // fore+mid+aft axial chain
                nodes.add(new Vector3(xFore, 0.0d, 0.0d));
                nodes.add(new Vector3(xMid, 0.0d, 0.0d));
                nodes.add(new Vector3(xAft, 0.0d, 0.0d));
                break;
            case 8: // fore+mid+aft distributed chain
                nodes.add(new Vector3(xFore, -ySpread, 0.0d));
                nodes.add(new Vector3(xFore, ySpread, 0.0d));
                nodes.add(new Vector3(xMid, -ySpread, 0.0d));
                nodes.add(new Vector3(xMid, ySpread, 0.0d));
                nodes.add(new Vector3(xAft, -ySpread, 0.0d));
                nodes.add(new Vector3(xAft, ySpread, 0.0d));
                break;
            case 9: // full-length axial chain
                nodes.add(new Vector3(xForeEnd, 0.0d, 0.0d));
                nodes.add(new Vector3(xFore, 0.0d, 0.0d));
                nodes.add(new Vector3(xMid, 0.0d, 0.0d));
                nodes.add(new Vector3(xAft, 0.0d, 0.0d));
                nodes.add(new Vector3(xAftEnd, 0.0d, 0.0d));
                break;
            case 10: // full-length distributed chain
                for (double x : new double[]{xForeEnd, xFore, xMid, xAft, xAftEnd}) {
                    nodes.add(new Vector3(x, -ySpread, 0.0d));
                    nodes.add(new Vector3(x, ySpread, 0.0d));
                }
                break;
            case 11: // asymmetric/offset pods
                nodes.add(new Vector3(xAft, 0.35d * width, 0.20d * height));
                nodes.add(new Vector3(xMid, -0.30d * width, -0.15d * height));
                break;
            case 12: // hull-wide distributed field
                for (double x : new double[]{xFore, xMid, xAft}) {
                    nodes.add(new Vector3(x, -ySpread, zSpread));
                    nodes.add(new Vector3(x, ySpread, zSpread));
                    nodes.add(new Vector3(x, -ySpread, -zSpread));
                    nodes.add(new Vector3(x, ySpread, -zSpread));
                }
                break;
            case 13: // modular reconfigurable internal array
                for (double x : new double[]{xFore, xMid, xAft}) {
                    nodes.add(new Vector3(x, -ySpread, 0.0d));
                    nodes.add(new Vector3(x, ySpread, 0.0d));
                }
                nodes.add(new Vector3(xMid, 0.0d, zSpread));
                nodes.add(new Vector3(xMid, 0.0d, -zSpread));
                break;
            default:
                break;
        }
        return nodes;
    }

    private int resolveEnginePlacementProfileClass(ChassisCalculationInput input) {
        return (input == null || input.getStructuralDesignBasis() == null) ? 0 : input.getStructuralDesignBasis().getEnginePlacementProfileClass();
    }

    private List<Vector3> buildEngineNodesFromLayoutConfig(EngineLayoutConfig config, double length, double width, double height) {
        List<Vector3> out = new ArrayList<Vector3>();
        if (config == null || config.isEmpty()) {
            return out;
        }
        double halfLen = 0.5d * length;
        for (EngineLayerConfig layer : config.getLayers()) {
            if (layer == null) {
                continue;
            }
            int count = Math.max(1, layer.getEnginesPerLayer());
            double x = clamp(layer.getXOffsetNormalized(), -1.0d, 1.0d) * halfLen;
            double ry = Math.max(0.0d, layer.getRadiusYNormalized()) * 0.5d * width;
            double rz = Math.max(0.0d, layer.getRadiusZNormalized()) * 0.5d * height;
            double theta = Math.toRadians(layer.getPlaneRotationDeg());
            List<Vector3> layerPoints = new ArrayList<Vector3>();

            switch (layer.getPattern()) {
                case LINE:
                    for (int i = 0; i < count; i++) {
                        double t = count == 1 ? 0.0d : ((double) i / (count - 1)) * 2.0d - 1.0d;
                        layerPoints.add(rotateYZ(x, t * ry, 0.0d, theta));
                    }
                    break;
                case RING:
                    for (int i = 0; i < count; i++) {
                        double a = (2.0d * Math.PI * i) / count;
                        layerPoints.add(rotateYZ(x, Math.cos(a) * ry, Math.sin(a) * rz, theta));
                    }
                    break;
                case CROSS:
                    layerPoints.add(rotateYZ(x, ry, 0.0d, theta));
                    if (count > 1) layerPoints.add(rotateYZ(x, -ry, 0.0d, theta));
                    if (count > 2) layerPoints.add(rotateYZ(x, 0.0d, rz, theta));
                    if (count > 3) layerPoints.add(rotateYZ(x, 0.0d, -rz, theta));
                    for (int i = 4; i < count; i++) {
                        double a = (2.0d * Math.PI * (i - 4)) / Math.max(1, count - 4);
                        layerPoints.add(rotateYZ(x, Math.cos(a) * ry, Math.sin(a) * rz, theta));
                    }
                    break;
                case GRID:
                    int n = (int) Math.ceil(Math.sqrt(count));
                    int placed = 0;
                    for (int gy = 0; gy < n && placed < count; gy++) {
                        for (int gz = 0; gz < n && placed < count; gz++) {
                            double ty = n == 1 ? 0.0d : ((double) gy / (n - 1)) * 2.0d - 1.0d;
                            double tz = n == 1 ? 0.0d : ((double) gz / (n - 1)) * 2.0d - 1.0d;
                            layerPoints.add(rotateYZ(x, ty * ry, tz * rz, theta));
                            placed++;
                        }
                    }
                    break;
                case SINGLE:
                default:
                    layerPoints.add(rotateYZ(x, 0.0d, 0.0d, theta));
                    for (int i = 1; i < count; i++) {
                        double a = (2.0d * Math.PI * (i - 1)) / Math.max(1, count - 1);
                        layerPoints.add(rotateYZ(x, Math.cos(a) * ry, Math.sin(a) * rz, theta));
                    }
                    break;
            }

            if (layer.getSymmetryMode() == EngineLayerSymmetryMode.STRICT || layer.getSymmetryMode() == EngineLayerSymmetryMode.MIRROR) {
                layerPoints = enforceMirrorSymmetry(layerPoints, x, layer.getSymmetryMode() == EngineLayerSymmetryMode.STRICT);
            }
            for (Vector3 p : layerPoints) {
                out.add(clampToEnvelope(p, length, width, height));
            }
        }
        return out;
    }

    private Vector3 rotateYZ(double x, double y, double z, double theta) {
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double yr = y * ct - z * st;
        double zr = y * st + z * ct;
        return new Vector3(x, yr, zr);
    }

    private List<Vector3> enforceMirrorSymmetry(List<Vector3> points, double x, boolean strict) {
        List<Vector3> out = new ArrayList<Vector3>();
        for (Vector3 p : points) {
            if (p == null) {
                continue;
            }
            out.add(p);
            if (Math.abs(p.getY()) > 1e-9d) {
                out.add(new Vector3(x, -p.getY(), p.getZ()));
            } else if (strict && Math.abs(p.getZ()) > 1e-9d) {
                out.add(new Vector3(x, p.getY(), -p.getZ()));
            }
        }
        return out;
    }

    private String stationCornerId(int i, String z, String y) {
        return "N-S" + i + "-" + z + y;
    }

    private String stationCenterId(int i) {
        return "N-S" + i + "-C";
    }

    private String nearestCenterId(Vector3 position, double length, int stations) {
        return nearestStationCenterId(position == null ? 0.0d : position.getX(), length, stations);
    }

    private String nearestStationCenterId(double x, double length, int stations) {
        int stationCount = Math.max(1, stations);
        double normalized = length <= 1e-9d ? 0.5d : (x + 0.5d * length) / length;
        int i = (int) Math.round(Math.max(0.0d, Math.min(1.0d, normalized)) * (stationCount - 1));
        return stationCenterId(i);
    }

    private Vector3 clampToEnvelope(Vector3 p, double length, double width, double height) {
        if (p == null) {
            return new Vector3(0.0d, 0.0d, 0.0d);
        }
        return new Vector3(
                clamp(p.getX(), -0.5d * length, 0.5d * length),
                clamp(p.getY(), -0.5d * width, 0.5d * width),
                clamp(p.getZ(), -0.5d * height, 0.5d * height)
        );
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private void addNode(List<StructuralGraphNode> nodes, Map<String, StructuralGraphNode> nodeById, String id, String name, StructuralNodeType type, Vector3 pos) {
        StructuralGraphNode node = new StructuralGraphNode(id, name, type, pos);
        nodes.add(node);
        nodeById.put(id, node);
    }

    private void addMember(List<StructuralGraphMember> members, String id, String name, StructuralMemberType type, String a, String b) {
        members.add(new StructuralGraphMember(id, name, type, classifyMemberRole(type), a, b));
    }

    private void addPanel(List<StructuralGraphPanel> panels, String id, String name, StructuralPanelType type, String... nodeIds) {
        panels.add(new StructuralGraphPanel(id, name, type, classifyPanelRole(type), Arrays.asList(nodeIds)));
    }

    private String safeName(String value, int idx) {
        return value == null || value.trim().isEmpty() ? String.valueOf(idx) : value;
    }

    private StructuralMemberRole classifyMemberRole(StructuralMemberType type) {
        if (type == null) {
            return StructuralMemberRole.SECONDARY_STIFFENING;
        }
        switch (type) {
            case LONGITUDINAL_RAIL:
            case TRANSVERSE_RING:
            case CENTER_SPINE:
                return StructuralMemberRole.PRIMARY_LOAD_PATH;
            case DIAGONAL_BRACE:
                return StructuralMemberRole.SECONDARY_STIFFENING;
            case ENGINE_SUPPORT:
            case LANDING_SUPPORT:
            case MASS_SUPPORT:
                return StructuralMemberRole.LOCAL_LOAD_TRANSFER;
            default:
                return StructuralMemberRole.SECONDARY_STIFFENING;
        }
    }

    private StructuralPanelRole classifyPanelRole(StructuralPanelType type) {
        if (type == null) {
            return StructuralPanelRole.SECONDARY_CLOSURE;
        }
        switch (type) {
            case BULKHEAD:
            case GENERIC_SHEAR_PANEL:
                return StructuralPanelRole.PRIMARY_SHEAR_CLOSURE;
            case DECK_PANEL:
                return StructuralPanelRole.LOCAL_PARTITION;
            case TOP_PANEL:
            case BOTTOM_PANEL:
            case PORT_PANEL:
            case STARBOARD_PANEL:
                return StructuralPanelRole.SECONDARY_CLOSURE;
            default:
                return StructuralPanelRole.SECONDARY_CLOSURE;
        }
    }

    private static final class BracingProfile {
        private final boolean addTransverseCrossBraces;
        private final boolean addSideLongitudinalDiagonals;
        private final boolean addTopBottomLongitudinalDiagonals;
        private final int longitudinalBraceStep;

        private BracingProfile(boolean addTransverseCrossBraces, boolean addSideLongitudinalDiagonals, boolean addTopBottomLongitudinalDiagonals, int longitudinalBraceStep) {
            this.addTransverseCrossBraces = addTransverseCrossBraces;
            this.addSideLongitudinalDiagonals = addSideLongitudinalDiagonals;
            this.addTopBottomLongitudinalDiagonals = addTopBottomLongitudinalDiagonals;
            this.longitudinalBraceStep = Math.max(1, longitudinalBraceStep);
        }

        static BracingProfile shellDominated() {
            return new BracingProfile(false, false, false, 1);
        }

        static BracingProfile frameLight() {
            return new BracingProfile(false, true, false, 1);
        }

        static BracingProfile spaceFrame() {
            return new BracingProfile(false, true, true, 1);
        }
    }

    private static final class IntrusionMetrics {
        private final double totalMemberLengthMeters;
        private final double intrusiveMemberLengthMeters;
        private final double estimatedInternalStructureIntrusionVolumeM3;
        private final double intrusiveMemberLengthFraction;

        private IntrusionMetrics(double totalMemberLengthMeters, double intrusiveMemberLengthMeters, double estimatedInternalStructureIntrusionVolumeM3) {
            this.totalMemberLengthMeters = totalMemberLengthMeters;
            this.intrusiveMemberLengthMeters = intrusiveMemberLengthMeters;
            this.estimatedInternalStructureIntrusionVolumeM3 = estimatedInternalStructureIntrusionVolumeM3;
            this.intrusiveMemberLengthFraction = totalMemberLengthMeters <= 1e-9d ? 0.0d : intrusiveMemberLengthMeters / totalMemberLengthMeters;
        }
    }
}
