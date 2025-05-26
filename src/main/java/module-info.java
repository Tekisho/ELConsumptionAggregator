module io.github.tekisho.elconsumptionaggregator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires java.desktop;


    opens io.github.tekisho.elconsumptionaggregator to javafx.fxml;
    exports io.github.tekisho.elconsumptionaggregator;
    exports io.github.tekisho.elconsumptionaggregator.controller;
    opens io.github.tekisho.elconsumptionaggregator.controller to javafx.fxml;
}