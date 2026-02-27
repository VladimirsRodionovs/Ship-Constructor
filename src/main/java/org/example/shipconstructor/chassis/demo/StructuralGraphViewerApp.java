package org.example.shipconstructor.chassis.demo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphBuildResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoad;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMember;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphMemberEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphNode;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanel;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphPanelEnvelope;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphSolveResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralMemberRole;
import org.example.shipconstructor.chassis.graph.domain.StructuralNodeType;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphAxialEnvelopeSolverService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphBuilderService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphLoadPlannerService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphSummaryFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StructuralGraphViewerApp extends Application {
    private final StructuralGraphBuilderService builder = new StructuralGraphBuilderService();
    private final StructuralGraphLoadPlannerService loadPlanner = new StructuralGraphLoadPlannerService();
    private final StructuralGraphAxialEnvelopeSolverService solver = new StructuralGraphAxialEnvelopeSolverService();

    private ComboBox<String> scenarioCombo;
    private CheckBox stressColorCheck;
    private CheckBox panelOverlayCheck;
    private CheckBox loadOverlayCheck;
    private CheckBox nodeLabelsCheck;
    private TextArea summaryArea;

    private Canvas topCanvas;
    private Canvas sideCanvas;
    private Canvas frontCanvas;

    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chassis StructuralGraph Viewer (Demo)");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(buildControls());
        root.setCenter(buildCanvases());
        root.setRight(buildSummary());

        refreshView();

        stage.setScene(new Scene(root, 1600, 960));
        stage.show();
    }

    private HBox buildControls() {
        scenarioCombo = new ComboBox<String>(FXCollections.observableArrayList(
                "Space Tug Push/Pull",
                "Utility Lander"
        ));
        scenarioCombo.setValue("Space Tug Push/Pull");
        scenarioCombo.setOnAction(e -> refreshView());

        stressColorCheck = new CheckBox("Stress Colors");
        stressColorCheck.setSelected(true);
        stressColorCheck.setOnAction(e -> refreshView());

        panelOverlayCheck = new CheckBox("Panels");
        panelOverlayCheck.setSelected(true);
        panelOverlayCheck.setOnAction(e -> refreshView());

        loadOverlayCheck = new CheckBox("Loads");
        loadOverlayCheck.setSelected(true);
        loadOverlayCheck.setOnAction(e -> refreshView());

        nodeLabelsCheck = new CheckBox("Node Labels");
        nodeLabelsCheck.setSelected(false);
        nodeLabelsCheck.setOnAction(e -> refreshView());

        HBox box = new HBox(10,
                new Label("Scenario"), scenarioCombo,
                stressColorCheck,
                panelOverlayCheck,
                loadOverlayCheck,
                nodeLabelsCheck);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 0, 10, 0));
        return box;
    }

    private GridPane buildCanvases() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        topCanvas = new Canvas(520, 420);
        sideCanvas = new Canvas(520, 420);
        frontCanvas = new Canvas(520, 420);

        grid.add(wrapCanvas("Top (X-Y)", topCanvas), 0, 0);
        grid.add(wrapCanvas("Side (X-Z)", sideCanvas), 1, 0);
        grid.add(wrapCanvas("Front (Y-Z)", frontCanvas), 0, 1);
        GridPane.setHgrow(grid, Priority.ALWAYS);
        GridPane.setVgrow(grid, Priority.ALWAYS);
        return grid;
    }

    private VBox wrapCanvas(String title, Canvas canvas) {
        Label label = new Label(title);
        VBox box = new VBox(4, label, canvas);
        VBox.setVgrow(canvas, Priority.ALWAYS);
        box.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 6;");
        return box;
    }

    private VBox buildSummary() {
        summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setWrapText(false);
        summaryArea.setPrefWidth(520);
        VBox box = new VBox(new Label("Summary"), summaryArea);
        box.setPadding(new Insets(0, 0, 0, 10));
        VBox.setVgrow(summaryArea, Priority.ALWAYS);
        return box;
    }

    private void refreshView() {
        ChassisCalculationInput input = "Utility Lander".equals(scenarioCombo.getValue())
                ? buildLanderScenario()
                : buildSpaceTugScenario();

        StructuralGraphBuildResult graphResult = builder.build(input);
        StructuralGraph graph = graphResult.getGraph();
        StructuralGraphLoadSet loadSet = loadPlanner.plan(input, graph);
        StructuralGraphSolveResult solveResult = solver.solve(input, graph, loadSet);

        Map<String, StructuralGraphMemberEnvelope> memberEnv = byMemberId(solveResult.getMemberEnvelopes());
        Map<String, StructuralGraphPanelEnvelope> panelEnv = byPanelId(solveResult.getPanelEnvelopes());

        drawProjection(topCanvas, graph, loadSet, memberEnv, panelEnv, Projection.TOP_XY);
        drawProjection(sideCanvas, graph, loadSet, memberEnv, panelEnv, Projection.SIDE_XZ);
        drawProjection(frontCanvas, graph, loadSet, memberEnv, panelEnv, Projection.FRONT_YZ);

        StringBuilder sb = new StringBuilder();
        sb.append(StructuralGraphSummaryFormatter.summarize(graph, loadSet, solveResult));
        if (!graphResult.getWarnings().isEmpty()) {
            sb.append("\nGraph warnings:\n");
            for (String w : graphResult.getWarnings()) {
                sb.append("- ").append(w).append('\n');
            }
        }
        if (!loadSet.getWarnings().isEmpty()) {
            sb.append("\nLoad warnings:\n");
            for (String w : loadSet.getWarnings()) {
                sb.append("- ").append(w).append('\n');
            }
        }
        if (!solveResult.getWarnings().isEmpty()) {
            sb.append("\nSolver warnings:\n");
            for (String w : solveResult.getWarnings()) {
                sb.append("- ").append(w).append('\n');
            }
        }
        summaryArea.setText(sb.toString());
    }

    private void drawProjection(
            Canvas canvas,
            StructuralGraph graph,
            StructuralGraphLoadSet loadSet,
            Map<String, StructuralGraphMemberEnvelope> memberEnv,
            Map<String, StructuralGraphPanelEnvelope> panelEnv,
            Projection projection) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.setFill(Color.rgb(20, 22, 26));
        g.fillRect(0, 0, w, h);

        RectWorld world = projection.world(graph);
        double pad = 28.0d;
        double sx = (w - 2 * pad) / Math.max(1e-6d, world.maxX - world.minX);
        double sy = (h - 2 * pad) / Math.max(1e-6d, world.maxY - world.minY);
        double scale = Math.min(sx, sy);

        g.setStroke(Color.rgb(70, 75, 90));
        g.setLineWidth(1.0d);
        drawGrid(g, world, projection, graph, w, h, pad, scale);

        Map<String, StructuralGraphNode> nodeById = new HashMap<String, StructuralGraphNode>();
        for (StructuralGraphNode n : graph.getNodes()) {
            nodeById.put(n.getId(), n);
        }

        if (panelOverlayCheck.isSelected()) {
            for (StructuralGraphPanel p : graph.getPanels()) {
                drawPanel(g, p, nodeById, panelEnv.get(p.getId()), projection, world, w, h, pad, scale);
            }
        }

        for (StructuralGraphMember m : graph.getMembers()) {
            StructuralGraphNode a = nodeById.get(m.getNodeAId());
            StructuralGraphNode b = nodeById.get(m.getNodeBId());
            if (a == null || b == null) {
                continue;
            }
            drawMember(g, m, a, b, memberEnv.get(m.getId()), projection, world, w, h, pad, scale);
        }

        if (loadOverlayCheck.isSelected()) {
            drawLoads(g, graph, loadSet, projection, world, w, h, pad, scale);
        }

        for (StructuralGraphNode n : graph.getNodes()) {
            drawNode(g, n, projection, world, w, h, pad, scale);
        }
    }

    private void drawGrid(GraphicsContext g, RectWorld world, Projection projection, StructuralGraph graph, double w, double h, double pad, double scale) {
        Vector2 a = project(projection, new Vector3(0, world.minX, world.minY), world, w, h, pad, scale, true);
        // just crosshair axes for readability using actual graph center
        Vector2 cx1 = projectPoint(projection, new Vector3(-0.5d * graph.getLengthMeters(), 0, 0), world, w, h, pad, scale);
        Vector2 cx2 = projectPoint(projection, new Vector3(0.5d * graph.getLengthMeters(), 0, 0), world, w, h, pad, scale);
        g.strokeLine(cx1.x, cx1.y, cx2.x, cx2.y);
        if (projection != Projection.FRONT_YZ) {
            Vector2 cz1 = projectPoint(projection, new Vector3(0, 0, -0.5d * graph.getHeightMeters()), world, w, h, pad, scale);
            Vector2 cz2 = projectPoint(projection, new Vector3(0, 0, 0.5d * graph.getHeightMeters()), world, w, h, pad, scale);
            g.strokeLine(cz1.x, cz1.y, cz2.x, cz2.y);
        }
        if (projection != Projection.SIDE_XZ) {
            Vector2 cy1 = projectPoint(projection, new Vector3(0, -0.5d * graph.getWidthMeters(), 0), world, w, h, pad, scale);
            Vector2 cy2 = projectPoint(projection, new Vector3(0, 0.5d * graph.getWidthMeters(), 0), world, w, h, pad, scale);
            g.strokeLine(cy1.x, cy1.y, cy2.x, cy2.y);
        }
    }

    private void drawPanel(GraphicsContext g, StructuralGraphPanel panel, Map<String, StructuralGraphNode> nodeById,
                           StructuralGraphPanelEnvelope env, Projection projection, RectWorld world, double w, double h, double pad, double scale) {
        List<Double> xs = new ArrayList<Double>();
        List<Double> ys = new ArrayList<Double>();
        for (String id : panel.getBoundaryNodeIds()) {
            StructuralGraphNode n = nodeById.get(id);
            if (n == null) {
                continue;
            }
            Vector2 p = projectPoint(projection, n.getPositionMeters(), world, w, h, pad, scale);
            xs.add(Double.valueOf(p.x));
            ys.add(Double.valueOf(p.y));
        }
        if (xs.size() < 3) {
            return;
        }
        double[] x = toArray(xs);
        double[] y = toArray(ys);

        Color fill;
        if (stressColorCheck.isSelected() && env != null) {
            fill = heat(env.getPeakCombinedAbsIndex(), 0.16d, 0.25d).deriveColor(0, 1, 1, 0.22);
        } else {
            fill = env != null && env.getRole() != null && env.getRole().name().contains("PRIMARY")
                    ? Color.rgb(100, 180, 220, 0.18)
                    : Color.rgb(90, 100, 120, 0.10);
        }
        g.setFill(fill);
        g.fillPolygon(x, y, x.length);
        g.setStroke(Color.rgb(90, 110, 140, 0.35));
        g.setLineWidth(1.0d);
        g.strokePolygon(x, y, x.length);
    }

    private void drawMember(GraphicsContext g, StructuralGraphMember member, StructuralGraphNode a, StructuralGraphNode b,
                            StructuralGraphMemberEnvelope env, Projection projection, RectWorld world, double w, double h, double pad, double scale) {
        Vector2 pa = projectPoint(projection, a.getPositionMeters(), world, w, h, pad, scale);
        Vector2 pb = projectPoint(projection, b.getPositionMeters(), world, w, h, pad, scale);

        Color color;
        double lineWidth;
        if (stressColorCheck.isSelected() && env != null) {
            color = heat(env.getPeakCombinedAbsIndex(), 0.35d, 0.55d);
            lineWidth = member.getRole() == StructuralMemberRole.LOCAL_LOAD_TRANSFER ? 2.6d : 2.0d;
        } else {
            if (member.getRole() == StructuralMemberRole.PRIMARY_LOAD_PATH) {
                color = Color.rgb(230, 235, 245);
                lineWidth = 2.0d;
            } else if (member.getRole() == StructuralMemberRole.SECONDARY_STIFFENING) {
                color = Color.rgb(140, 170, 210);
                lineWidth = 1.5d;
            } else {
                color = Color.rgb(255, 180, 90);
                lineWidth = 2.2d;
            }
        }

        g.setStroke(color);
        g.setLineWidth(lineWidth);
        g.strokeLine(pa.x, pa.y, pb.x, pb.y);
    }

    private void drawNode(GraphicsContext g, StructuralGraphNode n, Projection projection, RectWorld world, double w, double h, double pad, double scale) {
        Vector2 p = projectPoint(projection, n.getPositionMeters(), world, w, h, pad, scale);
        Color c = nodeColor(n.getType());
        double r = nodeRadius(n.getType());
        g.setFill(c);
        g.fillOval(p.x - r, p.y - r, 2 * r, 2 * r);
        if (nodeLabelsCheck.isSelected()) {
            g.setFill(Color.rgb(220, 220, 220));
            g.fillText(n.getId(), p.x + 4, p.y - 4);
        }
    }

    private void drawLoads(GraphicsContext g, StructuralGraph graph, StructuralGraphLoadSet loadSet,
                           Projection projection, RectWorld world, double w, double h, double pad, double scale) {
        for (StructuralGraphLoad load : loadSet.getLoads()) {
            if (load.getDirectionUnit() == null) {
                continue;
            }
            List<Vector3> anchors = resolveLoadAnchors(graph, load);
            for (Vector3 anchor : anchors) {
                drawArrow(g, anchor, load.getDirectionUnit(), load.getMagnitudeG(), projection, world, w, h, pad, scale, loadColor(load));
            }
        }
    }

    private List<Vector3> resolveLoadAnchors(StructuralGraph graph, StructuralGraphLoad load) {
        List<Vector3> anchors = new ArrayList<Vector3>();
        if (load == null) {
            return anchors;
        }
        if (load.getTargetType() == null || load.getTargetType().name().equals("WHOLE_GRAPH")) {
            anchors.add(new Vector3(0.0d, 0.0d, 0.0d));
            return anchors;
        }
        if ("NODE_SET_BY_TYPE".equals(load.getTargetType().name())) {
            try {
                StructuralNodeType type = StructuralNodeType.valueOf(load.getTargetRef());
                for (StructuralGraphNode n : graph.getNodes()) {
                    if (n.getType() == type) {
                        anchors.add(n.getPositionMeters());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        if (anchors.isEmpty()) {
            anchors.add(new Vector3(0.0d, 0.0d, 0.0d));
        }
        return anchors.size() > 16 ? anchors.subList(0, 16) : anchors;
    }

    private void drawArrow(GraphicsContext g, Vector3 origin, Vector3 dir, double magnitudeG, Projection projection,
                           RectWorld world, double w, double h, double pad, double scale, Color color) {
        Vector3 n = normalize(dir);
        double lenWorld = Math.min(Math.max(0.5d, magnitudeG * 1.2d), 8.0d);
        Vector3 tip = new Vector3(
                origin.getX() + n.getX() * lenWorld,
                origin.getY() + n.getY() * lenWorld,
                origin.getZ() + n.getZ() * lenWorld
        );
        Vector2 p0 = projectPoint(projection, origin, world, w, h, pad, scale);
        Vector2 p1 = projectPoint(projection, tip, world, w, h, pad, scale);
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(1.2d);
        g.strokeLine(p0.x, p0.y, p1.x, p1.y);
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        double m = Math.sqrt(dx * dx + dy * dy);
        if (m < 1.0d) {
            return;
        }
        double ux = dx / m;
        double uy = dy / m;
        double ah = 7.0d;
        double aw = 4.0d;
        double bx = p1.x - ux * ah;
        double by = p1.y - uy * ah;
        double px = -uy;
        double py = ux;
        g.fillPolygon(
                new double[]{p1.x, bx + px * aw, bx - px * aw},
                new double[]{p1.y, by + py * aw, by - py * aw},
                3
        );
    }

    private Color nodeColor(StructuralNodeType type) {
        if (type == null) {
            return Color.GRAY;
        }
        switch (type) {
            case ENGINE_MOUNT:
            case THRUST_LOAD_POINT:
                return Color.ORANGE;
            case LANDING_LOAD_POINT:
                return Color.LIMEGREEN;
            case MASS_ANCHOR:
                return Color.GOLD;
            case FRAME_MIDPOINT:
                return Color.SKYBLUE;
            default:
                return Color.WHITE;
        }
    }

    private double nodeRadius(StructuralNodeType type) {
        if (type == StructuralNodeType.ENGINE_MOUNT || type == StructuralNodeType.THRUST_LOAD_POINT) {
            return 3.5d;
        }
        if (type == StructuralNodeType.LANDING_LOAD_POINT || type == StructuralNodeType.MASS_ANCHOR) {
            return 3.2d;
        }
        return 2.4d;
    }

    private Color loadColor(StructuralGraphLoad load) {
        if (load == null || load.getType() == null) {
            return Color.rgb(220, 220, 220, 0.65);
        }
        switch (load.getType()) {
            case THRUST_APPLICATION:
                return Color.rgb(255, 170, 70, 0.85);
            case LANDING_REACTION:
                return Color.rgb(90, 230, 120, 0.85);
            case GROUND_SUPPORT_REACTION:
                return Color.rgb(70, 200, 170, 0.80);
            case ENGINE_DIFFERENTIAL_CONTROL:
                return Color.rgb(255, 110, 150, 0.85);
            default:
                return Color.rgb(210, 220, 255, 0.65);
        }
    }

    private Color heat(double value, double yellowRef, double redRef) {
        double v = Math.max(0.0d, value);
        if (v <= yellowRef) {
            double t = yellowRef <= 1e-9d ? 1.0d : v / yellowRef;
            return lerp(Color.rgb(110, 170, 255), Color.rgb(255, 220, 80), t);
        }
        double t = redRef <= yellowRef ? 1.0d : (v - yellowRef) / (redRef - yellowRef);
        return lerp(Color.rgb(255, 220, 80), Color.rgb(255, 80, 80), Math.min(1.0d, t));
    }

    private Color lerp(Color a, Color b, double t) {
        double clamped = Math.max(0.0d, Math.min(1.0d, t));
        return new Color(
                a.getRed() + (b.getRed() - a.getRed()) * clamped,
                a.getGreen() + (b.getGreen() - a.getGreen()) * clamped,
                a.getBlue() + (b.getBlue() - a.getBlue()) * clamped,
                1.0d
        );
    }

    private Map<String, StructuralGraphMemberEnvelope> byMemberId(List<StructuralGraphMemberEnvelope> list) {
        Map<String, StructuralGraphMemberEnvelope> map = new HashMap<String, StructuralGraphMemberEnvelope>();
        for (StructuralGraphMemberEnvelope e : list) {
            map.put(e.getMemberId(), e);
        }
        return map;
    }

    private Map<String, StructuralGraphPanelEnvelope> byPanelId(List<StructuralGraphPanelEnvelope> list) {
        Map<String, StructuralGraphPanelEnvelope> map = new HashMap<String, StructuralGraphPanelEnvelope>();
        for (StructuralGraphPanelEnvelope e : list) {
            map.put(e.getPanelId(), e);
        }
        return map;
    }

    private Vector2 projectPoint(Projection p, Vector3 v, RectWorld world, double canvasW, double canvasH, double pad, double scale) {
        double x;
        double y;
        switch (p) {
            case TOP_XY:
                x = v.getX();
                y = v.getY();
                break;
            case SIDE_XZ:
                x = v.getX();
                y = v.getZ();
                break;
            case FRONT_YZ:
                x = v.getY();
                y = v.getZ();
                break;
            default:
                x = v.getX();
                y = v.getY();
        }
        double px = pad + (x - world.minX) * scale;
        double py = canvasH - pad - (y - world.minY) * scale;
        return new Vector2(px, py);
    }

    private Vector2 project(Projection p, Vector3 v, RectWorld world, double canvasW, double canvasH, double pad, double scale, boolean unused) {
        return projectPoint(p, v, world, canvasW, canvasH, pad, scale);
    }

    private double[] toArray(List<Double> values) {
        double[] arr = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            arr[i] = values.get(i).doubleValue();
        }
        return arr;
    }

    private Vector3 normalize(Vector3 v) {
        double m = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
        if (m <= 1e-9d) {
            return new Vector3(1.0d, 0.0d, 0.0d);
        }
        return new Vector3(v.getX() / m, v.getY() / m, v.getZ() / m);
    }

    private ChassisCalculationInput buildSpaceTugScenario() {
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

    private ChassisCalculationInput buildLanderScenario() {
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

    private enum Projection {
        TOP_XY, SIDE_XZ, FRONT_YZ;

        RectWorld world(StructuralGraph graph) {
            switch (this) {
                case TOP_XY:
                    return new RectWorld(-0.5d * graph.getLengthMeters(), 0.5d * graph.getLengthMeters(),
                            -0.5d * graph.getWidthMeters(), 0.5d * graph.getWidthMeters());
                case SIDE_XZ:
                    return new RectWorld(-0.5d * graph.getLengthMeters(), 0.5d * graph.getLengthMeters(),
                            -0.5d * graph.getHeightMeters(), 0.5d * graph.getHeightMeters());
                case FRONT_YZ:
                    return new RectWorld(-0.5d * graph.getWidthMeters(), 0.5d * graph.getWidthMeters(),
                            -0.5d * graph.getHeightMeters(), 0.5d * graph.getHeightMeters());
                default:
                    return new RectWorld(-1, 1, -1, 1);
            }
        }
    }

    private static final class RectWorld {
        private final double minX;
        private final double maxX;
        private final double minY;
        private final double maxY;

        private RectWorld(double minX, double maxX, double minY, double maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    private static final class Vector2 {
        private final double x;
        private final double y;

        private Vector2(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
