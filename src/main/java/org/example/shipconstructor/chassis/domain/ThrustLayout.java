package org.example.shipconstructor.chassis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThrustLayout {
    private final List<ThrustNode> thrustNodes;
    private final boolean symmetricPrimaryThrust;

    public ThrustLayout(List<ThrustNode> thrustNodes, boolean symmetricPrimaryThrust) {
        this.thrustNodes = thrustNodes == null
                ? Collections.<ThrustNode>emptyList()
                : Collections.unmodifiableList(new ArrayList<ThrustNode>(thrustNodes));
        this.symmetricPrimaryThrust = symmetricPrimaryThrust;
    }

    public List<ThrustNode> getThrustNodes() { return thrustNodes; }
    public boolean isSymmetricPrimaryThrust() { return symmetricPrimaryThrust; }
}
