package org.example.shipconstructor.service;

import org.example.shipconstructor.domain.ModuleEnergyProfile;
import org.example.shipconstructor.domain.ModuleStorageProfile;
import org.example.shipconstructor.domain.ShipConstructionResult;
import org.example.shipconstructor.domain.ShipDraft;
import org.example.shipconstructor.domain.ShipEnergySummary;
import org.example.shipconstructor.domain.ShipMassSummary;
import org.example.shipconstructor.domain.ShipModule;
import org.example.shipconstructor.domain.ShipVolumeSummary;
import org.example.shipconstructor.domain.StorageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShipConstructorService {
    private final ModuleCompatibilityValidator compatibilityValidator;

    public ShipConstructorService() {
        this(new ModuleCompatibilityValidator());
    }

    public ShipConstructorService(ModuleCompatibilityValidator compatibilityValidator) {
        this.compatibilityValidator = compatibilityValidator;
    }

    public ShipConstructionResult construct(ShipDraft draft) {
        if (draft == null) {
            throw new IllegalArgumentException("Ship draft is required");
        }

        List<ShipModule> modules = draft.getModules();
        List<String> warnings = new ArrayList<String>(compatibilityValidator.validate(modules, draft.getMaxAllowedTechLevelGap()));

        double dryWeightKg = 0.0d;
        double equippedWeightKg = 0.0d;
        double cargoWeightCapacityKg = 0.0d;
        int reliabilitySum = 0;
        int reliabilityCount = 0;

        int productionMin = 0;
        int productionMax = 0;
        int productionNominal = 0;
        int productionCritical = 0;
        int consumptionPowerUp = 0;
        int consumptionStandBy = 0;
        int consumptionOn = 0;
        int consumptionMax = 0;
        int energyStorage = 0;

        double cargoM3 = 0.0d;
        double ammoM3 = 0.0d;
        double consumablesM3 = 0.0d;
        double atmosphereM3 = 0.0d;
        double infrastructureM3 = 0.0d;

        for (ShipModule module : modules) {
            if (module == null) {
                continue;
            }

            dryWeightKg += module.getDryWeightKg();
            equippedWeightKg += module.getEquippedWeightKg();
            reliabilitySum += module.getReliability();
            reliabilityCount++;

            ModuleEnergyProfile energy = module.getEnergyProfile();
            productionMin += energy.getProductionMin();
            productionMax += energy.getProductionMax();
            productionNominal += energy.getProductionNominal();
            productionCritical += energy.getProductionCritical();
            consumptionPowerUp += energy.getConsumptionPowerUp();
            consumptionStandBy += energy.getConsumptionStandBy();
            consumptionOn += energy.getConsumptionOn();
            consumptionMax += energy.getConsumptionMax();
            energyStorage += energy.getStorageCapacity();

            ModuleStorageProfile storage = module.getStorageProfile();
            cargoM3 += storage.getCapacity(StorageType.CARGO);
            ammoM3 += storage.getCapacity(StorageType.AMMO);
            consumablesM3 += storage.getCapacity(StorageType.CONSUMABLES);
            atmosphereM3 += storage.getCapacity(StorageType.ATMOSPHERE);
            infrastructureM3 += storage.getInfrastructureVolumeM3();

            cargoWeightCapacityKg += estimateMaxCargoWeightKg(storage);
            infrastructureM3 += sumExtraVolumes(module.getExtraVolumesM3());
        }

        ShipMassSummary massSummary = new ShipMassSummary(
                dryWeightKg,
                equippedWeightKg,
                cargoWeightCapacityKg,
                equippedWeightKg + cargoWeightCapacityKg
        );

        ShipEnergySummary energySummary = new ShipEnergySummary(
                productionMin,
                productionMax,
                productionNominal,
                productionCritical,
                consumptionPowerUp,
                consumptionStandBy,
                consumptionOn,
                consumptionMax,
                energyStorage
        );

        ShipVolumeSummary volumeSummary = new ShipVolumeSummary(
                cargoM3,
                ammoM3,
                consumablesM3,
                atmosphereM3,
                infrastructureM3
        );

        int averageReliability = reliabilityCount == 0 ? 0 : Math.round((float) reliabilitySum / (float) reliabilityCount);

        if (productionNominal < consumptionOn) {
            warnings.add("Nominal energy production is lower than ON consumption");
        }
        if (productionMax < consumptionMax) {
            warnings.add("Max energy production is lower than max consumption");
        }

        return new ShipConstructionResult(draft, massSummary, energySummary, volumeSummary, averageReliability, warnings);
    }

    private double estimateMaxCargoWeightKg(ModuleStorageProfile storage) {
        double totalStorageM3 =
                storage.getCapacity(StorageType.CARGO) +
                storage.getCapacity(StorageType.AMMO) +
                storage.getCapacity(StorageType.CONSUMABLES) +
                storage.getCapacity(StorageType.ATMOSPHERE);
        return totalStorageM3 * 1000.0d;
    }

    private double sumExtraVolumes(Map<String, Double> extraVolumes) {
        if (extraVolumes == null || extraVolumes.isEmpty()) {
            return 0.0d;
        }

        double sum = 0.0d;
        for (Double value : extraVolumes.values()) {
            if (value != null && value.doubleValue() > 0.0d) {
                sum += value.doubleValue();
            }
        }
        return sum;
    }
}
