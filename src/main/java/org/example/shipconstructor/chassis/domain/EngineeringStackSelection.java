package org.example.shipconstructor.chassis.domain;

public class EngineeringStackSelection {
    private final Long baseMaterialId;
    private final Long structureTypeId;
    private final Long manufacturingProcessId;
    private final Long assemblyProcessId;
    private final Long qualityProfileId;
    private final Long environmentProfileId;

    public EngineeringStackSelection(
            Long baseMaterialId,
            Long structureTypeId,
            Long manufacturingProcessId,
            Long assemblyProcessId,
            Long qualityProfileId,
            Long environmentProfileId) {
        this.baseMaterialId = baseMaterialId;
        this.structureTypeId = structureTypeId;
        this.manufacturingProcessId = manufacturingProcessId;
        this.assemblyProcessId = assemblyProcessId;
        this.qualityProfileId = qualityProfileId;
        this.environmentProfileId = environmentProfileId;
    }

    public Long getBaseMaterialId() {
        return baseMaterialId;
    }

    public Long getStructureTypeId() {
        return structureTypeId;
    }

    public Long getManufacturingProcessId() {
        return manufacturingProcessId;
    }

    public Long getAssemblyProcessId() {
        return assemblyProcessId;
    }

    public Long getQualityProfileId() {
        return qualityProfileId;
    }

    public Long getEnvironmentProfileId() {
        return environmentProfileId;
    }
}
