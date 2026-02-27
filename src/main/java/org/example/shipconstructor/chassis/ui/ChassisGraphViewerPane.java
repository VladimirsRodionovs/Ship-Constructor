package org.example.shipconstructor.chassis.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraph;
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
import org.example.shipconstructor.chassis.graph.service.StructuralGraphSummaryFormatter;
import org.example.shipconstructor.chassis.domain.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChassisGraphViewerPane extends BorderPane {
    private final CheckBox stressColorCheck = new CheckBox("Stress Colors");
    private final CheckBox panelOverlayCheck = new CheckBox("Panels");
    private final CheckBox loadOverlayCheck = new CheckBox("Loads");
    private final CheckBox nodeLabelsCheck = new CheckBox("Node Labels");
    private final TextArea summaryArea = new TextArea();

    private final Canvas topCanvas = new Canvas(480, 360);
    private final Canvas sideCanvas = new Canvas(480, 360);
    private final Canvas frontCanvas = new Canvas(480, 360);
    private final Canvas wire3dCanvas = new Canvas(480, 360);

    private StructuralGraph graph;
    private StructuralGraphLoadSet loadSet;
    private StructuralGraphSolveResult solveResult;
    private String extraWarningsText;
    private double viewYawRad = Math.toRadians(35.0d);
    private double viewPitchRad = Math.toRadians(-18.0d);
    private double viewZoom = 1.0d;
    private double dragLastX;
    private double dragLastY;

    public ChassisGraphViewerPane() {
        setPadding(new Insets(8));
        setTop(buildControls());
        setCenter(buildCanvases());
        setRight(buildSummary());
        stressColorCheck.setSelected(true);
        panelOverlayCheck.setSelected(true);
        loadOverlayCheck.setSelected(true);
        nodeLabelsCheck.setSelected(false);
        install3dInteraction();
        redraw();
    }

    private HBox buildControls() {
        stressColorCheck.setOnAction(e -> redraw());
        panelOverlayCheck.setOnAction(e -> redraw());
        loadOverlayCheck.setOnAction(e -> redraw());
        nodeLabelsCheck.setOnAction(e -> redraw());
        HBox box = new HBox(10, stressColorCheck, panelOverlayCheck, loadOverlayCheck, nodeLabelsCheck);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 0, 8, 0));
        return box;
    }

    private GridPane buildCanvases() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(wrapCanvas("Top (X-Y)", topCanvas), 0, 0);
        grid.add(wrapCanvas("Side (X-Z)", sideCanvas), 1, 0);
        grid.add(wrapCanvas("Front (Y-Z)", frontCanvas), 0, 1);
        grid.add(wrapCanvas("3D Wireframe (drag rotate, wheel zoom)", wire3dCanvas), 1, 1);
        return grid;
    }

    private VBox wrapCanvas(String title, Canvas canvas) {
        Label label = new Label(title);
        VBox box = new VBox(4, label, canvas);
        box.setStyle("-fx-border-color: #444; -fx-border-width: 1; -fx-padding: 6;");
        return box;
    }

    private VBox buildSummary() {
        summaryArea.setEditable(false);
        summaryArea.setWrapText(false);
        summaryArea.setPrefWidth(520);
        VBox box = new VBox(new Label("Summary"), summaryArea);
        box.setPadding(new Insets(0, 0, 0, 10));
        VBox.setVgrow(summaryArea, Priority.ALWAYS);
        return box;
    }

    public void setData(StructuralGraph graph, StructuralGraphLoadSet loadSet, StructuralGraphSolveResult solveResult, String extraWarningsText) {
        this.graph = graph;
        this.loadSet = loadSet;
        this.solveResult = solveResult;
        this.extraWarningsText = extraWarningsText;
        redraw();
    }

    public void clear(String message) {
        this.graph = null;
        this.loadSet = null;
        this.solveResult = null;
        this.extraWarningsText = message;
        redraw();
    }

    private void install3dInteraction() {
        wire3dCanvas.setOnMousePressed(e -> {
            dragLastX = e.getX();
            dragLastY = e.getY();
        });
        wire3dCanvas.setOnMouseDragged(e -> {
            double dx = e.getX() - dragLastX;
            double dy = e.getY() - dragLastY;
            dragLastX = e.getX();
            dragLastY = e.getY();
            viewYawRad += dx * 0.0125d;
            viewPitchRad = clamp(viewPitchRad - dy * 0.0100d, Math.toRadians(-85.0d), Math.toRadians(85.0d));
            redraw();
        });
        wire3dCanvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            double factor = e.getDeltaY() > 0 ? 1.08d : 0.92d;
            viewZoom = clamp(viewZoom * factor, 0.35d, 4.0d);
            redraw();
            e.consume();
        });
    }

    private void redraw() {
        if (graph == null || loadSet == null || solveResult == null) {
            clearCanvas(topCanvas, "No chassis graph");
            clearCanvas(sideCanvas, "Build graph from form");
            clearCanvas(frontCanvas, "Type=0 recommended");
            clearCanvas(wire3dCanvas, "3D view");
            summaryArea.setText(extraWarningsText == null ? "No graph built" : extraWarningsText);
            return;
        }

        Map<String, StructuralGraphMemberEnvelope> memberEnv = new HashMap<String, StructuralGraphMemberEnvelope>();
        for (StructuralGraphMemberEnvelope e : solveResult.getMemberEnvelopes()) {
            memberEnv.put(e.getMemberId(), e);
        }
        Map<String, StructuralGraphPanelEnvelope> panelEnv = new HashMap<String, StructuralGraphPanelEnvelope>();
        for (StructuralGraphPanelEnvelope e : solveResult.getPanelEnvelopes()) {
            panelEnv.put(e.getPanelId(), e);
        }

        drawProjection(topCanvas, graph, loadSet, memberEnv, panelEnv, Projection.TOP_XY);
        drawProjection(sideCanvas, graph, loadSet, memberEnv, panelEnv, Projection.SIDE_XZ);
        drawProjection(frontCanvas, graph, loadSet, memberEnv, panelEnv, Projection.FRONT_YZ);
        draw3dWireframe(wire3dCanvas, graph, loadSet, memberEnv, panelEnv);

        StringBuilder sb = new StringBuilder();
        sb.append(StructuralGraphSummaryFormatter.summarize(graph, loadSet, solveResult));
        if (extraWarningsText != null && !extraWarningsText.trim().isEmpty()) {
            sb.append("\n").append(extraWarningsText);
        }
        summaryArea.setText(sb.toString());
    }

    private void clearCanvas(Canvas canvas, String text) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        g.setFill(Color.rgb(20, 22, 26));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.rgb(180, 185, 195));
        g.fillText(text, 20, 30);
    }

    private void drawProjection(Canvas canvas, StructuralGraph graph, StructuralGraphLoadSet loadSet,
                                Map<String, StructuralGraphMemberEnvelope> memberEnv,
                                Map<String, StructuralGraphPanelEnvelope> panelEnv,
                                Projection projection) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.setFill(Color.rgb(20, 22, 26));
        g.fillRect(0, 0, w, h);

        RectWorld world = projection.world(graph);
        double pad = 22.0d;
        double sx = (w - 2 * pad) / Math.max(1e-6d, world.maxX - world.minX);
        double sy = (h - 2 * pad) / Math.max(1e-6d, world.maxY - world.minY);
        double scale = Math.min(sx, sy);

        g.setStroke(Color.rgb(70, 75, 90));
        g.setLineWidth(1.0d);
        drawAxes(g, graph, projection, world, w, h, pad, scale);

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
            if (a == null || b == null) { continue; }
            drawMember(g, m, a, b, memberEnv.get(m.getId()), projection, world, w, h, pad, scale);
        }

        if (loadOverlayCheck.isSelected()) {
            drawLoads(g, graph, loadSet, projection, world, w, h, pad, scale);
        }

        for (StructuralGraphNode n : graph.getNodes()) {
            drawNode(g, n, projection, world, w, h, pad, scale);
        }
    }

    private void drawAxes(GraphicsContext g, StructuralGraph graph, Projection projection, RectWorld world,
                          double w, double h, double pad, double scale) {
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
                           StructuralGraphPanelEnvelope env, Projection projection, RectWorld world,
                           double w, double h, double pad, double scale) {
        List<Double> xs = new ArrayList<Double>();
        List<Double> ys = new ArrayList<Double>();
        for (String id : panel.getBoundaryNodeIds()) {
            StructuralGraphNode n = nodeById.get(id);
            if (n == null) { continue; }
            Vector2 p = projectPoint(projection, n.getPositionMeters(), world, w, h, pad, scale);
            xs.add(Double.valueOf(p.x));
            ys.add(Double.valueOf(p.y));
        }
        if (xs.size() < 3) { return; }
        double[] xa = toArray(xs);
        double[] ya = toArray(ys);
        Color fill;
        if (stressColorCheck.isSelected() && env != null) {
            fill = heat(env.getPeakCombinedAbsIndex(), 0.12d, 0.24d).deriveColor(0, 1, 1, 0.22);
        } else {
            fill = Color.rgb(90, 105, 125, env != null && env.getRole().name().contains("PRIMARY") ? 0.20 : 0.10);
        }
        g.setFill(fill);
        g.fillPolygon(xa, ya, xa.length);
        g.setStroke(Color.rgb(100, 120, 145, 0.35));
        g.setLineWidth(1.0d);
        g.strokePolygon(xa, ya, xa.length);
    }

    private void drawMember(GraphicsContext g, StructuralGraphMember member, StructuralGraphNode a, StructuralGraphNode b,
                            StructuralGraphMemberEnvelope env, Projection projection, RectWorld world,
                            double w, double h, double pad, double scale) {
        Vector2 pa = projectPoint(projection, a.getPositionMeters(), world, w, h, pad, scale);
        Vector2 pb = projectPoint(projection, b.getPositionMeters(), world, w, h, pad, scale);
        Color color;
        double lineWidth;
        if (stressColorCheck.isSelected() && env != null) {
            color = heat(env.getPeakCombinedAbsIndex(), 0.30d, 0.50d);
            lineWidth = member.getRole() == StructuralMemberRole.LOCAL_LOAD_TRANSFER ? 2.4d : 1.9d;
        } else {
            if (member.getRole() == StructuralMemberRole.PRIMARY_LOAD_PATH) {
                color = Color.rgb(230, 235, 245);
                lineWidth = 1.9d;
            } else if (member.getRole() == StructuralMemberRole.SECONDARY_STIFFENING) {
                color = Color.rgb(145, 175, 215);
                lineWidth = 1.4d;
            } else {
                color = Color.rgb(255, 180, 90);
                lineWidth = 2.2d;
            }
        }
        g.setStroke(color);
        g.setLineWidth(lineWidth);
        g.strokeLine(pa.x, pa.y, pb.x, pb.y);
    }

    private void drawNode(GraphicsContext g, StructuralGraphNode n, Projection projection, RectWorld world,
                          double w, double h, double pad, double scale) {
        Vector2 p = projectPoint(projection, n.getPositionMeters(), world, w, h, pad, scale);
        g.setFill(nodeColor(n.getType()));
        double r = nodeRadius(n.getType());
        g.fillOval(p.x - r, p.y - r, 2 * r, 2 * r);
        if (nodeLabelsCheck.isSelected()) {
            g.setFill(Color.rgb(220, 220, 220));
            g.fillText(n.getId(), p.x + 3, p.y - 3);
        }
    }

    private void drawLoads(GraphicsContext g, StructuralGraph graph, StructuralGraphLoadSet loadSet, Projection projection,
                           RectWorld world, double w, double h, double pad, double scale) {
        for (StructuralGraphLoad load : loadSet.getLoads()) {
            if (load.getDirectionUnit() == null) { continue; }
            for (Vector3 anchor : resolveLoadAnchors(graph, load)) {
                drawArrow(g, anchor, load.getDirectionUnit(), load.getMagnitudeG(), projection, world, w, h, pad, scale, loadColor(load));
            }
        }
    }

    private void draw3dWireframe(Canvas canvas, StructuralGraph graph, StructuralGraphLoadSet loadSet,
                                 Map<String, StructuralGraphMemberEnvelope> memberEnv,
                                 Map<String, StructuralGraphPanelEnvelope> panelEnv) {
        GraphicsContext g = canvas.getGraphicsContext2D();
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        g.setFill(Color.rgb(20, 22, 26));
        g.fillRect(0, 0, w, h);

        Map<String, StructuralGraphNode> nodeById = new HashMap<String, StructuralGraphNode>();
        for (StructuralGraphNode n : graph.getNodes()) {
            nodeById.put(n.getId(), n);
        }

        double halfSpan = 0.5d * Math.max(graph.getLengthMeters(), Math.max(graph.getWidthMeters(), graph.getHeightMeters()));
        double baseScale = (Math.min(w, h) * 0.40d) / Math.max(1e-6d, halfSpan);
        double scale = baseScale * viewZoom;
        double cx = w * 0.5d;
        double cy = h * 0.55d;

        draw3dAxes(g, graph, cx, cy, scale);

        if (panelOverlayCheck.isSelected()) {
            java.util.List<PanelDraw3D> panels3d = new ArrayList<PanelDraw3D>();
            for (StructuralGraphPanel p : graph.getPanels()) {
                PanelDraw3D pd = build3dPanel(p, nodeById, panelEnv.get(p.getId()), cx, cy, scale);
                if (pd != null) {
                    panels3d.add(pd);
                }
            }
            java.util.Collections.sort(panels3d, (a, b) -> Double.compare(a.depth, b.depth));
            for (PanelDraw3D pd : panels3d) {
                g.setFill(pd.fill);
                g.fillPolygon(pd.xs, pd.ys, pd.xs.length);
                g.setStroke(pd.stroke);
                g.setLineWidth(1.0d);
                g.strokePolygon(pd.xs, pd.ys, pd.xs.length);
            }
        }

        java.util.List<MemberDraw3D> members3d = new ArrayList<MemberDraw3D>();
        for (StructuralGraphMember m : graph.getMembers()) {
            StructuralGraphNode a = nodeById.get(m.getNodeAId());
            StructuralGraphNode b = nodeById.get(m.getNodeBId());
            if (a == null || b == null) {
                continue;
            }
            members3d.add(build3dMember(m, a, b, memberEnv.get(m.getId()), cx, cy, scale));
        }
        java.util.Collections.sort(members3d, (a, b) -> Double.compare(a.depth, b.depth));
        for (MemberDraw3D md : members3d) {
            g.setStroke(md.color);
            g.setLineWidth(md.width);
            g.strokeLine(md.ax, md.ay, md.bx, md.by);
        }

        if (loadOverlayCheck.isSelected()) {
            for (StructuralGraphLoad load : loadSet.getLoads()) {
                if (load.getDirectionUnit() == null) { continue; }
                for (Vector3 anchor : resolveLoadAnchors(graph, load)) {
                    drawArrow3d(g, anchor, load.getDirectionUnit(), load.getMagnitudeG(), cx, cy, scale, loadColor(load));
                }
            }
        }

        java.util.List<NodeDraw3D> nodes3d = new ArrayList<NodeDraw3D>();
        for (StructuralGraphNode n : graph.getNodes()) {
            nodes3d.add(build3dNode(n, cx, cy, scale));
        }
        java.util.Collections.sort(nodes3d, (a, b) -> Double.compare(a.depth, b.depth));
        for (NodeDraw3D nd : nodes3d) {
            g.setFill(nd.color);
            g.fillOval(nd.x - nd.r, nd.y - nd.r, 2 * nd.r, 2 * nd.r);
            if (nodeLabelsCheck.isSelected()) {
                g.setFill(Color.rgb(220, 220, 220));
                g.fillText(nd.label, nd.x + 3, nd.y - 3);
            }
        }

        g.setFill(Color.rgb(180, 190, 205));
        g.fillText(String.format("yaw %.0f°, pitch %.0f°, zoom %.2fx",
                Math.toDegrees(viewYawRad), Math.toDegrees(viewPitchRad), viewZoom), 12, 18);
    }

    private void draw3dAxes(GraphicsContext g, StructuralGraph graph, double cx, double cy, double scale) {
        drawAxis3d(g, new Vector3(-0.5d * graph.getLengthMeters(), 0, 0), new Vector3(0.5d * graph.getLengthMeters(), 0, 0), cx, cy, scale, Color.rgb(220, 120, 120));
        drawAxis3d(g, new Vector3(0, -0.5d * graph.getWidthMeters(), 0), new Vector3(0, 0.5d * graph.getWidthMeters(), 0), cx, cy, scale, Color.rgb(120, 220, 160));
        drawAxis3d(g, new Vector3(0, 0, -0.5d * graph.getHeightMeters()), new Vector3(0, 0, 0.5d * graph.getHeightMeters()), cx, cy, scale, Color.rgb(130, 170, 255));
    }

    private void drawAxis3d(GraphicsContext g, Vector3 a, Vector3 b, double cx, double cy, double scale, Color color) {
        Vec3View va = viewTransform(a);
        Vec3View vb = viewTransform(b);
        g.setStroke(color.deriveColor(0, 1, 1, 0.6));
        g.setLineWidth(1.0d);
        g.strokeLine(cx + va.x * scale, cy - va.y * scale, cx + vb.x * scale, cy - vb.y * scale);
    }

    private PanelDraw3D build3dPanel(StructuralGraphPanel panel, Map<String, StructuralGraphNode> nodeById,
                                     StructuralGraphPanelEnvelope env, double cx, double cy, double scale) {
        List<Double> xs = new ArrayList<Double>();
        List<Double> ys = new ArrayList<Double>();
        double depthSum = 0.0d;
        int count = 0;
        for (String id : panel.getBoundaryNodeIds()) {
            StructuralGraphNode n = nodeById.get(id);
            if (n == null) { continue; }
            Vec3View v = viewTransform(n.getPositionMeters());
            xs.add(Double.valueOf(cx + v.x * scale));
            ys.add(Double.valueOf(cy - v.y * scale));
            depthSum += v.z;
            count++;
        }
        if (count < 3) {
            return null;
        }
        Color fill;
        if (stressColorCheck.isSelected() && env != null) {
            fill = heat(env.getPeakCombinedAbsIndex(), 0.12d, 0.24d).deriveColor(0, 1, 1, 0.20);
        } else {
            fill = Color.rgb(95, 110, 130, env != null && env.getRole().name().contains("PRIMARY") ? 0.18 : 0.08);
        }
        return new PanelDraw3D(toArray(xs), toArray(ys), fill, Color.rgb(110, 125, 150, 0.28), depthSum / count);
    }

    private MemberDraw3D build3dMember(StructuralGraphMember member, StructuralGraphNode a, StructuralGraphNode b,
                                       StructuralGraphMemberEnvelope env, double cx, double cy, double scale) {
        Vec3View va = viewTransform(a.getPositionMeters());
        Vec3View vb = viewTransform(b.getPositionMeters());
        Color color;
        double lineWidth;
        if (stressColorCheck.isSelected() && env != null) {
            color = heat(env.getPeakCombinedAbsIndex(), 0.30d, 0.50d);
            lineWidth = member.getRole() == StructuralMemberRole.LOCAL_LOAD_TRANSFER ? 2.4d : 1.9d;
        } else {
            if (member.getRole() == StructuralMemberRole.PRIMARY_LOAD_PATH) {
                color = Color.rgb(230, 235, 245);
                lineWidth = 1.9d;
            } else if (member.getRole() == StructuralMemberRole.SECONDARY_STIFFENING) {
                color = Color.rgb(145, 175, 215);
                lineWidth = 1.4d;
            } else {
                color = Color.rgb(255, 180, 90);
                lineWidth = 2.2d;
            }
        }
        double depthShade = clamp01(0.65d + 0.35d * (1.0d - clamp01(((va.z + vb.z) * 0.5d + 1.0d) / 2.0d)));
        color = color.deriveColor(0, 1, depthShade, 1.0d);
        return new MemberDraw3D(cx + va.x * scale, cy - va.y * scale, cx + vb.x * scale, cy - vb.y * scale, color, lineWidth, (va.z + vb.z) * 0.5d);
    }

    private NodeDraw3D build3dNode(StructuralGraphNode n, double cx, double cy, double scale) {
        Vec3View v = viewTransform(n.getPositionMeters());
        Color c = nodeColor(n.getType());
        double r = nodeRadius(n.getType()) * (0.8d + 0.35d * clamp01(1.0d - (v.z + 1.0d) * 0.5d));
        return new NodeDraw3D(cx + v.x * scale, cy - v.y * scale, r, c, v.z, n.getId());
    }

    private void drawArrow3d(GraphicsContext g, Vector3 origin, Vector3 dir, double magnitudeG, double cx, double cy, double scale, Color color) {
        Vector3 n = normalize(dir);
        double lenWorld = Math.min(Math.max(0.5d, magnitudeG * 1.2d), 8.0d);
        Vector3 tip = new Vector3(origin.getX() + n.getX() * lenWorld, origin.getY() + n.getY() * lenWorld, origin.getZ() + n.getZ() * lenWorld);
        Vec3View v0 = viewTransform(origin);
        Vec3View v1 = viewTransform(tip);
        double x0 = cx + v0.x * scale;
        double y0 = cy - v0.y * scale;
        double x1 = cx + v1.x * scale;
        double y1 = cy - v1.y * scale;
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(1.1d);
        g.strokeLine(x0, y0, x1, y1);
        double dx = x1 - x0, dy = y1 - y0;
        double m = Math.sqrt(dx * dx + dy * dy);
        if (m < 1.0d) { return; }
        double ux = dx / m, uy = dy / m;
        double ah = 6.0d, aw = 3.5d;
        double bx = x1 - ux * ah, by = y1 - uy * ah;
        double px = -uy, py = ux;
        g.fillPolygon(new double[]{x1, bx + px * aw, bx - px * aw}, new double[]{y1, by + py * aw, by - py * aw}, 3);
    }

    private Vec3View viewTransform(Vector3 p) {
        double cy = Math.cos(viewYawRad);
        double sy = Math.sin(viewYawRad);
        double cp = Math.cos(viewPitchRad);
        double sp = Math.sin(viewPitchRad);
        double x1 = p.getX() * cy - p.getY() * sy;
        double y1 = p.getX() * sy + p.getY() * cy;
        double z1 = p.getZ();
        double x2 = x1;
        double y2 = y1 * cp - z1 * sp;
        double z2 = y1 * sp + z1 * cp;
        return new Vec3View(x2, y2, z2);
    }

    private List<Vector3> resolveLoadAnchors(StructuralGraph graph, StructuralGraphLoad load) {
        List<Vector3> anchors = new ArrayList<Vector3>();
        if (load == null) { return anchors; }
        if (load.getTargetType() == null || "WHOLE_GRAPH".equals(load.getTargetType().name())) {
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
        Vector3 tip = new Vector3(origin.getX() + n.getX() * lenWorld, origin.getY() + n.getY() * lenWorld, origin.getZ() + n.getZ() * lenWorld);
        Vector2 p0 = projectPoint(projection, origin, world, w, h, pad, scale);
        Vector2 p1 = projectPoint(projection, tip, world, w, h, pad, scale);
        g.setStroke(color);
        g.setFill(color);
        g.setLineWidth(1.1d);
        g.strokeLine(p0.x, p0.y, p1.x, p1.y);
        double dx = p1.x - p0.x;
        double dy = p1.y - p0.y;
        double m = Math.sqrt(dx * dx + dy * dy);
        if (m < 1.0d) { return; }
        double ux = dx / m;
        double uy = dy / m;
        double ah = 6.0d;
        double aw = 3.5d;
        double bx = p1.x - ux * ah;
        double by = p1.y - uy * ah;
        double px = -uy;
        double py = ux;
        g.fillPolygon(new double[]{p1.x, bx + px * aw, bx - px * aw}, new double[]{p1.y, by + py * aw, by - py * aw}, 3);
    }

    private Color nodeColor(StructuralNodeType type) {
        if (type == null) { return Color.GRAY; }
        switch (type) {
            case ENGINE_MOUNT:
            case THRUST_LOAD_POINT: return Color.ORANGE;
            case LANDING_LOAD_POINT: return Color.LIMEGREEN;
            case MASS_ANCHOR: return Color.GOLD;
            case FRAME_MIDPOINT: return Color.SKYBLUE;
            default: return Color.WHITE;
        }
    }

    private double nodeRadius(StructuralNodeType type) {
        if (type == StructuralNodeType.ENGINE_MOUNT || type == StructuralNodeType.THRUST_LOAD_POINT) return 3.2d;
        if (type == StructuralNodeType.LANDING_LOAD_POINT || type == StructuralNodeType.MASS_ANCHOR) return 3.0d;
        return 2.2d;
    }

    private Color loadColor(StructuralGraphLoad load) {
        if (load == null || load.getType() == null) { return Color.rgb(210, 220, 255, 0.65); }
        switch (load.getType()) {
            case THRUST_APPLICATION: return Color.rgb(255, 170, 70, 0.85);
            case LANDING_REACTION: return Color.rgb(90, 230, 120, 0.85);
            case GROUND_SUPPORT_REACTION: return Color.rgb(70, 200, 170, 0.80);
            case ENGINE_DIFFERENTIAL_CONTROL: return Color.rgb(255, 110, 150, 0.85);
            default: return Color.rgb(210, 220, 255, 0.65);
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
        double c = Math.max(0.0d, Math.min(1.0d, t));
        return new Color(a.getRed() + (b.getRed() - a.getRed()) * c, a.getGreen() + (b.getGreen() - a.getGreen()) * c, a.getBlue() + (b.getBlue() - a.getBlue()) * c, 1.0d);
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    private double clamp01(double v) {
        return clamp(v, 0.0d, 1.0d);
    }

    private Vector2 projectPoint(Projection p, Vector3 v, RectWorld world, double canvasW, double canvasH, double pad, double scale) {
        double x;
        double y;
        switch (p) {
            case TOP_XY: x = v.getX(); y = v.getY(); break;
            case SIDE_XZ: x = v.getX(); y = v.getZ(); break;
            case FRONT_YZ: x = v.getY(); y = v.getZ(); break;
            default: x = v.getX(); y = v.getY();
        }
        return new Vector2(pad + (x - world.minX) * scale, canvasH - pad - (y - world.minY) * scale);
    }

    private double[] toArray(List<Double> values) {
        double[] arr = new double[values.size()];
        for (int i = 0; i < values.size(); i++) { arr[i] = values.get(i).doubleValue(); }
        return arr;
    }

    private Vector3 normalize(Vector3 v) {
        double m = Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
        if (m <= 1e-9d) { return new Vector3(1.0d, 0.0d, 0.0d); }
        return new Vector3(v.getX() / m, v.getY() / m, v.getZ() / m);
    }

    private enum Projection {
        TOP_XY, SIDE_XZ, FRONT_YZ;

        RectWorld world(StructuralGraph graph) {
            switch (this) {
                case TOP_XY:
                    return new RectWorld(-0.5d * graph.getLengthMeters(), 0.5d * graph.getLengthMeters(), -0.5d * graph.getWidthMeters(), 0.5d * graph.getWidthMeters());
                case SIDE_XZ:
                    return new RectWorld(-0.5d * graph.getLengthMeters(), 0.5d * graph.getLengthMeters(), -0.5d * graph.getHeightMeters(), 0.5d * graph.getHeightMeters());
                case FRONT_YZ:
                    return new RectWorld(-0.5d * graph.getWidthMeters(), 0.5d * graph.getWidthMeters(), -0.5d * graph.getHeightMeters(), 0.5d * graph.getHeightMeters());
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
            this.minX = minX; this.maxX = maxX; this.minY = minY; this.maxY = maxY;
        }
    }

    private static final class Vector2 {
        private final double x;
        private final double y;

        private Vector2(double x, double y) { this.x = x; this.y = y; }
    }

    private static final class Vec3View {
        private final double x;
        private final double y;
        private final double z;

        private Vec3View(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static final class MemberDraw3D {
        private final double ax;
        private final double ay;
        private final double bx;
        private final double by;
        private final Color color;
        private final double width;
        private final double depth;

        private MemberDraw3D(double ax, double ay, double bx, double by, Color color, double width, double depth) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.color = color;
            this.width = width;
            this.depth = depth;
        }
    }

    private static final class PanelDraw3D {
        private final double[] xs;
        private final double[] ys;
        private final Color fill;
        private final Color stroke;
        private final double depth;

        private PanelDraw3D(double[] xs, double[] ys, Color fill, Color stroke, double depth) {
            this.xs = xs;
            this.ys = ys;
            this.fill = fill;
            this.stroke = stroke;
            this.depth = depth;
        }
    }

    private static final class NodeDraw3D {
        private final double x;
        private final double y;
        private final double r;
        private final Color color;
        private final double depth;
        private final String label;

        private NodeDraw3D(double x, double y, double r, Color color, double depth, String label) {
            this.x = x;
            this.y = y;
            this.r = r;
            this.color = color;
            this.depth = depth;
            this.label = label;
        }
    }
}
