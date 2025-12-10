package myPackage;

import java.util.ArrayList;
import java.util.List;

public enum ClassType {
    CLASS_TYPE("Class Type--"),
    HIIT("HIIT"),
    CARDIO("Cardio"),
    WEIGHT_TRAINING("Weight Training"),
    YOGA("Yoga"),
    PILATES("Pilates"),
    CALISTHENICS("Calisthenics");

    private final String type;

    /**
     * Constructs a ClassType enum with a specified name.
     * @author Owen Chipman
     */
    ClassType(String name) {
        this.type = name;
    }

    // Provide a public getter method to retrieve the string value
    /**
     * Provides a public getter method to retrieve the string value of the ClassType.
     * @author Owen Chipman
     * @return The string representation of the ClassType.
     */
    public String getType() {
        return this.type;
    }

    //returns a ClassType from a given String
    /**
     * Returns a ClassType enum from a given String.
     * @author Owen Chipman
     * @return The ClassType corresponding to the given string.
     */
    public static ClassType fromString(String text) {
        for (ClassType b : ClassType.values()) {
            if (b.type.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
    /**
     * Returns a list of all class types as strings.
     * @author Owen Chipman
     * @return A List of strings representing the class types.
     */
    public static List getClassTypes() {
        List<String> list = new ArrayList<String>();
        for (ClassType b : ClassType.values()) {
            list.add(b.type);
        }
        return list;
    }
}
