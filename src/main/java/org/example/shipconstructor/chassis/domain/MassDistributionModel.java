package org.example.shipconstructor.chassis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MassDistributionModel {
    private final Vector3 centerOfMassDry;
    private final Vector3 centerOfMassLoaded;
    private final Vector3 centerOfMassFueled;
    private final List<MassNode> majorMassNodes;

    public MassDistributionModel(
            Vector3 centerOfMassDry,
            Vector3 centerOfMassLoaded,
            Vector3 centerOfMassFueled,
            List<MassNode> majorMassNodes) {
        this.centerOfMassDry = centerOfMassDry;
        this.centerOfMassLoaded = centerOfMassLoaded;
        this.centerOfMassFueled = centerOfMassFueled;
        this.majorMassNodes = majorMassNodes == null
                ? Collections.<MassNode>emptyList()
                : Collections.unmodifiableList(new ArrayList<MassNode>(majorMassNodes));
    }

    public Vector3 getCenterOfMassDry() { return centerOfMassDry; }
    public Vector3 getCenterOfMassLoaded() { return centerOfMassLoaded; }
    public Vector3 getCenterOfMassFueled() { return centerOfMassFueled; }
    public List<MassNode> getMajorMassNodes() { return majorMassNodes; }
}
