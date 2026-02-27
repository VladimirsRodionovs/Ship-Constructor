package org.example.shipconstructor.ui;

import org.example.shipconstructor.chassis.domain.AccelerationEnvelope;
import org.example.shipconstructor.chassis.domain.ChassisCalculationInput;
import org.example.shipconstructor.chassis.domain.CubeDimensions;
import org.example.shipconstructor.chassis.domain.EngineeringStackSelection;
import org.example.shipconstructor.chassis.domain.MissionPriority;
import org.example.shipconstructor.chassis.domain.SafetyPriority;
import org.example.shipconstructor.chassis.domain.StructuralDesignBasis;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphBuildResult;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphLoadSet;
import org.example.shipconstructor.chassis.graph.domain.StructuralGraphSolveResult;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphAxialEnvelopeSolverService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphBuilderService;
import org.example.shipconstructor.chassis.graph.service.StructuralGraphLoadPlannerService;
import org.example.shipconstructor.chassis.ui.ChassisGraphViewerPane;
import org.example.shipconstructor.db.DatabaseConfig;
import org.example.shipconstructor.db.JdbcShipModulesRepository;
import org.example.shipconstructor.db.ShipModulesRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.function.UnaryOperator;

public class ModuleDesignerApp extends Application {
    private final Map<String, TextField> textFields = new LinkedHashMap<String, TextField>();
    private final Map<String, TextArea> jsonFields = new LinkedHashMap<String, TextArea>();
    private final Map<String, CheckBox> checkBoxes = new LinkedHashMap<String, CheckBox>();
    private final Map<String, ComboBox<Long>> chassisIdComboBoxes = new LinkedHashMap<String, ComboBox<Long>>();
    private final ModuleDesignJsonService jsonService = new ModuleDesignJsonService();
    private final StructuralGraphBuilderService chassisGraphBuilder = new StructuralGraphBuilderService();
    private final StructuralGraphLoadPlannerService chassisGraphLoadPlanner = new StructuralGraphLoadPlannerService();
    private final StructuralGraphAxialEnvelopeSolverService chassisGraphSolver = new StructuralGraphAxialEnvelopeSolverService();

    private ComboBox<Integer> typeComboBox;
    private ComboBox<Integer> sizeTypeComboBox;
    private ComboBox<Integer> enginePlacementProfileComboBox;
    private ComboBox<String> structureProfileCodeComboBox;
    private ChassisGraphViewerPane chassisGraphViewerPane;
    private TextArea previewArea;
    private TextArea messagesArea;
    private Label statusLabel;
    private Stage primaryStage;

    public static void launchApp(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Module Designer (Local)");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(buildToolbar());
        root.setCenter(buildTabs());
        root.setBottom(buildStatusBar());

        applyDefaults();

        Scene scene = new Scene(root, 1280, 860);
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildToolbar() {
        Button newButton = new Button("New");
        Button validateButton = new Button("Validate");
        Button saveLocalButton = new Button("Save Local");
        Button saveDbButton = new Button("Save to DB");
        Button loadLocalButton = new Button("Load Local");
        Button duplicateButton = new Button("Duplicate");
        Button clearButton = new Button("Clear");

        newButton.setOnAction(event -> {
            clearForm();
            applyDefaults();
            assignNextModuleIdFromDatabase();
        });
        clearButton.setOnAction(event -> {
            clearForm();
            setStatus("Form cleared");
        });
        duplicateButton.setOnAction(event -> duplicateCurrent());
        validateButton.setOnAction(event -> validateAndRefreshPreview());
        saveLocalButton.setOnAction(event -> saveLocal());
        saveDbButton.setOnAction(event -> saveToDatabase());
        loadLocalButton.setOnAction(event -> loadLocal());

        HBox toolbar = new HBox(8, newButton, validateButton, saveLocalButton, saveDbButton, loadLocalButton, duplicateButton, clearButton);
        toolbar.setPadding(new Insets(0, 0, 10, 0));
        toolbar.setAlignment(Pos.CENTER_LEFT);
        return toolbar;
    }

    private TabPane buildTabs() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(createGeneralTab());
        tabPane.getTabs().add(createEnergyTab());
        tabPane.getTabs().add(createFuelTab());
        tabPane.getTabs().add(createLinesTab());
        tabPane.getTabs().add(createStorageTab());
        tabPane.getTabs().add(createSpecificJsonTab());
        tabPane.getTabs().add(createPreviewTab());
        tabPane.getTabs().add(createChassisGraphTab());
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabPane;
    }

    private Tab createGeneralTab() {
        GridPane grid = createFormGrid();

        int row = 0;
        row = addSizeTypeCombo(grid, row);
        row = addTextField(grid, row, "SizeTotal", "SizeTotal (cube count)", numericFilter(true, true));
        row = addTextField(grid, row, "SizeDimX", "SizeDim X (cubes)", numericFilter(true, true));
        row = addTextField(grid, row, "SizeDimY", "SizeDim Y (cubes)", numericFilter(true, true));
        row = addTextField(grid, row, "SizeDimZ", "SizeDim Z (cubes)", numericFilter(true, true));
        row = addStructureProfileCodeCombo(grid, row);
        row = addEnginePlacementProfileCombo(grid, row);
        row = addChassisIdCombo(grid, row, "chassisBaseMaterialId", "Chassis A BaseMaterial", ChassisEngineeringCatalogs.baseMaterials());
        row = addChassisIdCombo(grid, row, "chassisStructureTypeId", "Chassis B StructureType", ChassisEngineeringCatalogs.structureTypes());
        row = addChassisIdCombo(grid, row, "chassisManufacturingProcessId", "Chassis C Manufacturing", ChassisEngineeringCatalogs.manufacturing());
        row = addChassisIdCombo(grid, row, "chassisAssemblyProcessId", "Chassis D Assembly", ChassisEngineeringCatalogs.assembly());
        row = addChassisIdCombo(grid, row, "chassisQualityProfileId", "Chassis E Quality", ChassisEngineeringCatalogs.quality());
        row = addChassisIdCombo(grid, row, "chassisEnvironmentProfileId", "Chassis F Environment", ChassisEngineeringCatalogs.environments());
        row = addTextField(grid, row, "ModuleID", "ModuleID", numericFilter(true, true));
        row = addTextField(grid, row, "PartyID", "PartyID", numericFilter(true, true));
        row = addTextField(grid, row, "ModName", "ModName", null);
        row = addTextField(grid, row, "NumberProduced", "NumberProduced", numericFilter(true, true));
        row = addTypeCombo(grid, row);
        row = addTextField(grid, row, "Name", "Name", null);
        row = addTextField(grid, row, "SpecFunctionTypeName", "SpecFunctionTypeName", null);
        row = addTextField(grid, row, "ManufacturerName", "ManufacturerName", null);
        row = addTextField(grid, row, "DeveloperName", "DeveloperName", null);
        row = addTextField(grid, row, "DryWeight", "DryWeight (tons)", numericFilter(false, true));
        row = addTextField(grid, row, "FullWeight", "FullWeight (tons)", numericFilter(false, true));
        row = addTextField(grid, row, "CargoVolumeMax", "CargoVolumeMax", numericFilter(true, true));
        row = addTextField(grid, row, "CargoType", "CargoType", numericFilter(true, true));
        row = addTextField(grid, row, "Reliability", "Reliability (0..100)", numericFilter(true, true));
        row = addTextField(grid, row, "ManufactureDate", "ManufactureDate", null);
        row = addTextField(grid, row, "ManufactureTimeSec", "ManufactureTimeSec", numericFilter(true, true));
        row = addTextField(grid, row, "technologyDirection", "technologyDirection", numericFilter(true, true));
        row = addTextField(grid, row, "technologyLevel", "technologyLevel", numericFilter(true, true));
        row = addTextField(grid, row, "DockType", "DockType", numericFilter(true, true));
        row = addTextField(grid, row, "AmmoType", "AmmoType", null);
        row = addTextField(grid, row, "InternalDefence", "InternalDefence", numericFilter(true, true));
        addTextField(grid, row, "MeshID", "MeshID", numericFilter(true, true));

        return createScrollTab("General", grid);
    }

