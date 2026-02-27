package org.example.shipconstructor.chassis.graph.service;

import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoad;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphBuildResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMember;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMemberEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphNode;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanelEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphSolveResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralMemberRole;
import org.example.shipconstructor.chassis.graph.domain.StructuralNodeType;
import org.example.shipconstructor.chassis.graph.domain.StructuralPanelRole;

import java.util.Locale;

public final class StructuralGraphSummaryFormatter {
    private StructuralGraphSummaryFormatter() {
    }

    public static String summarize(StructuralGraph graph, StructuralGraphLoadSet loadSet) {
        return summarize(graph, loadSet, null);
    }

    public static String summarize(StructuralGraphBuildResult buildResult, StructuralGraphLoadSet loadSet, StructuralGraphSolveResult solveResult) {
        if (buildResult == null) {
            return summarize((StructuralGraph) null, loadSet, solveResult);
        }
        String base = summarize(buildResult.getGraph(), loadSet, solveResult);
        StringBuilder sb = new StringBuilder(base == null ? "" : base);
        sb.append("Internal intrusion (coarse): intrusiveLen=")
                .append(f(buildResult.getIntrusiveMemberLengthMeters()))
                .append(" m / totalLen=")
                .append(f(buildResult.getTotalMemberLengthMeters()))
                .append(" m (frac=")
                .append(f(buildResult.getIntrusiveMemberLengthFraction()))
                .append("), volProxy=")
                .append(f(buildResult.getEstimatedInternalStructureIntrusionVolumeM3()))
                .append(" m3")
                .append('\n');
        return sb.toString();
    }

    public static String summarize(StructuralGraph graph, StructuralGraphLoadSet loadSet, StructuralGraphSolveResult solveResult) {
        if (graph == null) {
            return "Graph: <none>\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: ")
                .append(graph.getNodes().size()).append(" nodes, ")
                .append(graph.getMembers().size()).append(" members, ")
                .append(graph.getPanels().size()).append(" panels")
                .append('\n');
        sb.append("Envelope m: ")
                .append(f(graph.getLengthMeters())).append(" x ")
                .append(f(graph.getWidthMeters())).append(" x ")
                .append(f(graph.getHeightMeters())).append(" (cubeEdge=")
                .append(f(graph.getCubeEdgeMeters())).append(")")
                .append('\n');

        for (StructuralNodeType type : StructuralNodeType.values()) {
            int count = 0;
            for (StructuralGraphNode n : graph.getNodes()) {
                if (n.getType() == type) {
                    count++;
                }
            }
            if (count > 0) {
                sb.append("  ").append(type.name()).append(": ").append(count).append('\n');
            }
        }

        for (StructuralMemberRole role : StructuralMemberRole.values()) {
            int count = 0;
            for (StructuralGraphMember m : graph.getMembers()) {
                if (m.getRole() == role) {
                    count++;
                }
            }
            if (count > 0) {
                sb.append("  MEMBER_").append(role.name()).append(": ").append(count).append('\n');
            }
        }

        if (loadSet != null) {
            sb.append("Loads: ").append(loadSet.getLoads().size()).append('\n');
            int limit = Math.min(8, loadSet.getLoads().size());
            for (int i = 0; i < limit; i++) {
                StructuralGraphLoad l = loadSet.getLoads().get(i);
                sb.append("  - ")
                        .append(l.getType()).append(" | ")
                        .append(l.getName()).append(" | target=")
                        .append(l.getTargetType()).append(':').append(l.getTargetRef())
                        .append(" | g=")
                        .append(f(l.getMagnitudeG()))
                        .append(" | mode=")
                        .append(l.getPrimaryLoadMode())
                        .append('\n');
            }
            if (loadSet.getLoads().size() > limit) {
                sb.append("  ... +").append(loadSet.getLoads().size() - limit).append(" more loads\n");
            }
        }

        if (solveResult != null) {
            sb.append("Load sharing (coarse B-linked): members=")
                    .append(f(solveResult.getMemberParticipationShare()))
                    .append(", panels=")
                    .append(f(solveResult.getPanelParticipationShare()))
                    .append(" [")
                    .append(solveResult.getLoadSharingProfileLabel())
                    .append("]")
                    .append('\n');

            sb.append("Axial envelope (pre-solver): ")
                    .append(solveResult.getMemberEnvelopes().size())
                    .append(" members solved")
                    .append('\n');
            java.util.List<StructuralGraphMemberEnvelope> sorted = new java.util.ArrayList<StructuralGraphMemberEnvelope>(solveResult.getMemberEnvelopes());
            java.util.Collections.sort(sorted, new java.util.Comparator<StructuralGraphMemberEnvelope>() {
                @Override
                public int compare(StructuralGraphMemberEnvelope a, StructuralGraphMemberEnvelope b) {
                    return Double.compare(b.getPeakCombinedAbsIndex(), a.getPeakCombinedAbsIndex());
                }
            });
            int limit = Math.min(8, sorted.size());
            for (int i = 0; i < limit; i++) {
                StructuralGraphMemberEnvelope e = sorted.get(i);
                sb.append("  * ")
                        .append(e.getMemberId())
                        .append(" [").append(e.getRole()).append("]")
                        .append(" | abs=")
                        .append(f(e.getPeakCombinedAbsIndex()))
                        .append(" | T=")
                        .append(f(e.getPeakTensionIndex()))
                        .append(" | C=")
                        .append(f(e.getPeakCompressionIndex()))
                        .append(" | gov=")
                        .append(e.getGoverningLoadName())
                        .append(" (").append(e.getGoverningLoadMode()).append(")")
                        .append('\n');
            }

            for (StructuralPanelRole role : StructuralPanelRole.values()) {
                int count = 0;
                for (StructuralGraphPanelEnvelope p : solveResult.getPanelEnvelopes()) {
                    if (p.getRole() == role) {
                        count++;
                    }
                }
                if (count > 0) {
                    sb.append("  PANEL_").append(role.name()).append(": ").append(count).append('\n');
                }
            }

            sb.append("Panel envelope (pre-solver): ")
                    .append(solveResult.getPanelEnvelopes().size())
                    .append(" panels solved")
                    .append('\n');
            java.util.List<StructuralGraphPanelEnvelope> pSorted = new java.util.ArrayList<StructuralGraphPanelEnvelope>(solveResult.getPanelEnvelopes());
            java.util.Collections.sort(pSorted, new java.util.Comparator<StructuralGraphPanelEnvelope>() {
                @Override
                public int compare(StructuralGraphPanelEnvelope a, StructuralGraphPanelEnvelope b) {
                    return Double.compare(b.getPeakCombinedAbsIndex(), a.getPeakCombinedAbsIndex());
                }
            });
            int pLimit = Math.min(6, pSorted.size());
            for (int i = 0; i < pLimit; i++) {
                StructuralGraphPanelEnvelope e = pSorted.get(i);
                sb.append("  # ")
                        .append(e.getPanelId())
                        .append(" [").append(e.getRole()).append("]")
                        .append(" | abs=")
                        .append(f(e.getPeakCombinedAbsIndex()))
                        .append(" | shear=")
                        .append(f(e.getPeakShearIndex()))
                        .append(" | normal=")
                        .append(f(e.getPeakNormalIndex()))
                        .append(" | gov=")
                        .append(e.getGoverningLoadName())
                        .append(" (").append(e.getGoverningLoadMode()).append(")")
                        .append('\n');
            }
        }

        return sb.toString();
    }

    private static String f(double v) {
        return String.format(Locale.US, "%.3f", v);
    }
}
