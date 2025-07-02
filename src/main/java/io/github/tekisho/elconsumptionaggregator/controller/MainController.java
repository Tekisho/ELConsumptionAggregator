package io.github.tekisho.elconsumptionaggregator.controller;

import io.github.tekisho.elconsumptionaggregator.manager.StageManager;

import io.github.tekisho.elconsumptionaggregator.model.ConsumptionDay;
import io.github.tekisho.elconsumptionaggregator.service.GroupedAverageInsertionService;
import io.github.tekisho.elconsumptionaggregator.service.ExportService;
import io.github.tekisho.elconsumptionaggregator.service.ImportService;
import io.github.tekisho.elconsumptionaggregator.util.TooltipUtils;
import io.github.tekisho.elconsumptionaggregator.util.WindowUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MainController {
    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @FXML private Button importFileButton;
    @FXML private Button exportFileButton;
    @FXML private Button aboutApplicationButton;
    @FXML private Label statusLabel;

    FileChooser fileChooser = new FileChooser();
    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();

    ImportService importService = new ImportService();
    GroupedAverageInsertionService aggregationService = new GroupedAverageInsertionService();
    ExportService exportService = new ExportService();

    List<ConsumptionDay> loadedConsumptionDays = new ArrayList<>();
    List<ConsumptionDay> processedConsumptionDays = new ArrayList<>();

    // TODO: refactor generaol css.
    @FXML private void initialize() {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));

        importFileButton.setOnAction(event -> handleSelect());
        exportFileButton.setOnAction(event -> handleSave());
        aboutApplicationButton.setOnAction(event -> handleAbout());

        statusLabel.textProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean isEmpty = newValue == null || newValue.trim().isEmpty();

            if (!isEmpty) {
                statusLabel.setVisible(true);
                statusLabel.setManaged(true);

                Stage stage = (Stage) statusLabel.getScene().getWindow();
                if (stage != null) {
                    Platform.runLater(stage::sizeToScene);
                }
            } else {
                statusLabel.setVisible(false);
                statusLabel.setManaged(false);
            }
        });

        TooltipUtils.bindTooltipIfClipped(statusLabel);
    }

    @FXML
    private void handleSelect() {
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(importFileButton.getScene().getWindow());

        if (file != null) {
            selectedFile.set(file);

            Task<Void> importTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    loadedConsumptionDays = importService.load(selectedFile.get());
                    return null;
                }
            };

            importTask.setOnRunning(event -> {
                importFileButton.setDisable(true);
                exportFileButton.setDisable(true);
                statusLabel.setText("Importing...");
            });
            importTask.setOnSucceeded(event -> {
                importFileButton.setDisable(false);
                exportFileButton.setDisable(false);
                processedConsumptionDays.clear();
                statusLabel.setText("Loaded: " + file.getName());
            });
            importTask.setOnFailed(event -> {
                selectedFile.set(null);
                loadedConsumptionDays.clear();

                importFileButton.setDisable(false);

                Throwable exception = importTask.getException();
                logger.error("Error importing excel file. {}", exception.getMessage(), exception);
                if (exception instanceof FileNotFoundException || exception.getMessage().contains("used by another process")) {
                    statusLabel.setText("Failed to load file. Please close the Excel file before importing.");
                } else {
                    statusLabel.setText("Failed to load file. Try again.");
                }
            });

            new Thread(importTask).start();
        }
    }

    @FXML
    private void handleSave() {
        fileChooser.setTitle("Save Resource File");
        File file = fileChooser.showSaveDialog(exportFileButton.getScene().getWindow());

        if (file != null) {
            if (loadedConsumptionDays == null || loadedConsumptionDays.isEmpty()) {
                logger.warn("Cannot export: no data loaded.");
                statusLabel.setText("No data to export");
                return;
            }

            Task<Void> exportTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    processedConsumptionDays = aggregationService.insertGroupedAverages(loadedConsumptionDays);
                    exportService.export(file.toPath(), selectedFile.get().toPath(), processedConsumptionDays);
                    return null;
                }
            };

            exportTask.setOnRunning(event -> {
                importFileButton.setDisable(true);
                exportFileButton.setDisable(true);

                statusLabel.setText("Exporting...");
            });
            exportTask.setOnSucceeded(event -> {
                importFileButton.setDisable(false);
                exportFileButton.setDisable(false);

                statusLabel.setText("Exported " + file.getName() + ", to " + file.getAbsolutePath());
            });
            exportTask.setOnFailed(event -> {
                importFileButton.setDisable(false);
                exportFileButton.setDisable(false);

                Throwable exception = exportTask.getException();
                logger.error("Error exporting excel file: {}", exception.getMessage(), exception);
            });

            new Thread(exportTask).start();
        }
    }

    @FXML
    private void handleAbout() {
        StageManager.getInstance().createOrShowExistingStage(StageManager.WindowType.ABOUT);
    }
}