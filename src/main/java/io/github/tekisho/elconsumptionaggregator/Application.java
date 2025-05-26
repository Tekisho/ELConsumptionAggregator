package io.github.tekisho.elconsumptionaggregator;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

// https://stackoverflow.com/questions/45905053/how-can-i-prevent-a-window-from-being-too-small-in-javafx
public class Application extends javafx.application.Application {
    private static Application instance;

    private static final String FXML_PATH = "view/main-view.fxml";
    private static final String MODAL_FXML_PATH = "view/modal-view.fxml";
    private static final String CSS_PATH = "styles/main-style.css";
    private static final String APP_ICON_PATH = "images/app-icon.png";
    private static final String HELP_ICON_PATH = "images/help-icon.png";
    private static final String APP_TITLE = "ELConsumptionAggregator";
    private static final String MODAL_TITLE = "About Application";

    private Stage primaryStage;
    private Stage modalStage;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.primaryStage = primaryStage;
        launchMainWindow();
    }

    public static Application getInstance() {
        return instance;
    }

    private void launchMainWindow() {
        try {
            FXMLLoader fxmlLoader = loadFXML(FXML_PATH);
            Parent root = fxmlLoader.load();

            Scene scene = createScene(root);

            configurePrimaryStage(scene);
        } catch (IOException e) {
            // ...
            Platform.exit();
        }
    }

    private FXMLLoader loadFXML(String fxml) {
        return new FXMLLoader(this.getClass().getResource(fxml));
    }

    private Scene createScene(Parent root) {
        Scene scene = new Scene(root);
        addStyleSheet(scene, CSS_PATH);
        return scene;
    }

    private void addStyleSheet(Scene scene, String styleSheet) {
        String cssPath = Objects.requireNonNull(this.getClass().getResource(styleSheet)).toExternalForm();
        scene.getStylesheets().add(cssPath);
    }

    private void configurePrimaryStage(Scene scene) {
        String iconPath = Objects.requireNonNull(this.getClass().getResource(APP_ICON_PATH)).toExternalForm();
        primaryStage.getIcons().add(new Image(iconPath));

        primaryStage.setTitle(APP_TITLE);
        primaryStage.setScene(scene);

        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    public void showModalStage() {
        try {
            if (modalStage == null) {
                createModalWindow();
            }
            if (modalStage != null && modalStage.getScene() != null && !modalStage.isShowing()) {
                modalStage.showAndWait();
            } else {
                // logging
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createModalWindow() {
        try {
            FXMLLoader fxmlLoader = loadFXML(MODAL_FXML_PATH);
            Parent modalRoot = fxmlLoader.load();

            modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);
            String iconPath = Objects.requireNonNull(this.getClass().getResource(HELP_ICON_PATH)).toExternalForm();
            modalStage.getIcons().add(new Image(iconPath));
            modalStage.setTitle(MODAL_TITLE);

            Scene modelScene = createScene(modalRoot);

            addStyleSheet(modelScene, CSS_PATH);

            modalStage.setScene(modelScene);

            modalStage.show();
            modalStage.setMinWidth(modalStage.getWidth());
            modalStage.setMinHeight(modalStage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {
        if (modalStage != null) {
            modalStage.close();
        }
        // ...
    }

    public static void main(String[] args) {
        launch();
    }
}