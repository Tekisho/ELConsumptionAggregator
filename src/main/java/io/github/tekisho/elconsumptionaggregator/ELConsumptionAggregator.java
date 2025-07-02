package io.github.tekisho.elconsumptionaggregator;

import io.github.tekisho.elconsumptionaggregator.manager.StageManager;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


// https://stackoverflow.com/questions/45905053/how-can-i-prevent-a-window-from-being-too-small-in-javafx
public class ELConsumptionAggregator extends javafx.application.Application {
    private static final Logger logger = LoggerFactory.getLogger(ELConsumptionAggregator.class);

    @Override
    public void init() {
        Path logDir = Path.of("logs/archived");
        if (Files.notExists(logDir)) {
            try {
                Files.createDirectories(logDir);
            } catch (IOException ioException) {
                logger.error("Failed to create log directories", ioException);
            }
            logger.debug("Log directories created at {}", logDir);
        } else {
            logger.debug("Log directories already exist at {}", logDir);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        StageManager.getInstance().initializePrimaryStage(primaryStage);
        StageManager.getInstance().createOrShowExistingStage(StageManager.WindowType.MAIN);
    }


    @Override
    public void stop() {
        // ...
    }

    public static void main(String[] args) {
        launch();
    }
}