package org.example.shipconstructor.chassis.domain;

public class ChassisCoefficientSet {
    private final double materialDensityKgM3;
    private final double materialStrengthIndex;
    private final double materialStiffnessIndex;
    private final double materialFatigueIndex;
    private final double materialAnisotropyPenaltyFactor;
    private final double materialBrittlenessPenaltyFactor;
    private final double materialTemperaturePenaltyFactor;
    private final double materialThermalCyclingPenaltyFactor;

    private final double structureEffectiveDensityFactor;
    private final double structureSpecificStrengthFactor;
    private final double structureSpecificStiffnessFactor;
    private final double structureBucklingResistanceFactor;
    private final double structureDamageToleranceFactor;

    private final double manufacturingQualityFactor;
    private final double manufacturingDefectPenaltyFactor;
    private final double assemblyJointEfficiencyFactor;
    private final double assemblyStressConcentrationPenaltyFactor;

    private final double qualityConfidenceFactor;
    private final double qualityMarginReductionFactor;
    private final double environmentDegradationFactor;
    private final double environmentFatigueAccelerationFactor;

    public ChassisCoefficientSet(
            double materialDensityKgM3,
            double materialStrengthIndex,
            double materialStiffnessIndex,
            double materialFatigueIndex,
            double materialAnisotropyPenaltyFactor,
            double materialBrittlenessPenaltyFactor,
            double materialTemperaturePenaltyFactor,
            double materialThermalCyclingPenaltyFactor,
            double structureEffectiveDensityFactor,
            double structureSpecificStrengthFactor,
            double structureSpecificStiffnessFactor,
            double structureBucklingResistanceFactor,
            double structureDamageToleranceFactor,
            double manufacturingQualityFactor,
            double manufacturingDefectPenaltyFactor,
            double assemblyJointEfficiencyFactor,
            double assemblyStressConcentrationPenaltyFactor,
            double qualityConfidenceFactor,
            double qualityMarginReductionFactor,
            double environmentDegradationFactor,
            double environmentFatigueAccelerationFactor) {
        this.materialDensityKgM3 = materialDensityKgM3;
        this.materialStrengthIndex = materialStrengthIndex;
        this.materialStiffnessIndex = materialStiffnessIndex;
        this.materialFatigueIndex = materialFatigueIndex;
        this.materialAnisotropyPenaltyFactor = materialAnisotropyPenaltyFactor;
        this.materialBrittlenessPenaltyFactor = materialBrittlenessPenaltyFactor;
        this.materialTemperaturePenaltyFactor = materialTemperaturePenaltyFactor;
        this.materialThermalCyclingPenaltyFactor = materialThermalCyclingPenaltyFactor;
        this.structureEffectiveDensityFactor = structureEffectiveDensityFactor;
        this.structureSpecificStrengthFactor = structureSpecificStrengthFactor;
        this.structureSpecificStiffnessFactor = structureSpecificStiffnessFactor;
        this.structureBucklingResistanceFactor = structureBucklingResistanceFactor;
        this.structureDamageToleranceFactor = structureDamageToleranceFactor;
        this.manufacturingQualityFactor = manufacturingQualityFactor;
        this.manufacturingDefectPenaltyFactor = manufacturingDefectPenaltyFactor;
        this.assemblyJointEfficiencyFactor = assemblyJointEfficiencyFactor;
        this.assemblyStressConcentrationPenaltyFactor = assemblyStressConcentrationPenaltyFactor;
        this.qualityConfidenceFactor = qualityConfidenceFactor;
        this.qualityMarginReductionFactor = qualityMarginReductionFactor;
        this.environmentDegradationFactor = environmentDegradationFactor;
        this.environmentFatigueAccelerationFactor = environmentFatigueAccelerationFactor;
    }

    public static ChassisCoefficientSet conservativeDefaults() {
        return new ChassisCoefficientSet(
                7800.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d,
                1.0d, 1.0d, 1.0d, 1.0d, 1.0d,
                0.80d, 0.90d, 0.80d, 1.20d,
                0.70d, 1.00d, 0.90d, 1.10d
        );
    }

    public double getMaterialDensityKgM3() { return materialDensityKgM3; }
    public double getMaterialStrengthIndex() { return materialStrengthIndex; }
    public double getMaterialStiffnessIndex() { return materialStiffnessIndex; }
    public double getMaterialFatigueIndex() { return materialFatigueIndex; }
    public double getMaterialAnisotropyPenaltyFactor() { return materialAnisotropyPenaltyFactor; }
    public double getMaterialBrittlenessPenaltyFactor() { return materialBrittlenessPenaltyFactor; }
    public double getMaterialTemperaturePenaltyFactor() { return materialTemperaturePenaltyFactor; }
    public double getMaterialThermalCyclingPenaltyFactor() { return materialThermalCyclingPenaltyFactor; }
    public double getStructureEffectiveDensityFactor() { return structureEffectiveDensityFactor; }
    public double getStructureSpecificStrengthFactor() { return structureSpecificStrengthFactor; }
    public double getStructureSpecificStiffnessFactor() { return structureSpecificStiffnessFactor; }
    public double getStructureBucklingResistanceFactor() { return structureBucklingResistanceFactor; }
    public double getStructureDamageToleranceFactor() { return structureDamageToleranceFactor; }
    public double getManufacturingQualityFactor() { return manufacturingQualityFactor; }
    public double getManufacturingDefectPenaltyFactor() { return manufacturingDefectPenaltyFactor; }
    public double getAssemblyJointEfficiencyFactor() { return assemblyJointEfficiencyFactor; }
    public double getAssemblyStressConcentrationPenaltyFactor() { return assemblyStressConcentrationPenaltyFactor; }
    public double getQualityConfidenceFactor() { return qualityConfidenceFactor; }
    public double getQualityMarginReductionFactor() { return qualityMarginReductionFactor; }
    public double getEnvironmentDegradationFactor() { return environmentDegradationFactor; }
    public double getEnvironmentFatigueAccelerationFactor() { return environmentFatigueAccelerationFactor; }
}
