import myPackage.workoutData;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutDataTest {

    @Test
    public void testConstructorWithoutCalories() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        workoutData wd = new workoutData(date, "Running", 30);

        assertEquals(date, wd.getDate());
        assertEquals("Running", wd.getWorkoutType());
        assertEquals(30, wd.getExerciseMinutes());
        assertEquals(0, wd.getCaloriesBurnt()); // default int value
    }

    @Test
    public void testConstructorWithCalories() {
        LocalDate date = LocalDate.of(2025, 1, 2);
        workoutData wd = new workoutData(date, "Cycling", 45, 300);

        assertEquals(date, wd.getDate());
        assertEquals("Cycling", wd.getWorkoutType());
        assertEquals(45, wd.getExerciseMinutes());
        assertEquals(300, wd.getCaloriesBurnt());
    }

    @Test
    public void testSetters() {
        workoutData wd = new workoutData(LocalDate.now(), "X", 0);

        LocalDate newDate = LocalDate.of(2025, 3, 3);

        wd.setDate(newDate);
        wd.setWorkoutType("Swimming");
        wd.setExerciseMinutes(60);
        wd.setCaloriesBurnt(500);

        assertEquals(newDate, wd.getDate());
        assertEquals("Swimming", wd.getWorkoutType());
        assertEquals(60, wd.getExerciseMinutes());
        assertEquals(500, wd.getCaloriesBurnt());
    }
}