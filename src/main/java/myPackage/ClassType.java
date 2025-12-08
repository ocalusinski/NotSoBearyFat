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

    ClassType(String name) {
        this.type = name;
    }

    // Provide a public getter method to retrieve the string value
    public String getType() {
        return this.type;
    }

    //returns a ClassType from a given String
    public static ClassType fromString(String text) {
        for (ClassType b : ClassType.values()) {
            if (b.type.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
    public static List getClassTypes() {
        List<String> list = new ArrayList<String>();
        for (ClassType b : ClassType.values()) {
            list.add(b.type);
        }
        return list;
    }
}
