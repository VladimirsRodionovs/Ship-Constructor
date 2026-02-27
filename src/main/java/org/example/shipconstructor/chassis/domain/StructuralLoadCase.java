package org.example.shipconstructor.chassis.domain;

public class StructuralLoadCase {
    private final String name;
    private final double longitudinalG;
    private final double lateralG;
    private final double verticalG;
    private final double durationSec;
    private final double occurrenceWeight;
    private final boolean certificationCritical;
    private final String primaryLoadMode;

    public StructuralLoadCase(
            String name,
            double longitudinalG,
            double lateralG,
            double verticalG,
            double durationSec,
            double occurrenceWeight,
            boolean certificationCritical,
            String primaryLoadMode) {
        this.name = name;
        this.longitudinalG = longitudinalG;
        this.lateralG = lateralG;
        this.verticalG = verticalG;
        this.durationSec = durationSec;
        this.occurrenceWeight = occurrenceWeight;
        this.certificationCritical = certificationCritical;
        this.primaryLoadMode = primaryLoadMode;
    }

    public String getName() { return name; }
    public double getLongitudinalG() { return longitudinalG; }
    public double getLateralG() { return lateralG; }
    public double getVerticalG() { return verticalG; }
    public double getDurationSec() { return durationSec; }
    public double getOccurrenceWeight() { return occurrenceWeight; }
    public boolean isCertificationCritical() { return certificationCritical; }
    public String getPrimaryLoadMode() { return primaryLoadMode; }

    public AccelerationEnvelope toAccelerationEnvelopeAbs() {
        return new AccelerationEnvelope(
                Math.abs(longitudinalG),
                Math.abs(lateralG),
                Math.abs(verticalG)
        );
    }
}
