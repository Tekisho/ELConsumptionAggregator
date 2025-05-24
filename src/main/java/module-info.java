module io.github.tekisho.elconsumptionaggregator {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.tekisho.elconsumptionaggregator to javafx.fxml;
    exports io.github.tekisho.elconsumptionaggregator;
}