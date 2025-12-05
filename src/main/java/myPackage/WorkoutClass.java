package myPackage;
import java.time.LocalDateTime;
public class WorkoutClass {
    public enum ClassType {
        HIIT("HIIT"),
        CARDIO("Cardio"),
        WEIGHT_TRAINING("Weight Training"),
        YOGA("Yoga"),
        PILATES("Pilates"),
        CALISTHENICS("Calisthenics");

        private final String type;

        ClassType(String name) {
            this.type = name;
        }

        // Provide a public getter method to retrieve the string valu
        public String getType() {
            return this.type;
        }

        public static ClassType fromString(String text) {
            for (ClassType b : ClassType.values()) {
                if (b.type.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    private int id;
    private String trainerUsername;
    private ClassType classType;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
    private int maxParticipants;
    private double cost;


    public WorkoutClass(int id, String trainerUsername, String classType, String description,
                        LocalDateTime startTime, LocalDateTime endTime, int maxParticipants, double cost) {
        this.id = id;
        this.trainerUsername = trainerUsername;
        this.classType = ClassType.fromString(classType);
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
        return classType.getType();
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
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
        this.classType = ClassType.fromString(classType);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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




