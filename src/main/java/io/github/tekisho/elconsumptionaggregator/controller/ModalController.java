package io.github.tekisho.elconsumptionaggregator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ModalController {
    private  static final Logger logger = LoggerFactory.getLogger(ModalController.class);

    private static final String PROPERTIES_FILE = "/app.properties";

    private static final String KEY_DESCRIPTION = "application.description";
    private static final String KEY_VERSION = "application.version";
    private static final String KEY_AUTHOR = "application.author";

    private static final String AUTHOR_PREFIX = "Author: ";
    private static final String VERSION_PREFIX = "Version: ";
    private static final String DESCRIPTION_PREFIX = "Description: ";
    private static final String DEV_VALUE = "DEV";

    private final Properties properties = new Properties();

    @FXML private Label descriptionLabel;
    @FXML private Label versionLabel;
    @FXML private Label authorLabel;

    @FXML
    public void initialize() {
        try {
            loadProperties();
            syncViewWithProperties();
        } catch (Exception e) {
            logger.error("Failed to initialize modal controller: {}", e.getMessage(), e);
        }
    }

    private void loadProperties() throws IOException {
        try (InputStream inputStream = this.getClass().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Unable to load properties file: " + PROPERTIES_FILE);
            }
        }
    }

    private void syncViewWithProperties() {
        descriptionLabel.setText(DESCRIPTION_PREFIX + properties.getProperty(KEY_DESCRIPTION, DEV_VALUE));
        versionLabel.setText(VERSION_PREFIX + properties.getProperty(KEY_VERSION, DEV_VALUE));
        authorLabel.setText(AUTHOR_PREFIX + properties.getProperty(KEY_AUTHOR, DEV_VALUE));
    }
}
