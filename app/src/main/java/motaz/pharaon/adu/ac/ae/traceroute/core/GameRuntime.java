package motaz.pharaon.adu.ac.ae.traceroute.core;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;

public class GameRuntime {
    /*
     * Purpose:
     * - Shared runtime state for the current play session.
     *
     * Responsibilities:
     * - Own gameplay systems that must be visible to both OpenGL gameplay and Android UI.
     * - Seed temporary hardcoded MVP content until DataLoader/GameRepository are ready.
     * - Avoid making Fragment classes own evidence, thread, or timeline state.
     *
     * Depends on:
     * - EvidenceSystem for collected evidence state.
     *
     * Used by:
     * - GameController and WorldInvestigationScene for gameplay updates.
     * - HandheldFragment / HandheldViewBinder for displaying player state.
     */

    private static GameRuntime instance;

    private final EvidenceSystem evidenceSystem;
    private final ThreadSystem threadSystem;

    private GameRuntime() {
        evidenceSystem = new EvidenceSystem();
        threadSystem = new ThreadSystem();
        seedEvidence();
        seedThreads();
    }

    public static synchronized GameRuntime getInstance() {
        if (instance == null) {
            instance = new GameRuntime();
        }

        return instance;
    }

    public EvidenceSystem getEvidenceSystem() {
        return evidenceSystem;
    }

    public ThreadSystem getThreadSystem() {
        return threadSystem;
    }

    public EvidenceItem collectEvidence(String evidenceId) {
        EvidenceItem evidenceItem = evidenceSystem.collectEvidence(evidenceId);
        if (evidenceItem != null) {
            threadSystem.onEvidenceCollected(evidenceId, evidenceSystem);
        }

        return evidenceItem;
    }

    private void seedEvidence() {
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E01_BODY_CONDITION_REPORT",
                "Body Condition Report",
                "Physical / forensic",
                "Living Room / Body Scene",
                "The body condition does not cleanly match a death after 21:00.",
                "Cooling and stiffness are inconsistent with the apparent late activity window."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E02_THERMOSTAT_AC_LOG",
                "Thermostat / AC Log",
                "Digital / environmental",
                "Kitchen / Side Area",
                "The apartment was cooled unusually after the real murder window began.",
                "AC dropped sharply at 19:34, which can distort body-temperature interpretation."
        ));
    }

    private void seedThreads() {
        threadSystem.registerThread(new InvestigationThread(
                "T01_DEATH_WINDOW_INSTABILITY",
                "Death Window Instability",
                "When did the victim actually die?",
                "The death window was distorted. The victim likely died before the post-21:00 activity."
        ));
    }
}
