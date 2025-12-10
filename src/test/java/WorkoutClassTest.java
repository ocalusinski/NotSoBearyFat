import myPackage.WorkoutClass;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutClassTest {

    @Test
    public void testConstructor() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 1, 1, 11, 30); // 90 minutes

        WorkoutClass wc = new WorkoutClass(
                1,
                "trainer123",
                "Yoga",
                "Morning flow",
                start,
                end,
                15,
                12.5
        );

        assertEquals(1, wc.getId());
        assertEquals("trainer123", wc.getTrainerUsername());
        assertEquals("Yoga", wc.getClassType());        // from ClassType.getType()
        assertEquals("Morning flow", wc.getDescription());
        assertEquals(start, wc.getStartTime());
        assertEquals(end, wc.getEndTime());
        assertEquals(15, wc.getMaxParticipants());
        assertEquals(12.5, wc.getCost());
        assertEquals(90, wc.getDuration(), "Duration should be in minutes");
    }

    @Test
    public void testSetters() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 1, 1, 9, 0);

        WorkoutClass wc = new WorkoutClass(
                1,
                "trainer123",
                "Cardio",
                "Old",
                start,
                end,
                10,
                5.0
        );

        wc.setId(2);
        wc.setTrainerUsername("newTrainer");
        wc.setClassType("HIIT");
        wc.setDescription("High intensity");
        LocalDateTime newStart = LocalDateTime.of(2025, 1, 2, 18, 0);
        LocalDateTime newEnd   = LocalDateTime.of(2025, 1, 2, 19, 0);
        wc.setStartTime(newStart);
        wc.setEndTime(newEnd);
        wc.setMaxParticipants(25);
        wc.setCost(20.0);

        assertEquals(2, wc.getId());
        assertEquals("newTrainer", wc.getTrainerUsername());
        assertEquals("HIIT", wc.getClassType());  // string from enum.getType()
        assertEquals("High intensity", wc.getDescription());
        assertEquals(newStart, wc.getStartTime());
        assertEquals(newEnd, wc.getEndTime());
        assertEquals(25, wc.getMaxParticipants());
        assertEquals(20.0, wc.getCost());
    }

    @Test
    public void testToStringContainsKeyInfo() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 1, 1, 11, 0);

        WorkoutClass wc = new WorkoutClass(
                1,
                "trainer123",
                "Yoga",
                "desc",
                start,
                end,
                10,
                15.0
        );

        String s = wc.toString();
        assertTrue(s.contains("YOGA"));
        assertTrue(s.contains(start.toString()));
        assertTrue(s.contains(end.toString()));
        assertTrue(s.contains("trainer123"));
    }
}
