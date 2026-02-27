package org.example.shipconstructor.chassis.demo;

import org.example.shipconstructor.chassis.domain.AccelerationEnvelope;
import org.example.shipconstructor.chassis.domain.CapabilityClassSet;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.CubeDimensions;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;
import org.example.shipconstructor.chassis.domain.MassDistributionModel;
import org.example.shipconstructor.chassis.domain.MassNode;
import org.example.shipconstructor.chassis.domain.MissionPriority;
import org.example.shipconstructor.chassis.domain.SafetyPriority;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.domain.StructuralLoadCase;
import org.example.shipconstructor.chassis.domain.StructuralLoadCaseSet;
import org.example.shipconstructor.chassis.domain.ThrustLayout;
import org.example.shipconstructor.chassis.domain.ThrustNode;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphBuildResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphSolveResult;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphAxialEnvelopeSolverService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphBuilderService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphLoadPlannerService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphSummaryFormatter;

import java.util.ArrayList;
import java.util.List;

public class StructuralGraphDemoRunner {
    public static void main(String[] args) {
        StructuralGraphBuilderService builder = new StructuralGraphBuilderService();
        StructuralGraphLoadPlannerService loadPlanner = new StructuralGraphLoadPlannerService();
        StructuralGraphAxialEnvelopeSolverService solver = new StructuralGraphAxialEnvelopeSolverService();

        ChassisCalculationInput tug = buildSpaceTugScenario();
        ChassisCalculationInput lander = buildLanderScenario();

        print("Space Tug Push/Pull (profile=8)", tug, builder, loadPlanner, solver);
        print("Utility Lander (profile=6)", lander, builder, loadPlanner, solver);
    }

    private static void print(
            String title,
            ChassisCalculationInput input,
            StructuralGraphBuilderService builder,
            StructuralGraphLoadPlannerService loadPlanner,
            StructuralGraphAxialEnvelopeSolverService solver) {
        StructuralGraphBuildResult graphResult = builder.build(input);
        StructuralGraphLoadSet loadSet = loadPlanner.plan(input, graphResult.getGraph());
        StructuralGraphSolveResult solveResult = solver.solve(input, graphResult.getGraph(), loadSet);

        System.out.println("\n=== " + title + " ===");
        System.out.print(StructuralGraphSummaryFormatter.summarize(graphResult, loadSet, solveResult));
        if (!graphResult.getWarnings().isEmpty()) {
            System.out.println("Graph warnings:");
            for (String w : graphResult.getWarnings()) {
                System.out.println("  - " + w);
            }
        }
        if (!loadSet.getWarnings().isEmpty()) {
            System.out.println("Load warnings:");
            for (String w : loadSet.getWarnings()) {
                System.out.println("  - " + w);
            }
        }
        if (!solveResult.getWarnings().isEmpty()) {
            System.out.println("Solver warnings:");
            for (String w : solveResult.getWarnings()) {
                System.out.println("  - " + w);
            }
        }
    }

    private static ChassisCalculationInput buildSpaceTugScenario() {
        List<StructuralLoadCase> cases = new ArrayList<StructuralLoadCase>();
        cases.add(new StructuralLoadCase("Nominal aft thrust cruise", 0.18d, 0.03d, 0.03d, 86400.0d, 1.0d, false, "compression"));
        cases.add(new StructuralLoadCase("Emergency aft thrust burn", 0.45d, 0.08d, 0.08d, 1200.0d, 0.2d, true, "compression"));
        cases.add(new StructuralLoadCase("Fore tractor decel burn", -0.40d, 0.06d, 0.06d, 900.0d, 0.15d, true, "tension"));

        List<ThrustNode> thrustNodes = new ArrayList<ThrustNode>();
        thrustNodes.add(new ThrustNode("AftMain-L", new Vector3(-35.0d, -3.0d, 0.0d), new Vector3(1.0d, 0.0d, 0.0d), 2.0e7));
        thrustNodes.add(new ThrustNode("AftMain-R", new Vector3(-35.0d, 3.0d, 0.0d), new Vector3(1.0d, 0.0d, 0.0d), 2.0e7));
        thrustNodes.add(new ThrustNode("ForeTractor", new Vector3(38.0d, 0.0d, 0.0d), new Vector3(-1.0d, 0.0d, 0.0d), 2.4e7));

        List<MassNode> massNodes = new ArrayList<MassNode>();
        massNodes.add(new MassNode("Reactor", new Vector3(4.0d, 0.0d, 0.0d), 180_000.0d));
        massNodes.add(new MassNode("CargoClamp", new Vector3(-10.0d, 0.0d, 0.0d), 120_000.0d));

        return new ChassisCalculationInput(
                3,
                320,
                new CubeDimensions(20, 5, 4),
                new EngineeringStackSelection(2L, 10L, 26L, 9L, 12L, 1L),
                new AccelerationEnvelope(0.5d, 0.2d, 0.2d),
                new StructuralDesignBasis(
                        "Space tug push/pull",
                        true,
                        false,
                        false,
                        true,
                        50000,
                        new CapabilityClassSet(0, 0, 0, 1, 1, 0, 0, 2, 1, 2),
                        8
                ),
                new StructuralLoadCaseSet(cases),
                new ThrustLayout(thrustNodes, false),
                new MassDistributionModel(
                        new Vector3(0.0d, 0.0d, 0.0d),
                        new Vector3(2.0d, 0.0d, 0.0d),
                        new Vector3(-4.0d, 0.0d, 0.0d),
                        massNodes
                ),
                "STR_TRUSS_FRAME",
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }

    private static ChassisCalculationInput buildLanderScenario() {
        return new ChassisCalculationInput(
                2,
                180,
                new CubeDimensions(15, 4, 4),
                new EngineeringStackSelection(5L, 17L, 3L, 8L, 6L, 34L),
                new AccelerationEnvelope(1.4d, 0.3d, 0.3d),
                new StructuralDesignBasis(
                        "Utility planetary lander",
                        false,
                        true,
                        true,
                        false,
                        12000,
                        new CapabilityClassSet(1, 1, 2, 2, 5, 2, 1, 1, 2, 2),
                        6
                ),
                null,
                null,
                new MassDistributionModel(
                        new Vector3(0.0d, 0.0d, 0.0d),
                        new Vector3(1.0d, 0.0d, -0.3d),
                        null,
                        null
                ),
                "STR_ISOGRID_SHELL",
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }
}
