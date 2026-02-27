package org.example.shipconstructor.chassis.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChassisCoefficientLibrary {
    public static class MaterialProfile {
        public String name;
        public Double materialDensityKgM3;
        public Double materialStrengthIndex;
        public Double materialStiffnessIndex;
        public Double materialFatigueIndex;
        public Double materialAnisotropyPenaltyFactor;
        public Double materialBrittlenessPenaltyFactor;
        public Double materialTemperaturePenaltyFactor;
        public Double materialThermalCyclingPenaltyFactor;
    }

    public static class StructureProfile {
        public String name;
        public Double structureEffectiveDensityFactor;
        public Double structureSpecificStrengthFactor;
        public Double structureSpecificStiffnessFactor;
        public Double structureBucklingResistanceFactor;
        public Double structureDamageToleranceFactor;
    }

    public static class ManufacturingProfile {
        public String name;
        public Double manufacturingQualityFactor;
        public Double manufacturingDefectPenaltyFactor;
    }

    public static class AssemblyProfile {
        public String name;
        public Double assemblyJointEfficiencyFactor;
        public Double assemblyStressConcentrationPenaltyFactor;
    }

    public static class QualityProfile {
        public String name;
        public Double qualityConfidenceFactor;
        public Double qualityMarginReductionFactor;
    }

    public static class EnvironmentProfile {
        public String name;
        public Double environmentDegradationFactor;
        public Double environmentFatigueAccelerationFactor;
    }

    public String schema;
    public String description;
    public Map<String, MaterialProfile> material_profiles_by_base_material_id = new LinkedHashMap<String, MaterialProfile>();
    public Map<String, StructureProfile> structure_profiles_by_structure_type_id = new LinkedHashMap<String, StructureProfile>();
    public Map<String, ManufacturingProfile> manufacturing_profiles_by_process_id = new LinkedHashMap<String, ManufacturingProfile>();
    public Map<String, AssemblyProfile> assembly_profiles_by_process_id = new LinkedHashMap<String, AssemblyProfile>();
    public Map<String, QualityProfile> quality_profiles_by_quality_id = new LinkedHashMap<String, QualityProfile>();
    public Map<String, EnvironmentProfile> environment_profiles_by_environment_id = new LinkedHashMap<String, EnvironmentProfile>();

    public Map<String, MaterialProfile> getMaterialProfiles() {
        return material_profiles_by_base_material_id == null
                ? Collections.<String, MaterialProfile>emptyMap()
                : material_profiles_by_base_material_id;
    }

    public Map<String, StructureProfile> getStructureProfiles() {
        return structure_profiles_by_structure_type_id == null
                ? Collections.<String, StructureProfile>emptyMap()
                : structure_profiles_by_structure_type_id;
    }

    public Map<String, ManufacturingProfile> getManufacturingProfiles() {
        return manufacturing_profiles_by_process_id == null
                ? Collections.<String, ManufacturingProfile>emptyMap()
                : manufacturing_profiles_by_process_id;
    }

    public Map<String, AssemblyProfile> getAssemblyProfiles() {
        return assembly_profiles_by_process_id == null
                ? Collections.<String, AssemblyProfile>emptyMap()
                : assembly_profiles_by_process_id;
    }

    public Map<String, QualityProfile> getQualityProfiles() {
        return quality_profiles_by_quality_id == null
                ? Collections.<String, QualityProfile>emptyMap()
                : quality_profiles_by_quality_id;
    }

    public Map<String, EnvironmentProfile> getEnvironmentProfiles() {
        return environment_profiles_by_environment_id == null
                ? Collections.<String, EnvironmentProfile>emptyMap()
                : environment_profiles_by_environment_id;
    }
}
