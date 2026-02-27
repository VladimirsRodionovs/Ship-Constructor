package org.example.shipconstructor.chassis.domain;

public class CubeDimensions {
    private final int x;
    private final int y;
    private final int z;

    public CubeDimensions(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public long getBoundingCubeCount() {
        return (long) x * (long) y * (long) z;
    }
}
