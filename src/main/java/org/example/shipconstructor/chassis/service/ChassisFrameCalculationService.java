package org.example.shipconstructor.chassis.service;

import org.example.shipconstructor.chassis.domain.AccelerationEnvelope;
import org.example.shipconstructor.chassis.domain.CapabilityClassSet;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.ChassisCalculationResult;
import org.example.shipconstructor.chassis.domain.ChassisCalculationTrace;
import org.example.shipconstructor.chassis.domain.ChassisCoefficientSet;
import org.example.shipconstructor.chassis.domain.ChassisGeometryMetrics;
import org.example.shipconstructor.chassis.domain.ChassisIdealLimit;
import org.example.shipconstructor.chassis.domain.ChassisPhysicsLimits;
import org.example.shipconstructor.chassis.domain.CubeDimensions;
import org.example.shipconstructor.chassis.domain.SafetyPriority;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.domain.StructuralLoadCase;
import org.example.shipconstructor.chassis.domain.StructuralLoadCaseSet;
import org.example.shipconstructor.chassis.domain.ThrustLayout;
import org.example.shipconstructor.chassis.domain.ThrustNode;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.ui.SizeTypeCatalog;

import java.util.ArrayList;
import java.util.List;

public class ChassisFrameCalculationService {
    private final ChassisCoefficientProvider coefficientProvider;
    private final ChassisPhysicsLimits physicsLimits;

    public ChassisFrameCalculationService() {
        this(buildDefaultCoefficientProvider(), ChassisPhysicsLimits.defaults());
    }

    public ChassisFrameCalculationService(
            ChassisCoefficientProvider coefficientProvider,
            ChassisPhysicsLimits physicsLimits) {
        this.coefficientProvider = coefficientProvider;
        this.physicsLimits = physicsLimits;
    }

    public ChassisCalculationResult calculate(ChassisCalculationInput input) {
        validateInput(input);
        ChassisCalculationTrace trace = new ChassisCalculationTrace();
        trace.add("validate", "status", "input validated");

        ChassisGeometryMetrics geometry = deriveGeometry(input);
        addGeometryTrace(trace, geometry);
        AccelerationEnvelope effectiveAccelerationEnvelope = resolveEffectiveAccelerationEnvelope(input, trace);
        ChassisCoefficientSet coefficients = coefficientProvider.resolve(input);
        addCoefficientTrace(trace, coefficients);
        ChassisIdealLimit idealLimit = estimateIdealLimit(input, geometry, coefficients, effectiveAccelerationEnvelope, trace);

        double realizationFactor = computeRealizationFactor(coefficients, geometry);
        trace.add("realization", "realizationFactor", realizationFactor, "Manufacturing+assembly+scale realization factor capped by practical efficiency");
        double preQualityLimitKg = idealLimit.getIdealMaxSupportedMassKg() * realizationFactor;
        trace.add("realization", "preQualityLimitKg", preQualityLimitKg, "Ideal limit after realization penalties (before QA/environment/safety)");

        double appliedSafetyFactor = computeSafetyFactor(input.getSafetyPriority(), coefficients);
        trace.add("safety", "appliedSafetyFactor", appliedSafetyFactor, "Safety factor after QA-based margin reduction allowance and floor");
        double materialEnvironmentComposite = clampPositive(coefficients.getMaterialTemperaturePenaltyFactor())
                * clampPositive(coefficients.getMaterialThermalCyclingPenaltyFactor());
        double qualityAdjustedLimitKg = preQualityLimitKg
                * clamp01(coefficients.getQualityConfidenceFactor())
                * materialEnvironmentComposite
                * clampPositive(coefficients.getEnvironmentDegradationFactor())
                / Math.max(appliedSafetyFactor, physicsLimits.getSafetyFactorFloor());
        trace.add("qa_env", "qualityConfidenceFactor", clamp01(coefficients.getQualityConfidenceFactor()), "QA confidence multiplier");
        trace.add("qa_env", "materialTemperaturePenaltyFactor", clampPositive(coefficients.getMaterialTemperaturePenaltyFactor()), "A: material hot-service suitability penalty");
        trace.add("qa_env", "materialThermalCyclingPenaltyFactor", clampPositive(coefficients.getMaterialThermalCyclingPenaltyFactor()), "A: material thermal-cycling suitability penalty");
        trace.add("qa_env", "materialEnvironmentComposite", materialEnvironmentComposite, "A: material thermal suitability composite");
        trace.add("qa_env", "environmentDegradationFactor", clampPositive(coefficients.getEnvironmentDegradationFactor()), "Environment degradation multiplier");
        trace.add("qa_env", "qualityAdjustedLimitKg", qualityAdjustedLimitKg, "After QA confidence, environment degradation and safety factor");

        double finalOperationalMaxSupportedMassKg = Math.min(
                idealLimit.getIdealMaxSupportedMassKg(),
                Math.max(0.0d, qualityAdjustedLimitKg)
        );
        trace.add("final", "finalOperationalMaxSupportedMassKg", finalOperationalMaxSupportedMassKg, "Clamped to ideal limit and non-negative range");

        double recommendedFrameMassKg = estimateRecommendedFrameMass(geometry, coefficients, finalOperationalMaxSupportedMassKg, trace);
        double frameMassFraction = finalOperationalMaxSupportedMassKg <= 0.0d
                ? 0.0d
                : (recommendedFrameMassKg / finalOperationalMaxSupportedMassKg);

        if (frameMassFraction < physicsLimits.getFrameMassFractionFloor() && finalOperationalMaxSupportedMassKg > 0.0d) {
            recommendedFrameMassKg = finalOperationalMaxSupportedMassKg * physicsLimits.getFrameMassFractionFloor();
            frameMassFraction = physicsLimits.getFrameMassFractionFloor();
            trace.add("final", "frameMassFloorApplied", 1.0d, "Recommended frame mass raised to physical floor fraction");
        }

        double recommendedTotalMassTargetKg = finalOperationalMaxSupportedMassKg * 0.85d;
        double jointEfficiencyComposite = clamp01(coefficients.getAssemblyJointEfficiencyFactor());
        double bucklingReserveIndex = idealLimit.getIdealBucklingReserveIndex();
        trace.add("final", "recommendedFrameMassKg", recommendedFrameMassKg, "Recommended frame/chassis mass");
        trace.add("final", "frameMassFraction", frameMassFraction, "Recommended frame mass fraction of final operational limit");
        trace.add("final", "recommendedTotalMassTargetKg", recommendedTotalMassTargetKg, "Planning target below max supported mass");
        trace.add("final", "jointEfficiencyComposite", jointEfficiencyComposite, "Assembly joint efficiency composite (A-D effective)");
        trace.add("final", "bucklingReserveIndex", bucklingReserveIndex, "Buckling reserve diagnostic index");

        List<String> warnings = buildWarnings(input, geometry, coefficients, idealLimit, finalOperationalMaxSupportedMassKg, bucklingReserveIndex);

        return new ChassisCalculationResult(
                input,
                geometry,
                coefficients,
                idealLimit,
                trace,
                realizationFactor,
                qualityAdjustedLimitKg,
                finalOperationalMaxSupportedMassKg,
                recommendedFrameMassKg,
                recommendedTotalMassTargetKg,
                frameMassFraction,
                appliedSafetyFactor,
                bucklingReserveIndex,
                jointEfficiencyComposite,
                warnings
        );
    }

