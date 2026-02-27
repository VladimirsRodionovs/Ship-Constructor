package org.example.shipconstructor.chassis.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChassisCalculationResult {
    private final ChassisCalculationInput input;
    private final ChassisGeometryMetrics geometry;
    private final ChassisCoefficientSet coefficients;
    private final ChassisIdealLimit idealLimit;
    private final ChassisCalculationTrace trace;

    private final double realizationFactor;
    private final double qualityAdjustedLimitKg;
    private final double finalOperationalMaxSupportedMassKg;
    private final double recommendedFrameMassKg;
    private final double recommendedTotalMassTargetKg;
    private final double frameMassFraction;
    private final double appliedSafetyFactor;
    private final double bucklingReserveIndex;
    private final double jointEfficiencyComposite;
    private final List<String> warnings;

    public ChassisCalculationResult(
            ChassisCalculationInput input,
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet coefficients,
            ChassisIdealLimit idealLimit,
            ChassisCalculationTrace trace,
            double realizationFactor,
            double qualityAdjustedLimitKg,
            double finalOperationalMaxSupportedMassKg,
            double recommendedFrameMassKg,
            double recommendedTotalMassTargetKg,
            double frameMassFraction,
            double appliedSafetyFactor,
            double bucklingReserveIndex,
            double jointEfficiencyComposite,
            List<String> warnings) {
        this.input = input;
        this.geometry = geometry;
        this.coefficients = coefficients;
        this.idealLimit = idealLimit;
        this.trace = trace;
        this.realizationFactor = realizationFactor;
        this.qualityAdjustedLimitKg = qualityAdjustedLimitKg;
        this.finalOperationalMaxSupportedMassKg = finalOperationalMaxSupportedMassKg;
        this.recommendedFrameMassKg = recommendedFrameMassKg;
        this.recommendedTotalMassTargetKg = recommendedTotalMassTargetKg;
        this.frameMassFraction = frameMassFraction;
        this.appliedSafetyFactor = appliedSafetyFactor;
        this.bucklingReserveIndex = bucklingReserveIndex;
        this.jointEfficiencyComposite = jointEfficiencyComposite;
        this.warnings = warnings == null
                ? Collections.<String>emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(warnings));
    }

    public ChassisCalculationInput getInput() { return input; }
    public ChassisGeometryMetrics getGeometry() { return geometry; }
    public ChassisCoefficientSet getCoefficients() { return coefficients; }
    public ChassisIdealLimit getIdealLimit() { return idealLimit; }
    public ChassisCalculationTrace getTrace() { return trace; }
    public double getRealizationFactor() { return realizationFactor; }
    public double getQualityAdjustedLimitKg() { return qualityAdjustedLimitKg; }
    public double getFinalOperationalMaxSupportedMassKg() { return finalOperationalMaxSupportedMassKg; }
    public double getRecommendedFrameMassKg() { return recommendedFrameMassKg; }
    public double getRecommendedTotalMassTargetKg() { return recommendedTotalMassTargetKg; }
    public double getFrameMassFraction() { return frameMassFraction; }
    public double getAppliedSafetyFactor() { return appliedSafetyFactor; }
    public double getBucklingReserveIndex() { return bucklingReserveIndex; }
    public double getJointEfficiencyComposite() { return jointEfficiencyComposite; }
    public List<String> getWarnings() { return warnings; }
}
