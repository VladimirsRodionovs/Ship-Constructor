package org.example.shipconstructor.chassis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralLoadCaseSet {
    private final List<StructuralLoadCase> loadCases;

    public StructuralLoadCaseSet(List<StructuralLoadCase> loadCases) {
        this.loadCases = loadCases == null
                ? Collections.<StructuralLoadCase>emptyList()
                : Collections.unmodifiableList(new ArrayList<StructuralLoadCase>(loadCases));
    }

    public List<StructuralLoadCase> getLoadCases() {
        return loadCases;
    }

    public boolean isEmpty() {
        return loadCases.isEmpty();
    }
}