    private void validateInput(ChassisCalculationInput input) {
        if (input == null) {
            throw new IllegalArgumentException("Chassis calculation input is required");
        }
        if (!SizeTypeCatalog.values().containsKey(Integer.valueOf(input.getSizeType()))) {
            throw new IllegalArgumentException("Unsupported sizeType: " + input.getSizeType());
        }
        if (input.getSizeTotal() <= 0L) {
            throw new IllegalArgumentException("sizeTotal must be > 0");
        }
        CubeDimensions d = input.getSizeDimensions();
        if (d == null) {
            throw new IllegalArgumentException("sizeDimensions is required");
        }
        if (d.getX() <= 0 || d.getY() <= 0 || d.getZ() <= 0) {
            throw new IllegalArgumentException("sizeDimensions values must be > 0");
        }
        if (input.getSizeTotal() > d.getBoundingCubeCount()) {
            throw new IllegalArgumentException("sizeTotal exceeds bounding cube count x*y*z");
        }
        AccelerationEnvelope a = input.getAccelerationEnvelope();
        StructuralLoadCaseSet loadCaseSet = input.getLoadCaseSet();
        if (a == null && (loadCaseSet == null || loadCaseSet.isEmpty())) {
            throw new IllegalArgumentException("accelerationEnvelope or non-empty loadCaseSet is required");
        }
        if (a == null) {
            a = new AccelerationEnvelope(0.0d, 0.0d, 0.0d);
        }
        if (a.getLongitudinalG() < 0.0d || a.getLateralG() < 0.0d || a.getVerticalG() < 0.0d) {
            throw new IllegalArgumentException("accelerations must be >= 0");
        }
        if (a.getMaxG() > physicsLimits.getMaxSupportedAccelerationG()) {
            throw new IllegalArgumentException("requested acceleration is outside supported model range");
        }
        if (input.getEngineeringStack() == null) {
            throw new IllegalArgumentException("engineeringStack is required");
        }
        if (input.getEngineeringStack().getBaseMaterialId() == null
                || input.getEngineeringStack().getStructureTypeId() == null
                || input.getEngineeringStack().getManufacturingProcessId() == null
                || input.getEngineeringStack().getAssemblyProcessId() == null
                || input.getEngineeringStack().getQualityProfileId() == null
                || input.getEngineeringStack().getEnvironmentProfileId() == null) {
            throw new IllegalArgumentException("engineeringStack A-F IDs are required");
        }
    }

    private ChassisGeometryMetrics deriveGeometry(ChassisCalculationInput input) {
        double cubeEdgeM = SizeTypeCatalog.values().get(Integer.valueOf(input.getSizeType())).getCubeEdgeMeters();
        CubeDimensions d = input.getSizeDimensions();

        double lengthM = cubeEdgeM * d.getX();
        double widthM = cubeEdgeM * d.getY();
        double heightM = cubeEdgeM * d.getZ();
        double boundingVolumeM3 = lengthM * widthM * heightM;
        double occupiedVolumeM3 = input.getSizeTotal() * cubeEdgeM * cubeEdgeM * cubeEdgeM;
        double packingFillRatio = boundingVolumeM3 <= 0.0d ? 0.0d : occupiedVolumeM3 / boundingVolumeM3;
        double surfaceArea = 2.0d * (lengthM * widthM + lengthM * heightM + widthM * heightM);
        double slenderLW = widthM <= 0.0d ? 0.0d : lengthM / widthM;
        double slenderLH = heightM <= 0.0d ? 0.0d : lengthM / heightM;
        double characteristicSpan = Math.max(lengthM, Math.max(widthM, heightM));

        return new ChassisGeometryMetrics(
                cubeEdgeM,
                lengthM,
                widthM,
                heightM,
                boundingVolumeM3,
                occupiedVolumeM3,
                packingFillRatio,
                surfaceArea,
                slenderLW,
                slenderLH,
                characteristicSpan
        );
    }

    private ChassisIdealLimit estimateIdealLimit(
            ChassisCalculationInput input,
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet coefficients,
            AccelerationEnvelope effectiveAccelerationEnvelope,
            ChassisCalculationTrace trace) {
        // Calibratable v0 model: explicit staged terms, still placeholder coefficients.
        // "Ideal" here means perfect realization of A+B without C/D/E/F penalties.
        double occupiedVolumeM3 = geometry.getOccupiedVolumeM3();
        double effectiveFrameMaterialVolumeM3 = occupiedVolumeM3 * estimateIdealFrameVolumeFraction(geometry);
        trace.add("ideal", "occupiedVolumeM3", occupiedVolumeM3, "Occupied chassis envelope volume from size cubes");
        trace.add("ideal", "effectiveFrameMaterialVolumeM3", effectiveFrameMaterialVolumeM3, "Estimated ideal structural material volume fraction");

        double baselineFrameMassKg = effectiveFrameMaterialVolumeM3
                * coefficients.getMaterialDensityKgM3()
                * coefficients.getStructureEffectiveDensityFactor();
        trace.add("ideal", "baselineFrameMassKg", baselineFrameMassKg, "Idealized frame mass before load scaling");

        double baseAccelerationDemandFactor = computeAccelerationDemandFactor(effectiveAccelerationEnvelope);
        double thrustLineOffsetMomentIndex = computeThrustLineOffsetMomentIndex(input, geometry);
        AxialLoadModeDiagnostics axialDiag = computeAxialLoadModeDiagnostics(input);
        double axialLoadModeBias = axialDiag.bias;
        double bendingLoadFraction = axialDiag.bendingFraction;
        double torsionProxyFraction = axialDiag.torsionProxyFraction;
        double accelerationDemandFactor = baseAccelerationDemandFactor * (1.0d + 0.45d * thrustLineOffsetMomentIndex);
        double slendernessPenalty = computeSlendernessPenalty(geometry);
        double packingPenalty = computePackingPenalty(geometry);
        trace.add("ideal", "baseAccelerationDemandFactor", baseAccelerationDemandFactor, "Acceleration envelope demand factor before thrust-line offset moment");
        trace.add("ideal", "thrustLineOffsetMomentIndex", thrustLineOffsetMomentIndex, "Normalized bending/torsion demand proxy from thrust-line offset vs COM");
        trace.add("ideal", "axialLoadModeBias", axialLoadModeBias, "Axial load mode bias: -1 compression dominant, +1 tension dominant");
        trace.add("loads", "axialLoadModeCompressionFraction", axialDiag.compressionFraction, "Weighted share of compression-dominant axial cases");
        trace.add("loads", "axialLoadModeTensionFraction", axialDiag.tensionFraction, "Weighted share of tension-dominant axial cases");
        trace.add("loads", "axialLoadModeMixedFraction", axialDiag.mixedFraction, "Weighted share of mixed/bending axial cases");
        trace.add("loads", "bendingLoadFraction", bendingLoadFraction, "Weighted share of bending-dominant load behavior");
        trace.add("loads", "torsionProxyFraction", torsionProxyFraction, "Proxy fraction for torsion-prone load behavior");
        trace.add("loads", "axialLoadModeSource", axialDiag.source);
        trace.add("ideal", "accelerationDemandFactor", accelerationDemandFactor, "Acceleration demand factor after thrust-line offset moment contribution");
        trace.add("ideal", "slendernessPenalty", slendernessPenalty, "Penalty for high geometric slenderness");
        trace.add("ideal", "packingPenalty", packingPenalty, "Penalty for inefficient fill ratio/extreme packing");
        double axialCompressionBucklingPenalty = 1.0d - (0.20d * Math.max(0.0d, -axialLoadModeBias));
        double bucklingCapacityFactor = coefficients.getStructureBucklingResistanceFactor() * slendernessPenalty * Math.max(0.60d, axialCompressionBucklingPenalty);
        double stiffnessCapacityFactor = coefficients.getMaterialStiffnessIndex()
                * coefficients.getStructureSpecificStiffnessFactor()
                * packingPenalty;
        double strengthCapacityFactor = coefficients.getMaterialStrengthIndex()
                * coefficients.getStructureSpecificStrengthFactor();
        double materialPenaltyComposite = clampPositive(coefficients.getMaterialAnisotropyPenaltyFactor())
                * clampPositive(coefficients.getMaterialBrittlenessPenaltyFactor());
        double axialTensionBrittlePenalty = 1.0d - (0.35d * Math.max(0.0d, axialLoadModeBias) * (1.0d - clamp01(coefficients.getMaterialBrittlenessPenaltyFactor())));
        materialPenaltyComposite = materialPenaltyComposite * Math.max(0.60d, axialTensionBrittlePenalty);
        double loadComplexity = Math.max(0.0d, Math.min(1.5d, 0.7d * bendingLoadFraction + 0.8d * torsionProxyFraction));
        double damageToleranceLoadPenalty = 1.0d - (0.20d * loadComplexity / Math.max(0.70d, coefficients.getStructureDamageToleranceFactor()));
        materialPenaltyComposite = materialPenaltyComposite * Math.max(0.70d, damageToleranceLoadPenalty);
        trace.add("ideal", "strengthCapacityFactor", strengthCapacityFactor, "Material+structure strength composite factor");
        trace.add("ideal", "stiffnessCapacityFactor", stiffnessCapacityFactor, "Material+structure stiffness composite factor");
        trace.add("ideal", "axialCompressionBucklingPenalty", axialCompressionBucklingPenalty, "Compression-dominant axial loading reduces buckling reserve");
        trace.add("ideal", "bucklingCapacityFactor", bucklingCapacityFactor, "Structure buckling factor with geometry penalty");
        trace.add("ideal", "axialTensionBrittlePenalty", axialTensionBrittlePenalty, "Tension-dominant axial loading penalty for brittle materials");
        trace.add("ideal", "damageToleranceLoadPenalty", damageToleranceLoadPenalty, "Bending/torsion load complexity penalty reduced by structure damage tolerance");
        trace.add("ideal", "materialPenaltyComposite", materialPenaltyComposite, "Material anisotropy/brittleness chassis-use penalty");

        double idealCapacityMultiplier = strengthCapacityFactor
                * Math.sqrt(Math.max(0.05d, stiffnessCapacityFactor))
                * Math.max(0.10d, bucklingCapacityFactor)
                * materialPenaltyComposite;
        trace.add("ideal", "idealCapacityMultiplier", idealCapacityMultiplier, "Composite ideal capacity multiplier");

        // Payload support factor is separate from frame self-mass.
        // Total supported mass must include the frame itself; otherwise the model can become physically inconsistent.
        double payloadSupportFactor = 24.0d
                * idealCapacityMultiplier
                / accelerationDemandFactor;
        double idealMaxSupportedMassKg = baselineFrameMassKg
                * (1.0d + Math.max(0.0d, payloadSupportFactor));
        trace.add("ideal", "payloadSupportFactor", payloadSupportFactor, "Payload mass supported per unit frame mass before C/D/E/F degradation");
        trace.add("ideal", "totalSupportToFrameRatio", baselineFrameMassKg <= 0.0d ? 0.0d : idealMaxSupportedMassKg / baselineFrameMassKg, "Ideal total-supported-mass to frame-mass ratio");

        double idealRecommendedFrameMassKg = baselineFrameMassKg;
        double globalBucklingReserveFactor = computeGlobalBucklingReserveFactor(geometry, coefficients, accelerationDemandFactor);
        double localBucklingReserveFactor = computeLocalBucklingReserveFactor(
                geometry,
                coefficients,
                accelerationDemandFactor,
                bendingLoadFraction,
                torsionProxyFraction
        );
        double idealBucklingReserveIndex = Math.max(
                0.35d,
                0.55d * globalBucklingReserveFactor + 0.45d * localBucklingReserveFactor
        );
        double idealStiffnessReserveIndex = Math.max(0.5d, 2.0d * stiffnessCapacityFactor / accelerationDemandFactor);
        trace.add("ideal", "globalBucklingReserveFactor", globalBucklingReserveFactor, "Global frame buckling reserve factor");
        trace.add("ideal", "localBucklingReserveFactor", localBucklingReserveFactor, "Local panel/member buckling reserve factor");
        trace.add("ideal", "idealMaxSupportedMassKg", idealMaxSupportedMassKg, "Ideal physical upper bound before C/D/E/F degradation");
        trace.add("ideal", "idealRecommendedFrameMassKg", idealRecommendedFrameMassKg, "Ideal recommended frame mass");
        trace.add("ideal", "idealBucklingReserveIndex", idealBucklingReserveIndex, "Ideal buckling reserve diagnostic");
        trace.add("ideal", "idealStiffnessReserveIndex", idealStiffnessReserveIndex, "Ideal stiffness reserve diagnostic");

        return new ChassisIdealLimit(
                Math.max(0.0d, idealMaxSupportedMassKg),
                Math.max(0.0d, idealRecommendedFrameMassKg),
                idealBucklingReserveIndex,
                idealStiffnessReserveIndex
        );
    }

