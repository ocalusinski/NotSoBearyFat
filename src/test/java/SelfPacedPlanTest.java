import myPackage.SelfPacedPlan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SelfPacedPlanTest {

    @Test
    public void testConstructor() {
        SelfPacedPlan p = new SelfPacedPlan(
                5,
                10,
                "Strength Plan",
                "Build muscle",
                "Intermediate",
                "Dumbbells",
                "45 min",
                "3/week"
        );

        assertEquals(5, p.getId());
        assertEquals(10, p.getTrainerId());
        assertEquals("Strength Plan", p.getTitle());
        assertEquals("Build muscle", p.getDescription());
        assertEquals("Intermediate", p.getFitnessLevel());
        assertEquals("Dumbbells", p.getEquipment());
        assertEquals("45 min", p.getSessionLength());
        assertEquals("3/week", p.getFrequency());
    }

    @Test
    public void testShortConstructor() {
        SelfPacedPlan p = new SelfPacedPlan(
                "Cardio Blast",
                "High-energy program",
                "Beginner",
                "None",
                "30 min",
                "5/week"
        );

        assertEquals(0, p.getId());
        assertEquals(0, p.getTrainerId());
        assertEquals("Cardio Blast", p.getTitle());
        assertEquals("High-energy program", p.getDescription());
        assertEquals("Beginner", p.getFitnessLevel());
        assertEquals("None", p.getEquipment());
        assertEquals("30 min", p.getSessionLength());
        assertEquals("5/week", p.getFrequency());
    }

    @Test
    public void testSetters() {
        SelfPacedPlan p = new SelfPacedPlan("A", "B", "C", "D", "E", "F");

        p.setId(100);
        p.setTrainerId(200);
        p.setTitle("New Title");
        p.setDescription("New Description");
        p.setFitnessLevel("Advanced");
        p.setEquipment("Barbell");
        p.setSessionLength("60 min");
        p.setFrequency("2/week");

        assertEquals(100, p.getId());
        assertEquals(200, p.getTrainerId());
        assertEquals("New Title", p.getTitle());
        assertEquals("New Description", p.getDescription());
        assertEquals("Advanced", p.getFitnessLevel());
        assertEquals("Barbell", p.getEquipment());
        assertEquals("60 min", p.getSessionLength());
        assertEquals("2/week", p.getFrequency());
    }

    @Test
    public void testUpdateFrom() {
        SelfPacedPlan original = new SelfPacedPlan("A", "B", "C", "D", "E", "F");
        SelfPacedPlan other = new SelfPacedPlan(
                "New Title",
                "New Description",
                "Intermediate",
                "Kettlebell",
                "40 min",
                "4/week"
        );

        original.updateFrom(other);

        assertEquals("New Title", original.getTitle());
        assertEquals("New Description", original.getDescription());
        assertEquals("Intermediate", original.getFitnessLevel());
        assertEquals("Kettlebell", original.getEquipment());
        assertEquals("40 min", original.getSessionLength());
        assertEquals("4/week", original.getFrequency());
    }

    @Test
    public void testUpdateFromWithNull() {
        SelfPacedPlan original = new SelfPacedPlan("A", "B", "C", "D", "E", "F");

        original.updateFrom(null);

        assertEquals("A", original.getTitle());
        assertEquals("B", original.getDescription());
        assertEquals("C", original.getFitnessLevel());
        assertEquals("D", original.getEquipment());
        assertEquals("E", original.getSessionLength());
        assertEquals("F", original.getFrequency());
    }

    @Test
    public void testToStringWithFitnessLevel() {
        SelfPacedPlan p = new SelfPacedPlan("Yoga Flow", "desc", "Intermediate", "mat", "30 min", "daily");
        assertEquals("Yoga Flow (Intermediate)", p.toString());
    }

    @Test
    public void testToStringWithoutFitnessLevel() {
        SelfPacedPlan p = new SelfPacedPlan("Yoga Flow", "desc", "", "mat", "30 min", "daily");

        // When fitnessLevel is empty should say "Any Level"
        assertEquals("Yoga Flow (Any Level)", p.toString());
    }
}