package org.example.shipconstructor.chassis.graph.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraph {
    private final double cubeEdgeMeters;
    private final double lengthMeters;
    private final double widthMeters;
    private final double heightMeters;
    private final List<StructuralGraphNode> nodes;
    private final List<StructuralGraphMember> members;
    private final List<StructuralGraphPanel> panels;

    public StructuralGraph(
            double cubeEdgeMeters,
            double lengthMeters,
            double widthMeters,
            double heightMeters,
            List<StructuralGraphNode> nodes,
            List<StructuralGraphMember> members,
            List<StructuralGraphPanel> panels) {
        this.cubeEdgeMeters = cubeEdgeMeters;
        this.lengthMeters = lengthMeters;
        this.widthMeters = widthMeters;
        this.heightMeters = heightMeters;
        this.nodes = nodes == null ? Collections.<StructuralGraphNode>emptyList() : Collections.unmodifiableList(new ArrayList<StructuralGraphNode>(nodes));
        this.members = members == null ? Collections.<StructuralGraphMember>emptyList() : Collections.unmodifiableList(new ArrayList<StructuralGraphMember>(members));
        this.panels = panels == null ? Collections.<StructuralGraphPanel>emptyList() : Collections.unmodifiableList(new ArrayList<StructuralGraphPanel>(panels));
    }

    public double getCubeEdgeMeters() { return cubeEdgeMeters; }
    public double getLengthMeters() { return lengthMeters; }
    public double getWidthMeters() { return widthMeters; }
    public double getHeightMeters() { return heightMeters; }
    public List<StructuralGraphNode> getNodes() { return nodes; }
    public List<StructuralGraphMember> getMembers() { return members; }
    public List<StructuralGraphPanel> getPanels() { return panels; }
}