    private Tab createEnergyTab() {
        GridPane grid = createFormGrid();
        int row = 0;
        row = addTextField(grid, row, "EnergyConsMax", "EnergyConsMax", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyConsPowerUp", "EnergyConsPowerUp", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyConsStandBy", "EnergyConsStandBy", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyConsOn", "EnergyConsOn", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyProdMin", "EnergyProdMin", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyProdMax", "EnergyProdMax", numericFilter(true, true));
        row = addTextField(grid, row, "EnergyProdNominal", "EnergyProdNominal", numericFilter(true, true));
        addTextField(grid, row, "EnergyProdCritical", "EnergyProdCritical", numericFilter(true, true));
        return createScrollTab("Energy", grid);
    }

    private Tab createFuelTab() {
        GridPane grid = createFormGrid();
        int row = 0;
        row = addTextField(grid, row, "FuelConsumption", "FuelConsumption", numericFilter(true, true));
        row = addTextField(grid, row, "FuelQantity", "FuelQantity", numericFilter(true, true));
        row = addCheckBox(grid, row, "FuelRadioactiveMaterial", "FuelRadioactiveMaterial");
        row = addTextField(grid, row, "DegradationSpeedPerYear", "DegradationSpeedPerYear", numericFilter(false, true));
        row = addTextField(grid, row, "FuelType", "FuelType", null);
        addTextField(grid, row, "DateOfRefueling", "DateOfRefueling", null);
        return createScrollTab("Fuel", grid);
    }

    private Tab createLinesTab() {
        GridPane grid = createFormGrid();
        int row = 0;
        row = addTextField(grid, row, "ControlLinesAmount", "ControlLinesAmount", numericFilter(true, true));
        row = addTextField(grid, row, "TermalLinesAmount", "TermalLinesAmount", numericFilter(true, true));
        row = addTextField(grid, row, "FuelLinesAmount", "FuelLinesAmount", numericFilter(true, true));
        addTextField(grid, row, "PowerLinesAmount", "PowerLinesAmount", numericFilter(true, true));
        return createScrollTab("Lines", grid);
    }

    private Tab createStorageTab() {
        GridPane grid = createFormGrid();
        int row = 0;
        row = addTextField(grid, row, "StorageCargoM3", "Storage Cargo (m3)", numericFilter(false, true));
        row = addTextField(grid, row, "StorageAmmoM3", "Storage Ammo (m3)", numericFilter(false, true));
        row = addTextField(grid, row, "StorageConsumablesM3", "Storage Consumables (m3)", numericFilter(false, true));
        row = addTextField(grid, row, "StorageAtmosphereM3", "Storage Atmosphere (m3)", numericFilter(false, true));
        addTextField(grid, row, "InfrastructureVolumeM3", "Infrastructure Volume (m3)", numericFilter(false, true));
        return createScrollTab("Storage", grid);
    }

    private Tab createSpecificJsonTab() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.getChildren().addAll(
                createJsonArea("SizeDimentions", "SizeDimentions (JSON, cube dimensions x/y/z)"),
                createJsonArea("SpecificModParamCrew", "SpecificModParamCrew"),
                createJsonArea("SpecificModParamEmissions", "SpecificModParamEmissions"),
                createJsonArea("SpecificModParamWeapon", "SpecificModParamWeapon"),
                createJsonArea("SpecificModParamTrust", "SpecificModParamTrust"),
                createJsonArea("Armored", "Armored"),
                createJsonArea("Blocks", "Blocks")
        );

        Button validateJsonButton = new Button("Validate JSON Fields");
        validateJsonButton.setOnAction(event -> validateAndRefreshPreview());
        box.getChildren().add(validateJsonButton);

        ScrollPane scroll = new ScrollPane(box);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        Tab tab = new Tab("Specific JSON", scroll);
        tab.setClosable(false);
        return tab;
    }

    private Tab createPreviewTab() {
        previewArea = new TextArea();
        previewArea.setEditable(false);
        previewArea.setWrapText(false);
        previewArea.setPrefRowCount(24);

        messagesArea = new TextArea();
        messagesArea.setEditable(false);
        messagesArea.setWrapText(true);
        messagesArea.setPrefRowCount(10);

        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        VBox.setVgrow(previewArea, Priority.ALWAYS);
        VBox.setVgrow(messagesArea, Priority.SOMETIMES);
        box.getChildren().addAll(new Label("Preview JSON"), previewArea, new Label("Validation Messages"), messagesArea);

        Tab tab = new Tab("Preview", box);
        tab.setClosable(false);
        return tab;
    }

