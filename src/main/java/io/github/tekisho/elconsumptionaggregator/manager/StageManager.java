package io.github.tekisho.elconsumptionaggregator.manager;

import io.github.tekisho.elconsumptionaggregator.config.ViewConfig;
import io.github.tekisho.elconsumptionaggregator.factory.StageFactory;
import io.github.tekisho.elconsumptionaggregator.util.WindowUtils;
import javafx.stage.Stage;

import java.util.EnumMap;
import java.util.Map;


public final class StageManager {
    private static final StageManager instance = new StageManager();

    private final Map<WindowType, Stage> stages;
    private Stage primaryStage;

    private StageManager() {
        this.stages = new EnumMap<>(WindowType.class);
    }

    public static StageManager getInstance() {
        return instance;
    }

    public enum WindowType {
        MAIN(ViewConfig.View.MAIN, ViewConfig.ResourcePath.APP_ICON, ViewConfig.WindowTitle.MAIN, false),
        ABOUT(ViewConfig.View.ABOUT, ViewConfig.ResourcePath.HELP_ICON, ViewConfig.WindowTitle.ABOUT, false);

        private final ViewConfig.View view;
        private final ViewConfig.ResourcePath icon;
        private final ViewConfig.WindowTitle title;
        private final boolean isResizeable;

        WindowType(ViewConfig.View view, ViewConfig.ResourcePath icon, ViewConfig.WindowTitle title, boolean isResizeable) {
            this.view = view;
            this.icon = icon;
            this.title = title;
            this.isResizeable = isResizeable;
        }

        public ViewConfig.View getView() {
            return view;
        }

        public ViewConfig.ResourcePath getIcon() {
            return icon;
        }

        public String getTitle() {
            return title.getTitle();
        }

        public boolean isResizeable() {
            return isResizeable;
        }
    }

    public void initializePrimaryStage(Stage primaryStage) {
        if (this.primaryStage != null) {
            throw new IllegalStateException("Primary stage has already been initialized");
        }
        this.primaryStage = primaryStage;
        configurePrimaryStageEventHandlers();
    }

    public void createOrShowExistingStage(WindowType type) {
        if (type == WindowType.MAIN) {
            if (primaryStage == null) {
                throw new IllegalStateException("Primary stage must be initialized before showing main window");
            }
            StageFactory.getInstance().configureStage(WindowType.MAIN, primaryStage);
            registerStage(type, primaryStage);
            return;
        }

        Stage existingStage = stages.get(type);
        if (existingStage != null) {
            if (type == WindowType.ABOUT) {
                WindowUtils.setSecondaryStageCoordinates(type, WindowUtils.Side.RIGHT);
            }
            existingStage.show();
            existingStage.toFront();
            return;
        }

        Stage newStage = StageFactory.getInstance().createStage(type);
        registerStage(type, newStage);

        if (type == WindowType.ABOUT) {
            WindowUtils.setSecondaryStageCoordinates(type, WindowUtils.Side.RIGHT);
        }
    }

    private void registerStage(WindowType type, Stage stage) {
        stages.put(type, stage);
    }

    public Stage getPrimaryStage () {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage is not initialized");
        }
        return primaryStage;
    }

    public Stage getStage(WindowType type) {
        return stages.get(type);
    }

    private void configurePrimaryStageEventHandlers() {
        primaryStage.iconifiedProperty().addListener((observable, oldValue, isIconified) ->
            stages.values().stream()
                    .filter(stage -> stage != primaryStage)
                    .forEach(stage -> stage.setIconified(isIconified))
        );
    }

    public void setCoordinates(WindowType stageKey, double xPosition, double yPosition) {
        Stage stage = getStage(stageKey);
        stage.setX(xPosition);
        stage.setY(yPosition);
    }
}