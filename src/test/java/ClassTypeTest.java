import myPackage.ClassType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTypeTest {

    @Test
    public void testFromStringValidValues() {
        assertEquals(ClassType.YOGA, ClassType.fromString("Yoga"));
        assertEquals(ClassType.CARDIO, ClassType.fromString("Cardio"));
        assertEquals(ClassType.CLASS_TYPE, ClassType.fromString("Class Type--"));
        assertEquals(ClassType.HIIT, ClassType.fromString("hiit")); //case insensitive test
    }

    @Test
    public void testFromStringThrowsOnInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ClassType.fromString("NotARealType");
        });
    }

    @Test
    public void testGetType() {
        assertEquals("Yoga", ClassType.YOGA.getType());
        assertEquals("Cardio", ClassType.CARDIO.getType());
        assertEquals("Class Type--", ClassType.CLASS_TYPE.getType());
    }

    @Test
    public void testGetClass() {
        List types = ClassType.getClassTypes();

        assertEquals(ClassType.values().length, types.size());
        assertTrue(types.contains("Yoga"));
        assertTrue(types.contains("Cardio"));
        assertTrue(types.contains("Class Type--"));
        assertTrue(types.contains("HIIT"));
    }
}
