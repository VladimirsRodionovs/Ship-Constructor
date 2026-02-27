package org.example.shipconstructor.chassis.service;

import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.ChassisCoefficientSet;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;

public class DefaultChassisCoefficientProvider implements ChassisCoefficientProvider {
    @Override
    public ChassisCoefficientSet resolve(ChassisCalculationInput input) {
        ChassisCoefficientSet base = ChassisCoefficientSet.conservativeDefaults();
        EngineeringStackSelection s = input.getEngineeringStack();
        if (s == null) {
            return base;
        }

        // Test mapping by IDs (temporary): A-F IDs influence coefficient groups.
        // Replace with DB/JSON-backed reference lookup later.
        double materialDensity = mapMaterialDensity(s.getBaseMaterialId(), base.getMaterialDensityKgM3());
        double materialStrength = mapMaterialStrengthIndex(s.getBaseMaterialId(), base.getMaterialStrengthIndex());
        double materialStiffness = mapMaterialStiffnessIndex(s.getBaseMaterialId(), base.getMaterialStiffnessIndex());
        double materialFatigue = mapMaterialFatigueIndex(s.getBaseMaterialId(), base.getMaterialFatigueIndex());
        double materialAnisotropyPenalty = mapMaterialAnisotropyPenalty(s.getBaseMaterialId(), base.getMaterialAnisotropyPenaltyFactor());
        double materialBrittlenessPenalty = mapMaterialBrittlenessPenalty(s.getBaseMaterialId(), base.getMaterialBrittlenessPenaltyFactor());
        double materialTemperaturePenalty = mapMaterialTemperaturePenalty(s.getBaseMaterialId(), base.getMaterialTemperaturePenaltyFactor());
        double materialThermalCyclingPenalty = mapMaterialThermalCyclingPenalty(s.getBaseMaterialId(), base.getMaterialThermalCyclingPenaltyFactor());

        double structureDensity = mapStructureEffectiveDensityFactor(s.getStructureTypeId(), base.getStructureEffectiveDensityFactor());
        double structureStrength = mapStructureSpecificStrengthFactor(s.getStructureTypeId(), base.getStructureSpecificStrengthFactor());
        double structureStiffness = mapStructureSpecificStiffnessFactor(s.getStructureTypeId(), base.getStructureSpecificStiffnessFactor());
        double structureBuckling = mapStructureBucklingFactor(s.getStructureTypeId(), base.getStructureBucklingResistanceFactor());
        double structureDamage = mapStructureDamageTolerance(s.getStructureTypeId(), base.getStructureDamageToleranceFactor());

        double mfgQuality = mapManufacturingQuality(s.getManufacturingProcessId(), base.getManufacturingQualityFactor());
        double mfgDefect = mapManufacturingDefectPenalty(s.getManufacturingProcessId(), base.getManufacturingDefectPenaltyFactor());
        double asmJointEff = mapAssemblyJointEfficiency(s.getAssemblyProcessId(), base.getAssemblyJointEfficiencyFactor());
        double asmStressPenalty = mapAssemblyStressConcentrationPenalty(s.getAssemblyProcessId(), base.getAssemblyStressConcentrationPenaltyFactor());

        double qaConfidence = mapQualityConfidence(s.getQualityProfileId(), base.getQualityConfidenceFactor());
        double qaMarginReduction = mapQualityMarginReduction(s.getQualityProfileId(), base.getQualityMarginReductionFactor());
        double envDegradation = mapEnvironmentDegradation(s.getEnvironmentProfileId(), base.getEnvironmentDegradationFactor());
        double envFatigue = mapEnvironmentFatigueAcceleration(s.getEnvironmentProfileId(), base.getEnvironmentFatigueAccelerationFactor());

        return new ChassisCoefficientSet(
                materialDensity,
                materialStrength,
                materialStiffness,
                materialFatigue,
                materialAnisotropyPenalty,
                materialBrittlenessPenalty,
                materialTemperaturePenalty,
                materialThermalCyclingPenalty,
                structureDensity,
                structureStrength,
                structureStiffness,
                structureBuckling,
                structureDamage,
                mfgQuality,
                mfgDefect,
                asmJointEff,
                asmStressPenalty,
                qaConfidence,
                qaMarginReduction,
                envDegradation,
                envFatigue
        );
    }

