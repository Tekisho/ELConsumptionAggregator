package io.github.tekisho.elconsumptionaggregator.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TimeSlots {
    public static final int SLOT_STEP_MINUTES = 15;
    public static final int SLOTS_PER_DAY = (24 * 60) / SLOT_STEP_MINUTES;
    private final List<LocalTime> slots;

    public TimeSlots() {
        this.slots = generateSlots();
    }

    private List<LocalTime> generateSlots() {
        List<LocalTime> slots = new ArrayList<>(SLOTS_PER_DAY);
        LocalTime endTime = LocalTime.MIDNIGHT;
        LocalTime startTime = endTime.plusMinutes(SLOT_STEP_MINUTES);

        while (!startTime.equals(endTime)) {
            slots.add(startTime);
            startTime = startTime.plusMinutes(SLOT_STEP_MINUTES);
        }
        slots.add(endTime);

        return Collections.unmodifiableList(slots);
    }

    public List<LocalTime> getSlots() {
        return slots;
    }
}
