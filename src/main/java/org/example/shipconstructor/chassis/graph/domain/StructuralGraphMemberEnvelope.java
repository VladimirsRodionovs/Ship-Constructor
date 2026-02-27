package org.example.shipconstructor.chassis.graph.domain;

public class StructuralGraphMemberEnvelope {
    private final String memberId;
    private final StructuralMemberRole role;
    private final double lengthMeters;
    private final double axisLongitudinalAlignment;
    private final double peakTensionIndex;
    private final double peakCompressionIndex;
    private final double peakCombinedAbsIndex;
    private final String governingLoadName;
    private final String governingLoadMode;

    public StructuralGraphMemberEnvelope(
            String memberId,
            StructuralMemberRole role,
            double lengthMeters,
            double axisLongitudinalAlignment,
            double peakTensionIndex,
            double peakCompressionIndex,
            double peakCombinedAbsIndex,
            String governingLoadName,
            String governingLoadMode) {
        this.memberId = memberId;
        this.role = role;
        this.lengthMeters = lengthMeters;
        this.axisLongitudinalAlignment = axisLongitudinalAlignment;
        this.peakTensionIndex = peakTensionIndex;
        this.peakCompressionIndex = peakCompressionIndex;
        this.peakCombinedAbsIndex = peakCombinedAbsIndex;
        this.governingLoadName = governingLoadName;
        this.governingLoadMode = governingLoadMode;
    }

    public String getMemberId() { return memberId; }
    public StructuralMemberRole getRole() { return role; }
    public double getLengthMeters() { return lengthMeters; }
    public double getAxisLongitudinalAlignment() { return axisLongitudinalAlignment; }
    public double getPeakTensionIndex() { return peakTensionIndex; }
    public double getPeakCompressionIndex() { return peakCompressionIndex; }
    public double getPeakCombinedAbsIndex() { return peakCombinedAbsIndex; }
    public String getGoverningLoadName() { return governingLoadName; }
    public String getGoverningLoadMode() { return governingLoadMode; }
}
