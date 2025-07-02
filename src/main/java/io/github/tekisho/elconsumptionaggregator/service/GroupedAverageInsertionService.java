package io.github.tekisho.elconsumptionaggregator.service;

import io.github.tekisho.elconsumptionaggregator.model.ConsumptionDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GroupedAverageInsertionService {
    private static final Logger logger = LoggerFactory.getLogger(GroupedAverageInsertionService.class);

    public static final int GROUP_SIZE = 4;

    public List<ConsumptionDay> insertGroupedAverages(List<ConsumptionDay> consumptionDays) {
        logger.debug("Starting grouped average insertion for {} days", consumptionDays.size());

        for (ConsumptionDay consumptionDay : consumptionDays) {
            logger.debug("Processing day {}", consumptionDay.getDate());
            consumptionDay.setValues(processDay(consumptionDay.getValues()));
        }

        logger.debug("Completed grouped average insertion");
        return consumptionDays;
    }

    private List<Double> processDay(List<Double> actualValues) {
        List<Double> aggregatedValues = new ArrayList<>();
        Double groupSumValue = 0d;
        int counter = 0;

        for (Double actualValue : actualValues) {
            counter++;
            groupSumValue += actualValue;
            aggregatedValues.add(actualValue);

            if (counter == GROUP_SIZE) {
                double hourMeanValue = groupSumValue / GROUP_SIZE;
                aggregatedValues.add(hourMeanValue);
                groupSumValue = 0d;
                counter = 0;
            }
        }

        return aggregatedValues;
    }
}
