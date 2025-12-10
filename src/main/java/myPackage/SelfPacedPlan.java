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

    /**
     * Constructor for the SelfPacedPlan.
     * @author Oluwalademi Aromolaran
     */
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

    /**
     * Constructor for the SelfPacedPlan without an id or trainerId.
     * @author Oluwalademi Aromolaran
     */
    public SelfPacedPlan(String title, String description, String fitnessLevel, String equipment, String sessionLength, String frequency) {
        this(0, 0, title, description, fitnessLevel, equipment, sessionLength, frequency);
    }

    /**
     * Gets the ID of the plan.
     * @author Oluwalademi Aromolaran
     * @return The ID of the plan.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the trainer who created the plan.
     * @author Oluwalademi Aromolaran
     * @return The trainer's ID.
     */
    public int getTrainerId() {
        return trainerId;
    }

    /**
     * Sets the ID of the trainer who created the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * Gets the title of the plan.
     * @author Oluwalademi Aromolaran
     * @return The title of the plan.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the plan.
     * @author Oluwalademi Aromolaran
     * @return The description of the plan.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the fitness level of the plan.
     * @author Oluwalademi Aromolaran
     * @return The fitness level of the plan.
     */
    public String getFitnessLevel() {
        return fitnessLevel;
    }

    /**
     * Sets the fitness level of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    /**
     * Gets the equipment required for the plan.
     * @author Oluwalademi Aromolaran
     * @return The equipment required for the plan.
     */
    public String getEquipment() {
        return equipment;
    }

    /**
     * Sets the equipment required for the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    /**
     * Gets the session length of the plan.
     * @author Oluwalademi Aromolaran
     * @return The session length of the plan.
     */
    public String getSessionLength() {
        return sessionLength;
    }

    /**
     * Sets the session length of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    /**
     * Gets the frequency of the plan.
     * @author Oluwalademi Aromolaran
     * @return The frequency of the plan.
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the frequency of the plan.
     * @author Oluwalademi Aromolaran
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * Updates the plan's fields from another plan object.
     * @author Oluwalademi Aromolaran
     */
    public void updateFrom(SelfPacedPlan other) {
        if (other == null) return;
        this.title = other.title;
        this.description = other.description;
        this.fitnessLevel = other.fitnessLevel;
        this.equipment = other.equipment;
        this.sessionLength = other.sessionLength;
        this.frequency = other.frequency;
    }

    /**
     * Returns a string representation of the plan.
     * @author Oluwalademi Aromolaran
     * @return A string representation of the plan.
     */
    public String toString() {
        String level = (fitnessLevel != null && !fitnessLevel.isEmpty()) ? fitnessLevel : "Any Level";
        return title + " (" + level + ")";
    }
}
