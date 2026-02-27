package org.example.shipconstructor.chassis.graph.domain;

public class StructuralGraphMember {
    private final String id;
    private final String name;
    private final StructuralMemberType type;
    private final StructuralMemberRole role;
    private final String nodeAId;
    private final String nodeBId;

    public StructuralGraphMember(String id, String name, StructuralMemberType type, StructuralMemberRole role, String nodeAId, String nodeBId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.role = role;
        this.nodeAId = nodeAId;
        this.nodeBId = nodeBId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public StructuralMemberType getType() { return type; }
    public StructuralMemberRole getRole() { return role; }
    public String getNodeAId() { return nodeAId; }
    public String getNodeBId() { return nodeBId; }
}
