package org.example.shipconstructor.ui;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class SizeTypeCatalog {
    public static final class SizeTypeInfo {
        private final int index;
        private final String name;
        private final int cubeEdgeMeters;

        public SizeTypeInfo(int index, String name, int cubeEdgeMeters) {
            this.index = index;
            this.name = name;
            this.cubeEdgeMeters = cubeEdgeMeters;
        }

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }

        public int getCubeEdgeMeters() {
            return cubeEdgeMeters;
        }
    }

    private static final Map<Integer, SizeTypeInfo> VALUES = build();

    private SizeTypeCatalog() {
    }

    public static Map<Integer, SizeTypeInfo> values() {
        return Collections.unmodifiableMap(VALUES);
    }

    public static String displayName(int sizeType) {
        SizeTypeInfo info = VALUES.get(Integer.valueOf(sizeType));
        if (info == null) {
            return sizeType + " - Unknown";
        }
        return info.getIndex() + " - " + info.getName() + " (" + info.getCubeEdgeMeters() + " m cube)";
    }

    private static Map<Integer, SizeTypeInfo> build() {
        Map<Integer, SizeTypeInfo> map = new LinkedHashMap<Integer, SizeTypeInfo>();
        map.put(Integer.valueOf(1), new SizeTypeInfo(1, "Micro", 1));
        map.put(Integer.valueOf(2), new SizeTypeInfo(2, "Small", 2));
        map.put(Integer.valueOf(3), new SizeTypeInfo(3, "Medium", 4));
        map.put(Integer.valueOf(4), new SizeTypeInfo(4, "Large", 8));
        map.put(Integer.valueOf(5), new SizeTypeInfo(5, "Very Large", 16));
        map.put(Integer.valueOf(6), new SizeTypeInfo(6, "Huge", 32));
        map.put(Integer.valueOf(7), new SizeTypeInfo(7, "Titanic", 64));
        map.put(Integer.valueOf(8), new SizeTypeInfo(8, "Colossal", 128));
        map.put(Integer.valueOf(9), new SizeTypeInfo(9, "Gargantuan", 256));
        map.put(Integer.valueOf(10), new SizeTypeInfo(10, "Ultra", 512));
        map.put(Integer.valueOf(11), new SizeTypeInfo(11, "Mega", 1024));
        map.put(Integer.valueOf(12), new SizeTypeInfo(12, "Giga", 2048));
        map.put(Integer.valueOf(13), new SizeTypeInfo(13, "Tera", 4096));
        map.put(Integer.valueOf(14), new SizeTypeInfo(14, "Peta", 8192));
        map.put(Integer.valueOf(15), new SizeTypeInfo(15, "Exa", 16384));
        return map;
    }
}
