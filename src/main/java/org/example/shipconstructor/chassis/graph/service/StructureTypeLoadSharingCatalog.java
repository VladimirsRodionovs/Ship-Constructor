package org.example.shipconstructor.chassis.graph.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class StructureTypeLoadSharingCatalog {

    static final class LoadSharingProfile {
        private final String profileKey;
        private final double memberShare;
        private final double panelShare;
        private final String label;

        LoadSharingProfile(String profileKey, double memberShare, double panelShare, String label) {
            this.profileKey = profileKey;
            double m = Math.max(0.0d, Math.min(1.0d, memberShare));
            double p = Math.max(0.0d, Math.min(1.0d, panelShare));
            double s = m + p;
            if (s <= 1e-9d) {
                m = 0.8d;
                p = 0.2d;
                s = 1.0d;
            }
            this.memberShare = m / s;
            this.panelShare = p / s;
            this.label = label;
        }

        String getProfileKey() { return profileKey; }
        double getMemberShare() { return memberShare; }
        double getPanelShare() { return panelShare; }
        String getLabel() { return label; }
    }

    private static final LoadSharingProfile DEFAULT = new LoadSharingProfile("B_FAMILY_DEFAULT", 0.80d, 0.20d, "frame-heavy default");
    private static final Map<String, LoadSharingProfile> PROFILES = buildProfiles();
    private static final Map<Long, String> PROFILE_KEY_BY_STRUCTURE_ID = buildStructureAliases();

    private StructureTypeLoadSharingCatalog() {
    }

    static LoadSharingProfile resolve(Long structureTypeId) {
        if (structureTypeId == null) {
            return DEFAULT;
        }
        String profileKey = PROFILE_KEY_BY_STRUCTURE_ID.get(structureTypeId);
        if (profileKey == null) {
            return DEFAULT;
        }
        LoadSharingProfile p = PROFILES.get(profileKey);
        return p == null ? DEFAULT : p;
    }

    static LoadSharingProfile resolveByProfileKey(String profileKey) {
        if (profileKey == null || profileKey.trim().isEmpty()) {
            return DEFAULT;
        }
        LoadSharingProfile p = PROFILES.get(profileKey.trim());
        return p == null ? DEFAULT : p;
    }

    static Map<String, LoadSharingProfile> profiles() {
        return Collections.unmodifiableMap(PROFILES);
    }

    static Map<Long, String> structureAliases() {
        return Collections.unmodifiableMap(PROFILE_KEY_BY_STRUCTURE_ID);
    }

    private static Map<String, LoadSharingProfile> buildProfiles() {
        Map<String, LoadSharingProfile> m = new HashMap<String, LoadSharingProfile>();

        // Family defaults (used until a more specific concrete entry is mapped).
        m.put("B1_GENERIC_SECTION", new LoadSharingProfile("B1_GENERIC_SECTION", 0.90d, 0.10d, "B1 monolithic/basic sections"));
        m.put("B2_GENERIC_FRAME", new LoadSharingProfile("B2_GENERIC_FRAME", 0.88d, 0.12d, "B2 frame/truss/space-frame"));
        m.put("B3_GENERIC_SHELL_GRID", new LoadSharingProfile("B3_GENERIC_SHELL_GRID", 0.60d, 0.40d, "B3 shell-grid family"));
        m.put("B4_GENERIC_CELLULAR_CORE", new LoadSharingProfile("B4_GENERIC_CELLULAR_CORE", 0.58d, 0.42d, "B4 cellular/honeycomb family"));
        m.put("B5_GENERIC_SANDWICH", new LoadSharingProfile("B5_GENERIC_SANDWICH", 0.55d, 0.45d, "B5 sandwich family"));
        m.put("B6_GENERIC_LATTICE", new LoadSharingProfile("B6_GENERIC_LATTICE", 0.70d, 0.30d, "B6 lattice family"));
        m.put("B7_GENERIC_METACORE", new LoadSharingProfile("B7_GENERIC_METACORE", 0.62d, 0.38d, "B7 graded/metacore family"));

        // Concrete profiles aligned to ENGINEERING_REFERENCE B detailed entries / codes.
        m.put("STR_MONOLITHIC_SOLID", new LoadSharingProfile("STR_MONOLITHIC_SOLID", 0.93d, 0.07d, "B1-01 Monolithic Solid Section"));
        m.put("STR_HOLLOW_SECTION", new LoadSharingProfile("STR_HOLLOW_SECTION", 0.90d, 0.10d, "B1-02 Hollow Structural Section"));
        m.put("STR_BOX_BEAM", new LoadSharingProfile("STR_BOX_BEAM", 0.89d, 0.11d, "B1-05 Box Beam"));
        m.put("STR_TUBULAR_MEMBER", new LoadSharingProfile("STR_TUBULAR_MEMBER", 0.90d, 0.10d, "B1-07 Tubular Frame Member"));
        m.put("STR_PLATE_RIB", new LoadSharingProfile("STR_PLATE_RIB", 0.84d, 0.16d, "B1-03 Plate-and-Rib Section"));
        m.put("STR_CORRUGATED_PANEL", new LoadSharingProfile("STR_CORRUGATED_PANEL", 0.72d, 0.28d, "B1-04 Corrugated Structural Panel"));
        m.put("STR_IBEAM_SPAR", new LoadSharingProfile("STR_IBEAM_SPAR", 0.91d, 0.09d, "B1-06 I-Beam / Spar"));
        m.put("STR_LAMINATED_PLATE_METAL", new LoadSharingProfile("STR_LAMINATED_PLATE_METAL", 0.76d, 0.24d, "B1-08 Laminated Plate (metal stack)"));

        m.put("STR_RIBBED_FRAME", new LoadSharingProfile("STR_RIBBED_FRAME", 0.84d, 0.16d, "B2-01 Ribbed Frame"));
        m.put("STR_TRUSS_FRAME", new LoadSharingProfile("STR_TRUSS_FRAME", 0.88d, 0.12d, "B2-02 Truss Frame"));
        m.put("STR_SPACE_FRAME", new LoadSharingProfile("STR_SPACE_FRAME", 0.85d, 0.15d, "B2-03 Space Frame"));
        m.put("STR_REDUNDANT_LOAD_PATH_FRAME", new LoadSharingProfile("STR_REDUNDANT_LOAD_PATH_FRAME", 0.82d, 0.18d, "B2-04 Redundant Load-Path Frame"));
        m.put("STR_MODULAR_NODE_LOCK_FRAME", new LoadSharingProfile("STR_MODULAR_NODE_LOCK_FRAME", 0.83d, 0.17d, "B2-05 Modular Node-Lock Frame"));
        m.put("STR_TENSEGRITY_ASSISTED", new LoadSharingProfile("STR_TENSEGRITY_ASSISTED", 0.78d, 0.22d, "B2-06 Tensegrity-Assisted Frame"));
        m.put("STR_DAMAGE_ARREST_SEGMENTED", new LoadSharingProfile("STR_DAMAGE_ARREST_SEGMENTED", 0.80d, 0.20d, "B2-07 Damage-Arrest Segmented Frame"));
        m.put("STR_MORPH_OPT_PRESSURE_FRAME", new LoadSharingProfile("STR_MORPH_OPT_PRESSURE_FRAME", 0.76d, 0.24d, "B2-08 Morphology-Optimized Pressure Frame"));

        m.put("STR_ISOGRID_SHELL", new LoadSharingProfile("STR_ISOGRID_SHELL", 0.60d, 0.40d, "B3-01 Iso-grid Shell"));
        m.put("STR_ORTHOGRID_SHELL", new LoadSharingProfile("STR_ORTHOGRID_SHELL", 0.62d, 0.38d, "B3-02 Ortho-grid Shell"));
        m.put("STR_STIFFENED_SHELL_PANEL", new LoadSharingProfile("STR_STIFFENED_SHELL_PANEL", 0.68d, 0.32d, "B3-03 Stiffened Shell Panel"));
        m.put("STR_RING_STRINGER_SHELL", new LoadSharingProfile("STR_RING_STRINGER_SHELL", 0.70d, 0.30d, "B3-04 Ring-and-Stringer Shell"));
        m.put("STR_GRADED_GRID_SHELL", new LoadSharingProfile("STR_GRADED_GRID_SHELL", 0.58d, 0.42d, "B3-05 Graded Grid Shell"));
        m.put("STR_THERMAL_COMP_SHELL_GRID", new LoadSharingProfile("STR_THERMAL_COMP_SHELL_GRID", 0.57d, 0.43d, "B3-06 Thermal-Strain-Compensated Shell Grid"));

        m.put("STR_HONEYCOMB_METAL", new LoadSharingProfile("STR_HONEYCOMB_METAL", 0.62d, 0.38d, "B4-01 Honeycomb Core (metal)"));
        m.put("STR_HONEYCOMB_COMPOSITE", new LoadSharingProfile("STR_HONEYCOMB_COMPOSITE", 0.58d, 0.42d, "B4-02 Honeycomb Core (composite)"));
        m.put("STR_HONEYCOMB_CERAMIC", new LoadSharingProfile("STR_HONEYCOMB_CERAMIC", 0.55d, 0.45d, "B4-03 Honeycomb Core (ceramic)"));
        m.put("STR_HONEYCOMB_GRADED", new LoadSharingProfile("STR_HONEYCOMB_GRADED", 0.56d, 0.44d, "B4-04 Graded Honeycomb Core"));
        m.put("STR_METAL_FOAM_CORE", new LoadSharingProfile("STR_METAL_FOAM_CORE", 0.60d, 0.40d, "B4-05 Cellular Foam Core (metal)"));
        m.put("STR_CERAMIC_FOAM_CORE", new LoadSharingProfile("STR_CERAMIC_FOAM_CORE", 0.54d, 0.46d, "B4-06 Cellular Foam Core (ceramic)"));
        m.put("STR_CELLULAR_GAS_FILLED", new LoadSharingProfile("STR_CELLULAR_GAS_FILLED", 0.56d, 0.44d, "B4-07 Gas-Filled Cellular Structure"));
        m.put("STR_CELLULAR_VACUUM", new LoadSharingProfile("STR_CELLULAR_VACUUM", 0.53d, 0.47d, "B4-08 Vacuum Cellular Structure"));
        m.put("STR_CELLULAR_DAMAGE_ARREST", new LoadSharingProfile("STR_CELLULAR_DAMAGE_ARREST", 0.59d, 0.41d, "B4-09 Damage-Arrest Cellular Core"));

        m.put("STR_SANDWICH_SINGLE_CORE", new LoadSharingProfile("STR_SANDWICH_SINGLE_CORE", 0.58d, 0.42d, "B5-01 Sandwich Panel (single core)"));
        m.put("STR_SANDWICH_METAL_HONEYCOMB", new LoadSharingProfile("STR_SANDWICH_METAL_HONEYCOMB", 0.55d, 0.45d, "B5-02 Sandwich (metal skins + honeycomb core)"));
        m.put("STR_SANDWICH_COMP_FOAM", new LoadSharingProfile("STR_SANDWICH_COMP_FOAM", 0.50d, 0.50d, "B5-03 Sandwich (composite skins + foam core)"));
        m.put("STR_SANDWICH_MULTI_CORE", new LoadSharingProfile("STR_SANDWICH_MULTI_CORE", 0.52d, 0.48d, "B5-04 Multi-Core Sandwich"));
        m.put("STR_SANDWICH_GRADED_CORE", new LoadSharingProfile("STR_SANDWICH_GRADED_CORE", 0.50d, 0.50d, "B5-05 Graded-Core Sandwich"));
        m.put("STR_SANDWICH_IMPACT_OPT", new LoadSharingProfile("STR_SANDWICH_IMPACT_OPT", 0.60d, 0.40d, "B5-06 Impact-Optimized Sandwich"));
        m.put("STR_SANDWICH_THERMAL_ISO_STRUCT", new LoadSharingProfile("STR_SANDWICH_THERMAL_ISO_STRUCT", 0.54d, 0.46d, "B5-07 Thermal-Isolation Structural Sandwich"));
        m.put("STR_SANDWICH_MICROCHANNEL", new LoadSharingProfile("STR_SANDWICH_MICROCHANNEL", 0.53d, 0.47d, "B5-08 Microchannel Structural Sandwich"));

        m.put("STR_LATTICE_UNIFORM", new LoadSharingProfile("STR_LATTICE_UNIFORM", 0.76d, 0.24d, "B6-01 Uniform Lattice"));
        m.put("STR_LATTICE_TOPO_OPT", new LoadSharingProfile("STR_LATTICE_TOPO_OPT", 0.72d, 0.28d, "B6-02 Topology-Optimized Lattice"));
        m.put("STR_LATTICE_HIERARCHICAL", new LoadSharingProfile("STR_LATTICE_HIERARCHICAL", 0.70d, 0.30d, "B6-03 Hierarchical Lattice"));
        m.put("STR_LATTICE_AUXETIC", new LoadSharingProfile("STR_LATTICE_AUXETIC", 0.68d, 0.32d, "B6-04 Auxetic Lattice"));
        m.put("STR_HYBRID_LATTICE_SHELL", new LoadSharingProfile("STR_HYBRID_LATTICE_SHELL", 0.62d, 0.38d, "B6-05 Hybrid Lattice-Shell"));
        m.put("STR_LATTICE_REINFORCED_BEAM", new LoadSharingProfile("STR_LATTICE_REINFORCED_BEAM", 0.78d, 0.22d, "B6-06 Lattice-Reinforced Beam"));
        m.put("STR_LATTICE_REINFORCED_PANEL", new LoadSharingProfile("STR_LATTICE_REINFORCED_PANEL", 0.66d, 0.34d, "B6-07 Lattice-Reinforced Panel"));
        m.put("STR_LATTICE_REDUNDANT_DAMAGE_TOL", new LoadSharingProfile("STR_LATTICE_REDUNDANT_DAMAGE_TOL", 0.72d, 0.28d, "B6-08 Damage-Tolerant Redundant Lattice"));
        m.put("STR_LATTICE_CRYO_OPT", new LoadSharingProfile("STR_LATTICE_CRYO_OPT", 0.74d, 0.26d, "B6-09 Cryo-optimized Lattice Geometry"));
        m.put("STR_LATTICE_HIGH_DAMP_META", new LoadSharingProfile("STR_LATTICE_HIGH_DAMP_META", 0.64d, 0.36d, "B6-10 High-Damping Metageometry Lattice"));

        m.put("STR_CORE_FGM", new LoadSharingProfile("STR_CORE_FGM", 0.60d, 0.40d, "B7-01 Functionally Graded Structural Core"));
        m.put("STR_CORE_META_DAMP", new LoadSharingProfile("STR_CORE_META_DAMP", 0.58d, 0.42d, "B7-02 Metamaterial-Inspired Damping Core"));
        m.put("STR_CORE_META_SHOCK", new LoadSharingProfile("STR_CORE_META_SHOCK", 0.56d, 0.44d, "B7-03 Metamaterial-Inspired Shock-Spreading Core"));
        m.put("STR_CORE_DIRECTIONAL_STIFFNESS", new LoadSharingProfile("STR_CORE_DIRECTIONAL_STIFFNESS", 0.64d, 0.36d, "B7-04 Directional-Stiffness Core"));
        m.put("STR_CORE_THERMAL_BUFFER", new LoadSharingProfile("STR_CORE_THERMAL_BUFFER", 0.55d, 0.45d, "B7-05 Thermal-Gradient Buffer Core"));
        m.put("STR_CORE_EMBEDDED_CHANNEL_UTILITY", new LoadSharingProfile("STR_CORE_EMBEDDED_CHANNEL_UTILITY", 0.57d, 0.43d, "B7-06 Embedded-Channel Utility Core"));
        m.put("STR_CORE_SENSOR_INTEGRATED", new LoadSharingProfile("STR_CORE_SENSOR_INTEGRATED", 0.59d, 0.41d, "B7-07 Sensor-Integrated Structural Core"));
        m.put("STR_CORE_GAS_DAMPED_META", new LoadSharingProfile("STR_CORE_GAS_DAMPED_META", 0.55d, 0.45d, "B7-08 Gas-Damped Cellular Metacore"));

        return m;
    }

    private static Map<Long, String> buildStructureAliases() {
        Map<Long, String> m = new HashMap<Long, String>();

        // Current project/sample IDs -> concrete profile keys.
        m.put(1L, "STR_MONOLITHIC_SOLID");
        m.put(10L, "STR_TRUSS_FRAME");
        m.put(17L, "STR_ISOGRID_SHELL");
        m.put(26L, "STR_ISOGRID_SHELL"); // legacy placeholder alias used in earlier demos
        m.put(30L, "STR_SANDWICH_METAL_HONEYCOMB"); // provisional local ID
        m.put(42L, "STR_LATTICE_HIERARCHICAL");

        return m;
    }
}
