import myPackage.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class DatabaseManagerTest {
    private DatabaseManager db;

    @BeforeEach
    public void setUp() throws SQLException {
        clearDatabase();
        db = new DatabaseManager();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (db != null) {
            db.closeConnection();
        }
        clearDatabase();
    }

    private void clearDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:notsobearyfat.db");
        Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = OFF");

            stmt.executeUpdate("DELETE FROM plan_enrollments");
            stmt.executeUpdate("DELETE FROM class_enrollments");
            stmt.executeUpdate("DELETE FROM friends");
            stmt.executeUpdate("DELETE FROM login_streaks");
            stmt.executeUpdate("DELETE FROM workouts");
            stmt.executeUpdate("DELETE FROM user_data");
            stmt.executeUpdate("DELETE FROM goals");
            stmt.executeUpdate("DELETE FROM self_paced_plans");
            stmt.executeUpdate("DELETE FROM classes");
            stmt.executeUpdate("DELETE FROM classes");
            stmt.executeUpdate("DELETE FROM users");
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            System.err.println("clearDatabase warning: " + e.getMessage());
        }
    }

    private int createTestUser(String username, String type) {
        String email = username + "@test.com";
        boolean ok = db.registerUser(username, "password", email, type, "First", "Last");
        assertTrue(ok, "registerUser should succeed for " + username);
        int id = db.getUserIdByUsername(username);
        assertTrue(id > 0, "User id should be > 0 for " + username);
        return id;
    }

    @Test
    public void testRegisterAndLoginUserSuccess() {
        boolean saved = db.registerUser("testuser", "secret", "testuser@example.com", "Client", "Test", "User");
        assertTrue(saved);
        User loggedIn = db.loginUser("testuser", "secret");
        assertNotNull(loggedIn);
        assertEquals("testuser", loggedIn.getUsername());
        assertEquals("Client", loggedIn.getUserType());
    }

    @Test
    public void testRegisterUserDuplicateUsernameFails() {
        boolean first = db.registerUser("duplicate", "pw1", "dup1@example.com", "Client", "A", "B");
        assertTrue(first);
        boolean second = db.registerUser("duplicate", "pw2", "dup2@example.com", "Client", "C", "D");
        assertFalse(second);
        assertTrue(db.usernameExists("duplicate"));
    }

    @Test
    public void testLoginUserFailWrongPassword() {
        db.registerUser("bob", "correct", "bob@example.com", "Client", "Bob", "Smith");
        User badLogin = db.loginUser("bob", "wrong");
        assertNull(badLogin);
    }

    @Test
    public void testSaveUserDataAndGetLatest() {
        int userId = createTestUser("dataUser", "Client");
        boolean ok = db.saveUserData(userId, "2025-12-01", 2000, 150.5, 7.5, 500);
        assertTrue(ok);
        double[] latest = db.getLatestUserDataDouble(userId);
        assertNotNull(latest);
        assertEquals(2000, latest[0]);
        assertEquals(150.5, latest[1], 0.0001);
        assertEquals(7.5, latest[2], 0.0001);
        assertEquals(500, latest[3]);
    }

    @Test
    public void testSaveWorkoutAlsoUpdatesUserData() {
        int userId = createTestUser("workoutUser", "Client");
        String date = "12-01-2025";
        boolean ok = db.saveWorkoutData(userId, date, "Running", 30, 300);
        assertTrue(ok);
        List<Object[]> workouts = db.getWorkoutData(userId, 0);
        assertEquals(1, workouts.size());
        Object[] row = workouts.get(0);
        assertEquals(date, row[0]);
        assertEquals("Running", row[1]);
        assertEquals(30, row[2]);
        assertEquals(300, row[3]);

        List<Object[]> history = db.getHistoricalUserData(userId, 0);
        assertEquals(1, history.size());
        Object[] histRow = history.get(0);
        assertEquals(300, histRow[4]);
    }

    @Test
    public void testGetWorkoutCountsByType() {
        int userId = createTestUser("countsUser", "Client");
        db.saveWorkoutData(userId, "12-01-2025", "Running", 30, 300);
        db.saveWorkoutData(userId, "12-02-2025", "Running", 20, 200);
        db.saveWorkoutData(userId, "12-03-2025", "Yoga", 40, 150);
        Map<String, Integer> counts = db.getWorkoutCountsByType(userId, 0);
        assertEquals(2, counts.get("Running"));
        assertEquals(1, counts.get("Yoga"));
    }

    @Test
    public void testSaveAndGetGoal() {
        int userId = createTestUser("goalUser", "Client");
        Goal goal = new Goal(
                "Lose Weight",
                "Weight Loss",
                500,
                "Cardio",
                "3x/week",
                "Medium",
                "30 min",
                "Treadmill workouts"
        );

        boolean saved = db.saveGoal(userId, goal);
        assertTrue(saved);
        assertNotNull(goal.getId(), "Goal id should be set after insert");

        List<Goal> goals = db.getGoalsForUser(userId);
        assertEquals(1, goals.size());
        Goal stored = goals.get(0);

        assertEquals("Lose Weight", stored.getGoalName());
        assertEquals("Weight Loss", stored.getFitnessObjective());
        assertEquals(500, stored.getCalories());
    }

    @Test
    public void testUpdateGoal() {
        int userId = createTestUser("goalUpdateUser", "Client");
        Goal goal = new Goal(
                "Lose Weight",
                "Weight Loss",
                500,
                "Cardio",
                "3x/week",
                "Medium",
                "30 min",
                "Treadmill"
        );
        assertTrue(db.saveGoal(userId, goal));
        Integer originalId = goal.getId();
        assertNotNull(originalId);
        goal.setGoalName("Build Muscle");
        goal.setFitnessObjective("Muscle Gain");
        goal.setCalories(700);
        assertTrue(db.saveGoal(userId, goal));
        List<Goal> goals = db.getGoalsForUser(userId);
        assertEquals(1, goals.size());
        Goal updated = goals.get(0);

        assertEquals(originalId, updated.getId());
        assertEquals("Build Muscle", updated.getGoalName());
        assertEquals("Muscle Gain", updated.getFitnessObjective());
        assertEquals(700, updated.getCalories());
    }

    @Test
    public void testDeleteGoal() {
        int userId = createTestUser("goalDeleteUser", "Client");
        Goal goal = new Goal(
                "Lose Weight",
                "Weight Loss",
                500,
                "Cardio",
                "3x/week",
                "Medium",
                "30 min",
                "Treadmill"
        );
        db.saveGoal(userId, goal);
        assertNotNull(goal.getId());
        boolean deleted = db.deleteGoal(goal.getId(), userId);
        assertTrue(deleted);
        List<Goal> goals = db.getGoalsForUser(userId);
        assertEquals(0, goals.size());
    }

    @Test
    public void testSaveAndGetSelfPacedPlanForTrainer() {
        int trainerId = createTestUser("trainer1", "Trainer");
        SelfPacedPlan plan = new SelfPacedPlan(
                0,
                trainerId,
                "Beginner Plan",
                "Great for newbies",
                "Beginner",
                "None",
                "30 min",
                "3x/week"
        );

        boolean saved = db.saveSelfPacedPlan(trainerId, plan);
        assertTrue(saved);
        assertTrue(plan.getId() > 0, "Plan id should be set after insert");
        List<SelfPacedPlan> trainerPlans = db.getPlansForTrainer(trainerId);
        assertEquals(1, trainerPlans.size());
        SelfPacedPlan stored = trainerPlans.get(0);
        assertEquals("Beginner Plan", stored.getTitle());
        assertEquals("Beginner", stored.getFitnessLevel());
        assertEquals("3x/week", stored.getFrequency());
    }

    @Test
    public void testDeleteSelfPacedPlan() {
        int trainerId = createTestUser("trainerToDeletePlan", "Trainer");

        SelfPacedPlan plan = new SelfPacedPlan(
                0,
                trainerId,
                "Delete Me Plan",
                "Short term plan",
                "Intermediate",
                "Dumbbells",
                "45 min",
                "4x/week"
        );
        db.saveSelfPacedPlan(trainerId, plan);
        int planId = plan.getId();
        assertTrue(planId > 0);

        boolean deleted = db.deleteSelfPacedPlan(planId);
        assertTrue(deleted);

        List<SelfPacedPlan> trainerPlans = db.getPlansForTrainer(trainerId);
        assertEquals(0, trainerPlans.size());
    }

    @Test
    public void testEnrollUserInPlan() {
        int trainerId = createTestUser("trainerEnroll", "Trainer");
        int clientId = createTestUser("ClientEnroll", "Client");

        SelfPacedPlan plan = new SelfPacedPlan(
                0,
                trainerId,
                "Enrollment Plan",
                "Plan to test enrollments",
                "Beginner",
                "None",
                "20 min",
                "2x/week"
        );
        db.saveSelfPacedPlan(trainerId, plan);
        int planId = plan.getId();
        assertTrue(planId > 0);

        boolean enrolled = db.enrollUserInPlan(clientId, planId);
        assertTrue(enrolled);

        boolean enrolledAgain = db.enrollUserInPlan(clientId, planId);
        assertFalse(enrolledAgain);

        List<SelfPacedPlan> enrolledPlans = db.getUserEnrolledPlans(clientId);
        assertEquals(1, enrolledPlans.size());
        assertEquals("Enrollment Plan", enrolledPlans.get(0).getTitle());
    }

    @Test
    public void testSaveAndGetClass() {
        int trainerId = createTestUser("classTrainer", "Trainer");

        boolean saved = db.saveClass(
                "classTrainer",
                "HIIT",
                "High intensity workout",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(45),
                20,
                15.0
        );
        assertTrue(saved);

        ClassSearchParams csp = new ClassSearchParams();
        List<WorkoutClass> classes = db.getAllClasses(csp);
        assertEquals(1, classes.size());
        assertEquals("HIIT", classes.get(0).getClassType());
    }

}
