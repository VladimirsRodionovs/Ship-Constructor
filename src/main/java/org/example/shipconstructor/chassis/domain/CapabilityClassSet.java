package org.example.shipconstructor.chassis.domain;

public class CapabilityClassSet {
    private final int planetLandingCapabilityClass;
    private final int groundSupportSurfaceClass;
    private final int atmosphereOperationClass;
    private final int pressureDifferentialClass;
    private final int thermalEnvironmentClass;
    private final int corrosionChemicalExposureClass;
    private final int toxicityBiohazardExposureClass;
    private final int radiationExposureClass;
    private final int maneuverAgilityClass;
    private final int structuralDamageToleranceClass;

    public CapabilityClassSet(
            int planetLandingCapabilityClass,
            int groundSupportSurfaceClass,
            int atmosphereOperationClass,
            int pressureDifferentialClass,
            int thermalEnvironmentClass,
            int corrosionChemicalExposureClass,
            int toxicityBiohazardExposureClass,
            int radiationExposureClass,
            int maneuverAgilityClass,
            int structuralDamageToleranceClass) {
        this.planetLandingCapabilityClass = planetLandingCapabilityClass;
        this.groundSupportSurfaceClass = groundSupportSurfaceClass;
        this.atmosphereOperationClass = atmosphereOperationClass;
        this.pressureDifferentialClass = pressureDifferentialClass;
        this.thermalEnvironmentClass = thermalEnvironmentClass;
        this.corrosionChemicalExposureClass = corrosionChemicalExposureClass;
        this.toxicityBiohazardExposureClass = toxicityBiohazardExposureClass;
        this.radiationExposureClass = radiationExposureClass;
        this.maneuverAgilityClass = maneuverAgilityClass;
        this.structuralDamageToleranceClass = structuralDamageToleranceClass;
    }

    public static CapabilityClassSet defaultsSpaceOnly() {
        return new CapabilityClassSet(0, 0, 0, 1, 1, 0, 0, 1, 1, 1);
    }

    public int getPlanetLandingCapabilityClass() { return planetLandingCapabilityClass; }
    public int getGroundSupportSurfaceClass() { return groundSupportSurfaceClass; }
    public int getAtmosphereOperationClass() { return atmosphereOperationClass; }
    public int getPressureDifferentialClass() { return pressureDifferentialClass; }
    public int getThermalEnvironmentClass() { return thermalEnvironmentClass; }
    public int getCorrosionChemicalExposureClass() { return corrosionChemicalExposureClass; }
    public int getToxicityBiohazardExposureClass() { return toxicityBiohazardExposureClass; }
    public int getRadiationExposureClass() { return radiationExposureClass; }
    public int getManeuverAgilityClass() { return maneuverAgilityClass; }
    public int getStructuralDamageToleranceClass() { return structuralDamageToleranceClass; }
}
