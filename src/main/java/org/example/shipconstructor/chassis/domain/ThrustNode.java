package org.example.shipconstructor.chassis.domain;

public class ThrustNode {
    private final String name;
    private final Vector3 position;
    private final Vector3 directionUnit;
    private final double maxThrustNewton;

    public ThrustNode(String name, Vector3 position, Vector3 directionUnit, double maxThrustNewton) {
        this.name = name;
        this.position = position;
        this.directionUnit = directionUnit;
        this.maxThrustNewton = maxThrustNewton;
    }

    public String getName() { return name; }
    public Vector3 getPosition() { return position; }
    public Vector3 getDirectionUnit() { return directionUnit; }
    public double getMaxThrustNewton() { return maxThrustNewton; }
}
