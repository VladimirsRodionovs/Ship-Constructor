package org.example.shipconstructor.chassis.graph.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraphLoadSet {
    private final List<StructuralGraphLoad> loads;
    private final List<String> warnings;

    public StructuralGraphLoadSet(List<StructuralGraphLoad> loads, List<String> warnings) {
        this.loads = loads == null ? Collections.<StructuralGraphLoad>emptyList() : Collections.unmodifiableList(new ArrayList<StructuralGraphLoad>(loads));
        this.warnings = warnings == null ? Collections.<String>emptyList() : Collections.unmodifiableList(new ArrayList<String>(warnings));
    }

    public List<StructuralGraphLoad> getLoads() { return loads; }
    public List<String> getWarnings() { return warnings; }
}
