package org.example.shipconstructor.chassis.graph.service;

import org.example.shipconstructor.chassis.domain.CapabilityClassSet;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.domain.StructuralLoadCase;
import org.example.shipconstructor.chassis.domain.StructuralLoadCaseSet;
import org.example.shipconstructor.chassis.domain.ThrustLayout;
import org.example.shipconstructor.chassis.domain.ThrustNode;
import org.example.shipconstructor.chassis.domain.Vector3;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoad;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadTargetType;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadType;
import org.example.shipconstructor.chassis.graph.domain.StructuralNodeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructuralGraphLoadPlannerService {

    public StructuralGraphLoadSet plan(ChassisCalculationInput input, StructuralGraph graph) {
        if (input == null) {
            throw new IllegalArgumentException("input is required");
        }
        if (graph == null) {
            throw new IllegalArgumentException("graph is required");
        }

        List<String> warnings = new ArrayList<String>();
        List<StructuralGraphLoad> loads = new ArrayList<StructuralGraphLoad>();

        List<StructuralLoadCase> cases = synthesizeLoadCases(input);
        int i = 0;
        for (StructuralLoadCase c : cases) {
            if (c == null) {
                continue;
            }
            Vector3 dir = normalizeLoadDirection(c);
            double mag = vectorMagnitude(c.getLongitudinalG(), c.getLateralG(), c.getVerticalG());
            loads.add(new StructuralGraphLoad(
                    "GL-BODY-" + i,
                    c.getName(),
                    mapCaseToGraphLoadType(c),
                    StructuralGraphLoadTargetType.WHOLE_GRAPH,
                    "FRAME",
                    dir,
                    mag,
                    c.getDurationSec(),
                    c.getOccurrenceWeight(),
                    c.isCertificationCritical(),
                    c.getName(),
                    c.getPrimaryLoadMode()
            ));
            i++;
        }

        appendThrustLoads(input, graph, loads, warnings);
        appendLandingReactionLoads(input, graph, loads);
        appendGroundSupportLoads(input, graph, loads);

        return new StructuralGraphLoadSet(loads, warnings);
    }

    public List<StructuralLoadCase> synthesizeLoadCases(ChassisCalculationInput input) {
        if (input == null) {
            return Collections.emptyList();
        }
        List<StructuralLoadCase> result = new ArrayList<StructuralLoadCase>();
        StructuralLoadCaseSet explicit = input.getLoadCaseSet();
        if (explicit != null && !explicit.isEmpty()) {
            result.addAll(explicit.getLoadCases());
        }
        result.addAll(buildCapabilityGeneratedLoadCases(input));
        return result;
    }

    private void appendThrustLoads(
            ChassisCalculationInput input,
            StructuralGraph graph,
            List<StructuralGraphLoad> loads,
            List<String> warnings) {
        ThrustLayout layout = input.getThrustLayout();
        if (layout != null && !layout.getThrustNodes().isEmpty()) {
            int i = 0;
            for (ThrustNode n : layout.getThrustNodes()) {
                if (n == null || n.getDirectionUnit() == null) {
                    continue;
                }
                double gEquivalent = n.getMaxThrustNewton() > 0.0d ? estimateThrustGEquivalent(n.getMaxThrustNewton(), input) : 0.25d;
                loads.add(new StructuralGraphLoad(
                        "GL-THR-" + i,
                        "Thrust application " + (n.getName() == null ? i : n.getName()),
                        StructuralGraphLoadType.THRUST_APPLICATION,
                        StructuralGraphLoadTargetType.NODE_SET_BY_TYPE,
                        StructuralNodeType.THRUST_LOAD_POINT.name(),
                        normalizeVector(n.getDirectionUnit()),
                        gEquivalent,
                        10.0d,
                        0.3d,
                        true,
                        null,
                        "thrust"
                ));
                i++;
            }
            return;
        }

        int profile = resolveEnginePlacementProfileClass(input);
        if (profile > 0) {
            loads.add(new StructuralGraphLoad(
                    "GL-THR-PROFILE",
                    "Synthetic thrust distribution from engine placement profile",
                    (profile == 11 || profile == 12 || profile == 13 || profile == 10 || profile == 8)
                            ? StructuralGraphLoadType.ENGINE_DIFFERENTIAL_CONTROL
                            : StructuralGraphLoadType.THRUST_APPLICATION,
                    StructuralGraphLoadTargetType.NODE_SET_BY_TYPE,
                    StructuralNodeType.THRUST_LOAD_POINT.name(),
                    new Vector3(1.0d, 0.0d, 0.0d),
                    0.35d,
                    10.0d,
                    0.2d,
                    true,
                    null,
                    "thrust"
            ));
        } else if (countNodesByType(graph, StructuralNodeType.THRUST_LOAD_POINT) == 0) {
            warnings.add("no thrustLayout and no enginePlacementProfileClass: no thrust application loads generated");
        }
    }

    private void appendLandingReactionLoads(ChassisCalculationInput input, StructuralGraph graph, List<StructuralGraphLoad> loads) {
        CapabilityClassSet caps = capabilityClassSet(input);
        int landing = caps == null ? 0 : caps.getPlanetLandingCapabilityClass();
        if (landing <= 0 || countNodesByType(graph, StructuralNodeType.LANDING_LOAD_POINT) == 0) {
            return;
        }

        double mag = (landing == 1) ? 0.65d : (landing >= 2 && landing <= 4) ? (0.55d + 0.10d * (landing - 2)) : (landing == 6 ? 0.80d : 0.60d);
        loads.add(new StructuralGraphLoad(
                "GL-LAND-REACTION",
                "Landing reaction envelope",
                StructuralGraphLoadType.LANDING_REACTION,
                StructuralGraphLoadTargetType.NODE_SET_BY_TYPE,
                StructuralNodeType.LANDING_LOAD_POINT.name(),
                new Vector3(0.0d, 0.0d, 1.0d),
                mag,
                2.0d,
                0.25d,
                true,
                "Capability-generated landing support reaction",
                "compression"
        ));
    }

    private void appendGroundSupportLoads(ChassisCalculationInput input, StructuralGraph graph, List<StructuralGraphLoad> loads) {
        CapabilityClassSet caps = capabilityClassSet(input);
        int surface = caps == null ? 0 : caps.getGroundSupportSurfaceClass();
        if (surface <= 0 || countNodesByType(graph, StructuralNodeType.LANDING_LOAD_POINT) == 0) {
            return;
        }

        double mag = 0.12d + Math.min(0.25d, 0.05d * surface);
        loads.add(new StructuralGraphLoad(
                "GL-GROUND-SUPPORT",
                "Ground support uneven reaction",
                StructuralGraphLoadType.GROUND_SUPPORT_REACTION,
                StructuralGraphLoadTargetType.NODE_SET_BY_TYPE,
                StructuralNodeType.LANDING_LOAD_POINT.name(),
                new Vector3(0.0d, 0.15d, 1.0d),
                mag,
                60.0d,
                0.8d,
                false,
                "Capability-generated ground support load",
                "compression"
        ));
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

    private void appendEnginePlacementGeneratedLoadCases(ChassisCalculationInput input, List<StructuralLoadCase> generated) {
        int profile = resolveEnginePlacementProfileClass(input);
        if (profile <= 0) {
            return;
        }
        if (profile == 3 || profile == 4) {
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

    private StructuralGraphLoadType mapCaseToGraphLoadType(StructuralLoadCase c) {
        if (c == null || c.getName() == null) {
            return StructuralGraphLoadType.UNKNOWN;
        }
        String n = c.getName().toLowerCase(java.util.Locale.US);
        if (n.contains("landing") || n.contains("water landing")) {
            return StructuralGraphLoadType.LANDING_REACTION;
        }
        if (n.contains("ground support")) {
            return StructuralGraphLoadType.GROUND_SUPPORT_REACTION;
        }
        if (n.contains("differential-control")) {
            return StructuralGraphLoadType.ENGINE_DIFFERENTIAL_CONTROL;
        }
        if (n.contains("reserve certification")) {
            return StructuralGraphLoadType.CERTIFICATION_RESERVE;
        }
        return StructuralGraphLoadType.BODY_INERTIAL;
    }

    private Vector3 normalizeLoadDirection(StructuralLoadCase c) {
        double x = c.getLongitudinalG();
        double y = c.getLateralG();
        double z = c.getVerticalG();
        double m = Math.sqrt(x * x + y * y + z * z);
        if (m <= 1e-9d) {
            return new Vector3(1.0d, 0.0d, 0.0d);
        }
        return new Vector3(x / m, y / m, z / m);
    }

    private double vectorMagnitude(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }

    private Vector3 normalizeVector(Vector3 v) {
        if (v == null) {
            return new Vector3(1.0d, 0.0d, 0.0d);
        }
        double m = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
        if (m <= 1e-9d) {
            return new Vector3(1.0d, 0.0d, 0.0d);
        }
        return new Vector3(v.getX() / m, v.getY() / m, v.getZ() / m);
    }

    private double estimateThrustGEquivalent(double thrustNewton, ChassisCalculationInput input) {
        double referenceMassKg = Math.max(10_000.0d, input.getSizeTotal() * 500.0d);
        return Math.min(5.0d, Math.max(0.05d, thrustNewton / (referenceMassKg * 9.80665d)));
    }

    private CapabilityClassSet capabilityClassSet(ChassisCalculationInput input) {
        StructuralDesignBasis basis = input == null ? null : input.getStructuralDesignBasis();
        return basis == null ? null : basis.getCapabilityClassSet();
    }

    private int resolveEnginePlacementProfileClass(ChassisCalculationInput input) {
        StructuralDesignBasis basis = input == null ? null : input.getStructuralDesignBasis();
        return basis == null ? 0 : basis.getEnginePlacementProfileClass();
    }

    private int countNodesByType(StructuralGraph graph, StructuralNodeType type) {
        int count = 0;
        for (org.example.shipconstructor.chassis.graph.domain.StructuralGraphNode n : graph.getNodes()) {
            if (n != null && n.getType() == type) {
                count++;
            }
        }
        return count;
    }
}