    private void addGeometryTrace(ChassisCalculationTrace trace, ChassisGeometryMetrics g) {
        trace.add("geometry", "cubeEdgeM", g.getCubeEdgeM(), "Cube edge size from SizeType");
        trace.add("geometry", "lengthM", g.getLengthM(), "Bounding dimension X");
        trace.add("geometry", "widthM", g.getWidthM(), "Bounding dimension Y");
        trace.add("geometry", "heightM", g.getHeightM(), "Bounding dimension Z");
        trace.add("geometry", "boundingVolumeM3", g.getBoundingVolumeM3(), "Bounding box volume");
        trace.add("geometry", "occupiedVolumeM3", g.getOccupiedVolumeM3(), "Occupied volume by SizeTotal cubes");
        trace.add("geometry", "packingFillRatio", g.getPackingFillRatio(), "Occupied/bounding volume ratio");
        trace.add("geometry", "surfaceAreaApproxM2", g.getSurfaceAreaApproxM2(), "Approximate rectangular surface area");
        trace.add("geometry", "slendernessLengthToWidth", g.getSlendernessLengthToWidth(), "L/W slenderness");
        trace.add("geometry", "slendernessLengthToHeight", g.getSlendernessLengthToHeight(), "L/H slenderness");
        trace.add("geometry", "characteristicSpanM", g.getCharacteristicSpanM(), "Largest dimension used for scale penalties");
    }

    private void addCoefficientTrace(ChassisCalculationTrace trace, ChassisCoefficientSet c) {
        trace.add("coefficients", "materialDensityKgM3", c.getMaterialDensityKgM3(), "A: base material density");
        trace.add("coefficients", "materialStrengthIndex", c.getMaterialStrengthIndex(), "A: normalized material strength");
        trace.add("coefficients", "materialStiffnessIndex", c.getMaterialStiffnessIndex(), "A: normalized material stiffness");
        trace.add("coefficients", "materialFatigueIndex", c.getMaterialFatigueIndex(), "A: normalized material fatigue");
        trace.add("coefficients", "materialAnisotropyPenaltyFactor", c.getMaterialAnisotropyPenaltyFactor(), "A: chassis-use penalty for directional behavior");
        trace.add("coefficients", "materialBrittlenessPenaltyFactor", c.getMaterialBrittlenessPenaltyFactor(), "A: chassis-use penalty for brittle failure propensity");
        trace.add("coefficients", "materialTemperaturePenaltyFactor", c.getMaterialTemperaturePenaltyFactor(), "A: hot-service suitability penalty");
        trace.add("coefficients", "materialThermalCyclingPenaltyFactor", c.getMaterialThermalCyclingPenaltyFactor(), "A: thermal-cycling suitability penalty");
        trace.add("coefficients", "structureEffectiveDensityFactor", c.getStructureEffectiveDensityFactor(), "B: effective density factor");
        trace.add("coefficients", "structureSpecificStrengthFactor", c.getStructureSpecificStrengthFactor(), "B: specific strength factor");
        trace.add("coefficients", "structureSpecificStiffnessFactor", c.getStructureSpecificStiffnessFactor(), "B: specific stiffness factor");
        trace.add("coefficients", "structureBucklingResistanceFactor", c.getStructureBucklingResistanceFactor(), "B: buckling resistance factor");
        trace.add("coefficients", "manufacturingQualityFactor", c.getManufacturingQualityFactor(), "C: manufacturing quality factor");
        trace.add("coefficients", "manufacturingDefectPenaltyFactor", c.getManufacturingDefectPenaltyFactor(), "C: manufacturing defect penalty factor");
        trace.add("coefficients", "assemblyJointEfficiencyFactor", c.getAssemblyJointEfficiencyFactor(), "D: joint efficiency factor");
        trace.add("coefficients", "assemblyStressConcentrationPenaltyFactor", c.getAssemblyStressConcentrationPenaltyFactor(), "D: stress concentration penalty");
        trace.add("coefficients", "qualityConfidenceFactor", c.getQualityConfidenceFactor(), "E: QA confidence factor");
        trace.add("coefficients", "qualityMarginReductionFactor", c.getQualityMarginReductionFactor(), "E: safety margin reduction allowance");
        trace.add("coefficients", "environmentDegradationFactor", c.getEnvironmentDegradationFactor(), "F: environmental degradation multiplier");
        trace.add("coefficients", "environmentFatigueAccelerationFactor", c.getEnvironmentFatigueAccelerationFactor(), "F: fatigue acceleration factor");
    }

