package myPackage;

public class GoalManager {
    private Goal currentGoal;

    public boolean saveGoals(int userId, Goal goalDetails) {
        if (goalDetails == null) {
            System.err.println("Unable to save goals: goalDetails is null.");
            return false;
        }

        // In a real system, we'd store in a database. For now:
        this.currentGoal = goalDetails;

        System.out.println("Saved goal for userId " + userId);
        System.out.println("Objective: " + goalDetails.getFitnessObjective());
        System.out.println("Calories: " + goalDetails.getCalories());

        return true;
    }


    public Goal getCurrentGoal() {
        return currentGoal;
    }
}
