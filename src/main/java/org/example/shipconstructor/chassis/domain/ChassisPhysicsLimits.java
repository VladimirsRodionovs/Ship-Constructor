package org.example.shipconstructor.chassis.domain;

public class ChassisPhysicsLimits {
    private final double theoreticalEfficiencyCap;
    private final double practicalEfficiencyCap;
    private final double safetyFactorFloor;
    private final double frameMassFractionFloor;
    private final double bucklingReserveFloor;
    private final double maxSupportedAccelerationG;

    public ChassisPhysicsLimits(
            double theoreticalEfficiencyCap,
            double practicalEfficiencyCap,
            double safetyFactorFloor,
            double frameMassFractionFloor,
            double bucklingReserveFloor,
            double maxSupportedAccelerationG) {
        this.theoreticalEfficiencyCap = theoreticalEfficiencyCap;
        this.practicalEfficiencyCap = practicalEfficiencyCap;
        this.safetyFactorFloor = safetyFactorFloor;
        this.frameMassFractionFloor = frameMassFractionFloor;
        this.bucklingReserveFloor = bucklingReserveFloor;
        this.maxSupportedAccelerationG = maxSupportedAccelerationG;
    }

    public static ChassisPhysicsLimits defaults() {
        return new ChassisPhysicsLimits(1.0d, 0.95d, 1.20d, 0.03d, 1.05d, 50.0d);
    }

    public double getTheoreticalEfficiencyCap() { return theoreticalEfficiencyCap; }
    public double getPracticalEfficiencyCap() { return practicalEfficiencyCap; }
    public double getSafetyFactorFloor() { return safetyFactorFloor; }
    public double getFrameMassFractionFloor() { return frameMassFractionFloor; }
    public double getBucklingReserveFloor() { return bucklingReserveFloor; }
    public double getMaxSupportedAccelerationG() { return maxSupportedAccelerationG; }
}
