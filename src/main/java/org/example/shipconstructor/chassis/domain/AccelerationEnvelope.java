package org.example.shipconstructor.chassis.domain;

public class AccelerationEnvelope {
    private final double longitudinalG;
    private final double lateralG;
    private final double verticalG;

    public AccelerationEnvelope(double longitudinalG, double lateralG, double verticalG) {
        this.longitudinalG = longitudinalG;
        this.lateralG = lateralG;
        this.verticalG = verticalG;
    }

    public double getLongitudinalG() {
        return longitudinalG;
    }

    public double getLateralG() {
        return lateralG;
    }

    public double getVerticalG() {
        return verticalG;
    }

    public double getMaxG() {
        return Math.max(longitudinalG, Math.max(lateralG, verticalG));
    }

    public double getEuclideanSeverity() {
        return Math.sqrt(longitudinalG * longitudinalG + lateralG * lateralG + verticalG * verticalG);
    }
}
