package org.example.shipconstructor.domain;

public class ShipVolumeSummary {
    private final double cargoM3;
    private final double ammoM3;
    private final double consumablesM3;
    private final double atmosphereM3;
    private final double infrastructureM3;

    public ShipVolumeSummary(double cargoM3, double ammoM3, double consumablesM3, double atmosphereM3, double infrastructureM3) {
        this.cargoM3 = cargoM3;
        this.ammoM3 = ammoM3;
        this.consumablesM3 = consumablesM3;
        this.atmosphereM3 = atmosphereM3;
        this.infrastructureM3 = infrastructureM3;
    }

    public double getCargoM3() {
        return cargoM3;
    }

    public double getAmmoM3() {
        return ammoM3;
    }

    public double getConsumablesM3() {
        return consumablesM3;
    }

    public double getAtmosphereM3() {
        return atmosphereM3;
    }

    public double getInfrastructureM3() {
        return infrastructureM3;
    }
}
