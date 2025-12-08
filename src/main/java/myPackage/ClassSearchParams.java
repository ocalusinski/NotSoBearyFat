package myPackage;

public class ClassSearchParams {
    private ClassType classType;
    private String trainerUsername;
    private String duration;
    private String timeOfDay;
    public void assignVals(String classType, String targetUsername, String duration, String timeOfDay) {
        this.classType = ClassType.valueOf(classType);
        this.trainerUsername = targetUsername;
        this.duration = duration;
        this.timeOfDay = timeOfDay;
    }
    public ClassType getClassType() {
        return classType;
    }
    public String getTrainerUsername() {
        return trainerUsername;
    }
    public String getDuration() {
        return duration;
    }
    public String getTimeOfDay() {
        return timeOfDay;
    }


}
