package org.example.shipconstructor.chassis.domain;

public class MassNode {
    private final String name;
    private final Vector3 position;
    private final double massKg;

    public MassNode(String name, Vector3 position, double massKg) {
        this.name = name;
        this.position = position;
        this.massKg = massKg;
    }

    public String getName() { return name; }
    public Vector3 getPosition() { return position; }
    public double getMassKg() { return massKg; }
}
