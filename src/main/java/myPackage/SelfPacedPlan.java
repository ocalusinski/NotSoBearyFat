package myPackage;

public class SelfPacedPlan {
    private int id;
    private int trainerId;
    private String title;
    private String description;
    private String fitnessLevel;
    private String equipment;
    private String sessionLength;
    private String frequency;

    public SelfPacedPlan(int id, int trainerId, String title, String description, String fitnessLevel, String equipment, String sessionLength, String frequency) {
        this.id = id;
        this.trainerId = trainerId;
        this.title = title;
        this.description = description;
        this.fitnessLevel = fitnessLevel;
        this.equipment = equipment;
        this.sessionLength = sessionLength;
        this.frequency = frequency;
    }

    public SelfPacedPlan(String title, String description, String fitnessLevel, String equipment, String sessionLength, String frequency) {
        this(0, 0, title, description, fitnessLevel, equipment, sessionLength, frequency);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void updateFrom(SelfPacedPlan other) {
        if (other == null) return;
        this.title = other.title;
        this.description = other.description;
        this.fitnessLevel = other.fitnessLevel;
        this.equipment = other.equipment;
        this.sessionLength = other.sessionLength;
        this.frequency = other.frequency;
    }

    public String toString() {
        String level = (fitnessLevel != null && !fitnessLevel.isEmpty()) ? fitnessLevel : "Any Level";
        return title + " (" + level + ")";
    }
}