    private double mapMaterialDensity(Long id, double fallback) {
        if (id == null) {
            return fallback;
        }
        long v = id.longValue();
        if (v == 1L) { return 7850.0d; }  // structural steel
        if (v == 2L) { return 7850.0d; }  // HSLA steel
        if (v == 5L) { return 2700.0d; }  // aluminum alloy
        if (v == 10L) { return 4450.0d; } // titanium alloy
        if (v == 13L) { return 8210.0d; } // nickel superalloy / Inconel-718-like
        if (v == 27L) { return 3850.0d; } // alumina ceramic (simplified)
        if (v == 28L) { return 3000.0d; } // silicon carbide ceramic (simplified)
        if (v == 35L || v == 39L) { return 1575.0d; } // CFRP-like (legacy/ref IDs)
        if (v == 59L) { return 4300.0d; } // nanograined titanium-like
        return fallback;
    }

    private double mapMaterialStrengthIndex(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 1.00d; }
        if (v == 2L) { return 2.70d; } // HSLA (Strenx-700-class baseline)
        if (v == 5L) { return 1.10d; } // Al alloy (6061-T6-like baseline vs A36-class steel)
        if (v == 10L) { return 3.40d; } // Ti alloy (Ti-6Al-4V-like baseline)
        if (v == 13L) { return 4.10d; } // Inconel-718-like baseline
        if (v == 27L) { return 1.20d; } // Alumina simplified isotropic/chassis placeholder
        if (v == 28L) { return 1.00d; } // SiC simplified flexural-based placeholder
        if (v == 35L || v == 39L) { return 1.80d; } // CFRP averaged/clamped directional equivalent
        if (v == 57L) { return 1.45d; } // nanograined steel-like
        if (v == 59L) { return 1.55d; } // nanograined Ti-like
        return fallback;
    }

    private double mapMaterialStiffnessIndex(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 5L) { return 0.34d; }  // aluminum lower modulus (6061-T6-like)
        if (v == 10L) { return 0.56d; } // titanium lower modulus than steel
        if (v == 13L) { return 1.00d; } // Inconel-718-like
        if (v == 27L) { return 1.70d; } // Alumina high stiffness but brittle
        if (v == 28L) { return 1.90d; } // SiC high stiffness but brittle
        if (v == 35L || v == 39L) { return 0.65d; } // CFRP averaged/clamped anisotropy
        if (v == 50L) { return 0.85d; } // MMC general
        return fallback;
    }

    private double mapMaterialFatigueIndex(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 2L) { return 1.10d; }  // HSLA
        if (v == 10L) { return 1.20d; } // Ti alloy
        if (v == 13L) { return 1.10d; } // superalloy baseline (application dependent)
        if (v == 27L) { return 0.45d; } // ceramic brittle/cycle-sensitive for chassis use
        if (v == 28L) { return 0.40d; } // ceramic brittle/cycle-sensitive for chassis use
        if (v == 35L || v == 39L) { return 0.90d; } // CFRP depends on laminate, joints and impact
        if (v == 48L) { return 1.15d; } // radiation-tailored matrix advanced
        return fallback;
    }

    private double mapMaterialAnisotropyPenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 35L || v == 39L) { return 0.78d; } // CFRP averaged to chassis-global load directions
        if (v == 50L || v == 51L || v == 52L) { return 0.88d; } // MMC/CMC/carbon-carbon classes can be process-direction sensitive
        return fallback;
    }

    private double mapMaterialBrittlenessPenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 27L) { return 0.55d; } // alumina
        if (v == 28L) { return 0.60d; } // SiC
        if (v == 29L) { return 0.50d; } // B4C
        if (v == 30L) { return 0.70d; } // zirconia tougher ceramic
        if (v == 35L || v == 39L) { return 0.90d; } // CFRP impact/delamination sensitivity in chassis context
        if (v == 51L) { return 0.75d; } // CMC
        return fallback;
    }

    private double mapMaterialTemperaturePenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 5L) { return 0.88d; }  // aluminum loses high-temp margin sooner
        if (v == 10L) { return 0.95d; } // titanium generally good mid-high temp (not superalloy hot-zone)
        if (v == 13L) { return 1.00d; } // nickel superalloy hot baseline
        if (v == 27L) { return 0.92d; } // alumina can handle temp but interfaces/thermal shock matter
        if (v == 28L) { return 0.96d; } // SiC excellent temp potential (joining still separate issue)
        if (v == 35L || v == 39L) { return 0.80d; } // CFRP matrix-limited unless advanced matrix selected
        return fallback;
    }

    private double mapMaterialThermalCyclingPenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 5L) { return 0.92d; }  // aluminum thermal fatigue sensitivity
        if (v == 10L) { return 0.97d; } // titanium generally good
        if (v == 13L) { return 0.98d; } // Inconel-like hot-cycle capable
        if (v == 27L) { return 0.72d; } // alumina thermal shock/interface risk
        if (v == 28L) { return 0.78d; } // SiC better than alumina but still brittle-class
        if (v == 35L || v == 39L) { return 0.82d; } // CFRP matrix/interface cycling limits
        return fallback;
    }

    private double mapStructureEffectiveDensityFactor(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 1.00d; } // monolithic solid
        if (v == 2L) { return 0.72d; } // hollow
        if (v == 9L) { return 0.70d; } // ribbed frame
        if (v == 10L) { return 0.62d; } // truss
        if (v == 26L) { return 0.22d; } // metal honeycomb core
        if (v == 30L) { return 0.38d; } // sandwich panel structural shell/core system
        if (v == 40L) { return 0.28d; } // uniform lattice
        if (v == 42L) { return 0.18d; } // hierarchical lattice
        return fallback;
    }

    private double mapStructureSpecificStrengthFactor(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 1.00d; }
        if (v == 10L) { return 1.15d; } // truss
        if (v == 11L) { return 1.20d; } // space frame
        if (v == 17L) { return 1.18d; } // isogrid shell
        if (v == 30L) { return 1.12d; } // sandwich panel, good specific strength if joints are good
        if (v == 33L) { return 1.10d; } // multi-core sandwich
        if (v == 41L) { return 1.25d; } // topo lattice
        if (v == 42L) { return 1.28d; } // hierarchical lattice
        return fallback;
    }

    private double mapStructureSpecificStiffnessFactor(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 2L) { return 1.10d; }  // hollow section
        if (v == 5L) { return 1.15d; }  // box beam
        if (v == 17L) { return 1.20d; } // isogrid shell
        if (v == 26L) { return 1.18d; } // honeycomb core
        if (v == 30L) { return 1.20d; } // single-core sandwich
        if (v == 41L) { return 1.22d; } // topo lattice
        return fallback;
    }

    private double mapStructureBucklingFactor(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 1.00d; }
        if (v == 5L) { return 1.10d; }
        if (v == 10L) { return 0.95d; } // truss can be buckling-limited
        if (v == 17L) { return 1.20d; }
        if (v == 18L) { return 1.15d; }
        if (v == 30L) { return 0.92d; } // sandwich can be local-buckling/face wrinkling limited
        if (v == 42L) { return 1.05d; } // depends heavily on quality
        return fallback;
    }

    private double mapStructureDamageTolerance(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 12L) { return 1.25d; } // redundant frame
        if (v == 15L) { return 1.20d; } // damage-arrest segmented frame
        if (v == 34L) { return 1.20d; } // impact-optimized sandwich
        if (v == 30L) { return 0.95d; } // baseline sandwich, damage tolerance depends on core/facesheet system
        if (v == 47L) { return 1.25d; } // damage-tolerant lattice
        return fallback;
    }

    private double mapManufacturingQuality(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 0.72d; } // casting
        if (v == 3L) { return 0.86d; } // forging
        if (v == 11L) { return 0.90d; } // precision CNC
        if (v == 24L) { return 0.84d; } // AM PBF metal
        if (v == 26L) { return 0.90d; } // AM + HIP
        if (v == 42L) { return 0.95d; } // full in-situ tomography closed loop
        if (v == 43L) { return 0.97d; } // full defect cartography guided mfg
        return fallback;
    }

    private double mapManufacturingDefectPenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 0.80d; }
        if (v == 3L) { return 0.93d; }
        if (v == 24L) { return 0.82d; }
        if (v == 26L) { return 0.92d; }
        if (v == 42L) { return 0.96d; }
        if (v == 43L) { return 0.98d; }
        return fallback;
    }

    private double mapAssemblyJointEfficiency(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 0.74d; } // bolted
        if (v == 5L) { return 0.82d; } // arc weld
        if (v == 8L) { return 0.90d; } // friction stir weld
        if (v == 9L) { return 0.94d; } // diffusion bonded panels
        if (v == 17L) { return 0.86d; } // structural adhesive
        if (v == 34L) { return 0.97d; } // atomic-interface-assisted critical joint
        return fallback;
    }

    private double mapAssemblyStressConcentrationPenalty(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 1.25d; } // bolted holes
        if (v == 2L) { return 1.20d; } // riveted
        if (v == 8L) { return 1.08d; } // FSW
        if (v == 9L) { return 1.04d; } // diffusion bond
        if (v == 17L) { return 1.10d; } // adhesive
        if (v == 34L) { return 1.02d; } // advanced critical joints
        return fallback;
    }

    private double mapQualityConfidence(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 1L) { return 0.55d; } // basic industrial
        if (v == 6L) { return 0.74d; } // pressure hull reliability QA
        if (v == 12L) { return 0.84d; } // mission critical
        if (v == 15L) { return 0.88d; } // predictive life cert
        if (v == 19L) { return 0.94d; } // full defect cartography
        if (v == 25L) { return 0.97d; } // near-theoretical margin cert
        return fallback;
    }

    private double mapQualityMarginReduction(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v <= 5L) { return 0.20d; }
        if (v <= 12L) { return 0.45d; }
        if (v <= 18L) { return 0.65d; }
        if (v <= 24L) { return 0.82d; }
        return 0.90d;
    }

    private double mapEnvironmentDegradation(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v >= 1L && v <= 5L) { return 0.94d; }   // baseline vac variants
        if (v >= 6L && v <= 11L) { return 0.88d; }  // thermal extremes
        if (v >= 12L && v <= 17L) { return 0.84d; } // corrosive/atmo
        if (v >= 18L && v <= 22L) { return 0.86d; } // radiation
        if (v >= 23L && v <= 28L) { return 0.87d; } // abrasive
        if (v >= 29L && v <= 34L) { return 0.82d; } // mixed missions tougher
        return fallback;
    }

    private double mapEnvironmentFatigueAcceleration(Long id, double fallback) {
        if (id == null) { return fallback; }
        long v = id.longValue();
        if (v == 8L || v == 11L) { return 1.25d; }  // thermal cycling / thermal shock
        if (v == 21L) { return 1.20d; }            // reactor-adjacent
        if (v == 31L) { return 1.35d; }            // military high-g combat
        if (v == 34L) { return 1.30d; }            // atmospheric-entry hybrid
        return fallback;
    }
}
