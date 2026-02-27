package org.example.shipconstructor.domain;

public class ShipMassSummary {
    private final double dryWeightKg;
    private final double equippedWeightKg;
    private final double maxCargoWeightKg;
    private final double totalMaxWeightKg;

    public ShipMassSummary(double dryWeightKg, double equippedWeightKg, double maxCargoWeightKg, double totalMaxWeightKg) {
        this.dryWeightKg = dryWeightKg;
        this.equippedWeightKg = equippedWeightKg;
        this.maxCargoWeightKg = maxCargoWeightKg;
        this.totalMaxWeightKg = totalMaxWeightKg;
    }

    public double getDryWeightKg() {
        return dryWeightKg;
    }

    public double getEquippedWeightKg() {
        return equippedWeightKg;
    }

    public double getMaxCargoWeightKg() {
        return maxCargoWeightKg;
    }

    public double getTotalMaxWeightKg() {
        return totalMaxWeightKg;
    }
}