    private double estimateIdealFrameVolumeFraction(ChassisGeometryMetrics geometry) {
        // First calibration hook: sparse/open frames use less material volume than dense shells.
        // Lower packing ratio suggests more empty envelope, but not linearly "free".
        double fill = geometry.getPackingFillRatio();
        double fraction = 0.035d + (fill * 0.045d);
        return Math.max(0.02d, Math.min(0.12d, fraction));
    }

    private double computeAccelerationDemandFactor(AccelerationEnvelope a) {
        // Weighted combination: longitudinal drives thrust frame loads, lateral/vertical drive maneuver loads.
        double weighted = (1.00d * a.getLongitudinalG()) + (0.85d * a.getLateralG()) + (0.85d * a.getVerticalG());
        return Math.max(1.0d, 1.0d + weighted);
    }

    private double computeSlendernessPenalty(ChassisGeometryMetrics geometry) {
        double worst = Math.max(geometry.getSlendernessLengthToWidth(), geometry.getSlendernessLengthToHeight());
        double excess = Math.max(0.0d, worst - 1.5d);
        return 1.0d / (1.0d + 0.06d * excess + 0.004d * excess * excess);
    }

    private double computePackingPenalty(ChassisGeometryMetrics geometry) {
        double fill = geometry.getPackingFillRatio();
        if (fill <= 0.0d) {
            return 0.5d;
        }
        // Very low fill means lots of empty envelope and longer unsupported runs.
        if (fill < 0.20d) {
            return 0.70d;
        }
        if (fill > 0.85d) {
            // Very dense packing can hurt service routing and thermal/stress path simplicity.
            return 0.95d;
        }
        return 1.00d;
    }

    private double computeGlobalBucklingReserveFactor(
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet c,
            double accelerationDemandFactor) {
        double slenderPenalty = computeSlendernessPenalty(geometry);
        double structureBuckling = Math.max(0.30d, c.getStructureBucklingResistanceFactor());
        double stiffnessSupport = Math.sqrt(Math.max(0.20d, c.getMaterialStiffnessIndex() * c.getStructureSpecificStiffnessFactor()));
        return 10.0d * slenderPenalty * structureBuckling * stiffnessSupport / Math.max(1.0d, accelerationDemandFactor);
    }

    private double computeLocalBucklingReserveFactor(
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet c,
            double accelerationDemandFactor,
            double bendingLoadFraction,
            double torsionProxyFraction) {
        double fill = geometry.getPackingFillRatio();
        double fillShapeFactor;
        if (fill < 0.20d) {
            fillShapeFactor = 0.70d;
        } else if (fill < 0.45d) {
            fillShapeFactor = 0.90d;
        } else if (fill <= 0.80d) {
            fillShapeFactor = 1.00d;
        } else {
            fillShapeFactor = 0.88d;
        }

        double panelStiffness = Math.sqrt(Math.max(0.20d, c.getMaterialStiffnessIndex() * c.getStructureSpecificStiffnessFactor()));
        double panelBuckling = Math.max(0.30d, c.getStructureBucklingResistanceFactor());
        double brittlePenalty = Math.sqrt(Math.max(0.20d, c.getMaterialBrittlenessPenaltyFactor()));
        double localLoadComplexity = Math.max(0.0d, Math.min(1.5d, 0.8d * bendingLoadFraction + 1.0d * torsionProxyFraction));
        double localBendingTorsionPenalty = 1.0d - (0.25d * localLoadComplexity / Math.max(0.70d, c.getStructureDamageToleranceFactor()));
        return 8.0d
                * fillShapeFactor
                * panelStiffness
                * panelBuckling
                * brittlePenalty
                * Math.max(0.65d, localBendingTorsionPenalty)
                / Math.max(1.0d, accelerationDemandFactor);
    }

    private double computeRealizationFactor(ChassisCoefficientSet c, ChassisGeometryMetrics geometry) {
        double scalePenalty = 1.0d / (1.0d + Math.max(0.0d, geometry.getCharacteristicSpanM() - 50.0d) / 500.0d);
        double inspectionPenalty = 1.0d / (1.0d + Math.max(0.0d, geometry.getPackingFillRatio() - 0.70d));

        double factor = c.getManufacturingQualityFactor()
                * c.getManufacturingDefectPenaltyFactor()
                * c.getAssemblyJointEfficiencyFactor()
                / Math.max(1.0d, c.getAssemblyStressConcentrationPenaltyFactor())
                * scalePenalty
                * inspectionPenalty;

        double capped = Math.min(factor, physicsLimits.getPracticalEfficiencyCap());
        return Math.max(0.0d, capped);
    }

    private double computeSafetyFactor(SafetyPriority priority, ChassisCoefficientSet coefficients) {
        double base;
        if (priority == SafetyPriority.ECONOMY) {
            base = 1.20d;
        } else if (priority == SafetyPriority.HIGH_MARGIN) {
            base = 1.80d;
        } else {
            base = 1.50d;
        }

        double qaBenefit = clamp01(coefficients.getQualityMarginReductionFactor());
        double adjusted = base - (base - physicsLimits.getSafetyFactorFloor()) * (qaBenefit * 0.25d);
        return Math.max(physicsLimits.getSafetyFactorFloor(), adjusted);
    }

