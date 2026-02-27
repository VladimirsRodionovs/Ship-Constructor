package org.example.shipconstructor.domain;

public class ShipEnergySummary {
    private final int productionMin;
    private final int productionMax;
    private final int productionNominal;
    private final int productionCritical;
    private final int consumptionPowerUp;
    private final int consumptionStandBy;
    private final int consumptionOn;
    private final int consumptionMax;
    private final int storageCapacity;

    public ShipEnergySummary(
            int productionMin,
            int productionMax,
            int productionNominal,
            int productionCritical,
            int consumptionPowerUp,
            int consumptionStandBy,
            int consumptionOn,
            int consumptionMax,
            int storageCapacity) {
        this.productionMin = productionMin;
        this.productionMax = productionMax;
        this.productionNominal = productionNominal;
        this.productionCritical = productionCritical;
        this.consumptionPowerUp = consumptionPowerUp;
        this.consumptionStandBy = consumptionStandBy;
        this.consumptionOn = consumptionOn;
        this.consumptionMax = consumptionMax;
        this.storageCapacity = storageCapacity;
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

    public int getConsumptionPowerUp() {
        return consumptionPowerUp;
    }

    public int getConsumptionStandBy() {
        return consumptionStandBy;
    }

    public int getConsumptionOn() {
        return consumptionOn;
    }

    public int getConsumptionMax() {
        return consumptionMax;
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }
}
