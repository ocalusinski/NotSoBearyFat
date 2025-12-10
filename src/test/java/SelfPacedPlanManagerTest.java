import myPackage.SelfPacedPlanManager;
import myPackage.SelfPacedPlan;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SelfPacedPlanManagerTest {

    @Test
    public void testHasAllRequiredFields() {
        SelfPacedPlanManager mgr = new SelfPacedPlanManager();

        SelfPacedPlan plan = new SelfPacedPlan(
                "Cardio Blast",
                "High-energy cardio program",
                "Beginner",
                "None",
                "30 min",
                "3/week"
        );

        List<String> missing = new ArrayList<>();
        missing.add("Junk"); // ensure method clears it

        boolean result = mgr.hasMissingRequiredFields(plan, missing);

        assertFalse(result, "Should report no missing fields");
        assertTrue(missing.isEmpty(), "Missing list should be cleared and left empty");
    }

    @Test
    public void testHasMissingRequirements() {
        SelfPacedPlanManager mgr = new SelfPacedPlanManager();

        // Some fields blank / missing
        SelfPacedPlan plan = new SelfPacedPlan(
                "",            // title missing
                "Desc",             // description OK
                " ",                // fitness level blank
                null,               // equipment missing
                "45 min",           // ok
                ""                  // frequency missing
        );

        List<String> missing = new ArrayList<>();

        boolean result = mgr.hasMissingRequiredFields(plan, missing);

        assertTrue(result, "Should report missing required fields");
        assertTrue(missing.contains("Title"));
        assertTrue(missing.contains("Fitness Level"));
        assertTrue(missing.contains("Equipment Needs"));
        assertTrue(missing.contains("Frequency"));
        // Should NOT flag Description or Session Length here
        assertFalse(missing.contains("Description"));
        assertFalse(missing.contains("Session Length"));
    }

    @Test
    public void testHasMissingRequiredFieldsNull() {
        SelfPacedPlanManager mgr = new SelfPacedPlanManager();

        List<String> missing = new ArrayList<>();

        boolean result = mgr.hasMissingRequiredFields(null, missing);

        assertTrue(result, "Null plan should be treated as missing");
        assertEquals(1, missing.size());
        assertEquals("Plan", missing.get(0));
    }

    @Test
    public void testHasMissingRequiredFieldsWhitespaceOnly() {
        SelfPacedPlanManager mgr = new SelfPacedPlanManager();

        SelfPacedPlan plan = new SelfPacedPlan(
                "   ",
                "Some description",
                "   ",
                "   ",
                "30 min",
                "   "
        );
        List<String> missing = new ArrayList<>();
        boolean result = mgr.hasMissingRequiredFields(plan, missing);
        assertTrue(result, "Whitespace-only required fields should be treated as missing");
        assertTrue(missing.contains("Title"));
        assertTrue(missing.contains("Fitness Level"));
        assertTrue(missing.contains("Equipment Needs"));
        assertTrue(missing.contains("Frequency"));
        assertFalse(missing.contains("Description"));
        assertFalse(missing.contains("Session Length"));
    }
}