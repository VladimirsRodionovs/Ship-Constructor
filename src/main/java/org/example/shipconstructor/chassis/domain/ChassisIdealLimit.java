package org.example.shipconstructor.chassis.domain;

public class ChassisIdealLimit {
    private final double idealMaxSupportedMassKg;
    private final double idealRecommendedFrameMassKg;
    private final double idealBucklingReserveIndex;
    private final double idealStiffnessReserveIndex;

    public ChassisIdealLimit(
            double idealMaxSupportedMassKg,
            double idealRecommendedFrameMassKg,
            double idealBucklingReserveIndex,
            double idealStiffnessReserveIndex) {
        this.idealMaxSupportedMassKg = idealMaxSupportedMassKg;
        this.idealRecommendedFrameMassKg = idealRecommendedFrameMassKg;
        this.idealBucklingReserveIndex = idealBucklingReserveIndex;
        this.idealStiffnessReserveIndex = idealStiffnessReserveIndex;
    }

    public double getIdealMaxSupportedMassKg() { return idealMaxSupportedMassKg; }
    public double getIdealRecommendedFrameMassKg() { return idealRecommendedFrameMassKg; }
    public double getIdealBucklingReserveIndex() { return idealBucklingReserveIndex; }
    public double getIdealStiffnessReserveIndex() { return idealStiffnessReserveIndex; }
}
