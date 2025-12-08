package myPackage;

public class ClassSearchParams {
    private ClassType classType;
    private String trainerUsername;
    private String duration;
    private String timeOfDay;
    //default constructor assigns defaults so original search logic works correctly
    ClassSearchParams(){
        classType = ClassType.fromString("Class Type--");
        trainerUsername = "Trainer--";
        duration = "Duration--";
        timeOfDay = "Time of Day--";
    }
    public void assignVals(String classType, String targetUsername, String duration, String timeOfDay) {
        this.classType = ClassType.fromString(classType);
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