    private double estimateRecommendedFrameMass(
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet coefficients,
            double finalOperationalMaxSupportedMassKg,
            ChassisCalculationTrace trace) {
        double occupiedVolumeM3 = geometry.getOccupiedVolumeM3();
        double density = coefficients.getMaterialDensityKgM3();
        double structureDensityFactor = clampPositive(coefficients.getStructureEffectiveDensityFactor());

        // Primary load path material volume fraction (before joints/reinforcements):
        // lower than initial v0 constant and shaped by structure and stiffness demand.
        double basePrimaryFraction = 0.010d + (0.012d * geometry.getPackingFillRatio());
        double stiffnessRelief = 1.0d / Math.sqrt(Math.max(0.25d, coefficients.getMaterialStiffnessIndex() * coefficients.getStructureSpecificStiffnessFactor()));
        double strengthRelief = 1.0d / Math.sqrt(Math.max(0.25d, coefficients.getMaterialStrengthIndex() * coefficients.getStructureSpecificStrengthFactor()));
        double primaryFraction = basePrimaryFraction * (0.65d * stiffnessRelief + 0.35d * strengthRelief);
        primaryFraction = Math.max(0.004d, Math.min(0.060d, primaryFraction));

        double primaryFrameMassKg = occupiedVolumeM3
                * density
                * structureDensityFactor
                * primaryFraction;

        // Interfaces / joints / load transfer overhead. Higher for poor joints and anisotropic/brittle materials.
        double jointDifficulty = Math.max(1.0d, coefficients.getAssemblyStressConcentrationPenaltyFactor())
                / Math.max(0.35d, coefficients.getAssemblyJointEfficiencyFactor());
        double materialInterfacePenalty = 1.0d
                / Math.max(0.40d, coefficients.getMaterialAnisotropyPenaltyFactor() * coefficients.getMaterialBrittlenessPenaltyFactor());
        double interfaceMassFractionOfPrimary = 0.08d * jointDifficulty * materialInterfacePenalty;
        interfaceMassFractionOfPrimary = Math.max(0.03d, Math.min(0.90d, interfaceMassFractionOfPrimary));
        double jointsAndInterfacesMassKg = primaryFrameMassKg * interfaceMassFractionOfPrimary;

        // Reinforcement/allowances for survivability, thermal cycling, and quality shortfalls.
        double thermalPenalty = 1.0d / Math.max(0.35d, coefficients.getMaterialTemperaturePenaltyFactor() * coefficients.getMaterialThermalCyclingPenaltyFactor());
        double environmentPenalty = Math.max(1.0d, coefficients.getEnvironmentFatigueAccelerationFactor());
        double qaPenalty = 1.0d / Math.max(0.40d, coefficients.getQualityConfidenceFactor());
        double bucklingPenalty = 1.0d / Math.max(0.40d, coefficients.getStructureBucklingResistanceFactor());

        double reinforcementMassFractionOfPrimary = 0.04d
                * Math.sqrt(thermalPenalty)
                * Math.sqrt(environmentPenalty)
                * Math.sqrt(qaPenalty)
                * Math.sqrt(bucklingPenalty);
        reinforcementMassFractionOfPrimary = Math.max(0.02d, Math.min(0.60d, reinforcementMassFractionOfPrimary));
        double reinforcementMassKg = primaryFrameMassKg * reinforcementMassFractionOfPrimary;

        // A final planning floor tied to target ship mass (not a cap): avoids underestimating structure in strong-material scenarios.
        double planningFraction = 0.025d
                + 0.015d / Math.max(0.35d, coefficients.getAssemblyJointEfficiencyFactor())
                + 0.010d / Math.max(0.40d, coefficients.getQualityConfidenceFactor());
        double planningMassFloorKg = finalOperationalMaxSupportedMassKg * planningFraction;

        double componentSumKg = primaryFrameMassKg + jointsAndInterfacesMassKg + reinforcementMassKg;
        double recommended = Math.max(componentSumKg, planningMassFloorKg);

        if (trace != null) {
            trace.add("final", "primaryFrameMassKg", primaryFrameMassKg, "Primary load-path structural mass estimate");
            trace.add("final", "jointsAndInterfacesMassKg", jointsAndInterfacesMassKg, "Joints, interfaces, and load transfer reinforcement mass");
            trace.add("final", "reinforcementMassKg", reinforcementMassKg, "Thermal/fatigue/QA/buckling reinforcement allowance");
            trace.add("final", "planningMassFloorKg", planningMassFloorKg, "Mass floor tied to final supported mass planning fraction");
            trace.add("final", "recommendedFrameMassComponentSumKg", componentSumKg, "Sum of frame component estimates before planning floor");
        }
        return recommended;
    }

    private List<String> buildWarnings(
            ChassisCalculationInput input,
            ChassisGeometryMetrics geometry,
            ChassisCoefficientSet coefficients,
            ChassisIdealLimit idealLimit,
            double finalOperationalMaxSupportedMassKg,
            double bucklingReserveIndex) {
        List<String> warnings = new ArrayList<String>();

        if (geometry.getPackingFillRatio() < 0.25d) {
            warnings.add("Low packing fill ratio: geometry bounding box is much larger than occupied volume");
        }
        if (geometry.getSlendernessLengthToWidth() > 8.0d || geometry.getSlendernessLengthToHeight() > 8.0d) {
            warnings.add("High slenderness geometry may require stronger structure type or higher quality assembly");
        }
        if (bucklingReserveIndex < physicsLimits.getBucklingReserveFloor()) {
            warnings.add("Buckling reserve is below floor for this model");
        }
        if (coefficients.getAssemblyJointEfficiencyFactor() < 0.70d) {
            warnings.add("Assembly joint efficiency is low; joints will dominate chassis limit");
        }
        if (coefficients.getEnvironmentDegradationFactor() < 0.80d) {
            warnings.add("Environment profile strongly degrades structural capacity");
        }
        if (finalOperationalMaxSupportedMassKg > 0.0d && finalOperationalMaxSupportedMassKg < estimateRecommendedFrameMass(geometry, coefficients, finalOperationalMaxSupportedMassKg, null)) {
            warnings.add("Model inconsistency: recommended frame mass exceeds final supported mass (recalibrate ideal load factor or frame volume fraction)");
        }
        if (idealLimit.getIdealMaxSupportedMassKg() <= 0.0d || finalOperationalMaxSupportedMassKg <= 0.0d) {
            warnings.add("Calculated mass support limit is non-positive; inputs or coefficients are outside usable range");
        }
        if (resolveEffectiveAccelerationEnvelope(input, null).getMaxG() > 15.0d) {
            warnings.add("High-G design request: validate material, structure, and QA assumptions carefully");
        }

        return warnings;
    }

    private double clamp01(double value) {
        return Math.max(0.0d, Math.min(1.0d, value));
    }

    private double clampPositive(double value) {
        return value <= 0.0d ? 0.000001d : value;
    }

    private double computeThrustLineOffsetMomentIndex(ChassisCalculationInput input, ChassisGeometryMetrics geometry) {
        if (input == null || geometry == null) {
            return 0.0d;
        }
        ThrustLayout thrustLayout = input.getThrustLayout();
        if (thrustLayout == null || thrustLayout.getThrustNodes() == null || thrustLayout.getThrustNodes().isEmpty()) {
            return computeEnginePlacementProfileMomentIndex(input.getStructuralDesignBasis());
        }

        Vector3 com = resolveCenterOfMass(input);
        if (com == null) {
            com = new Vector3(0.0d, 0.0d, 0.0d);
        }

        double totalThrust = 0.0d;
        double weightedLateralOffsetM = 0.0d;
        double weightedVerticalOffsetM = 0.0d;
        double weightedAxialOffsetM = 0.0d;
        double asymmetryAccumulator = 0.0d;

        for (ThrustNode node : thrustLayout.getThrustNodes()) {
            if (node == null || node.getPosition() == null || node.getDirectionUnit() == null) {
                continue;
            }
            double thrust = Math.max(0.0d, node.getMaxThrustNewton());
            if (thrust <= 0.0d) {
                continue;
            }
            totalThrust += thrust;

            double dx = node.getPosition().getX() - com.getX();
            double dy = node.getPosition().getY() - com.getY();
            double dz = node.getPosition().getZ() - com.getZ();

            weightedAxialOffsetM += Math.abs(dx) * thrust;
            weightedLateralOffsetM += Math.abs(dy) * thrust;
            weightedVerticalOffsetM += Math.abs(dz) * thrust;

            // Direction not aligned with the principal axis increases bending/torsion demand.
            Vector3 dir = node.getDirectionUnit();
            double dirLateralMagnitude = Math.sqrt(dir.getY() * dir.getY() + dir.getZ() * dir.getZ());
            asymmetryAccumulator += dirLateralMagnitude * thrust;
        }

        if (totalThrust <= 0.0d) {
            return 0.0d;
        }

        double avgAxialOffsetM = weightedAxialOffsetM / totalThrust;
        double avgLateralOffsetM = weightedLateralOffsetM / totalThrust;
        double avgVerticalOffsetM = weightedVerticalOffsetM / totalThrust;
        double avgTransverseOffsetM = Math.sqrt(avgLateralOffsetM * avgLateralOffsetM + avgVerticalOffsetM * avgVerticalOffsetM);
        double asymmetryFactor = thrustLayout.isSymmetricPrimaryThrust()
                ? 1.0d + (0.35d * (asymmetryAccumulator / totalThrust))
                : 1.0d + (0.70d * (asymmetryAccumulator / totalThrust));

        double characteristicSpan = Math.max(1.0d, geometry.getCharacteristicSpanM());
        double transverseOffsetRatio = avgTransverseOffsetM / characteristicSpan;
        double axialSeparationRatio = avgAxialOffsetM / characteristicSpan;

        // Axial separation itself is not bad, but with off-axis thrust it increases bending moment leverage.
        double leverAmplification = 1.0d + 0.35d * Math.min(2.5d, axialSeparationRatio);

        double index = transverseOffsetRatio * leverAmplification * asymmetryFactor;
        double exactIndex = Math.max(0.0d, Math.min(2.5d, index));
        double profileFallback = computeEnginePlacementProfileMomentIndex(input.getStructuralDesignBasis());
        // If profile indicates a more complex distributed placement than provided nodes capture (early design stage), keep a small uncertainty floor.
        return Math.max(exactIndex, 0.35d * profileFallback);
    }

