package io.github.tekisho.elconsumptionaggregator.model;

import java.util.List;

public final class ConsumptionDay {
    private String date;
    private List<Double> values;

    public ConsumptionDay(String date, List<Double> values) {
        this.date = date;
        this.values = values;
    }

    public ConsumptionDay() {}

    public String getDate() {
        return date;
    }

    public List<Double> getValues() {
        return values;
    }
    public void setValues(List<Double> values) {
        this.values = values;
    }
}
