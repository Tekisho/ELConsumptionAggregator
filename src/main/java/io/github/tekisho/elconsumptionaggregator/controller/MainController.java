package io.github.tekisho.elconsumptionaggregator.controller;

import io.github.tekisho.elconsumptionaggregator.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;

public class MainController {
    @FXML private Button selectBtn;
    @FXML private Button saveBtn;
    @FXML private Button helpBtn;
    @FXML private Label statusLabel;
    @FXML private TextArea detailsArea;

    FileChooser fileChooser = new FileChooser();
    private final ObjectProperty<File> selectedFile = new SimpleObjectProperty<>();

    @FXML private void initialize() {
        selectedFile.addListener((observable, oldValue, newValue) -> {
           if (newValue != null) {
               statusLabel.setText(newValue.getName());
               saveBtn.setDisable(false);
           }
        });
    }

    @FXML
    private void handleSelect() {
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File file = fileChooser.showOpenDialog(selectBtn.getScene().getWindow());

        if (file != null) {
            selectedFile.setValue(file);
        }
    }

    @FXML
    private void handleSave() {
        fileChooser.setTitle("Save Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files", "*.xls", "*.xlsx"));
        File file = fileChooser.showSaveDialog(selectBtn.getScene().getWindow());
    }

    @FXML
    private void handleHelp() {
        Application.getInstance().showModalStage();
    }


}