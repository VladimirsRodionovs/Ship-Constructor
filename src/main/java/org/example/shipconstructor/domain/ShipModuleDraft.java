package org.example.shipconstructor.domain;

import java.util.Map;

public class ShipModuleDraft {
    private final Long moduleId;
    private final String moduleName;
    private final int typeId;
    private final Integer technologyDirectionIndex;
    private final Integer technologyLevelIndex;
    private final String dryWeightText;
    private final String fullWeightText;
    private final Integer reliability;
    private final ModuleEnergyProfile energyProfile;
    private final ModuleStorageProfile storageProfile;
    private final Map<String, Double> extraVolumesM3;

    public ShipModuleDraft(
            Long moduleId,
            String moduleName,
            int typeId,
            Integer technologyDirectionIndex,
            Integer technologyLevelIndex,
            String dryWeightText,
            String fullWeightText,
            Integer reliability,
            ModuleEnergyProfile energyProfile,
            ModuleStorageProfile storageProfile,
            Map<String, Double> extraVolumesM3) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.typeId = typeId;
        this.technologyDirectionIndex = technologyDirectionIndex;
        this.technologyLevelIndex = technologyLevelIndex;
        this.dryWeightText = dryWeightText;
        this.fullWeightText = fullWeightText;
        this.reliability = reliability;
        this.energyProfile = energyProfile == null ? ModuleEnergyProfile.empty() : energyProfile;
        this.storageProfile = storageProfile == null ? ModuleStorageProfile.empty() : storageProfile;
        this.extraVolumesM3 = extraVolumesM3;
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

    public Integer getTechnologyDirectionIndex() {
        return technologyDirectionIndex;
    }

    public Integer getTechnologyLevelIndex() {
        return technologyLevelIndex;
    }

    public String getDryWeightText() {
        return dryWeightText;
    }

    public String getFullWeightText() {
        return fullWeightText;
    }

    public Integer getReliability() {
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
