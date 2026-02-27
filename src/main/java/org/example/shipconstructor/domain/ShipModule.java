package org.example.shipconstructor.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShipModule {
    private final Long moduleId;
    private final String moduleName;
    private final int typeId;
    private final int technologyDirectionIndex;
    private final int technologyLevelIndex;
    private final double dryWeightKg;
    private final double equippedWeightKg;
    private final int reliability;
    private final ModuleEnergyProfile energyProfile;
    private final ModuleStorageProfile storageProfile;
    private final Map<String, Double> extraVolumesM3;

    public ShipModule(
            Long moduleId,
            String moduleName,
            int typeId,
            int technologyDirectionIndex,
            int technologyLevelIndex,
            double dryWeightKg,
            double equippedWeightKg,
            int reliability,
            ModuleEnergyProfile energyProfile,
            ModuleStorageProfile storageProfile,
            Map<String, Double> extraVolumesM3) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.typeId = typeId;
        this.technologyDirectionIndex = technologyDirectionIndex;
        this.technologyLevelIndex = technologyLevelIndex;
        this.dryWeightKg = dryWeightKg;
        this.equippedWeightKg = equippedWeightKg;
        this.reliability = reliability;
        this.energyProfile = energyProfile;
        this.storageProfile = storageProfile;
        this.extraVolumesM3 = extraVolumesM3 == null
                ? Collections.<String, Double>emptyMap()
                : Collections.unmodifiableMap(new HashMap<String, Double>(extraVolumesM3));
    }

    public Long getModuleId() {
        return moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getTypeId() {
        return typeId;
    }

    public int getTechnologyDirectionIndex() {
        return technologyDirectionIndex;
    }

    public int getTechnologyLevelIndex() {
        return technologyLevelIndex;
    }

    public double getDryWeightKg() {
        return dryWeightKg;
    }

    public double getEquippedWeightKg() {
        return equippedWeightKg;
    }

    public int getReliability() {
        return reliability;
    }

    public ModuleEnergyProfile getEnergyProfile() {
        return energyProfile;
    }

    public ModuleStorageProfile getStorageProfile() {
        return storageProfile;
    }

    public Map<String, Double> getExtraVolumesM3() {
        return extraVolumesM3;
    }
}
