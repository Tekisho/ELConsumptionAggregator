module io.github.tekisho.elconsumptionaggregator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires ch.qos.logback.classic;
    requires org.slf4j;
    requires org.apache.commons.compress;
    requires java.desktop;


    opens io.github.tekisho.elconsumptionaggregator to javafx.fxml;
    exports io.github.tekisho.elconsumptionaggregator;
    exports io.github.tekisho.elconsumptionaggregator.controller;
    opens io.github.tekisho.elconsumptionaggregator.controller to javafx.fxml;
    opens io.github.tekisho.elconsumptionaggregator.view to javafx.fxml;

    exports io.github.tekisho.elconsumptionaggregator.model to io.github.tekisho.elconsumptionaggregator.tests;
}