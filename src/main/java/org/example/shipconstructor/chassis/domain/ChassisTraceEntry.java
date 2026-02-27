package org.example.shipconstructor.chassis.domain;

public class ChassisTraceEntry {
    private final String stage;
    private final String key;
    private final String note;
    private final Double value;

    public ChassisTraceEntry(String stage, String key, Double value, String note) {
        this.stage = stage;
        this.key = key;
        this.value = value;
        this.note = note;
    }

    public String getStage() { return stage; }
    public String getKey() { return key; }
    public String getNote() { return note; }
    public Double getValue() { return value; }
}
