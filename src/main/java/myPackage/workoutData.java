package myPackage;
import java.time.LocalDate;
import java.util.Date;

public class workoutData {
    private LocalDate date;
    private String workoutType;
    private int exerciseMinutes;
    private int caloriesBurnt;

    /**
     * Constructs a workoutData object with date, workout type, and exercise minutes.
     * @author ocalusinski
     */
    public workoutData(LocalDate date, String workoutType, int exerciseMinutes) {
        this.date = date;
        this.workoutType = workoutType;
        this.exerciseMinutes = exerciseMinutes;
    }

    /**
     * Constructs a workoutData object with date, workout type, exercise minutes, and calories burnt.
     * @author ocalusinski
     */
    public workoutData(LocalDate date, String workoutType, int exerciseMinutes, int caloriesBurnt) {
        this.date = date;
        this.workoutType = workoutType;
        this.exerciseMinutes = exerciseMinutes;
        this.caloriesBurnt = caloriesBurnt;
    }

    /**
     * Returns the date of the workout.
     * @author ocalusinski
     * @return The date of the workout.
     */
    public LocalDate getDate() {
        return date;
    }
    /**
     * Sets the date of the workout.
     * @author ocalusinski
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the type of workout.
     * @author ocalusinski
     * @return The type of workout.
     */
    public String getWorkoutType() {
        return workoutType;
    }
    /**
     * Sets the type of workout.
     * @author ocalusinski
     */
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    /**
     * Returns the duration of the exercise in minutes.
     * @author ocalusinski
     * @return The duration of the exercise in minutes.
     */
    public int getExerciseMinutes() {
        return exerciseMinutes;
    }
    /**
     * Sets the duration of the exercise in minutes.
     * @author ocalusinski
     */
    public void setExerciseMinutes(int exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    /**
     * Returns the calories burnt during the workout.
     * @author ocalusinski
     * @return The calories burnt during the workout.
     */
    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }
    /**
     * Sets the calories burnt during the workout.
     * @author ocalusinski
     */
    public void setCaloriesBurnt(int caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }
}
