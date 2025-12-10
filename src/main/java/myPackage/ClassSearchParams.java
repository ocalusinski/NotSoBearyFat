package myPackage;

public class ClassSearchParams {
    private ClassType classType;
    private String trainerUsername;
    private String duration;
    private String timeOfDay;
    //default constructor assigns defaults so original search logic works correctly
    /**
     * Default constructor that assigns default values.
     * @author Owen Chipman
     */
    ClassSearchParams(){
        classType = ClassType.fromString("Class Type--");
        trainerUsername = "Trainer--";
        duration = "Duration--";
        timeOfDay = "Time of Day--";
    }
    /**
     * Assigns values to the search parameters.
     * @author Owen Chipman
     */
    public void assignVals(String classType, String targetUsername, String duration, String timeOfDay) {
        this.classType = ClassType.fromString(classType);
        this.trainerUsername = targetUsername;
        this.duration = duration;
        this.timeOfDay = timeOfDay;
    }
    /**
     * Gets the class type search parameter.
     * @author Owen Chipman
     * @return The class type.
     */
    public ClassType getClassType() {
        return classType;
    }
    /**
     * Gets the trainer username search parameter.
     * @author Owen Chipman
     * @return The trainer username.
     */
    public String getTrainerUsername() {
        return trainerUsername;
    }
    /**
     * Gets the duration search parameter.
     * @author Owen Chipman
     * @return The duration.
     */
    public String getDuration() {
        return duration;
    }
    /**
     * Gets the time of day search parameter.
     * @author Owen Chipman
     * @return The time of day.
     */
    public String getTimeOfDay() {
        return timeOfDay;
    }


}
