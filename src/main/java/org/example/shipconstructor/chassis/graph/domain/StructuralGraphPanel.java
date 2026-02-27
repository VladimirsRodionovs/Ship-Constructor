package org.example.shipconstructor.chassis.graph.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraphPanel {
    private final String id;
    private final String name;
    private final StructuralPanelType type;
    private final StructuralPanelRole role;
    private final List<String> boundaryNodeIds;

    public StructuralGraphPanel(String id, String name, StructuralPanelType type, StructuralPanelRole role, List<String> boundaryNodeIds) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.role = role;
        this.boundaryNodeIds = boundaryNodeIds == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(boundaryNodeIds));
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public StructuralPanelType getType() { return type; }
    public StructuralPanelRole getRole() { return role; }
    public List<String> getBoundaryNodeIds() { return boundaryNodeIds; }
}
