package org.example.shipconstructor.chassis.domain;

public class StructuralDesignBasis {
    private final String designClass;
    private final boolean spaceOnly;
    private final boolean atmosphericCapable;
    private final boolean landingCapable;
    private final boolean thrustCanBeForeMounted;
    private final int targetServiceLifeCycles;
    private final CapabilityClassSet capabilityClassSet;
    private final int enginePlacementProfileClass;

    public StructuralDesignBasis(
            String designClass,
            boolean spaceOnly,
            boolean atmosphericCapable,
            boolean landingCapable,
            boolean thrustCanBeForeMounted,
            int targetServiceLifeCycles) {
        this(
                designClass,
                spaceOnly,
                atmosphericCapable,
                landingCapable,
                thrustCanBeForeMounted,
                targetServiceLifeCycles,
                null,
                0
        );
    }

    public StructuralDesignBasis(
            String designClass,
            boolean spaceOnly,
            boolean atmosphericCapable,
            boolean landingCapable,
            boolean thrustCanBeForeMounted,
            int targetServiceLifeCycles,
            CapabilityClassSet capabilityClassSet) {
        this(
                designClass,
                spaceOnly,
                atmosphericCapable,
                landingCapable,
                thrustCanBeForeMounted,
                targetServiceLifeCycles,
                capabilityClassSet,
                0
        );
    }

    public StructuralDesignBasis(
            String designClass,
            boolean spaceOnly,
            boolean atmosphericCapable,
            boolean landingCapable,
            boolean thrustCanBeForeMounted,
            int targetServiceLifeCycles,
            CapabilityClassSet capabilityClassSet,
            int enginePlacementProfileClass) {
        this.designClass = designClass;
        this.spaceOnly = spaceOnly;
        this.atmosphericCapable = atmosphericCapable;
        this.landingCapable = landingCapable;
        this.thrustCanBeForeMounted = thrustCanBeForeMounted;
        this.targetServiceLifeCycles = targetServiceLifeCycles;
        this.capabilityClassSet = capabilityClassSet;
        this.enginePlacementProfileClass = enginePlacementProfileClass;
    }

    public String getDesignClass() { return designClass; }
    public boolean isSpaceOnly() { return spaceOnly; }
    public boolean isAtmosphericCapable() { return atmosphericCapable; }
    public boolean isLandingCapable() { return landingCapable; }
    public boolean isThrustCanBeForeMounted() { return thrustCanBeForeMounted; }
    public int getTargetServiceLifeCycles() { return targetServiceLifeCycles; }
    public CapabilityClassSet getCapabilityClassSet() { return capabilityClassSet; }
    public int getEnginePlacementProfileClass() { return enginePlacementProfileClass; }
}
