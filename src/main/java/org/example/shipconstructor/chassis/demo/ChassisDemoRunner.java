package org.example.shipconstructor.chassis.demo;

import org.example.shipconstructor.chassis.domain.AccelerationEnvelope;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.ChassisCalculationResult;
import org.example.shipconstructor.chassis.domain.ChassisTraceEntry;
import org.example.shipconstructor.chassis.domain.CubeDimensions;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;
import org.example.shipconstructor.chassis.domain.CapabilityClassSet;
import org.example.shipconstructor.chassis.domain.MassDistributionModel;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.domain.StructuralLoadCase;
import org.example.shipconstructor.chassis.domain.StructuralLoadCaseSet;
import org.example.shipconstructor.chassis.domain.ThrustLayout;
import org.example.shipconstructor.chassis.domain.MissionPriority;
import org.example.shipconstructor.chassis.domain.SafetyPriority;
import org.example.shipconstructor.chassis.domain.ThrustNode;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.chassis.service.ChassisFrameCalculationService;
import org.example.shipconstructor.chassis.service.DefaultChassisCoefficientProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChassisDemoRunner {
    public static void main(String[] args) {
        ChassisFrameCalculationService service = new ChassisFrameCalculationService(
                new DefaultChassisCoefficientProvider(),
                org.example.shipconstructor.chassis.domain.ChassisPhysicsLimits.defaults()
        );

        List<Scenario> scenarios = new ArrayList<Scenario>();
        scenarios.add(new Scenario(
                "Steel Baseline",
                input(1L, 17L, 3L, 8L, 6L, 1L)
        ));
        scenarios.add(new Scenario(
                "HSLA Rugged",
                input(2L, 17L, 3L, 8L, 12L, 31L)
        ));
        scenarios.add(new Scenario(
                "Titanium Isogrid",
                input(10L, 17L, 26L, 9L, 12L, 8L)
        ));
        scenarios.add(new Scenario(
                "CFRP Sandwich",
                input(39L, 30L, 24L, 17L, 12L, 8L)
        ));
        scenarios.add(new Scenario(
                "Inconel Hot Zone",
                input(13L, 1L, 26L, 9L, 19L, 21L)
        ));
        scenarios.add(new Scenario(
                "Space Tug Push/Pull Load Cases",
                inputWithLoadCases(2L, 10L, 26L, 9L, 12L, 1L)
        ));
        scenarios.add(new Scenario(
                "Utility Lander Capability-Augmented",
                inputCapabilityAugmentedLander(5L, 10L, 3L, 8L, 6L, 34L)
        ));

        System.out.println("=== Chassis Demo (v0 calibration) ===");
        for (Scenario scenario : scenarios) {
            ChassisCalculationResult result = service.calculate(scenario.input);
            printScenario(scenario.name, result);
        }
    }

    private static ChassisCalculationInput inputWithLoadCases(
            Long baseMaterialId,
            Long structureTypeId,
            Long manufacturingId,
            Long assemblyId,
            Long qualityId,
            Long environmentId) {
        List<StructuralLoadCase> cases = new ArrayList<StructuralLoadCase>();
        cases.add(new StructuralLoadCase("Nominal aft thrust cruise", 0.18d, 0.03d, 0.03d, 86400.0d, 1.0d, false, "compression"));
        cases.add(new StructuralLoadCase("Emergency aft thrust burn", 0.45d, 0.08d, 0.08d, 1200.0d, 0.2d, true, "compression"));
        cases.add(new StructuralLoadCase("Fore tractor decel burn", -0.40d, 0.06d, 0.06d, 900.0d, 0.15d, true, "tension"));
        cases.add(new StructuralLoadCase("Docking bump", 0.10d, 0.10d, 0.10d, 5.0d, 0.5d, false, "mixed"));

        List<ThrustNode> thrustNodes = new ArrayList<ThrustNode>();
        thrustNodes.add(new ThrustNode("AftMain", new Vector3(-35.0d, 0.0d, 0.0d), new Vector3(1.0d, 0.0d, 0.0d), 3.2e7));
        thrustNodes.add(new ThrustNode("ForeTractor", new Vector3(38.0d, 0.0d, 0.0d), new Vector3(-1.0d, 0.0d, 0.0d), 2.4e7));

        return new ChassisCalculationInput(
                3,
                320,
                new CubeDimensions(20, 5, 4),
                new EngineeringStackSelection(baseMaterialId, structureTypeId, manufacturingId, assemblyId, qualityId, environmentId),
                null,
                new StructuralDesignBasis(
                        "Space tug push/pull",
                        true,
                        false,
                        false,
                        true,
                        50000,
                        new CapabilityClassSet(
                                0, // no landing
                                0, // no ground support
                                0, // vacuum only
                                1, // basic pressure differential
                                1, // ordinary orbital thermal cycling
                                0, // no corrosion exposure class on chassis
                                0, // no biohazard external requirement here
                                2, // elevated radiation exposure
                                1, // low agility
                                2  // standard+ damage tolerance
                        ),
                        8 // Fore + Mid + Aft distributed chain
                ),
                new StructuralLoadCaseSet(cases),
                new ThrustLayout(thrustNodes, false),
                new MassDistributionModel(
                        new Vector3(0.0d, 0.0d, 0.0d),
                        new Vector3(2.0d, 0.0d, 0.0d),
                        new Vector3(-4.0d, 0.0d, 0.0d),
                        null
                ),
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }

    private static ChassisCalculationInput inputCapabilityAugmentedLander(
            Long baseMaterialId,
            Long structureTypeId,
            Long manufacturingId,
            Long assemblyId,
            Long qualityId,
            Long environmentId) {
        return new ChassisCalculationInput(
                2, // 2m cube class
                180,
                new CubeDimensions(15, 4, 4),
                new EngineeringStackSelection(baseMaterialId, structureTypeId, manufacturingId, assemblyId, qualityId, environmentId),
                new AccelerationEnvelope(1.4d, 0.3d, 0.3d),
                new StructuralDesignBasis(
                        "Utility planetary lander",
                        false,
                        true,
                        true,
                        false,
                        12000,
                        new CapabilityClassSet(
                                1, // unprepared landing zones
                                1, // ground/rough support
                                2, // standard atmosphere limited
                                2, // standard pressure diff
                                5, // wide thermal range
                                2, // moderate corrosion exposure
                                1, // mild biohazard/toxicity exposure
                                1, // normal space radiation
                                2, // moderate agility
                                2  // standard damage tolerance
                        ),
                        6 // Midship distributed (advanced-friendly internal layout)
                ),
                null,
                null,
                null,
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }

    private static ChassisCalculationInput input(
            Long baseMaterialId,
            Long structureTypeId,
            Long manufacturingId,
            Long assemblyId,
            Long qualityId,
            Long environmentId) {
        return new ChassisCalculationInput(
                3, // SizeType=3 => 4m cubes (Medium)
                320, // occupied cubes
                new CubeDimensions(20, 5, 4), // bounding cubes = 400
                new EngineeringStackSelection(
                        baseMaterialId,
                        structureTypeId,
                        manufacturingId,
                        assemblyId,
                        qualityId,
                        environmentId
                ),
                new AccelerationEnvelope(4.0d, 2.5d, 2.5d),
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }

    private static void printScenario(String name, ChassisCalculationResult r) {
        System.out.println();
        System.out.println("[" + name + "]");
        System.out.println("  Final max supported mass : " + kgToT(r.getFinalOperationalMaxSupportedMassKg()) + " t");
        System.out.println("  Recommended frame mass   : " + kgToT(r.getRecommendedFrameMassKg()) + " t");
        System.out.println("  Recommended total target : " + kgToT(r.getRecommendedTotalMassTargetKg()) + " t");
        System.out.println("  Frame mass fraction      : " + pct(r.getFrameMassFraction()));
        System.out.println("  Safety factor            : " + f(r.getAppliedSafetyFactor()));
        System.out.println("  Buckling reserve index   : " + f(r.getBucklingReserveIndex()));
        System.out.println("  Joint efficiency comp.   : " + f(r.getJointEfficiencyComposite()));

        printTraceSubset(r, "coefficients",
                "materialDensityKgM3",
                "materialStrengthIndex",
                "materialStiffnessIndex",
                "materialAnisotropyPenaltyFactor",
                "materialBrittlenessPenaltyFactor",
                "materialTemperaturePenaltyFactor",
                "materialThermalCyclingPenaltyFactor");
        printTraceSubset(r, "ideal",
                "baselineFrameMassKg",
                "baseAccelerationDemandFactor",
                "thrustLineOffsetMomentIndex",
                "axialLoadModeBias",
                "accelerationDemandFactor",
                "strengthCapacityFactor",
                "stiffnessCapacityFactor",
                "axialCompressionBucklingPenalty",
                "axialTensionBrittlePenalty",
                "materialPenaltyComposite",
                "idealCapacityMultiplier",
                "idealMaxSupportedMassKg");
        printTraceSubset(r, "qa_env",
                "qualityConfidenceFactor",
                "materialEnvironmentComposite",
                "environmentDegradationFactor",
                "qualityAdjustedLimitKg");
        printTraceSubset(r, "loads",
                "enginePlacementProfileClass",
                "enginePlacementProfileMomentIndexFallback",
                "axialLoadModeCompressionFraction",
                "axialLoadModeTensionFraction",
                "axialLoadModeMixedFraction",
                "bendingLoadFraction",
                "torsionProxyFraction",
                "selectedLoadCaseCount",
                "effectiveLongitudinalG",
                "effectiveLateralG",
                "effectiveVerticalG");

        if (!r.getWarnings().isEmpty()) {
            System.out.println("  Warnings:");
            for (String warning : r.getWarnings()) {
                System.out.println("    - " + warning);
            }
        }
    }

    private static void printTraceSubset(ChassisCalculationResult r, String stage, String... keys) {
        System.out.println("  " + stage + ":");
        for (String key : keys) {
            ChassisTraceEntry e = findTrace(r, stage, key);
            if (e == null) {
                System.out.println("    " + key + " = <missing>");
                continue;
            }
            if (e.getValue() == null) {
                System.out.println("    " + key + " = <null>");
                continue;
            }
            System.out.println("    " + key + " = " + f(e.getValue().doubleValue()));
        }
    }

    private static ChassisTraceEntry findTrace(ChassisCalculationResult r, String stage, String key) {
        for (ChassisTraceEntry e : r.getTrace().getEntries()) {
            if (stage.equals(e.getStage()) && key.equals(e.getKey())) {
                return e;
            }
        }
        return null;
    }

    private static String kgToT(double kg) {
        return String.format(Locale.US, "%.2f", kg / 1000.0d);
    }

    private static String pct(double value) {
        return String.format(Locale.US, "%.2f%%", value * 100.0d);
    }

    private static String f(double value) {
        return String.format(Locale.US, "%.4f", value);
    }

    private static final class Scenario {
        private final String name;
        private final ChassisCalculationInput input;

        private Scenario(String name, ChassisCalculationInput input) {
            this.name = name;
            this.input = input;
        }
    }
}
