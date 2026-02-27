package org.example.shipconstructor.ui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ChassisEngineeringCatalogs {
    private static final Map<Long, String> BASE_MATERIALS = map(
            e(1L, "1 - Structural Steel"),
            e(2L, "2 - HSLA Steel"),
            e(5L, "5 - Aluminum Alloy"),
            e(10L, "10 - Titanium Alloy"),
            e(13L, "13 - Nickel Superalloy"),
            e(27L, "27 - Alumina Ceramic"),
            e(28L, "28 - Silicon Carbide Ceramic"),
            e(35L, "35 - CFRP (legacy temp ID)"),
            e(39L, "39 - CFRP (reference-aligned ID)")
    );

    private static final Map<Long, String> STRUCTURE_TYPES = map(
            e(1L, "1 - Monolithic Solid Section"),
            e(10L, "10 - Truss Frame"),
            e(17L, "17 - Iso-grid Shell"),
            e(42L, "42 - Hierarchical Lattice")
    );

    private static final Map<Long, String> MANUFACTURING = map(
            e(1L, "1 - Casting"),
            e(3L, "3 - Forging"),
            e(24L, "24 - Additive Manufacturing (PBF metal)"),
            e(26L, "26 - Additive + HIP Hybrid Manufacturing"),
            e(42L, "42 - Full In-situ Tomography Closed-Loop Manufacturing"),
            e(43L, "43 - Full Defect-Cartography Guided Manufacturing")
    );

    private static final Map<Long, String> ASSEMBLY = map(
            e(1L, "1 - Bolted Mechanical Assembly"),
            e(8L, "8 - Friction Stir Welding"),
            e(9L, "9 - Diffusion-Bonded Panel Assembly"),
            e(17L, "17 - Structural Adhesive Bonding"),
            e(34L, "34 - Atomic-Interface-Assisted Critical Joint Assembly")
    );

    private static final Map<Long, String> QUALITY = map(
            e(1L, "1 - Basic Industrial QA"),
            e(6L, "6 - Pressure-Hull Reliability QA"),
            e(12L, "12 - Mission-Critical Structural Certification QA"),
            e(19L, "19 - Full Defect Cartography QA"),
            e(25L, "25 - Near-Theoretical Margin Reduction Certified QA")
    );

    private static final Map<Long, String> ENVIRONMENTS = map(
            e(1L, "1 - Low-Radiation Deep Space Vacuum"),
            e(8L, "8 - Extreme Thermal Cycling Orbit"),
            e(21L, "21 - Reactor-Adjacent Structural Radiation Profile"),
            e(31L, "31 - Military High-G Maneuver + Combat Profile"),
            e(34L, "34 - Atmospheric Entry-Capable Hybrid Mission Profile")
    );

    private ChassisEngineeringCatalogs() {
    }

    public static Map<Long, String> baseMaterials() { return BASE_MATERIALS; }
    public static Map<Long, String> structureTypes() { return STRUCTURE_TYPES; }
    public static Map<Long, String> manufacturing() { return MANUFACTURING; }
    public static Map<Long, String> assembly() { return ASSEMBLY; }
    public static Map<Long, String> quality() { return QUALITY; }
    public static Map<Long, String> environments() { return ENVIRONMENTS; }

    @SafeVarargs
    private static Map<Long, String> map(Map.Entry<Long, String>... entries) {
        LinkedHashMap<Long, String> map = new LinkedHashMap<Long, String>();
        for (Map.Entry<Long, String> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map.Entry<Long, String> e(Long id, String name) {
        return new java.util.AbstractMap.SimpleImmutableEntry<Long, String>(id, name);
    }
}
