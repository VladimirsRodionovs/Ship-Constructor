package org.example.shipconstructor.domain;

public class ModuleEnergyProfile {
    private final int consumptionMax;
    private final int consumptionPowerUp;
    private final int consumptionStandBy;
    private final int consumptionOn;
    private final int productionMin;
    private final int productionMax;
    private final int productionNominal;
    private final int productionCritical;
    private final int storageCapacity;

    public ModuleEnergyProfile(
            int consumptionMax,
            int consumptionPowerUp,
            int consumptionStandBy,
            int consumptionOn,
            int productionMin,
            int productionMax,
            int productionNominal,
            int productionCritical,
            int storageCapacity) {
        this.consumptionMax = consumptionMax;
        this.consumptionPowerUp = consumptionPowerUp;
        this.consumptionStandBy = consumptionStandBy;
        this.consumptionOn = consumptionOn;
        this.productionMin = productionMin;
        this.productionMax = productionMax;
        this.productionNominal = productionNominal;
        this.productionCritical = productionCritical;
        this.storageCapacity = storageCapacity;
    }

    public static ModuleEnergyProfile empty() {
        return new ModuleEnergyProfile(0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public int getConsumptionMax() {
        return consumptionMax;
    }

    public int getConsumptionPowerUp() {
        return consumptionPowerUp;
    }

    public int getConsumptionStandBy() {
        return consumptionStandBy;
    }

    public int getConsumptionOn() {
        return consumptionOn;
    }

    public int getProductionMin() {
        return productionMin;
    }

    public int getProductionMax() {
        return productionMax;
    }

    public int getProductionNominal() {
        return productionNominal;
    }

    public int getProductionCritical() {
        return productionCritical;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }
}
