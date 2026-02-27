package org.example.shipconstructor.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ModuleStorageProfile {
    private final Map<StorageType, Double> capacitiesM3;
    private final double infrastructureVolumeM3;

    public ModuleStorageProfile(Map<StorageType, Double> capacitiesM3, double infrastructureVolumeM3) {
        EnumMap<StorageType, Double> copy = new EnumMap<StorageType, Double>(StorageType.class);
        if (capacitiesM3 != null) {
            copy.putAll(capacitiesM3);
        }
        this.capacitiesM3 = Collections.unmodifiableMap(copy);
        this.infrastructureVolumeM3 = infrastructureVolumeM3;
    }

    public static ModuleStorageProfile empty() {
        return new ModuleStorageProfile(null, 0.0d);
    }

    public Map<StorageType, Double> getCapacitiesM3() {
        return capacitiesM3;
    }

    public double getInfrastructureVolumeM3() {
        return infrastructureVolumeM3;
    }

    public double getCapacity(StorageType type) {
        Double value = capacitiesM3.get(type);
        return value == null ? 0.0d : value.doubleValue();
    }
}
