package org.example.shipconstructor.chassis.domain;

public class ChassisGeometryMetrics {
    private final double cubeEdgeM;
    private final double lengthM;
    private final double widthM;
    private final double heightM;
    private final double boundingVolumeM3;
    private final double occupiedVolumeM3;
    private final double packingFillRatio;
    private final double surfaceAreaApproxM2;
    private final double slendernessLengthToWidth;
    private final double slendernessLengthToHeight;
    private final double characteristicSpanM;

    public ChassisGeometryMetrics(
            double cubeEdgeM,
            double lengthM,
            double widthM,
            double heightM,
            double boundingVolumeM3,
            double occupiedVolumeM3,
            double packingFillRatio,
            double surfaceAreaApproxM2,
            double slendernessLengthToWidth,
            double slendernessLengthToHeight,
            double characteristicSpanM) {
        this.cubeEdgeM = cubeEdgeM;
        this.lengthM = lengthM;
        this.widthM = widthM;
        this.heightM = heightM;
        this.boundingVolumeM3 = boundingVolumeM3;
        this.occupiedVolumeM3 = occupiedVolumeM3;
        this.packingFillRatio = packingFillRatio;
        this.surfaceAreaApproxM2 = surfaceAreaApproxM2;
        this.slendernessLengthToWidth = slendernessLengthToWidth;
        this.slendernessLengthToHeight = slendernessLengthToHeight;
        this.characteristicSpanM = characteristicSpanM;
    }

    public double getCubeEdgeM() { return cubeEdgeM; }
    public double getLengthM() { return lengthM; }
    public double getWidthM() { return widthM; }
    public double getHeightM() { return heightM; }
    public double getBoundingVolumeM3() { return boundingVolumeM3; }
    public double getOccupiedVolumeM3() { return occupiedVolumeM3; }
    public double getPackingFillRatio() { return packingFillRatio; }
    public double getSurfaceAreaApproxM2() { return surfaceAreaApproxM2; }
    public double getSlendernessLengthToWidth() { return slendernessLengthToWidth; }
    public double getSlendernessLengthToHeight() { return slendernessLengthToHeight; }
    public double getCharacteristicSpanM() { return characteristicSpanM; }
}
