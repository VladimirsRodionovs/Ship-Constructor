package org.example.shipconstructor.chassis.graph.domain;

public class StructuralGraphPanelEnvelope {
    private final String panelId;
    private final StructuralPanelRole role;
    private final double areaProxyM2;
    private final double peakShearIndex;
    private final double peakNormalIndex;
    private final double peakCombinedAbsIndex;
    private final String governingLoadName;
    private final String governingLoadMode;

    public StructuralGraphPanelEnvelope(
            String panelId,
            StructuralPanelRole role,
            double areaProxyM2,
            double peakShearIndex,
            double peakNormalIndex,
            double peakCombinedAbsIndex,
            String governingLoadName,
            String governingLoadMode) {
        this.panelId = panelId;
        this.role = role;
        this.areaProxyM2 = areaProxyM2;
        this.peakShearIndex = peakShearIndex;
        this.peakNormalIndex = peakNormalIndex;
        this.peakCombinedAbsIndex = peakCombinedAbsIndex;
        this.governingLoadName = governingLoadName;
        this.governingLoadMode = governingLoadMode;
    }

    public String getPanelId() { return panelId; }
    public StructuralPanelRole getRole() { return role; }
    public double getAreaProxyM2() { return areaProxyM2; }
    public double getPeakShearIndex() { return peakShearIndex; }
    public double getPeakNormalIndex() { return peakNormalIndex; }
    public double getPeakCombinedAbsIndex() { return peakCombinedAbsIndex; }
    public String getGoverningLoadName() { return governingLoadName; }
    public String getGoverningLoadMode() { return governingLoadMode; }
}
