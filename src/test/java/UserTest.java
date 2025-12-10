import myPackage.User;
import myPackage.Goal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructor() {
        User u = new User(
                1,
                "jonah",
                "jonah@example.com",
                "Client",
                "Jonah",
                "Beck"
        );

        assertEquals(1, u.getId());
        assertEquals("jonah", u.getUsername());
        assertEquals("jonah@example.com", u.getEmail());
        assertEquals("Client", u.getUserType());
        assertEquals("Jonah", u.getFirstName());
        assertEquals("Beck", u.getLastName());
        assertNull(u.getGoal());
    }

    @Test
    public void testSetters() {
        User u = new User(0, "", "", "", "", "");

        u.setId(42);
        u.setUsername("newuser");
        u.setEmail("new@example.com");
        u.setUserType("Trainer");
        u.setFirstName("New");
        u.setLastName("Name");

        assertEquals(42, u.getId());
        assertEquals("newuser", u.getUsername());
        assertEquals("new@example.com", u.getEmail());
        assertEquals("Trainer", u.getUserType());
        assertEquals("New", u.getFirstName());
        assertEquals("Name", u.getLastName());
    }

    @Test
    public void testSetAndGetGoal() {
        User u = new User(1, "jonah", "j@example.com", "Client", "Jonah", "Beck");
        Goal g = new Goal("Lose Weight", "Weight Loss", 500, "Cardio", "3/week", "Moderate", "3 months", "desc");

        u.setGoal(g);

        assertNotNull(u.getGoal());
        assertEquals("Lose Weight", u.getGoal().getGoalName());
    }

    @Test
    public void testToStringWithNoGoal() {
        User u = new User(1, "jonah", "j@example.com", "Client", "Jonah", "Beck");

        String s = u.toString();
        assertTrue(s.contains("jonah"));
        assertTrue(s.contains("No Active Goal"));
    }

    @Test
    public void testToStringWithGoal() {
        User u = new User(1, "jonah", "j@example.com", "Client", "Jonah", "Beck");
        Goal g = new Goal("Run More", "Cardio", 300, "Running", "3/week", "Medium", "1 month", "desc");
        u.setGoal(g);

        String s = u.toString();
        assertTrue(s.contains("Run More"));
        assertFalse(s.contains("No Active Goal"));
    }
}
