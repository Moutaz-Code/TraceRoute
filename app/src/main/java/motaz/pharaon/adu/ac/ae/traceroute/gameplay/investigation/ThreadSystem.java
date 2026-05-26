package motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;

public class ThreadSystem {
    /*
     * Purpose:
     * - Owns investigation thread progress for the current playthrough.
     *
     * Responsibilities:
     * - Unlock thread observations when relevant evidence is collected.
     * - Mark threads stabilized when their required clue chain is complete.
     * - Provide readable thread state to the handheld UI.
     *
     * Depends on:
     * - InvestigationThread data.
     * - EvidenceSystem events or GameController notifications.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - HandheldFragment through HandheldController.
     */

    private static final String DEATH_WINDOW_THREAD_ID = "T01_DEATH_WINDOW_INSTABILITY";
    private static final String BODY_REPORT_ID = "E01_BODY_CONDITION_REPORT";
    private static final String AC_LOG_ID = "E02_THERMOSTAT_AC_LOG";

    private final Map<String, InvestigationThread> threads;

    public ThreadSystem() {
        threads = new LinkedHashMap<>();
    }

    public void registerThread(InvestigationThread investigationThread) {
        threads.put(investigationThread.getId(), investigationThread);
    }

    public void onEvidenceCollected(String evidenceId, EvidenceSystem evidenceSystem) {
        InvestigationThread deathWindowThread = threads.get(DEATH_WINDOW_THREAD_ID);
        if (deathWindowThread == null) {
            return;
        }

        if (BODY_REPORT_ID.equals(evidenceId)) {
            deathWindowThread.addObservation(
                    "Body condition weakens the assumption that the victim was alive after 21:00."
            );
        } else if (AC_LOG_ID.equals(evidenceId)) {
            deathWindowThread.addObservation(
                    "Thermostat activity shows the apartment was cooled during the suspicious window."
            );
        }

        if (evidenceSystem.isCollected(BODY_REPORT_ID) && evidenceSystem.isCollected(AC_LOG_ID)) {
            deathWindowThread.setStabilized(true);
        }
    }

    public List<InvestigationThread> getInvestigationThreads() {
        return new ArrayList<>(threads.values());
    }
}
