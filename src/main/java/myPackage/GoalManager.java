package myPackage;

import java.util.List;
import static myPackage.Constants.*;
public class GoalManager {
    private Goal currentGoal;


    /** Load all goals for this user from the database */
    public List<Goal> loadGoals(int userId) {
        return DB_MANAGER.getGoalsForUser(userId);
    }

    /** Save (insert or update) a goal for this user */
    public boolean saveGoals(int userId, Goal goalDetails) {
        if (DB_MANAGER == null || goalDetails == null) {
            System.err.println("Unable to save goals: DB_MANAGER or goalDetails is null.");
            return false;
        }

        boolean ok = DB_MANAGER.saveGoal(userId, goalDetails);
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
        return DB_MANAGER.deleteGoal(goal.getId(), userId);
    }

    /**
     * Gets the current goal set by the user.
     * @author Oluwalademi Aromolaran
     * @return The current Goal object.
     */
    public Goal getCurrentGoal() {
        return currentGoal;
    }

    /**
     * Sets the current goal for the user.
     * @author Oluwalademi Aromolaran
     */
    public void setCurrentGoal(Goal g) {
        this.currentGoal = g;
    }
}
