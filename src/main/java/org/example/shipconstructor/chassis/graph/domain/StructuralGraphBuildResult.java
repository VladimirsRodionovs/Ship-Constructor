package org.example.shipconstructor.chassis.graph.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraphBuildResult {
    private final StructuralGraph graph;
    private final List<String> warnings;
    private final double intrusiveMemberLengthMeters;
    private final double totalMemberLengthMeters;
    private final double estimatedInternalStructureIntrusionVolumeM3;

    public StructuralGraphBuildResult(StructuralGraph graph, List<String> warnings) {
        this(graph, warnings, 0.0d, 0.0d, 0.0d);
    }

    public StructuralGraphBuildResult(
            StructuralGraph graph,
            List<String> warnings,
            double intrusiveMemberLengthMeters,
            double totalMemberLengthMeters,
            double estimatedInternalStructureIntrusionVolumeM3) {
        this.graph = graph;
        this.warnings = warnings == null ? Collections.<String>emptyList() : Collections.unmodifiableList(new ArrayList<String>(warnings));
        this.intrusiveMemberLengthMeters = intrusiveMemberLengthMeters;
        this.totalMemberLengthMeters = totalMemberLengthMeters;
        this.estimatedInternalStructureIntrusionVolumeM3 = estimatedInternalStructureIntrusionVolumeM3;
    }

    public StructuralGraph getGraph() { return graph; }
    public List<String> getWarnings() { return warnings; }
    public double getIntrusiveMemberLengthMeters() { return intrusiveMemberLengthMeters; }
    public double getTotalMemberLengthMeters() { return totalMemberLengthMeters; }
    public double getEstimatedInternalStructureIntrusionVolumeM3() { return estimatedInternalStructureIntrusionVolumeM3; }

    public double getIntrusiveMemberLengthFraction() {
        if (totalMemberLengthMeters <= 1e-9d) {
            return 0.0d;
        }
        return intrusiveMemberLengthMeters / totalMemberLengthMeters;
    }
}
