package io.github.tekisho.elconsumptionaggregator.model;

import java.util.List;

public class ProcessedHour {
    private String dayId;
    private int hour;
    private List<Double> values;
    private Double mean;
}
