import myPackage.ClassSearchParams;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClassSearchParamsTest {
    @Test
    public void testDefaultConstructor() {
        ClassSearchParams params = new ClassSearchParams();

        assertNotNull(params.getClassType());
        assertEquals("Class Type--", params.getClassType().getType());
        assertEquals("Trainer--", params.getTrainerUsername());
        assertEquals("Duration--", params.getDuration());
        assertEquals("Time of Day--", params.getTimeOfDay());
    }

    @Test
    public void testAssignVals() {
        ClassSearchParams params = new ClassSearchParams();

        params.assignVals("Yoga", "trainer123", "1 Hour", "Morning");

        assertNotNull(params.getClassType());
        assertEquals("Yoga", params.getClassType().getType());
        assertEquals("trainer123", params.getTrainerUsername());
        assertEquals("1 Hour", params.getDuration());
        assertEquals("Morning", params.getTimeOfDay());
    }
}
