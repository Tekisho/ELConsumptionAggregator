module io.github.tekisho.elconsumptionaggregator.tests {
    requires io.github.tekisho.elconsumptionaggregator;
    requires org.junit.jupiter.api;


    opens io.github.tekisho.elconsumptionaggregator.tests to org.junit.platform.commons;
}