package io.github.tekisho.elconsumptionaggregator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ModalController {
    private static final String PROPERTIES_FILE = "/app.properties";

    private static final String KEY_DESCRIPTION = "application.description";
    private static final String KEY_VERSION = "application.version";

    private static final String VERSION_PREFIX = "Version: ";
    private static final String DESCRIPTION_PREFIX = "Description: ";
    private static final String DEV_VALUE = "DEV";

    @FXML private Label descriptionLabel;
    @FXML private Label versionLabel;

    @FXML
    public void initialize() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = getClass().getResourceAsStream(PROPERTIES_FILE);
            if (input != null) {
                prop.load(input);
                descriptionLabel.setText(DESCRIPTION_PREFIX + prop.getProperty(KEY_DESCRIPTION));
                versionLabel.setText(VERSION_PREFIX + prop.getProperty(KEY_VERSION));
            } else {
                versionLabel.setText(VERSION_PREFIX + DEV_VALUE);
                descriptionLabel.setText(DESCRIPTION_PREFIX + DEV_VALUE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
