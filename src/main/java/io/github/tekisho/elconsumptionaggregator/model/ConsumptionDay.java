package io.github.tekisho.elconsumptionaggregator.model;

import java.util.List;

public class ConsumptionDay {
    private String dayId;
    private List<Double> values;

    public ConsumptionDay(String dayId, List<Double> values) {
        this.dayId = dayId;
        this.values = values;
    }
}