    private Vector3 resolveCenterOfMass(ChassisCalculationInput input) {
        if (input == null || input.getMassDistributionModel() == null) {
            return null;
        }
        if (input.getMassDistributionModel().getCenterOfMassLoaded() != null) {
            return input.getMassDistributionModel().getCenterOfMassLoaded();
        }
        if (input.getMassDistributionModel().getCenterOfMassFueled() != null) {
            return input.getMassDistributionModel().getCenterOfMassFueled();
        }
        return input.getMassDistributionModel().getCenterOfMassDry();
    }

    private double computeEnginePlacementProfileMomentIndex(StructuralDesignBasis basis) {
        if (basis == null) {
            return 0.0d;
        }
        int p = basis.getEnginePlacementProfileClass();
        if (p <= 0) { return 0.0d; }   // unknown/not set
        if (p == 1) { return 0.05d; }  // aft axial single-line cluster
        if (p == 2) { return 0.16d; }  // aft distributed
        if (p == 3) { return 0.06d; }  // fore axial single-line cluster
        if (p == 4) { return 0.18d; }  // fore distributed
        if (p == 5) { return 0.03d; }  // midship axial single-line
        if (p == 6) { return 0.09d; }  // midship distributed (advanced, structurally good)
        if (p == 7) { return 0.10d; }  // fore+mid+aft axial chain
        if (p == 8) { return 0.17d; }  // fore+mid+aft distributed chain
        if (p == 9) { return 0.12d; }  // full-length axial chain
        if (p == 10) { return 0.20d; } // full-length distributed chain
        if (p == 11) { return 0.45d; } // offset pods / asymmetric
        if (p == 12) { return 0.11d; } // hull-wide distributed field (advanced control can reduce moments)
        if (p == 13) { return 0.14d; } // modular reconfigurable internal array
        return 0.0d;
    }

    private double computeEnginePlacementProfileAxialLoadBias(StructuralDesignBasis basis) {
        if (basis == null) {
            return 0.0d;
        }
        int p = basis.getEnginePlacementProfileClass();
        if (p <= 0) { return 0.0d; }
        if (p == 1 || p == 2) { return -0.70d; } // aft push => compression-dominant
        if (p == 3 || p == 4) { return 0.70d; }  // fore tractor => tension-dominant
        if (p == 5 || p == 6) { return 0.0d; }   // midship layouts can be balanced/mixed
        if (p == 7 || p == 8) { return -0.10d; } // fore+mid+aft usually mixed, slight compression bias in practice
        if (p == 9 || p == 10) { return 0.0d; }  // chain layouts mixed
        if (p == 11) { return 0.0d; }            // offset pods mostly bending/mixed
        if (p == 12 || p == 13) { return 0.0d; } // distributed fields/arrays mixed by control
        return 0.0d;
    }

    private int resolveEnginePlacementProfileClass(ChassisCalculationInput input) {
        return (input == null || input.getStructuralDesignBasis() == null)
                ? 0
                : input.getStructuralDesignBasis().getEnginePlacementProfileClass();
    }

    private double computeAxialLoadModeBias(ChassisCalculationInput input) {
        return computeAxialLoadModeDiagnostics(input).bias;
    }

