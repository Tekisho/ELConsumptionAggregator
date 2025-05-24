package io.github.tekisho.elconsumptionaggregator.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeSlots {
    private final List<LocalTime> slots;

    public TimeSlots() {
        this.slots = generateSlots();
    }

    private List<LocalTime> generateSlots() {
        List<LocalTime> slots = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                slots.add(LocalTime.of(hour, minute));
            }
        }
        return Collections.unmodifiableList(slots);
    }

    public List<LocalTime> getSlots() {
        return slots;
    }
}
