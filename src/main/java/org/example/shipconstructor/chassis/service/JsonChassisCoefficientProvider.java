package org.example.shipconstructor.chassis.service;

import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.ChassisCoefficientSet;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;

import java.nio.file.Path;

public class JsonChassisCoefficientProvider implements ChassisCoefficientProvider {
    private final ChassisCoefficientLibrary library;

    public JsonChassisCoefficientProvider(ChassisCoefficientLibrary library) {
        if (library == null) {
            throw new IllegalArgumentException("library is required");
        }
        this.library = library;
    }

    public static JsonChassisCoefficientProvider fromPath(Path path) throws Exception {
        ChassisCoefficientLibraryLoader loader = new ChassisCoefficientLibraryLoader();
        return new JsonChassisCoefficientProvider(loader.load(path));
    }

    @Override
    public ChassisCoefficientSet resolve(ChassisCalculationInput input) {
        ChassisCoefficientSet fallback = ChassisCoefficientSet.conservativeDefaults();
        EngineeringStackSelection s = input.getEngineeringStack();
        if (s == null) {
            return fallback;
        }

        ChassisCoefficientLibrary.MaterialProfile m = get(library.getMaterialProfiles(), s.getBaseMaterialId());
        ChassisCoefficientLibrary.StructureProfile b = get(library.getStructureProfiles(), s.getStructureTypeId());
        ChassisCoefficientLibrary.ManufacturingProfile c = get(library.getManufacturingProfiles(), s.getManufacturingProcessId());
        ChassisCoefficientLibrary.AssemblyProfile d = get(library.getAssemblyProfiles(), s.getAssemblyProcessId());
        ChassisCoefficientLibrary.QualityProfile e = get(library.getQualityProfiles(), s.getQualityProfileId());
        ChassisCoefficientLibrary.EnvironmentProfile f = get(library.getEnvironmentProfiles(), s.getEnvironmentProfileId());

        return new ChassisCoefficientSet(
                pick(m == null ? null : m.materialDensityKgM3, fallback.getMaterialDensityKgM3()),
                pick(m == null ? null : m.materialStrengthIndex, fallback.getMaterialStrengthIndex()),
                pick(m == null ? null : m.materialStiffnessIndex, fallback.getMaterialStiffnessIndex()),
                pick(m == null ? null : m.materialFatigueIndex, fallback.getMaterialFatigueIndex()),
                pick(m == null ? null : m.materialAnisotropyPenaltyFactor, fallback.getMaterialAnisotropyPenaltyFactor()),
                pick(m == null ? null : m.materialBrittlenessPenaltyFactor, fallback.getMaterialBrittlenessPenaltyFactor()),
                pick(m == null ? null : m.materialTemperaturePenaltyFactor, fallback.getMaterialTemperaturePenaltyFactor()),
                pick(m == null ? null : m.materialThermalCyclingPenaltyFactor, fallback.getMaterialThermalCyclingPenaltyFactor()),

                pick(b == null ? null : b.structureEffectiveDensityFactor, fallback.getStructureEffectiveDensityFactor()),
                pick(b == null ? null : b.structureSpecificStrengthFactor, fallback.getStructureSpecificStrengthFactor()),
                pick(b == null ? null : b.structureSpecificStiffnessFactor, fallback.getStructureSpecificStiffnessFactor()),
                pick(b == null ? null : b.structureBucklingResistanceFactor, fallback.getStructureBucklingResistanceFactor()),
                pick(b == null ? null : b.structureDamageToleranceFactor, fallback.getStructureDamageToleranceFactor()),

                pick(c == null ? null : c.manufacturingQualityFactor, fallback.getManufacturingQualityFactor()),
                pick(c == null ? null : c.manufacturingDefectPenaltyFactor, fallback.getManufacturingDefectPenaltyFactor()),
                pick(d == null ? null : d.assemblyJointEfficiencyFactor, fallback.getAssemblyJointEfficiencyFactor()),
                pick(d == null ? null : d.assemblyStressConcentrationPenaltyFactor, fallback.getAssemblyStressConcentrationPenaltyFactor()),

                pick(e == null ? null : e.qualityConfidenceFactor, fallback.getQualityConfidenceFactor()),
                pick(e == null ? null : e.qualityMarginReductionFactor, fallback.getQualityMarginReductionFactor()),
                pick(f == null ? null : f.environmentDegradationFactor, fallback.getEnvironmentDegradationFactor()),
                pick(f == null ? null : f.environmentFatigueAccelerationFactor, fallback.getEnvironmentFatigueAccelerationFactor())
        );
    }

    private static <T> T get(java.util.Map<String, T> map, Long id) {
        if (map == null || id == null) {
            return null;
        }
        return map.get(String.valueOf(id.longValue()));
    }

    private static double pick(Double value, double fallback) {
        return value == null ? fallback : value.doubleValue();
    }
}
