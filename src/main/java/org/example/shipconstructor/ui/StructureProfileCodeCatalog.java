package org.example.shipconstructor.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StructureProfileCodeCatalog {
    private static final Map<String, String> LABELS = build();

    private StructureProfileCodeCatalog() {
    }

    public static List<String> codes() {
        return Collections.unmodifiableList(new ArrayList<String>(LABELS.keySet()));
    }

    public static String displayLabel(String code) {
        if (code == null) {
            return null;
        }
        String normalized = code.trim();
        if (normalized.isEmpty()) {
            return null;
        }
        String suffix = LABELS.get(normalized);
        return suffix == null ? normalized : (normalized + " - " + suffix);
    }

    private static Map<String, String> build() {
        Map<String, String> m = new LinkedHashMap<String, String>();

        m.put("STR_MONOLITHIC_SOLID", "B1-01 Monolithic Solid Section");
        m.put("STR_HOLLOW_SECTION", "B1-02 Hollow Structural Section");
        m.put("STR_PLATE_RIB", "B1-03 Plate-and-Rib Section");
        m.put("STR_CORRUGATED_PANEL", "B1-04 Corrugated Structural Panel");
        m.put("STR_BOX_BEAM", "B1-05 Box Beam");
        m.put("STR_IBEAM_SPAR", "B1-06 I-Beam / Spar");
        m.put("STR_TUBULAR_MEMBER", "B1-07 Tubular Frame Member");

        m.put("STR_RIBBED_FRAME", "B2-01 Ribbed Frame");
        m.put("STR_TRUSS_FRAME", "B2-02 Truss Frame");
        m.put("STR_SPACE_FRAME", "B2-03 Space Frame");
        m.put("STR_REDUNDANT_LOAD_PATH_FRAME", "B2-04 Redundant Load-Path Frame");
        m.put("STR_MODULAR_NODE_LOCK_FRAME", "B2-05 Modular Node-Lock Frame");
        m.put("STR_DAMAGE_ARREST_SEGMENTED", "B2-07 Damage-Arrest Segmented Frame");
        m.put("STR_MORPH_OPT_PRESSURE_FRAME", "B2-08 Morphology-Optimized Pressure Frame");

        m.put("STR_ISOGRID_SHELL", "B3-01 Iso-grid Shell");
        m.put("STR_ORTHOGRID_SHELL", "B3-02 Ortho-grid Shell");
        m.put("STR_STIFFENED_SHELL_PANEL", "B3-03 Stiffened Shell Panel");
        m.put("STR_RING_STRINGER_SHELL", "B3-04 Ring-and-Stringer Shell");
        m.put("STR_GRADED_GRID_SHELL", "B3-05 Graded Grid Shell");
        m.put("STR_THERMAL_COMP_SHELL_GRID", "B3-06 Thermal-Compensated Shell Grid");

        m.put("STR_HONEYCOMB_METAL", "B4-01 Honeycomb Core (metal)");
        m.put("STR_HONEYCOMB_COMPOSITE", "B4-02 Honeycomb Core (composite)");
        m.put("STR_HONEYCOMB_CERAMIC", "B4-03 Honeycomb Core (ceramic)");
        m.put("STR_METAL_FOAM_CORE", "B4-05 Metal Foam Core");
        m.put("STR_CELLULAR_GAS_FILLED", "B4-07 Gas-Filled Cellular");
        m.put("STR_CELLULAR_VACUUM", "B4-08 Vacuum Cellular");

        m.put("STR_SANDWICH_SINGLE_CORE", "B5-01 Sandwich Panel (single core)");
        m.put("STR_SANDWICH_METAL_HONEYCOMB", "B5-02 Sandwich (metal+honeycomb)");
        m.put("STR_SANDWICH_COMP_FOAM", "B5-03 Sandwich (comp+foam)");
        m.put("STR_SANDWICH_MULTI_CORE", "B5-04 Multi-Core Sandwich");
        m.put("STR_SANDWICH_GRADED_CORE", "B5-05 Graded-Core Sandwich");
        m.put("STR_SANDWICH_IMPACT_OPT", "B5-06 Impact-Optimized Sandwich");
        m.put("STR_SANDWICH_THERMAL_ISO_STRUCT", "B5-07 Thermal-Isolation Structural Sandwich");
        m.put("STR_SANDWICH_MICROCHANNEL", "B5-08 Microchannel Structural Sandwich");

        m.put("STR_LATTICE_UNIFORM", "B6-01 Uniform Lattice");
        m.put("STR_LATTICE_TOPO_OPT", "B6-02 Topology-Optimized Lattice");
        m.put("STR_LATTICE_HIERARCHICAL", "B6-03 Hierarchical Lattice");
        m.put("STR_LATTICE_AUXETIC", "B6-04 Auxetic Lattice");
        m.put("STR_HYBRID_LATTICE_SHELL", "B6-05 Hybrid Lattice-Shell");
        m.put("STR_LATTICE_REINFORCED_BEAM", "B6-06 Lattice-Reinforced Beam");
        m.put("STR_LATTICE_REINFORCED_PANEL", "B6-07 Lattice-Reinforced Panel");
        m.put("STR_LATTICE_REDUNDANT_DAMAGE_TOL", "B6-08 Damage-Tolerant Redundant Lattice");
        m.put("STR_LATTICE_CRYO_OPT", "B6-09 Cryo-Optimized Lattice");
        m.put("STR_LATTICE_HIGH_DAMP_META", "B6-10 High-Damping Meta Lattice");

        m.put("STR_CORE_FGM", "B7-01 Functionally Graded Structural Core");
        m.put("STR_CORE_META_DAMP", "B7-02 Metamaterial Damping Core");
        m.put("STR_CORE_META_SHOCK", "B7-03 Metamaterial Shock-Spreading Core");
        m.put("STR_CORE_DIRECTIONAL_STIFFNESS", "B7-04 Directional-Stiffness Core");
        m.put("STR_CORE_THERMAL_BUFFER", "B7-05 Thermal-Gradient Buffer Core");
        m.put("STR_CORE_EMBEDDED_CHANNEL_UTILITY", "B7-06 Embedded-Channel Utility Core");
        m.put("STR_CORE_SENSOR_INTEGRATED", "B7-07 Sensor-Integrated Structural Core");
        m.put("STR_CORE_GAS_DAMPED_META", "B7-08 Gas-Damped Cellular Metacore");

        return m;
    }
}
