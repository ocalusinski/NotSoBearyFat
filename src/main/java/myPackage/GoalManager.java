package myPackage;

import java.util.List;

public class GoalManager {
    private final DatabaseManager dbManager;
    private Goal currentGoal;

    public GoalManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /** Load all goals for this user from the database */
    public List<Goal> loadGoals(int userId) {
        return dbManager.getGoalsForUser(userId);
    }

    /** Save (insert or update) a goal for this user */
    public boolean saveGoals(int userId, Goal goalDetails) {
        if (dbManager == null || goalDetails == null) {
            System.err.println("Unable to save goals: dbManager or goalDetails is null.");
            return false;
        }

        boolean ok = dbManager.saveGoal(userId, goalDetails);
        if (ok) {
            currentGoal = goalDetails;
            System.out.println("Goal saved. Objective: " +
                    goalDetails.getFitnessObjective() + ", Calories: " + goalDetails.getCalories());
        }
        return ok;
    }


    /** Delete a goal for this user */
    public boolean deleteGoal(int userId, Goal goal) {
        if (goal == null || goal.getId() == null) return false;
        return dbManager.deleteGoal(goal.getId(), userId);
    }

    public Goal getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(Goal g) {
        this.currentGoal = g;
    }
}
