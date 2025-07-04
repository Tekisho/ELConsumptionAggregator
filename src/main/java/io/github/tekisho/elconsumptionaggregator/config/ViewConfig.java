package io.github.tekisho.elconsumptionaggregator.config;


public final class ViewConfig {
    private ViewConfig() {}

    public enum View {
        MAIN("view/main-view.fxml"),
        ABOUT("view/about-view.fxml"),;

        private final String path;

        View(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public enum WindowTitle {
        MAIN("ELConsumptionAggregator"),
        ABOUT("About Application");

        private final String title;

        WindowTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum ResourcePath {
        STYLES("styles/main-style.css"),
        APP_ICON("images/internal-app-icon.png"),
        HELP_ICON("images/about-icon.png");

        private final String path;

        ResourcePath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
