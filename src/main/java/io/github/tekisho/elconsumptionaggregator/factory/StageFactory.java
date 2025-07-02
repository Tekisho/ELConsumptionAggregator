package io.github.tekisho.elconsumptionaggregator.factory;

import io.github.tekisho.elconsumptionaggregator.config.ViewConfig;
import io.github.tekisho.elconsumptionaggregator.manager.StageManager;
import io.github.tekisho.elconsumptionaggregator.util.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


public final class StageFactory {
    private static final StageFactory instance = new StageFactory();

    private StageFactory() {
    }

    public static StageFactory getInstance() {
        return instance;
    }

    public Stage createStage(StageManager.WindowType type) {
        Stage stage = new Stage();
        configureStage(type, stage);
        return stage;
    }

    public void configureStage(StageManager.WindowType type, Stage stage) {
        try {
            Parent root = createRoot(type);
            Scene scene = createScene(root);

            stage.setScene(scene);

            configureStageProperties(type, stage);

            stage.sizeToScene();

            if (type == StageManager.WindowType.MAIN) {
                stage.centerOnScreen();
            }

            stage.show();

            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());

            stage.setOpacity(0.96);
        } catch (IOException e) {
            throw new StageInitializationException("Failed to initialize stage: " + type, e);
        }
    }

    private Parent createRoot(StageManager.WindowType type) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourceLoader.loadFXML(type.getView().getPath()));
        return fxmlLoader.load();
    }

    private Scene createScene(Parent root) {
        Scene scene = new Scene(root);
        addStyleSheet(scene, ViewConfig.ResourcePath.STYLES.getPath());
        return scene;
    }

    private void configureStageProperties(StageManager.WindowType type, Stage stage) {
        stage.setTitle(type.getTitle());
        stage.getIcons().add(ResourceLoader.loadImage(type.getIcon().getPath()));
        stage.setResizable(type.isResizeable());

        if (type != StageManager.WindowType.MAIN) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(StageManager.getInstance().getPrimaryStage());
        }
    }

    private void addStyleSheet(Scene scene, String styleSheet) {
        scene.getStylesheets().add(ResourceLoader.loadStyleSheet(styleSheet));
    }

    public static class StageInitializationException extends RuntimeException {
        public StageInitializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