    private Tab createChassisGraphTab() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        Button buildButton = new Button("Build From Form");
        Button clearButton = new Button("Clear View");
        Label hint = new Label("Uses SizeType/SizeTotal/SizeDimentions + StructureProfileCode (+ EnginePlacementProfileClass if set)");
        HBox toolbar = new HBox(8, buildButton, clearButton, hint);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 8, 0));

        chassisGraphViewerPane = new ChassisGraphViewerPane();
        chassisGraphViewerPane.clear("No graph built yet.\nSelect chassis-like values and click 'Build From Form'.");

        buildButton.setOnAction(event -> buildChassisGraphFromForm());
        clearButton.setOnAction(event -> {
            if (chassisGraphViewerPane != null) {
                chassisGraphViewerPane.clear("View cleared.");
            }
            setStatus("Chassis graph view cleared");
        });

        pane.setTop(toolbar);
        pane.setCenter(chassisGraphViewerPane);
        Tab tab = new Tab("Chassis Graph", pane);
        tab.setClosable(false);
        return tab;
    }

    private HBox buildStatusBar() {
        statusLabel = new Label("Ready");
        HBox bar = new HBox(statusLabel);
        bar.setPadding(new Insets(10, 0, 0, 0));
        return bar;
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(8);
        return grid;
    }

    private Tab createScrollTab(String title, GridPane grid) {
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        Tab tab = new Tab(title, scroll);
        tab.setClosable(false);
        return tab;
    }

    private int addTextField(GridPane grid, int row, String key, String labelText, UnaryOperator<TextFormatter.Change> filter) {
        Label label = new Label(labelText);
        TextField field = new TextField();
        if (filter != null) {
            field.setTextFormatter(new TextFormatter<String>(filter));
        }
        textFields.put(key, field);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
        return row + 1;
    }

    private int addCheckBox(GridPane grid, int row, String key, String labelText) {
        Label label = new Label(labelText);
        CheckBox checkBox = new CheckBox();
        checkBoxes.put(key, checkBox);
        grid.add(label, 0, row);
        grid.add(checkBox, 1, row);
        return row + 1;
    }

    private int addTypeCombo(GridPane grid, int row) {
        Label label = new Label("Type");
        List<Integer> types = new ArrayList<Integer>();
        for (int i = 0; i <= 117; i++) {
            types.add(Integer.valueOf(i));
        }
        typeComboBox = new ComboBox<Integer>(FXCollections.observableArrayList(types));
        typeComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ModuleTypeCatalog.displayName(item.intValue()));
            }
        });
        typeComboBox.setButtonCell(new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ModuleTypeCatalog.displayName(item.intValue()));
            }
        });
        typeComboBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(label, 0, row);
        grid.add(typeComboBox, 1, row);
        GridPane.setHgrow(typeComboBox, Priority.ALWAYS);
        return row + 1;
    }

    private int addSizeTypeCombo(GridPane grid, int row) {
        Label label = new Label("SizeType");
        List<Integer> sizeTypes = new ArrayList<Integer>();
        for (int i = 1; i <= 15; i++) {
            sizeTypes.add(Integer.valueOf(i));
        }
        sizeTypeComboBox = new ComboBox<Integer>(FXCollections.observableArrayList(sizeTypes));
        sizeTypeComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : SizeTypeCatalog.displayName(item.intValue()));
            }
        });
        sizeTypeComboBox.setButtonCell(new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : SizeTypeCatalog.displayName(item.intValue()));
            }
        });
        sizeTypeComboBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(label, 0, row);
        grid.add(sizeTypeComboBox, 1, row);
        GridPane.setHgrow(sizeTypeComboBox, Priority.ALWAYS);
        return row + 1;
    }

    private int addStructureProfileCodeCombo(GridPane grid, int row) {
        Label label = new Label("StructureProfileCode");
        List<String> codes = new ArrayList<String>(StructureProfileCodeCatalog.codes());
        structureProfileCodeComboBox = new ComboBox<String>(FXCollections.observableArrayList(codes));
        structureProfileCodeComboBox.setEditable(true);
        structureProfileCodeComboBox.setPromptText("STR_... (chassis only, optional)");
        structureProfileCodeComboBox.setVisibleRowCount(12);
        structureProfileCodeComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : StructureProfileCodeCatalog.displayLabel(item));
            }
        });
        structureProfileCodeComboBox.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : StructureProfileCodeCatalog.displayLabel(item));
            }
        });
        structureProfileCodeComboBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(label, 0, row);
        grid.add(structureProfileCodeComboBox, 1, row);
        GridPane.setHgrow(structureProfileCodeComboBox, Priority.ALWAYS);
        return row + 1;
    }

    private int addEnginePlacementProfileCombo(GridPane grid, int row) {
        Label label = new Label("EnginePlacementProfile");
        List<Integer> profiles = new ArrayList<Integer>();
        for (int i = 0; i <= 13; i++) {
            profiles.add(Integer.valueOf(i));
        }
        enginePlacementProfileComboBox = new ComboBox<Integer>(FXCollections.observableArrayList(profiles));
        enginePlacementProfileComboBox.setCellFactory(listView -> new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : EnginePlacementProfileCatalog.displayName(item.intValue()));
            }
        });
        enginePlacementProfileComboBox.setButtonCell(new javafx.scene.control.ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : EnginePlacementProfileCatalog.displayName(item.intValue()));
            }
        });
        enginePlacementProfileComboBox.setMaxWidth(Double.MAX_VALUE);

        grid.add(label, 0, row);
        grid.add(enginePlacementProfileComboBox, 1, row);
        GridPane.setHgrow(enginePlacementProfileComboBox, Priority.ALWAYS);
        return row + 1;
    }

    private int addChassisIdCombo(GridPane grid, int row, String key, String labelText, Map<Long, String> catalog) {
        Label label = new Label(labelText);
        List<Long> ids = new ArrayList<Long>(catalog.keySet());
        ComboBox<Long> combo = new ComboBox<Long>(FXCollections.observableArrayList(ids));
        combo.setCellFactory(listView -> new javafx.scene.control.ListCell<Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : catalog.get(item));
            }
        });
        combo.setButtonCell(new javafx.scene.control.ListCell<Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : catalog.get(item));
            }
        });
        combo.setMaxWidth(Double.MAX_VALUE);
        chassisIdComboBoxes.put(key, combo);

        grid.add(label, 0, row);
        grid.add(combo, 1, row);
        GridPane.setHgrow(combo, Priority.ALWAYS);
        return row + 1;
    }

    private VBox createJsonArea(String key, String labelText) {
        Label label = new Label(labelText);
        TextArea area = new TextArea();
        area.setPrefRowCount(6);
        area.setWrapText(false);
        jsonFields.put(key, area);

        VBox box = new VBox(4, label, area);
        VBox.setVgrow(area, Priority.ALWAYS);
        return box;
    }

    private UnaryOperator<TextFormatter.Change> numericFilter(boolean integerOnly, boolean allowEmpty) {
        return change -> {
            String next = change.getControlNewText();
            if (allowEmpty && (next == null || next.isEmpty())) {
                return change;
            }
            String pattern = integerOnly ? "\\d*" : "\\d*(?:[\\.,]\\d*)?";
            return next.matches(pattern) ? change : null;
        };
    }

    private void validateAndRefreshPreview() {
        ValidationResult result = validateForm();
        messagesArea.setText(joinLines(result.messages));
        if (result.valid && result.normalizedModuleData != null) {
            try {
                previewArea.setText(jsonService.toPrettyJson(result.normalizedModuleData));
                setStatus("Validation OK");
            } catch (IOException e) {
                previewArea.setText("");
                setStatus("Preview serialization failed");
                showError("JSON serialization error", e.getMessage());
            }
        } else {
            previewArea.setText("");
            setStatus("Validation failed");
        }
    }

    private ValidationResult validateForm() {
        List<String> messages = new ArrayList<String>();
        if (typeComboBox.getValue() == null || !ModuleTypeCatalog.names().containsKey(typeComboBox.getValue())) {
            messages.add("Validation errors:");
            messages.add(" - Field is required: Type (select module type from list)");
            return ValidationResult.invalid(messages);
        }
        if (sizeTypeComboBox.getValue() == null || !SizeTypeCatalog.values().containsKey(sizeTypeComboBox.getValue())) {
            messages.add("Validation errors:");
            messages.add(" - Field is required: SizeType (select size type from list)");
            return ValidationResult.invalid(messages);
        }

        messages.add("Validation passed (minimal mode)");
        messages.add(" - Checked only: module Type and SizeType selected from lists");
        if (typeComboBox.getValue() != null && typeComboBox.getValue().intValue() == 0 && isBlank(getStructureProfileCodeValue())) {
            messages.add("Warnings:");
            messages.add(" - Type=0 (Chassis): StructureProfileCode is not set (recommended for graph/pre-solver behavior)");
        }
        if (typeComboBox.getValue() != null && typeComboBox.getValue().intValue() == 0) {
            if (getChassisIdValue("chassisBaseMaterialId") == null
                    || getChassisIdValue("chassisStructureTypeId") == null
                    || getChassisIdValue("chassisManufacturingProcessId") == null
                    || getChassisIdValue("chassisAssemblyProcessId") == null
                    || getChassisIdValue("chassisQualityProfileId") == null
                    || getChassisIdValue("chassisEnvironmentProfileId") == null) {
                if (!messages.contains("Warnings:")) {
                    messages.add("Warnings:");
                }
                messages.add(" - Type=0 (Chassis): A-F IDs are not fully set (graph works, but engineering stack defaults may be used)");
            }
        }
        return ValidationResult.valid(messages, buildPermissiveModuleData());
    }

    private Map<String, Object> buildPermissiveModuleData() {
        Map<String, Object> moduleData = new LinkedHashMap<String, Object>();

        putRawNumberOrText(moduleData, "ModuleID");
        putRawNumberOrText(moduleData, "PartyID");
        moduleData.put("SizeType", sizeTypeComboBox == null ? null : sizeTypeComboBox.getValue());
        putRawNumberOrText(moduleData, "SizeTotal");
        putRawNumberOrText(moduleData, "SizeDimX");
        putRawNumberOrText(moduleData, "SizeDimY");
        putRawNumberOrText(moduleData, "SizeDimZ");
        moduleData.put("structureProfileCode", getStructureProfileCodeValue());
        moduleData.put("enginePlacementProfileClass", getEnginePlacementProfileClassValue());
        putChassisId(moduleData, "chassisBaseMaterialId");
        putChassisId(moduleData, "chassisStructureTypeId");
        putChassisId(moduleData, "chassisManufacturingProcessId");
        putChassisId(moduleData, "chassisAssemblyProcessId");
        putChassisId(moduleData, "chassisQualityProfileId");
        putChassisId(moduleData, "chassisEnvironmentProfileId");
        putRawText(moduleData, "ModName");
        putRawNumberOrText(moduleData, "NumberProduced");
        moduleData.put("Type", typeComboBox.getValue());
        moduleData.put("TypeName", ModuleTypeCatalog.names().get(typeComboBox.getValue()));
        putRawText(moduleData, "Name");
        putRawText(moduleData, "SpecFunctionTypeName");
        putRawText(moduleData, "ManufacturerName");
        putRawText(moduleData, "DeveloperName");
        putRawNumberOrText(moduleData, "DryWeight");
        putRawNumberOrText(moduleData, "FullWeight");
        putRawNumberOrText(moduleData, "CargoVolumeMax");
        putRawNumberOrText(moduleData, "CargoType");
        putRawNumberOrText(moduleData, "Reliability");
        putRawText(moduleData, "ManufactureDate");
        putRawNumberOrText(moduleData, "ManufactureTimeSec");
        putRawNumberOrText(moduleData, "EnergyConsMax");
        putRawNumberOrText(moduleData, "EnergyConsPowerUp");
        putRawNumberOrText(moduleData, "EnergyConsStandBy");
        putRawNumberOrText(moduleData, "EnergyConsOn");
        putRawNumberOrText(moduleData, "EnergyProdMin");
        putRawNumberOrText(moduleData, "EnergyProdMax");
        putRawNumberOrText(moduleData, "EnergyProdNominal");
        putRawNumberOrText(moduleData, "EnergyProdCritical");
        putRawNumberOrText(moduleData, "FuelConsumption");
        putRawNumberOrText(moduleData, "FuelQantity");
        moduleData.put("FuelRadioactiveMaterial", checkBoxes.get("FuelRadioactiveMaterial").isSelected());
        putRawNumberOrText(moduleData, "DegradationSpeedPerYear");
        putRawText(moduleData, "FuelType");
        putRawText(moduleData, "DateOfRefueling");
        putRawNumberOrText(moduleData, "ControlLinesAmount");
        putRawNumberOrText(moduleData, "TermalLinesAmount");
        putRawNumberOrText(moduleData, "FuelLinesAmount");
        putRawNumberOrText(moduleData, "PowerLinesAmount");
        putSizeDimensionsJson(moduleData);
        putRawJsonText(moduleData, "SpecificModParamCrew");
        putRawJsonText(moduleData, "SpecificModParamEmissions");
        putRawJsonText(moduleData, "SpecificModParamWeapon");
        putRawJsonText(moduleData, "SpecificModParamTrust");
        putRawNumberOrText(moduleData, "DockType");
        putRawJsonText(moduleData, "Armored");
        putRawText(moduleData, "AmmoType");
        putRawNumberOrText(moduleData, "InternalDefence");
        putRawNumberOrText(moduleData, "MeshID");
        putRawJsonText(moduleData, "Blocks");
        putRawNumberOrText(moduleData, "technologyDirection");
        putRawNumberOrText(moduleData, "technologyLevel");

        Map<String, Object> localDesigner = new LinkedHashMap<String, Object>();
        putRawNumberOrText(localDesigner, "StorageCargoM3");
        putRawNumberOrText(localDesigner, "StorageAmmoM3");
        putRawNumberOrText(localDesigner, "StorageConsumablesM3");
        putRawNumberOrText(localDesigner, "StorageAtmosphereM3");
        putRawNumberOrText(localDesigner, "InfrastructureVolumeM3");
        moduleData.put("_localDesigner", localDesigner);

        return moduleData;
    }

    private void saveLocal() {
        ValidationResult result = validateForm();
        messagesArea.setText(joinLines(result.messages));
        if (!result.valid || result.normalizedModuleData == null) {
            setStatus("Cannot save: validation failed");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Module Design");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        chooser.setInitialDirectory(ensureDefaultDataDir());
        chooser.setInitialFileName(suggestFileName());

        File file = chooser.showSaveDialog(primaryStage);
        if (file == null) {
            return;
        }

        try {
            jsonService.save(file.toPath(), result.normalizedModuleData);
            previewArea.setText(jsonService.toPrettyJson(result.normalizedModuleData));
            setStatus("Saved: " + file.getAbsolutePath());
        } catch (IOException ex) {
            showError("Save failed", ex.getMessage());
            setStatus("Save failed");
        }
    }

    private void loadLocal() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load Module Design");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        chooser.setInitialDirectory(ensureDefaultDataDir());

        File file = chooser.showOpenDialog(primaryStage);
        if (file == null) {
            return;
        }

        try {
            Map<String, Object> data = jsonService.load(file.toPath());
            applyLoadedData(data);
            validateAndRefreshPreview();
            setStatus("Loaded: " + file.getAbsolutePath());
        } catch (IOException ex) {
            showError("Load failed", ex.getMessage());
            setStatus("Load failed");
        }
    }

    private void saveToDatabase() {
        ValidationResult result = validateForm();
        messagesArea.setText(joinLines(result.messages));
        if (!result.valid || result.normalizedModuleData == null) {
            setStatus("Cannot save to DB: validation failed");
            return;
        }

        try {
            DatabaseConfig config = DatabaseConfig.fromEnvFile(DatabaseConfig.defaultEnvPath());
            ShipModulesRepository repository = new JdbcShipModulesRepository(config);
            repository.upsertModuleDesign(result.normalizedModuleData);
            previewArea.setText(jsonService.toPrettyJson(result.normalizedModuleData));
            setStatus("Saved to DB: ShipModules.ModuleID=" + result.normalizedModuleData.get("ModuleID"));
        } catch (Exception ex) {
            showError("Save to DB failed", ex.getMessage());
            setStatus("Save to DB failed");
        }
    }

    private void buildChassisGraphFromForm() {
        if (chassisGraphViewerPane == null) {
            return;
        }
        try {
            ChassisCalculationInput input = buildChassisGraphInputFromForm();
            StructuralGraphBuildResult graphResult = chassisGraphBuilder.build(input);
            StructuralGraphLoadSet loadSet = chassisGraphLoadPlanner.plan(input, graphResult.getGraph());
            StructuralGraphSolveResult solveResult = chassisGraphSolver.solve(input, graphResult.getGraph(), loadSet);

            StringBuilder warnings = new StringBuilder();
            if (typeComboBox != null && typeComboBox.getValue() != null && typeComboBox.getValue().intValue() != 0) {
                warnings.append("Note: Module Type is not 0 (Chassis). Visualization is still generated from current form values.\\n");
            }
            if (!graphResult.getWarnings().isEmpty()) {
                warnings.append("Graph warnings:\\n");
                for (String w : graphResult.getWarnings()) {
                    warnings.append("- ").append(w).append('\n');
                }
            }
            warnings.append("Graph intrusion (coarse): intrusiveLen=")
                    .append(String.format(java.util.Locale.US, "%.2f", graphResult.getIntrusiveMemberLengthMeters()))
                    .append(" m / totalLen=")
                    .append(String.format(java.util.Locale.US, "%.2f", graphResult.getTotalMemberLengthMeters()))
                    .append(" m (frac=")
                    .append(String.format(java.util.Locale.US, "%.3f", graphResult.getIntrusiveMemberLengthFraction()))
                    .append("), volProxy=")
                    .append(String.format(java.util.Locale.US, "%.2f", graphResult.getEstimatedInternalStructureIntrusionVolumeM3()))
                    .append(" m3\n");
            if (!loadSet.getWarnings().isEmpty()) {
                warnings.append("Load warnings:\\n");
                for (String w : loadSet.getWarnings()) {
                    warnings.append("- ").append(w).append('\n');
                }
            }
            if (!solveResult.getWarnings().isEmpty()) {
                warnings.append("Solver warnings:\\n");
                for (String w : solveResult.getWarnings()) {
                    warnings.append("- ").append(w).append('\n');
                }
            }
            chassisGraphViewerPane.setData(graphResult.getGraph(), loadSet, solveResult, warnings.toString().trim());
            setStatus("Chassis graph built from form");
        } catch (Exception ex) {
            chassisGraphViewerPane.clear("Build failed: " + ex.getMessage());
            setStatus("Chassis graph build failed");
            showError("Chassis graph build failed", ex.getMessage());
        }
    }

    private ChassisCalculationInput buildChassisGraphInputFromForm() throws Exception {
        if (sizeTypeComboBox == null || sizeTypeComboBox.getValue() == null) {
            throw new IllegalArgumentException("SizeType is required");
        }

        long sizeTotal = parseOptionalLongField("SizeTotal", 0L);
        CubeDimensions cubeDimensions = parseSizeDimensionsJson();
        String structureProfileCode = getStructureProfileCodeValue();
        Integer enginePlacementValue = getEnginePlacementProfileClassValue();
        int enginePlacementProfileClass = enginePlacementValue == null ? 0 : enginePlacementValue.intValue();
        if (enginePlacementProfileClass < 0) {
            enginePlacementProfileClass = 0;
        }

        Long baseMaterialId = getChassisIdValue("chassisBaseMaterialId");
        Long structureTypeId = getChassisIdValue("chassisStructureTypeId");
        Long manufacturingProcessId = getChassisIdValue("chassisManufacturingProcessId");
        Long assemblyProcessId = getChassisIdValue("chassisAssemblyProcessId");
        Long qualityProfileId = getChassisIdValue("chassisQualityProfileId");
        Long environmentProfileId = getChassisIdValue("chassisEnvironmentProfileId");
        EngineeringStackSelection stack = new EngineeringStackSelection(
                baseMaterialId == null ? Long.valueOf(1L) : baseMaterialId,
                structureTypeId == null ? Long.valueOf(1L) : structureTypeId,
                manufacturingProcessId == null ? Long.valueOf(1L) : manufacturingProcessId,
                assemblyProcessId == null ? Long.valueOf(1L) : assemblyProcessId,
                qualityProfileId == null ? Long.valueOf(1L) : qualityProfileId,
                environmentProfileId == null ? Long.valueOf(1L) : environmentProfileId
        );

        StructuralDesignBasis basis = new StructuralDesignBasis(
                "ModuleDesigner chassis draft",
                true,
                false,
                false,
                enginePlacementProfileClass >= 3 && enginePlacementProfileClass <= 4,
                10000,
                null,
                enginePlacementProfileClass
        );

        return new ChassisCalculationInput(
                sizeTypeComboBox.getValue().intValue(),
                sizeTotal,
                cubeDimensions,
                stack,
                new AccelerationEnvelope(0.2d, 0.05d, 0.05d),
                basis,
                null,
                null,
                null,
                structureProfileCode,
                MissionPriority.BALANCED,
                SafetyPriority.BALANCED
        );
    }

    private long parseOptionalLongField(String key, long defaultValue) {
        TextField field = textFields.get(key);
        if (field == null || isBlank(field.getText())) {
            return defaultValue;
        }
        return Long.parseLong(normalizeIntegerText(field.getText()));
    }

    private Long parseOptionalLongBoxed(String key) {
        TextField field = textFields.get(key);
        if (field == null || isBlank(field.getText())) {
            return null;
        }
        return Long.valueOf(Long.parseLong(normalizeIntegerText(field.getText())));
    }

    private CubeDimensions parseSizeDimensionsJson() throws Exception {
        boolean hasX = !isBlank(getText("SizeDimX"));
        boolean hasY = !isBlank(getText("SizeDimY"));
        boolean hasZ = !isBlank(getText("SizeDimZ"));
        if (hasX || hasY || hasZ) {
            if (!(hasX && hasY && hasZ)) {
                throw new IllegalArgumentException("SizeDim X/Y/Z must all be set");
            }
            int x = toPositiveInt(Long.valueOf(parseOptionalLongField("SizeDimX", 0L)), "SizeDimX");
            int y = toPositiveInt(Long.valueOf(parseOptionalLongField("SizeDimY", 0L)), "SizeDimY");
            int z = toPositiveInt(Long.valueOf(parseOptionalLongField("SizeDimZ", 0L)), "SizeDimZ");
            return new CubeDimensions(x, y, z);
        }
        TextArea area = jsonFields.get("SizeDimentions");
        if (area == null || isBlank(area.getText())) {
            return new CubeDimensions(1, 1, 1);
        }
        Object parsed = jsonService.parseJsonValue(area.getText());
        if (!(parsed instanceof Map)) {
            throw new IllegalArgumentException("SizeDimentions must be JSON object like {\"x\":10,\"y\":4,\"z\":3}");
        }
        Map<?, ?> map = (Map<?, ?>) parsed;
        int x = toPositiveInt(map.get("x"), "SizeDimentions.x");
        int y = toPositiveInt(map.get("y"), "SizeDimentions.y");
        int z = toPositiveInt(map.get("z"), "SizeDimentions.z");
        return new CubeDimensions(x, y, z);
    }

    private int toPositiveInt(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Missing " + fieldName);
        }
        int v;
        if (value instanceof Number) {
            v = ((Number) value).intValue();
        } else {
            v = Integer.parseInt(String.valueOf(value));
        }
        if (v <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0");
        }
        return v;
    }

    private File ensureDefaultDataDir() {
        Path dir = Paths.get(System.getProperty("user.dir"), "data", "modules");
        File file = dir.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.exists() && file.isDirectory() ? file : new File(System.getProperty("user.dir"));
    }

    private String suggestFileName() {
        String modName = safeFilePart(textFields.get("ModName").getText());
        String name = safeFilePart(textFields.get("Name").getText());
        if (!isBlank(modName)) {
            return modName + ".json";
        }
        if (!isBlank(name)) {
            return name + ".json";
        }
        return "module-design.json";
    }

    private String safeFilePart(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("[^a-zA-Z0-9._-]+", "_");
    }

    private void applyLoadedData(Map<String, Object> data) {
        clearForm();

        for (Map.Entry<String, TextField> entry : textFields.entrySet()) {
            Object value = data.get(entry.getKey());
            if (value != null) {
                entry.getValue().setText(String.valueOf(value));
            }
        }

        Object typeValue = data.get("Type");
        if (typeValue instanceof Number) {
            typeComboBox.setValue(Integer.valueOf(((Number) typeValue).intValue()));
        } else if (typeValue != null) {
            try {
                typeComboBox.setValue(Integer.valueOf(Integer.parseInt(String.valueOf(typeValue))));
            } catch (NumberFormatException ignored) {
                typeComboBox.setValue(null);
            }
        }

        Object sizeTypeValue = data.get("SizeType");
        if (sizeTypeValue instanceof Number) {
            sizeTypeComboBox.setValue(Integer.valueOf(((Number) sizeTypeValue).intValue()));
        } else if (sizeTypeValue != null) {
            try {
                sizeTypeComboBox.setValue(Integer.valueOf(Integer.parseInt(String.valueOf(sizeTypeValue))));
            } catch (NumberFormatException ignored) {
                sizeTypeComboBox.setValue(null);
            }
        }

        Object enginePlacementProfileValue = data.get("enginePlacementProfileClass");
        if (enginePlacementProfileComboBox != null) {
            if (enginePlacementProfileValue instanceof Number) {
                enginePlacementProfileComboBox.setValue(Integer.valueOf(((Number) enginePlacementProfileValue).intValue()));
            } else if (enginePlacementProfileValue != null) {
                try {
                    enginePlacementProfileComboBox.setValue(Integer.valueOf(Integer.parseInt(String.valueOf(enginePlacementProfileValue))));
                } catch (NumberFormatException ignored) {
                    enginePlacementProfileComboBox.setValue(null);
                }
            }
        }

        applyChassisIdComboValue("chassisBaseMaterialId", data.get("chassisBaseMaterialId"));
        applyChassisIdComboValue("chassisStructureTypeId", data.get("chassisStructureTypeId"));
        applyChassisIdComboValue("chassisManufacturingProcessId", data.get("chassisManufacturingProcessId"));
        applyChassisIdComboValue("chassisAssemblyProcessId", data.get("chassisAssemblyProcessId"));
        applyChassisIdComboValue("chassisQualityProfileId", data.get("chassisQualityProfileId"));
        applyChassisIdComboValue("chassisEnvironmentProfileId", data.get("chassisEnvironmentProfileId"));

        Object structureProfileCodeValue = data.get("structureProfileCode");
        if (structureProfileCodeComboBox != null) {
            if (structureProfileCodeValue == null) {
                structureProfileCodeComboBox.setValue(null);
                if (structureProfileCodeComboBox.getEditor() != null) {
                    structureProfileCodeComboBox.getEditor().clear();
                }
            } else {
                String code = String.valueOf(structureProfileCodeValue).trim();
                if (code.isEmpty()) {
                    structureProfileCodeComboBox.setValue(null);
                    if (structureProfileCodeComboBox.getEditor() != null) {
                        structureProfileCodeComboBox.getEditor().clear();
                    }
                } else {
                    structureProfileCodeComboBox.setValue(code);
                    if (structureProfileCodeComboBox.getEditor() != null) {
                        structureProfileCodeComboBox.getEditor().setText(code);
                    }
                }
            }
        }

        Object radioactive = data.get("FuelRadioactiveMaterial");
        if (radioactive instanceof Boolean) {
            checkBoxes.get("FuelRadioactiveMaterial").setSelected(((Boolean) radioactive).booleanValue());
        } else if (radioactive != null) {
            checkBoxes.get("FuelRadioactiveMaterial").setSelected(Boolean.parseBoolean(String.valueOf(radioactive)));
        }

        for (Map.Entry<String, TextArea> entry : jsonFields.entrySet()) {
            Object value = data.get(entry.getKey());
            if (value == null) {
                continue;
            }
            try {
                entry.getValue().setText(jsonService.toPrettyJsonValue(value));
            } catch (IOException e) {
                entry.getValue().setText(String.valueOf(value));
            }
        }
        populateSizeDimFieldsFromJsonArea();

        Object localDesigner = data.get("_localDesigner");
        if (localDesigner instanceof Map) {
            Map<?, ?> localMap = (Map<?, ?>) localDesigner;
            applyLocalNumber("StorageCargoM3", localMap.get("StorageCargoM3"));
            applyLocalNumber("StorageAmmoM3", localMap.get("StorageAmmoM3"));
            applyLocalNumber("StorageConsumablesM3", localMap.get("StorageConsumablesM3"));
            applyLocalNumber("StorageAtmosphereM3", localMap.get("StorageAtmosphereM3"));
            applyLocalNumber("InfrastructureVolumeM3", localMap.get("InfrastructureVolumeM3"));
        }
    }

    private void applyLocalNumber(String key, Object value) {
        TextField field = textFields.get(key);
        if (field != null && value != null) {
            field.setText(String.valueOf(value));
        }
    }

    private void clearForm() {
        for (TextField field : textFields.values()) {
            field.clear();
        }
        for (TextArea area : jsonFields.values()) {
            area.clear();
        }
        for (CheckBox box : checkBoxes.values()) {
            box.setSelected(false);
        }
        if (typeComboBox != null) {
            typeComboBox.setValue(null);
        }
        if (sizeTypeComboBox != null) {
            sizeTypeComboBox.setValue(null);
        }
        if (enginePlacementProfileComboBox != null) {
            enginePlacementProfileComboBox.setValue(null);
        }
        for (ComboBox<Long> combo : chassisIdComboBoxes.values()) {
            combo.setValue(null);
        }
        if (structureProfileCodeComboBox != null) {
            structureProfileCodeComboBox.setValue(null);
            if (structureProfileCodeComboBox.getEditor() != null) {
                structureProfileCodeComboBox.getEditor().clear();
            }
        }
        if (previewArea != null) {
            previewArea.clear();
        }
        if (messagesArea != null) {
            messagesArea.clear();
        }
    }

    private void applyDefaults() {
        if (textFields.containsKey("Reliability")) {
            textFields.get("Reliability").setText("90");
        }
        if (sizeTypeComboBox != null && sizeTypeComboBox.getValue() == null) {
            sizeTypeComboBox.setValue(Integer.valueOf(2));
        }
        if (enginePlacementProfileComboBox != null && enginePlacementProfileComboBox.getValue() == null) {
            enginePlacementProfileComboBox.setValue(Integer.valueOf(0));
        }
        setIfPresent("SizeTotal", "0");
        setIfPresent("SizeDimX", "0");
        setIfPresent("SizeDimY", "0");
        setIfPresent("SizeDimZ", "0");
        setChassisIdDefault("chassisBaseMaterialId", 1L);
        setChassisIdDefault("chassisStructureTypeId", 1L);
        setChassisIdDefault("chassisManufacturingProcessId", 1L);
        setChassisIdDefault("chassisAssemblyProcessId", 1L);
        setChassisIdDefault("chassisQualityProfileId", 1L);
        setChassisIdDefault("chassisEnvironmentProfileId", 1L);
        setIfPresent("DryWeight", "0");
        setIfPresent("FullWeight", "0");
        setIfPresent("CargoVolumeMax", "0");
        setIfPresent("CargoType", "0");
        setIfPresent("ManufactureTimeSec", "0");
        setIfPresent("EnergyConsMax", "0");
        setIfPresent("EnergyConsPowerUp", "0");
        setIfPresent("EnergyConsStandBy", "0");
        setIfPresent("EnergyConsOn", "0");
        setIfPresent("EnergyProdMin", "0");
        setIfPresent("EnergyProdMax", "0");
        setIfPresent("EnergyProdNominal", "0");
        setIfPresent("EnergyProdCritical", "0");
        setIfPresent("FuelConsumption", "0");
        setIfPresent("FuelQantity", "0");
        setIfPresent("DegradationSpeedPerYear", "0");
        setIfPresent("ControlLinesAmount", "0");
        setIfPresent("TermalLinesAmount", "0");
        setIfPresent("FuelLinesAmount", "0");
        setIfPresent("PowerLinesAmount", "0");
        setIfPresent("DockType", "0");
        setIfPresent("InternalDefence", "0");
        setIfPresent("MeshID", "0");
        setIfPresent("StorageCargoM3", "0");
        setIfPresent("StorageAmmoM3", "0");
        setIfPresent("StorageConsumablesM3", "0");
        setIfPresent("StorageAtmosphereM3", "0");
        setIfPresent("InfrastructureVolumeM3", "0");

        setJsonDefault("SpecificModParamCrew", "{}");
        setJsonDefault("SpecificModParamEmissions", "{}");
        setJsonDefault("SpecificModParamWeapon", "{}");
        setJsonDefault("SpecificModParamTrust", "{}");
        setJsonDefault("Armored", "{}");
        setJsonDefault("SizeDimentions", "{\"x\":0,\"y\":0,\"z\":0}");
        setJsonDefault("Blocks", "[]");
    }

    private void assignNextModuleIdFromDatabase() {
        try {
            ShipModulesRepository repository = new JdbcShipModulesRepository(
                    DatabaseConfig.fromEnvFile(DatabaseConfig.defaultEnvPath()));
            long nextId = repository.findNextFreeModuleId();
            TextField moduleIdField = textFields.get("ModuleID");
            if (moduleIdField != null) {
                moduleIdField.setText(String.valueOf(nextId));
            }
            setStatus("New module form initialized, next ModuleID from DB: " + nextId);
        } catch (Exception ex) {
            setStatus("New form initialized (DB ModuleID unavailable: " + ex.getMessage() + ")");
            if (textFields.containsKey("ModuleID")) {
                textFields.get("ModuleID").clear();
            }
        }
    }

    private void setIfPresent(String key, String value) {
        TextField field = textFields.get(key);
        if (field != null && isBlank(field.getText())) {
            field.setText(value);
        }
    }

    private String getStructureProfileCodeValue() {
        if (structureProfileCodeComboBox == null) {
            return null;
        }
        String editorText = structureProfileCodeComboBox.getEditor() == null
                ? null
                : structureProfileCodeComboBox.getEditor().getText();
        String value = !isBlank(editorText) ? editorText : structureProfileCodeComboBox.getValue();
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private Integer getEnginePlacementProfileClassValue() {
        return enginePlacementProfileComboBox == null ? null : enginePlacementProfileComboBox.getValue();
    }

    private Long getChassisIdValue(String key) {
        ComboBox<Long> combo = chassisIdComboBoxes.get(key);
        return combo == null ? null : combo.getValue();
    }

    private void putChassisId(Map<String, Object> target, String key) {
        Long value = getChassisIdValue(key);
        target.put(key, value);
    }

    private void applyChassisIdComboValue(String key, Object value) {
        ComboBox<Long> combo = chassisIdComboBoxes.get(key);
        if (combo == null) {
            return;
        }
        if (value instanceof Number) {
            combo.setValue(Long.valueOf(((Number) value).longValue()));
            return;
        }
        if (value != null) {
            try {
                combo.setValue(Long.valueOf(Long.parseLong(String.valueOf(value))));
            } catch (NumberFormatException ignored) {
                combo.setValue(null);
            }
        }
    }

    private void setChassisIdDefault(String key, long value) {
        ComboBox<Long> combo = chassisIdComboBoxes.get(key);
        if (combo != null && combo.getValue() == null) {
            combo.setValue(Long.valueOf(value));
        }
    }

    private void putSizeDimensionsJson(Map<String, Object> target) {
        String x = getText("SizeDimX");
        String y = getText("SizeDimY");
        String z = getText("SizeDimZ");
        if (!isBlank(x) && !isBlank(y) && !isBlank(z)) {
            target.put("SizeDimentions", "{\"x\":" + normalizeIntegerText(x)
                    + ",\"y\":" + normalizeIntegerText(y)
                    + ",\"z\":" + normalizeIntegerText(z) + "}");
            return;
        }
        putRawJsonText(target, "SizeDimentions");
    }

    private void populateSizeDimFieldsFromJsonArea() {
        TextArea area = jsonFields.get("SizeDimentions");
        if (area == null || isBlank(area.getText())) {
            return;
        }
        try {
            Object parsed = jsonService.parseJsonValue(area.getText());
            if (!(parsed instanceof Map)) {
                return;
            }
            Map<?, ?> map = (Map<?, ?>) parsed;
            setFieldFromObject("SizeDimX", map.get("x"));
            setFieldFromObject("SizeDimY", map.get("y"));
            setFieldFromObject("SizeDimZ", map.get("z"));
        } catch (Exception ignored) {
            // leave manual JSON as-is
        }
    }

    private void setFieldFromObject(String key, Object value) {
        TextField field = textFields.get(key);
        if (field != null && value != null) {
            field.setText(String.valueOf(value));
        }
    }

    private String getText(String key) {
        TextField field = textFields.get(key);
        return field == null ? null : field.getText();
    }

    private void setJsonDefault(String key, String value) {
        TextArea area = jsonFields.get(key);
        if (area != null && isBlank(area.getText())) {
            area.setText(value);
        }
    }

    private void duplicateCurrent() {
        if (textFields.containsKey("ModuleID")) {
            textFields.get("ModuleID").clear();
        }
        if (textFields.containsKey("ModName")) {
            textFields.get("ModName").setText(withCopySuffix(textFields.get("ModName").getText()));
        }
        if (textFields.containsKey("Name")) {
            textFields.get("Name").setText(withCopySuffix(textFields.get("Name").getText()));
        }
        setStatus("Current module duplicated in form (ModuleID cleared)");
    }

    private String withCopySuffix(String value) {
        String base = value == null ? "" : value.trim();
        return base.isEmpty() ? "Copy" : base + " Copy";
    }

    private void parseLongField(String key, Map<String, Long> target, List<String> errors) {
        TextField field = textFields.get(key);
        if (field == null) {
            errors.add("Internal UI error: missing field " + key);
            return;
        }
        if (isBlank(field.getText())) {
            return;
        }
        try {
            long value = Long.parseLong(normalizeIntegerText(field.getText()));
            if (value < 0L) {
                errors.add(key + " must be >= 0");
                return;
            }
            target.put(key, Long.valueOf(value));
        } catch (NumberFormatException ex) {
            errors.add("Invalid integer value in " + key);
        }
    }

    private void parseDoubleField(String key, Map<String, Double> target, List<String> errors) {
        TextField field = textFields.get(key);
        if (field == null) {
            errors.add("Internal UI error: missing field " + key);
            return;
        }
        if (isBlank(field.getText())) {
            return;
        }
        try {
            double value = Double.parseDouble(normalizeNumberText(field.getText()));
            if (value < 0.0d) {
                errors.add(key + " must be >= 0");
                return;
            }
            target.put(key, Double.valueOf(value));
        } catch (NumberFormatException ex) {
            errors.add("Invalid decimal value in " + key);
        }
    }

    private void putRawText(Map<String, Object> target, String key) {
        TextField field = textFields.get(key);
        target.put(key, field == null ? null : field.getText());
    }

    private void putRawNumberOrText(Map<String, Object> target, String key) {
        TextField field = textFields.get(key);
        if (field == null) {
            target.put(key, null);
            return;
        }
        String value = field.getText();
        if (isBlank(value)) {
            target.put(key, null);
            return;
        }

        String normalized = normalizeNumberText(value);
        try {
            if (normalized.contains(".")) {
                target.put(key, Double.valueOf(Double.parseDouble(normalized)));
            } else {
                target.put(key, Long.valueOf(Long.parseLong(normalized)));
            }
        } catch (NumberFormatException ex) {
            target.put(key, value);
        }
    }

    private void putRawJsonText(Map<String, Object> target, String key) {
        TextArea area = jsonFields.get(key);
        target.put(key, area == null ? null : area.getText());
    }

    private String required(String value, String fieldName) {
        if (isBlank(value)) {
            throw new IllegalArgumentException("Field is required: " + fieldName);
        }
        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String normalizeIntegerText(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeNumberText(String value) {
        return value == null ? "" : value.trim().replace(',', '.');
    }

    private String joinLines(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i));
            if (i + 1 < lines.size()) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static final class ValidationResult {
        private final boolean valid;
        private final List<String> messages;
        private final Map<String, Object> normalizedModuleData;

        private ValidationResult(boolean valid, List<String> messages, Map<String, Object> normalizedModuleData) {
            this.valid = valid;
            this.messages = messages;
            this.normalizedModuleData = normalizedModuleData;
        }

        private static ValidationResult invalid(List<String> messages) {
            return new ValidationResult(false, messages, null);
        }

        private static ValidationResult valid(List<String> messages, Map<String, Object> normalizedModuleData) {
            return new ValidationResult(true, messages, normalizedModuleData);
        }
    }
}
