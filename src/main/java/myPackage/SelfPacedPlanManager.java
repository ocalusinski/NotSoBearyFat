package myPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SelfPacedPlanManager {
    private final List<SelfPacedPlan> plans = new ArrayList<>();
    private int nextId = 1;

    public boolean hasMissingRequiredFields(SelfPacedPlan plan, List<String> missingFields) {
        if (plan == null) {
            if (missingFields != null) {
                missingFields.add("Plan");
            }
            return true;
        }

        boolean missing = false;
        if (isBlank(plan.getTitle())) {
            if (missingFields != null) missingFields.add("Title");
            missing = true;
        }
        if (isBlank(plan.getSessionLength())) {
            if (missingFields != null) missingFields.add("Session Length");
            missing = true;
        }
        if (isBlank(plan.getFrequency())) {
            if (missingFields != null) missingFields.add("Frequency");
            missing = true;
        }
        return missing;
    }

    public boolean savePlan(int trainerId, SelfPacedPlan planDetails) {
        if (trainerId <= 0 || planDetails == null) {
            return false;
        }

        try {
            if (planDetails.getId() == 0) {
                planDetails.setId(nextId++);
                planDetails.setTrainerId(trainerId);
                plans.add(planDetails);
            }
            else {
                SelfPacedPlan existing = findById(planDetails.getId());
                if (existing != null) {
                    existing.updateFrom(planDetails);
                }
                else {
                    planDetails.setId(nextId++);
                    planDetails.setTrainerId(trainerId);
                    plans.add(planDetails);
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private SelfPacedPlan findById(int id) {
        for (SelfPacedPlan p : plans) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<SelfPacedPlan> getAllPlans() {
        return Collections.unmodifiableList(plans);
    }

    public List<SelfPacedPlan> getPlansForTrainer(int trainerId) {
        List<SelfPacedPlan> result = new ArrayList<>();
        for (SelfPacedPlan p : plans) {
            if (p.getTrainerId() == trainerId) {
                result.add(p);
            }
        }
        return result;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

}
