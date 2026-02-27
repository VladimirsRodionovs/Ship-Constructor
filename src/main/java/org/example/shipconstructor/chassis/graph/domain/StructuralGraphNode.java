package org.example.shipconstructor.chassis.graph.domain;

import org.example.shipconstructor.chassis.domain.Vector3;

public class StructuralGraphNode {
    private final String id;
    private final String name;
    private final StructuralNodeType type;
    private final Vector3 positionMeters;

    public StructuralGraphNode(String id, String name, StructuralNodeType type, Vector3 positionMeters) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.positionMeters = positionMeters;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public StructuralNodeType getType() { return type; }
    public Vector3 getPositionMeters() { return positionMeters; }
}
