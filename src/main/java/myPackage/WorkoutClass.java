package myPackage;
public class WorkoutClass {
    private int id;
    private String trainerUsername;
    private String classType;
    private String description;
    private String startTime;
    private String endTime;
    private int maxParticipants;
    private double cost;

    public WorkoutClass(int id, String trainerUsername, String classType, String description,
                        String startTime, String endTime, int maxParticipants, double cost) {
        this.id = id;
        this.trainerUsername = trainerUsername;
        this.classType = classType;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxParticipants = maxParticipants;
        this.cost = cost;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public String getClassType() {
        return classType;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public double getCost() {
        return cost;
    }
    
    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return classType + " (" + startTime + " - " + endTime + ") - Trainer: " + trainerUsername;
    }
}




