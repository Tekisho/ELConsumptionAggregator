package io.github.tekisho.elconsumptionaggregator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

// https://stackoverflow.com/questions/45905053/how-can-i-prevent-a-window-from-being-too-small-in-javafx
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(Application.class.getResource("style.css")).toExternalForm());
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Application.class.getResource("icon.png")).toExternalForm()));
        primaryStage.setTitle("ELConsumptionAggregator");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    public static void main(String[] args) {
        launch();
    }
}