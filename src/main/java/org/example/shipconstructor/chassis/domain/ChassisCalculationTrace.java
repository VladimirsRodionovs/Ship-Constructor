package org.example.shipconstructor.chassis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChassisCalculationTrace {
    private final List<ChassisTraceEntry> entries = new ArrayList<ChassisTraceEntry>();

    public void add(String stage, String key, double value, String note) {
        entries.add(new ChassisTraceEntry(stage, key, Double.valueOf(value), note));
    }

    public void add(String stage, String key, String note) {
        entries.add(new ChassisTraceEntry(stage, key, null, note));
    }

    public List<ChassisTraceEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
