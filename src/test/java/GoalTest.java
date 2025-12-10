import myPackage.Goal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTest {

    @Test
    public void testConstructor() {
        Goal g = new Goal(
                "Lose Weight",
                "Weight Loss",
                500,
                "Cardio",
                "4/week",
                "High",
                "2 months",
                "Burn fat"
        );

        assertEquals("Lose Weight", g.getGoalName());
        assertEquals("Weight Loss", g.getFitnessObjective());
        assertEquals(500, g.getCalories());
        assertEquals("Cardio", g.getExerciseType());
        assertEquals("4/week", g.getFrequency());
        assertEquals("High", g.getIntensity());
        assertEquals("2 months", g.getDuration());
        assertEquals("Burn fat", g.getDescription());
    }

    @Test
    public void testDefaultConstructor() {
        Goal g = new Goal();

        assertEquals("Unnamed Goal", g.getGoalName());
        assertEquals("Weight Loss", g.getFitnessObjective());
        assertEquals(500, g.getCalories());
        assertEquals("Cardio", g.getExerciseType());
        assertEquals("3/week", g.getFrequency());
        assertEquals("Moderate", g.getIntensity());
        assertEquals("3 months", g.getDuration());
        assertEquals("Blank description", g.getDescription());
    }

    @Test
    public void testSetters() {
        Goal g = new Goal();

        g.setGoalName("Build Muscle");
        g.setFitnessObjective("Strength");
        g.setCalories(800);
        g.setExerciseType("Weight Training");
        g.setFrequency("5/week");
        g.setIntensity("High");
        g.setDuration("6 months");
        g.setDescription("Increase muscle mass");
        g.setId(42);

        assertEquals("Build Muscle", g.getGoalName());
        assertEquals("Strength", g.getFitnessObjective());
        assertEquals(800, g.getCalories());
        assertEquals("Weight Training", g.getExerciseType());
        assertEquals("5/week", g.getFrequency());
        assertEquals("High", g.getIntensity());
        assertEquals("6 months", g.getDuration());
        assertEquals("Increase muscle mass", g.getDescription());
        assertEquals(42, g.getId());
    }

    @Test
    public void testUpdateFrom() {
        Goal original = new Goal();
        Goal updated = new Goal(
                "Better Sleep",
                "Healthy Living",
                0,
                "None",
                "Daily",
                "Low",
                "1 month",
                "Fix sleep schedule"
        );

        original.updateFrom(updated);

        assertEquals("Better Sleep", original.getGoalName());
        assertEquals("Healthy Living", original.getFitnessObjective());
        assertEquals(0, original.getCalories());
        assertEquals("None", original.getExerciseType());
        assertEquals("Daily", original.getFrequency());
        assertEquals("Low", original.getIntensity());
        assertEquals("1 month", original.getDuration());
        assertEquals("Fix sleep schedule", original.getDescription());
    }

    @Test
    public void testUpdateFromWithNull() {
        Goal g = new Goal("Test", "Obj", 500, "Cardio", "3/week", "Mod", "1 month", "desc");

        g.updateFrom(null);

        assertEquals("Test", g.getGoalName());
        assertEquals("Obj", g.getFitnessObjective());
    }

    @Test
    public void testToStringWhenGoalHasName() {
        Goal g = new Goal("Run More", "Cardio", 300, "Running", "3/week", "Medium", "1 month", "desc");

        assertEquals("Run More (Cardio)", g.toString());
    }

    @Test
    public void testToStringWhenNoName() {
        Goal g = new Goal("", "Strength", 400, "Weights", "4/week", "High", "2 months", "desc");

        assertEquals("Goal (Strength)", g.toString());
    }
}