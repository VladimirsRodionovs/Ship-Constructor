package org.example.shipconstructor.chassis.domain;

public class ChassisCalculationInput {
    private final int sizeType;
    private final long sizeTotal;
    private final CubeDimensions sizeDimensions;
    private final EngineeringStackSelection engineeringStack;
    private final AccelerationEnvelope accelerationEnvelope;
    private final StructuralDesignBasis structuralDesignBasis;
    private final StructuralLoadCaseSet loadCaseSet;
    private final ThrustLayout thrustLayout;
    private final MassDistributionModel massDistributionModel;
    private final String structureProfileCode;
    private final MissionPriority missionPriority;
    private final SafetyPriority safetyPriority;

    public ChassisCalculationInput(
            int sizeType,
            long sizeTotal,
            CubeDimensions sizeDimensions,
            EngineeringStackSelection engineeringStack,
            AccelerationEnvelope accelerationEnvelope,
            MissionPriority missionPriority,
            SafetyPriority safetyPriority) {
        this(
                sizeType,
                sizeTotal,
                sizeDimensions,
                engineeringStack,
                accelerationEnvelope,
                null,
                null,
                null,
                null,
                null,
                missionPriority,
                safetyPriority
        );
    }

    public ChassisCalculationInput(
            int sizeType,
            long sizeTotal,
            CubeDimensions sizeDimensions,
            EngineeringStackSelection engineeringStack,
            AccelerationEnvelope accelerationEnvelope,
            StructuralDesignBasis structuralDesignBasis,
            StructuralLoadCaseSet loadCaseSet,
            ThrustLayout thrustLayout,
            MassDistributionModel massDistributionModel,
            MissionPriority missionPriority,
            SafetyPriority safetyPriority) {
        this(
                sizeType,
                sizeTotal,
                sizeDimensions,
                engineeringStack,
                accelerationEnvelope,
                structuralDesignBasis,
                loadCaseSet,
                thrustLayout,
                massDistributionModel,
                null,
                missionPriority,
                safetyPriority
        );
    }

    public ChassisCalculationInput(
            int sizeType,
            long sizeTotal,
            CubeDimensions sizeDimensions,
            EngineeringStackSelection engineeringStack,
            AccelerationEnvelope accelerationEnvelope,
            StructuralDesignBasis structuralDesignBasis,
            StructuralLoadCaseSet loadCaseSet,
            ThrustLayout thrustLayout,
            MassDistributionModel massDistributionModel,
            String structureProfileCode,
            MissionPriority missionPriority,
            SafetyPriority safetyPriority) {
        this.sizeType = sizeType;
        this.sizeTotal = sizeTotal;
        this.sizeDimensions = sizeDimensions;
        this.engineeringStack = engineeringStack;
        this.accelerationEnvelope = accelerationEnvelope;
        this.structuralDesignBasis = structuralDesignBasis;
        this.loadCaseSet = loadCaseSet;
        this.thrustLayout = thrustLayout;
        this.massDistributionModel = massDistributionModel;
        this.structureProfileCode = structureProfileCode;
        this.missionPriority = missionPriority;
        this.safetyPriority = safetyPriority;
    }

    public int getSizeType() {
        return sizeType;
    }

    public long getSizeTotal() {
        return sizeTotal;
    }

    public CubeDimensions getSizeDimensions() {
        return sizeDimensions;
    }

    public EngineeringStackSelection getEngineeringStack() {
        return engineeringStack;
    }

    public AccelerationEnvelope getAccelerationEnvelope() {
        return accelerationEnvelope;
    }

    public StructuralDesignBasis getStructuralDesignBasis() {
        return structuralDesignBasis;
    }

    public StructuralLoadCaseSet getLoadCaseSet() {
        return loadCaseSet;
    }

    public ThrustLayout getThrustLayout() {
        return thrustLayout;
    }

    public MassDistributionModel getMassDistributionModel() {
        return massDistributionModel;
    }

    public String getStructureProfileCode() {
        return structureProfileCode;
    }

    public MissionPriority getMissionPriority() {
        return missionPriority;
    }

    public SafetyPriority getSafetyPriority() {
        return safetyPriority;
    }
}
