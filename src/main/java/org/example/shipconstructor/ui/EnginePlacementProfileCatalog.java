package org.example.shipconstructor.ui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EnginePlacementProfileCatalog {
    private static final Map<Integer, String> NAMES;

    static {
        Map<Integer, String> names = new LinkedHashMap<Integer, String>();
        names.put(Integer.valueOf(0), "0 - Unknown / Not set");
        names.put(Integer.valueOf(1), "1 - Aft axial single-line cluster");
        names.put(Integer.valueOf(2), "2 - Aft distributed (rear spread)");
        names.put(Integer.valueOf(3), "3 - Fore axial single-line cluster");
        names.put(Integer.valueOf(4), "4 - Fore distributed (front spread)");
        names.put(Integer.valueOf(5), "5 - Midship axial single-line");
        names.put(Integer.valueOf(6), "6 - Midship distributed");
        names.put(Integer.valueOf(7), "7 - Fore+Mid+Aft axial chain");
        names.put(Integer.valueOf(8), "8 - Fore+Mid+Aft distributed chain");
        names.put(Integer.valueOf(9), "9 - Full-length axial chain");
        names.put(Integer.valueOf(10), "10 - Full-length distributed chain");
        names.put(Integer.valueOf(11), "11 - Offset/asymmetric pods");
        names.put(Integer.valueOf(12), "12 - Hull-wide distributed field");
        names.put(Integer.valueOf(13), "13 - Modular reconfigurable internal array");
        NAMES = Collections.unmodifiableMap(names);
    }

    private EnginePlacementProfileCatalog() {
    }

    public static Map<Integer, String> names() {
        return NAMES;
    }

    public static String displayName(int value) {
        String name = NAMES.get(Integer.valueOf(value));
        return name == null ? String.valueOf(value) : name;
    }
}
