import java.time.LocalDate;
import java.util.Date;

public class workoutData {
    private LocalDate date;
    private String workoutType;
    private int exerciseMinutes;
    private int caloriesBurnt;

    public workoutData(LocalDate date, String workoutType, int exerciseMinutes) {
        this.date = date;
        this.workoutType = workoutType;
        this.exerciseMinutes = exerciseMinutes;
    }

    public workoutData(LocalDate date, String workoutType, int exerciseMinutes, int caloriesBurnt) {
        this.date = date;
        this.workoutType = workoutType;
        this.exerciseMinutes = exerciseMinutes;
        this.caloriesBurnt = caloriesBurnt;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWorkoutType() {
        return workoutType;
    }
    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public int getExerciseMinutes() {
        return exerciseMinutes;
    }
    public void setExerciseMinutes(int exerciseMinutes) {
        this.exerciseMinutes = exerciseMinutes;
    }

    public int getCaloriesBurnt() {
        return caloriesBurnt;
    }
    public void setCaloriesBurnt(int caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }
}
