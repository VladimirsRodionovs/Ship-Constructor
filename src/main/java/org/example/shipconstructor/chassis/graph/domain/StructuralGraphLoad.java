package org.example.shipconstructor.chassis.graph.domain;

import org.example.shipconstructor.chassis.domain.Vector3;

public class StructuralGraphLoad {
    private final String id;
    private final String name;
    private final StructuralGraphLoadType type;
    private final StructuralGraphLoadTargetType targetType;
    private final String targetRef;
    private final Vector3 directionUnit;
    private final double magnitudeG;
    private final double durationSec;
    private final double occurrenceWeight;
    private final boolean certificationCritical;
    private final String sourceLoadCaseName;
    private final String primaryLoadMode;

    public StructuralGraphLoad(
            String id,
            String name,
            StructuralGraphLoadType type,
            StructuralGraphLoadTargetType targetType,
            String targetRef,
            Vector3 directionUnit,
            double magnitudeG,
            double durationSec,
            double occurrenceWeight,
            boolean certificationCritical,
            String sourceLoadCaseName,
            String primaryLoadMode) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.targetType = targetType;
        this.targetRef = targetRef;
        this.directionUnit = directionUnit;
        this.magnitudeG = magnitudeG;
        this.durationSec = durationSec;
        this.occurrenceWeight = occurrenceWeight;
        this.certificationCritical = certificationCritical;
        this.sourceLoadCaseName = sourceLoadCaseName;
        this.primaryLoadMode = primaryLoadMode;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public StructuralGraphLoadType getType() { return type; }
    public StructuralGraphLoadTargetType getTargetType() { return targetType; }
    public String getTargetRef() { return targetRef; }
    public Vector3 getDirectionUnit() { return directionUnit; }
    public double getMagnitudeG() { return magnitudeG; }
    public double getDurationSec() { return durationSec; }
    public double getOccurrenceWeight() { return occurrenceWeight; }
    public boolean isCertificationCritical() { return certificationCritical; }
    public String getSourceLoadCaseName() { return sourceLoadCaseName; }
    public String getPrimaryLoadMode() { return primaryLoadMode; }
}
