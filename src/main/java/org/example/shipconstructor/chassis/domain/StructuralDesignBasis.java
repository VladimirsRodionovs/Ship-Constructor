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
    private final EngineLayoutConfig engineLayoutConfig;
    private final StructuralOptimizationMode optimizationMode;
    private final double targetMinMarginIndex;
    private final double targetMaxMarginIndex;

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
                0,
                null,
                StructuralOptimizationMode.BALANCED,
                2.0d,
                8.0d
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
                0,
                null,
                StructuralOptimizationMode.BALANCED,
                2.0d,
                8.0d
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
        this(
                designClass,
                spaceOnly,
                atmosphericCapable,
                landingCapable,
                thrustCanBeForeMounted,
                targetServiceLifeCycles,
                capabilityClassSet,
                enginePlacementProfileClass,
                null,
                StructuralOptimizationMode.BALANCED,
                2.0d,
                8.0d
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
            int enginePlacementProfileClass,
            StructuralOptimizationMode optimizationMode,
            double targetMinMarginIndex,
            double targetMaxMarginIndex) {
        this(
                designClass,
                spaceOnly,
                atmosphericCapable,
                landingCapable,
                thrustCanBeForeMounted,
                targetServiceLifeCycles,
                capabilityClassSet,
                enginePlacementProfileClass,
                null,
                optimizationMode,
                targetMinMarginIndex,
                targetMaxMarginIndex
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
            int enginePlacementProfileClass,
            EngineLayoutConfig engineLayoutConfig,
            StructuralOptimizationMode optimizationMode,
            double targetMinMarginIndex,
            double targetMaxMarginIndex) {
        this.designClass = designClass;
        this.spaceOnly = spaceOnly;
        this.atmosphericCapable = atmosphericCapable;
        this.landingCapable = landingCapable;
        this.thrustCanBeForeMounted = thrustCanBeForeMounted;
        this.targetServiceLifeCycles = targetServiceLifeCycles;
        this.capabilityClassSet = capabilityClassSet;
        this.enginePlacementProfileClass = enginePlacementProfileClass;
        this.engineLayoutConfig = engineLayoutConfig;
        this.optimizationMode = optimizationMode == null ? StructuralOptimizationMode.BALANCED : optimizationMode;
        this.targetMinMarginIndex = targetMinMarginIndex <= 0.0d ? 2.0d : targetMinMarginIndex;
        this.targetMaxMarginIndex = targetMaxMarginIndex <= this.targetMinMarginIndex ? this.targetMinMarginIndex + 2.0d : targetMaxMarginIndex;
    }

    public String getDesignClass() { return designClass; }
    public boolean isSpaceOnly() { return spaceOnly; }
    public boolean isAtmosphericCapable() { return atmosphericCapable; }
    public boolean isLandingCapable() { return landingCapable; }
    public boolean isThrustCanBeForeMounted() { return thrustCanBeForeMounted; }
    public int getTargetServiceLifeCycles() { return targetServiceLifeCycles; }
    public CapabilityClassSet getCapabilityClassSet() { return capabilityClassSet; }
    public int getEnginePlacementProfileClass() { return enginePlacementProfileClass; }
    public EngineLayoutConfig getEngineLayoutConfig() { return engineLayoutConfig; }
    public StructuralOptimizationMode getOptimizationMode() { return optimizationMode; }
    public double getTargetMinMarginIndex() { return targetMinMarginIndex; }
    public double getTargetMaxMarginIndex() { return targetMaxMarginIndex; }
}
