package myPackage;
import java.time.Duration;
import java.time.LocalDateTime;
public class WorkoutClass {

    private int id;
    private String trainerUsername;
    private ClassType classType;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private int maxParticipants;
    private double cost;


    /**
     * Constructs a new WorkoutClass object with all details.
     * @author zachtaylorcsc
     */
    public WorkoutClass(int id, String trainerUsername, String classType, String description,
                        LocalDateTime startTime, LocalDateTime endTime, int maxParticipants, double cost) {
        this.id = id;
        this.trainerUsername = trainerUsername;
        this.classType = ClassType.fromString(classType);
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = Duration.between(startTime, endTime).toMinutes();
        this.maxParticipants = maxParticipants;
        this.cost = cost;
    }

    //Getters
    /**
     * Returns the ID of the workout class.
     * @author zachtaylorcsc
     * @return The ID of the workout class.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the username of the trainer who created the class.
     * @author zachtaylorcsc
     * @return The trainer's username.
     */
    public String getTrainerUsername() {
        return trainerUsername;
    }

    /**
     * Returns the type of the workout class.
     * @author zachtaylorcsc
     * @return The class type.
     */
    public String getClassType() {
        return classType.getType();
    }

    /**
     * Returns the description of the workout class.
     * @author zachtaylorcsc
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the start time of the workout class.
     * @author Owen Chipman
     * @return The start time.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the workout class.
     * @author Owen Chipman
     * @return The end time.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the maximum number of participants for the class.
     * @author zachtaylorcsc
     * @return The maximum participants.
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Returns the cost of the workout class.
     * @author zachtaylorcsc
     * @return The cost.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Returns the duration of the workout class in minutes.
     * @author Owen Chipman
     * @return The duration in minutes.
     */
    public long getDuration() {return duration;}
    
    //Setters
    /**
     * Sets the ID of the workout class.
     * @author zachtaylorcsc
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the username of the trainer.
     * @author zachtaylorcsc
     */
    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    /**
     * Sets the type of the workout class.
     * @author zachtaylorcsc
     */
    public void setClassType(String classType) {
        this.classType = ClassType.fromString(classType);
    }

    /**
     * Sets the description of the workout class.
     * @author zachtaylorcsc
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the start time of the workout class.
     * @author zachtaylorcsc
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the end time of the workout class.
     * @author zachtaylorcsc
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the maximum number of participants for the class.
     * @author zachtaylorcsc
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Sets the cost of the workout class.
     * @author zachtaylorcsc
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


    /**
     * Returns a string representation of the WorkoutClass object.
     * @author zachtaylorcsc
     * @return A string representation of the WorkoutClass.
     */
    @Override
    public String toString() {
        return classType + " (" + startTime + " - " + endTime + ") - Trainer: " + trainerUsername;
    }
}