package io.github.tekisho.elconsumptionaggregator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static io.github.tekisho.elconsumptionaggregator.model.TimeSlots.SLOT_STEP_MINUTES;
import static io.github.tekisho.elconsumptionaggregator.model.TimeSlots.SLOTS_PER_DAY;


import io.github.tekisho.elconsumptionaggregator.model.TimeSlots;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

public class TimeSlotsTest {

    @Test
    void generateSlots_shouldReturnSlotsFrom0015To0000With15MinutesStep() {
        // Arrange
        TimeSlots generatedSlots = new TimeSlots();

        // Act
        List<LocalTime> slots = generatedSlots.getSlots();

        // Assert
        assertEquals(LocalTime.of(0, SLOT_STEP_MINUTES), slots.get(0), "First slot should be 00:15");
        assertEquals(LocalTime.MIDNIGHT, slots.get(slots.size() - 1), "Last slot should be 00:00 (24:00)");

        assertEquals(SLOTS_PER_DAY, slots.size(), "Should be 96 slots (from 00:15 to 24:00)");

        for (int i = 0; i < (slots.size() - 1); i++) {
            LocalTime currentSlot = slots.get(i);
            LocalTime nextSlot = slots.get(i + 1);
            assertEquals(nextSlot.minusMinutes(SLOT_STEP_MINUTES), currentSlot,
                    "Slot" + i + " : Expected " + nextSlot.minusMinutes(SLOT_STEP_MINUTES) + " but got " + currentSlot);
        }
    }
}
