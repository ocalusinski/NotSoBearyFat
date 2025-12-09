package myPackage;

import java.util.List;

public class SelfPacedPlanManager {
    private final DatabaseManager dbManager;

    public SelfPacedPlanManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /** Trainer-specific list (for editing) */
    public List<SelfPacedPlan> getPlansForTrainer(int trainerId) {
        return dbManager.getPlansForTrainer(trainerId);
    }

    /** All plans in the workout library (users can browse) */
    public List<SelfPacedPlan> getAllPlans() {
        return dbManager.getAllSelfPacedPlans();
    }

    /** Insert or update a plan */
    public boolean savePlan(int trainerId, SelfPacedPlan plan) {
        return dbManager.saveSelfPacedPlan(trainerId, plan);
    }

    /** Delete a plan created by this trainer */
    public boolean deletePlan(int planId) {
        return dbManager.deleteSelfPacedPlan(planId);
    }

    /** Check if a user is enrolled in a plan */
    public boolean isUserEnrolledInPlan(int userId, int planId) {
        return dbManager.isUserEnrolledInPlan(userId, planId);
    }

    /** Enroll a user in a plan */
    public boolean enrollUserInPlan(int userId, int planId) {
        return dbManager.enrollUserInPlan(userId, planId);
    }

    /** Get all plans that a user is enrolled in */
    public List<SelfPacedPlan> getEnrolledPlansForUser(int userId) {
        return dbManager.getUserEnrolledPlans(userId);
    }

    public boolean hasMissingRequiredFields(SelfPacedPlan plan, List<String> missingFields) {
        if (missingFields != null) {
            missingFields.clear();
        }

        if (plan == null) {
            if (missingFields != null) {
                missingFields.add("Plan");
            }
            return true;
        }

        if (isBlank(plan.getTitle())) {
            missingFields.add("Title");
        }
        if (isBlank(plan.getDescription())) {
            missingFields.add("Description");
        }
        if (isBlank(plan.getFitnessLevel())) {
            missingFields.add("Fitness Level");
        }
        if (isBlank(plan.getEquipment())) {
            missingFields.add("Equipment Needs");
        }
        if (isBlank(plan.getSessionLength())) {
            missingFields.add("Session Length");
        }
        if (isBlank(plan.getFrequency())) {
            missingFields.add("Frequency");
        }

        return !missingFields.isEmpty();
    }

    // small helper
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