    private AxialLoadModeDiagnostics computeAxialLoadModeDiagnostics(ChassisCalculationInput input) {
        if (input == null) {
            return new AxialLoadModeDiagnostics(0.0d, "none", 0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
        }

        List<StructuralLoadCase> allCases = new ArrayList<StructuralLoadCase>();
        if (input.getLoadCaseSet() != null && !input.getLoadCaseSet().isEmpty()) {
            allCases.addAll(input.getLoadCaseSet().getLoadCases());
        }
        allCases.addAll(buildCapabilityGeneratedLoadCases(input));

        boolean hasCritical = false;
        for (StructuralLoadCase c : allCases) {
            if (c != null && c.isCertificationCritical()) {
                hasCritical = true;
                break;
            }
        }

        double weightedBiasSum = 0.0d;
        double weightedMagnitudeSum = 0.0d;
        double compressionWeight = 0.0d;
        double tensionWeight = 0.0d;
        double mixedWeight = 0.0d;
        double bendingWeight = 0.0d;
        double torsionProxyWeightedSum = 0.0d;
        boolean usedExplicit = false;
        boolean usedGenerated = false;
        for (StructuralLoadCase c : allCases) {
            if (c == null) {
                continue;
            }
            if (hasCritical && !c.isCertificationCritical()) {
                continue;
            }
            double mode = mapPrimaryLoadModeToAxialBias(c.getPrimaryLoadMode());
            if (Math.abs(mode) < 1e-9) {
                continue;
            }
            AccelerationEnvelope e = c.toAccelerationEnvelopeAbs();
            double severity = Math.max(0.01d, e.getLongitudinalG() + 0.4d * e.getLateralG() + 0.4d * e.getVerticalG());
            double durationWeight = Math.max(0.2d, Math.min(2.0d, Math.sqrt(Math.max(0.01d, c.getDurationSec())) / 4.0d));
            double occurrenceWeight = Math.max(0.1d, c.getOccurrenceWeight());
            double w = severity * durationWeight * occurrenceWeight;
            if (isGeneratedCapabilityCase(c)) {
                usedGenerated = true;
            } else {
                usedExplicit = true;
            }
            weightedBiasSum += mode * w;
            weightedMagnitudeSum += w;
            if (mode < -0.5d) {
                compressionWeight += w;
            } else if (mode > 0.5d) {
                tensionWeight += w;
            } else {
                mixedWeight += w;
            }

            String loadMode = c.getPrimaryLoadMode() == null ? "" : c.getPrimaryLoadMode().trim().toLowerCase(java.util.Locale.US);
            if ("bending".equals(loadMode)) {
                bendingWeight += w;
            } else if ("mixed".equals(loadMode)) {
                bendingWeight += 0.6d * w;
            } else {
                // Axial cases can still induce bending/torsion if transverse loads are present.
                double transverse = e.getLateralG() + e.getVerticalG();
                double total = Math.max(1e-9d, e.getLongitudinalG() + transverse);
                bendingWeight += (0.25d * (transverse / total)) * w;
            }

            double transverseMax = Math.max(e.getLateralG(), e.getVerticalG());
            double transverseMin = Math.min(e.getLateralG(), e.getVerticalG());
            double transverseBalance = transverseMax <= 1e-9d ? 0.0d : (transverseMin / transverseMax);
            double transverseShare = (e.getLateralG() + e.getVerticalG())
                    / Math.max(1e-9d, e.getLongitudinalG() + e.getLateralG() + e.getVerticalG());
            double profileTorsionBias = computeEnginePlacementProfileTorsionBias(input.getStructuralDesignBasis());
            double torsionProxy = Math.max(0.0d, Math.min(1.0d, (0.65d * transverseBalance * transverseShare) + (0.35d * profileTorsionBias)));
            torsionProxyWeightedSum += torsionProxy * w;
        }

        if (weightedMagnitudeSum <= 0.0d) {
            double profileBias = computeEnginePlacementProfileAxialLoadBias(input.getStructuralDesignBasis());
            return new AxialLoadModeDiagnostics(
                    profileBias,
                    "profile-fallback",
                    0.0d,
                    0.0d,
                    0.0d,
                    0.0d,
                    computeEnginePlacementProfileTorsionBias(input.getStructuralDesignBasis())
            );
        }

        double bias = weightedBiasSum / weightedMagnitudeSum;
        double profileBias = computeEnginePlacementProfileAxialLoadBias(input.getStructuralDesignBasis());
        // Blend lightly with profile so conceptual intent still nudges ambiguous case sets.
        double blended = (0.85d * bias) + (0.15d * profileBias);
        double totalModeWeight = Math.max(1e-9d, compressionWeight + tensionWeight + mixedWeight);
        double bendingFraction = Math.max(0.0d, Math.min(1.0d, bendingWeight / Math.max(1e-9d, weightedMagnitudeSum)));
        double torsionProxyFraction = Math.max(0.0d, Math.min(1.0d, torsionProxyWeightedSum / Math.max(1e-9d, weightedMagnitudeSum)));
        String source;
        if (usedExplicit && usedGenerated) {
            source = "explicit+generated";
        } else if (usedExplicit) {
            source = "explicit";
        } else if (usedGenerated) {
            source = "generated";
        } else {
            source = "unknown";
        }
        return new AxialLoadModeDiagnostics(
                Math.max(-1.0d, Math.min(1.0d, blended)),
                source,
                compressionWeight / totalModeWeight,
                tensionWeight / totalModeWeight,
                mixedWeight / totalModeWeight,
                bendingFraction,
                torsionProxyFraction
        );
    }

    private double mapPrimaryLoadModeToAxialBias(String primaryLoadMode) {
        if (primaryLoadMode == null) {
            return 0.0d;
        }
        String m = primaryLoadMode.trim().toLowerCase(java.util.Locale.US);
        if ("compression".equals(m)) {
            return -1.0d;
        }
        if ("tension".equals(m)) {
            return 1.0d;
        }
        if ("bending".equals(m)) {
            return 0.0d;
        }
        if ("mixed".equals(m)) {
            return 0.0d;
        }
        return 0.0d;
    }

    private double computeEnginePlacementProfileTorsionBias(StructuralDesignBasis basis) {
        if (basis == null) {
            return 0.0d;
        }
        int p = basis.getEnginePlacementProfileClass();
        if (p == 11) { return 0.80d; } // offset asymmetric pods
        if (p == 2 || p == 4 || p == 8 || p == 10) { return 0.35d; } // distributed chains can create differential torsion
        if (p == 12 || p == 13) { return 0.25d; } // advanced distributed arrays, controlled but complex
        if (p == 6) { return 0.15d; } // midship distributed usually manageable
        return 0.05d; // small residual for other layouts
    }

    private static final class AxialLoadModeDiagnostics {
        private final double bias;
        private final String source;
        private final double compressionFraction;
        private final double tensionFraction;
        private final double mixedFraction;
        private final double bendingFraction;
        private final double torsionProxyFraction;

        private AxialLoadModeDiagnostics(
                double bias,
                String source,
                double compressionFraction,
                double tensionFraction,
                double mixedFraction,
                double bendingFraction,
                double torsionProxyFraction) {
            this.bias = bias;
            this.source = source;
            this.compressionFraction = compressionFraction;
            this.tensionFraction = tensionFraction;
            this.mixedFraction = mixedFraction;
            this.bendingFraction = bendingFraction;
            this.torsionProxyFraction = torsionProxyFraction;
        }
    }

    private AccelerationEnvelope resolveEffectiveAccelerationEnvelope(ChassisCalculationInput input, ChassisCalculationTrace trace) {
        StructuralLoadCaseSet loadCaseSet = input.getLoadCaseSet();
        List<StructuralLoadCase> generatedCases = buildCapabilityGeneratedLoadCases(input);
        boolean hasExplicitCases = loadCaseSet != null && !loadCaseSet.isEmpty();
        boolean hasAnyCases = hasExplicitCases || !generatedCases.isEmpty();

        if (!hasAnyCases) {
            AccelerationEnvelope fallback = input.getAccelerationEnvelope();
            if (fallback == null) {
                fallback = new AccelerationEnvelope(0.0d, 0.0d, 0.0d);
            }
            fallback = applyCapabilityEnvelopeAugment(input, fallback, trace);
            if (trace != null) {
                trace.add("loads", "enginePlacementProfileClass", (double) resolveEnginePlacementProfileClass(input), "Structural design engine placement profile");
                trace.add("loads", "enginePlacementProfileMomentIndexFallback", computeEnginePlacementProfileMomentIndex(input.getStructuralDesignBasis()), "Profile-based thrust-line offset moment proxy");
                trace.add("loads", "source", "AccelerationEnvelope fallback");
                trace.add("loads", "effectiveLongitudinalG", fallback.getLongitudinalG(), "Direct acceleration envelope input");
                trace.add("loads", "effectiveLateralG", fallback.getLateralG(), "Direct acceleration envelope input");
                trace.add("loads", "effectiveVerticalG", fallback.getVerticalG(), "Direct acceleration envelope input");
            }
            return fallback;
        }

        boolean hasCritical = false;
        List<StructuralLoadCase> allCases = new ArrayList<StructuralLoadCase>();
        if (hasExplicitCases) {
            allCases.addAll(loadCaseSet.getLoadCases());
        }
        allCases.addAll(generatedCases);

        for (StructuralLoadCase c : allCases) {
            if (c != null && c.isCertificationCritical()) {
                hasCritical = true;
                break;
            }
        }

        double maxLong = 0.0d;
        double maxLat = 0.0d;
        double maxVert = 0.0d;
        int selectedCount = 0;
        int generatedSelectedCount = 0;
        for (StructuralLoadCase c : allCases) {
            if (c == null) {
                continue;
            }
            if (hasCritical && !c.isCertificationCritical()) {
                continue;
            }
            AccelerationEnvelope e = c.toAccelerationEnvelopeAbs();
            maxLong = Math.max(maxLong, e.getLongitudinalG());
            maxLat = Math.max(maxLat, e.getLateralG());
            maxVert = Math.max(maxVert, e.getVerticalG());
            selectedCount++;
            if (isGeneratedCapabilityCase(c)) {
                generatedSelectedCount++;
            }
        }

        if (selectedCount <= 0) {
            AccelerationEnvelope fallback = input.getAccelerationEnvelope();
            if (fallback == null) {
                fallback = new AccelerationEnvelope(0.0d, 0.0d, 0.0d);
            }
            return fallback;
        }

        AccelerationEnvelope effective = new AccelerationEnvelope(maxLong, maxLat, maxVert);
        effective = applyCapabilityEnvelopeAugment(input, effective, trace);
        if (trace != null) {
            trace.add("loads", "enginePlacementProfileClass", (double) resolveEnginePlacementProfileClass(input), "Structural design engine placement profile");
            trace.add("loads", "enginePlacementProfileMomentIndexFallback", computeEnginePlacementProfileMomentIndex(input.getStructuralDesignBasis()), "Profile-based thrust-line offset moment proxy");
            if (hasExplicitCases && !generatedCases.isEmpty()) {
                trace.add("loads", "source", hasCritical ? "Explicit+Capability generated critical envelope" : "Explicit+Capability generated all-case envelope");
            } else if (hasExplicitCases) {
                trace.add("loads", "source", hasCritical ? "LoadCaseSet critical envelope" : "LoadCaseSet all-case envelope");
            } else {
                trace.add("loads", "source", hasCritical ? "Capability generated critical envelope" : "Capability generated all-case envelope");
            }
            trace.add("loads", "selectedLoadCaseCount", (double) selectedCount, "Count of load cases included in envelope");
            trace.add("loads", "generatedCapabilityLoadCaseCount", (double) generatedCases.size(), "Capability-derived synthetic load cases before filtering");
            trace.add("loads", "selectedGeneratedCapabilityLoadCaseCount", (double) generatedSelectedCount, "Selected capability-derived load cases included in envelope");
            trace.add("loads", "effectiveLongitudinalG", effective.getLongitudinalG(), "Envelope max absolute longitudinal acceleration");
            trace.add("loads", "effectiveLateralG", effective.getLateralG(), "Envelope max absolute lateral acceleration");
            trace.add("loads", "effectiveVerticalG", effective.getVerticalG(), "Envelope max absolute vertical acceleration");
        }
        return effective;
    }

    private List<StructuralLoadCase> buildCapabilityGeneratedLoadCases(ChassisCalculationInput input) {
        List<StructuralLoadCase> generated = new ArrayList<StructuralLoadCase>();
        StructuralDesignBasis basis = input.getStructuralDesignBasis();
        CapabilityClassSet caps = basis == null ? null : basis.getCapabilityClassSet();
        if (caps == null) {
            return generated;
        }

        int landing = caps.getPlanetLandingCapabilityClass();
        if (landing == 1) {
            generated.add(new StructuralLoadCase("Capability: rough-field landing impact", 0.20d, 0.20d, 0.65d, 3.0d, 0.3d, true, "mixed"));
        } else if (landing >= 2 && landing <= 4) {
            double v = 0.45d + 0.10d * (landing - 2);
            double l = 0.15d + 0.05d * (landing - 2);
            generated.add(new StructuralLoadCase("Capability: prepared-pad landing load", l, 0.12d, v, 2.0d, 0.3d, true, "compression"));
        } else if (landing == 5) {
            generated.add(new StructuralLoadCase("Capability: aerodynamic landing flare", 0.60d, 0.45d, 0.45d, 20.0d, 0.2d, true, "bending"));
            generated.add(new StructuralLoadCase("Capability: aerodynamic takeoff rotation", 0.55d, 0.35d, 0.40d, 15.0d, 0.2d, true, "bending"));
        } else if (landing == 6) {
            generated.add(new StructuralLoadCase("Capability: water landing slam load", 0.20d, 0.30d, 0.75d, 2.0d, 0.2d, true, "mixed"));
        }

        int surface = caps.getGroundSupportSurfaceClass();
        if (surface > 0) {
            double vert = 0.12d + Math.min(0.20d, 0.05d * surface);
            generated.add(new StructuralLoadCase("Capability: ground support uneven load", 0.05d, 0.08d, vert, 60.0d, 0.8d, false, "compression"));
        }

        int agility = caps.getManeuverAgilityClass();
        if (agility > 0) {
            double lat = 0.25d * agility;
            double vert = 0.15d * agility;
            generated.add(new StructuralLoadCase("Capability: agility certification maneuver", 0.10d, lat, vert, 30.0d, 0.4d, true, "mixed"));
        }

        int damageTol = caps.getStructuralDamageToleranceClass();
        if (damageTol >= 3) {
            double longG = 0.10d * (damageTol - 2);
            generated.add(new StructuralLoadCase("Capability: damage-tolerance reserve certification", longG, 0.0d, 0.0d, 10.0d, 0.2d, true, "compression"));
        }

        appendEnginePlacementGeneratedLoadCases(input, generated);

        return generated;
    }

    private boolean isGeneratedCapabilityCase(StructuralLoadCase c) {
        return c != null && c.getName() != null && c.getName().startsWith("Capability:");
    }

    private void appendEnginePlacementGeneratedLoadCases(ChassisCalculationInput input, List<StructuralLoadCase> generated) {
        if (generated == null) {
            return;
        }
        int profile = resolveEnginePlacementProfileClass(input);
        if (profile <= 0) {
            return;
        }

        if (profile == 3 || profile == 4) {
            // Fore tractor layouts imply primary axial tension certification case.
            generated.add(new StructuralLoadCase("Capability: fore-tractor axial tension case", -0.35d, 0.05d, 0.05d, 20.0d, 0.25d, true, "tension"));
        }
        if (profile == 2 || profile == 4 || profile == 8 || profile == 10 || profile == 12 || profile == 13) {
            generated.add(new StructuralLoadCase("Capability: distributed thrust differential-control case", 0.10d, 0.40d, 0.25d, 15.0d, 0.20d, true, "mixed"));
        }
        if (profile == 11) {
            generated.add(new StructuralLoadCase("Capability: offset thrust bending case", 0.20d, 0.60d, 0.35d, 10.0d, 0.20d, true, "bending"));
        }
        if (profile == 7 || profile == 9) {
            generated.add(new StructuralLoadCase("Capability: axial chain synchronization transient", 0.35d, 0.15d, 0.15d, 8.0d, 0.15d, false, "compression"));
        }
    }

    private AccelerationEnvelope applyCapabilityEnvelopeAugment(
            ChassisCalculationInput input,
            AccelerationEnvelope base,
            ChassisCalculationTrace trace) {
        if (base == null) {
            return new AccelerationEnvelope(0.0d, 0.0d, 0.0d);
        }
        StructuralDesignBasis basis = input.getStructuralDesignBasis();
        CapabilityClassSet caps = basis == null ? null : basis.getCapabilityClassSet();
        if (caps == null) {
            return base;
        }

        double longG = base.getLongitudinalG();
        double latG = base.getLateralG();
        double vertG = base.getVerticalG();

        double addLong = 0.0d;
        double addLat = 0.0d;
        double addVert = 0.0d;

        int landing = caps.getPlanetLandingCapabilityClass();
        if (landing == 1) {
            addVert = Math.max(addVert, 0.35d);
            addLat = Math.max(addLat, 0.15d);
        } else if (landing >= 2 && landing <= 4) {
            addVert = Math.max(addVert, 0.25d + (landing - 2) * 0.10d);
            addLat = Math.max(addLat, 0.10d + (landing - 2) * 0.05d);
        } else if (landing == 5) {
            addLong = Math.max(addLong, 0.40d);
            addLat = Math.max(addLat, 0.30d);
            addVert = Math.max(addVert, 0.35d);
        } else if (landing == 6) {
            addVert = Math.max(addVert, 0.45d);
            addLat = Math.max(addLat, 0.20d);
        }

        int surface = caps.getGroundSupportSurfaceClass();
        if (surface == 1) {
            addVert += 0.10d;
        } else if (surface >= 2) {
            addVert += Math.min(0.25d, 0.05d * (surface - 1));
        }

        int agility = caps.getManeuverAgilityClass();
        if (agility > 0) {
            addLat += 0.20d * agility;
            addVert += 0.10d * agility;
        }

        int damageTol = caps.getStructuralDamageToleranceClass();
        if (damageTol >= 3) {
            // Designing for damage tolerance tends to include harsher certification load allowances.
            addLong += 0.10d * (damageTol - 2);
        }

        AccelerationEnvelope augmented = new AccelerationEnvelope(
                longG + addLong,
                latG + addLat,
                vertG + addVert
        );

        if (trace != null) {
            trace.add("loads", "capabilityAddLongitudinalG", addLong, "Added by capability classes (landing/agility/damage tolerance)");
            trace.add("loads", "capabilityAddLateralG", addLat, "Added by capability classes (landing/agility)");
            trace.add("loads", "capabilityAddVerticalG", addVert, "Added by capability classes (landing/agility/ground support)");
        }
        return augmented;
    }

    private static ChassisCoefficientProvider buildDefaultCoefficientProvider() {
        try {
            Class<?> loaderClass = Class.forName("org.example.shipconstructor.chassis.service.ChassisCoefficientLibraryLoader");
            java.lang.reflect.Method defaultPathMethod = loaderClass.getMethod("defaultSamplePath");
            Object path = defaultPathMethod.invoke(null);

            Class<?> providerClass = Class.forName("org.example.shipconstructor.chassis.service.JsonChassisCoefficientProvider");
            java.lang.reflect.Method fromPathMethod = providerClass.getMethod("fromPath", java.nio.file.Path.class);
            Object provider = fromPathMethod.invoke(null, path);
            if (provider instanceof ChassisCoefficientProvider) {
                return (ChassisCoefficientProvider) provider;
            }
        } catch (Exception ignored) {
        }
        return new DefaultChassisCoefficientProvider();
    }
}
