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

    public Goal() {
        this("Unnamed Goal", "Weight Loss", 500, "Cardio", "3/week", "Moderate", "3 months", "Blank description");
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getFitnessObjective() {
        return fitnessObjective;
    }

    public void setFitnessObjective(String fitnessObjective) {
        this.fitnessObjective = fitnessObjective;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Helper function to update a Goal from another goal
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

    public String toString() {
        if (goalName != null && !goalName.isEmpty()) {
            return goalName + " (" + (fitnessObjective != null ? fitnessObjective : "Goal") + ")";
        }
        return "Goal (" + (fitnessObjective != null ? fitnessObjective: "No objective") + ")";
    }
}
