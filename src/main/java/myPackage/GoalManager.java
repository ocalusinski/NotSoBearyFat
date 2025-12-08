package myPackage;

public class GoalManager {
    private Goal currentGoal;

    public boolean saveGoals(int userId, Goal goalDetails) {
        if (userId == -1 || goalDetails == null) {
            System.err.println("Unable to save goals: goalDetails is null.");
            return false;
        }

        if (currentGoal == null) {
            currentGoal = goalDetails;
            System.out.println("Created new goal for userId=" + userId);
        }
        else {
            currentGoal.updateFrom(goalDetails);
            System.out.println("Updated existing goal for userID=" + userId);
        }

        System.out.println("Goal saved. Objective: " + goalDetails.getFitnessObjective() + ", Calories: " + goalDetails.getCalories());
        return true;
    }


    public Goal getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(Goal goal) {
        this.currentGoal = goal;
    }
}
