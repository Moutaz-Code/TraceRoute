package motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InvestigationThread {
    /*
     * Purpose:
     * - Data model for one semi-explicit investigation thread.
     *
     * Responsibilities:
     * - Identify a reasoning line such as Death Window Instability or Postmortem Activity.
     * - Store observations and stabilized conclusion text later.
     * - Stay as data; progression logic belongs to ThreadSystem.
     *
     * Depends on:
     * - Static content from DataLoader.
     *
     * Used by:
     * - ThreadSystem.
     * - HandheldFragment through HandheldController.
     */

    private final String id;
    private final String name;
    private final String coreQuestion;
    private final String stabilizedConclusion;
    private final List<String> observations;
    private boolean stabilized;

    public InvestigationThread(
            String id,
            String name,
            String coreQuestion,
            String stabilizedConclusion
    ) {
        this.id = id;
        this.name = name;
        this.coreQuestion = coreQuestion;
        this.stabilizedConclusion = stabilizedConclusion;
        observations = new ArrayList<>();
        stabilized = false;
    }

    public void addObservation(String observation) {
        if (!observations.contains(observation)) {
            observations.add(observation);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCoreQuestion() {
        return coreQuestion;
    }

    public List<String> getObservations() {
        return Collections.unmodifiableList(observations);
    }

    public boolean isStabilized() {
        return stabilized;
    }

    public void setStabilized(boolean stabilized) {
        this.stabilized = stabilized;
    }

    public String getStabilizedConclusion() {
        return stabilizedConclusion;
    }
}
