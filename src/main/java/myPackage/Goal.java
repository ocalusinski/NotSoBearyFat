package myPackage;

public class Goal {
    private Integer id;
    private String goalName;
    private String fitnessObjective;
    private Integer calories;
    private String exerciseType;
    private String frequency;
    private String intensity;
    private String duration;
    private String description;

    /**
     * Constructs a new Goal object with full details.
     * @author Oluwalademi Aromolaran
     */
    public Goal(String goalName, String fitnessObjective, Integer calories, String exerciseType, String frequency, String intensity, String duration, String description) {
        this.goalName = goalName;
        this.fitnessObjective = fitnessObjective;
        this.calories = calories;
        this.exerciseType = exerciseType;
        this.frequency = frequency;
        this.intensity = intensity;
        this.duration = duration;
        this.description = description;
    }

    /**
     * Default constructor for a Goal object, with some pre-filled values.
     * @author Oluwalademi Aromolaran
     */
    public Goal() {
        this("Unnamed Goal", "Weight Loss", 500, "Cardio", "3/week", "Moderate", "3 months", "Blank description");
    }

    /**
     * Gets the ID of the goal.
     * @author Oluwalademi Aromolaran
     * @return The ID of the goal.
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets the ID of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name of the goal.
     * @author Oluwalademi Aromolaran
     * @return The name of the goal.
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Sets the name of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    /**
     * Gets the fitness objective of the goal.
     * @author Oluwalademi Aromolaran
     * @return The fitness objective.
     */
    public String getFitnessObjective() {
        return fitnessObjective;
    }

    /**
     * Sets the fitness objective of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setFitnessObjective(String fitnessObjective) {
        this.fitnessObjective = fitnessObjective;
    }

    /**
     * Gets the target calories for the goal.
     * @author Oluwalademi Aromolaran
     * @return The target calories.
     */
    public Integer getCalories() {
        return calories;
    }

    /**
     * Sets the target calories for the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    /**
     * Gets the exercise type for the goal.
     * @author Oluwalademi Aromolaran
     * @return The exercise type.
     */
    public String getExerciseType() {
        return exerciseType;
    }

    /**
     * Sets the exercise type for the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    /**
     * Gets the frequency of the goal.
     * @author Oluwalademi Aromolaran
     * @return The frequency.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Gets the intensity of the goal.
     * @author Oluwalademi Aromolaran
     * @return The intensity.
     */
    public String getIntensity() {
        return intensity;
    }

    /**
     * Sets the intensity of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    /**
     * Gets the duration of the goal.
     * @author Oluwalademi Aromolaran
     * @return The duration.
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Gets the description of the goal.
     * @author Oluwalademi Aromolaran
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the goal.
     * @author Oluwalademi Aromolaran
     */
    public void setDescription(String description) {
        this.description = description;
    }

    // Helper function to update a Goal from another goal
    /**
     * Updates the fields of this Goal object from another Goal object.
     * @author Oluwalademi Aromolaran
     */
    public void updateFrom(Goal other) {
        if (other == null) return;
        this.goalName = other.goalName;
        this.fitnessObjective = other.fitnessObjective;
        this.calories = other.calories;
        this.exerciseType = other.exerciseType;
        this.frequency = other.frequency;
        this.intensity = other.intensity;
        this.duration = other.duration;
        this.description = other.description;
    }

    /**
     * Returns a string representation of the Goal object.
     * @author Oluwalademi Aromolaran
     * @return A string representation of the Goal.
     */
    public String toString() {
        if (goalName != null && !goalName.isEmpty()) {
            return goalName + " (" + (fitnessObjective != null ? fitnessObjective : "Goal") + ")";
        }
        return "Goal (" + (fitnessObjective != null ? fitnessObjective: "No objective") + ")";
    }
}
