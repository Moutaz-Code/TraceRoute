package motaz.pharaon.adu.ac.ae.traceroute.core;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.StatementSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;

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
    private final TimelineSystem timelineSystem;
    private final StatementSystem statementSystem;

    private GameRuntime() {
        evidenceSystem = new EvidenceSystem();
        threadSystem = new ThreadSystem();
        timelineSystem = new TimelineSystem();
        statementSystem = new StatementSystem();
        seedEvidence();
        seedThreads();
        seedTimeline();
        seedStatements();
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

    public TimelineSystem getTimelineSystem() {
        return timelineSystem;
    }

    public StatementSystem getStatementSystem() {
        return statementSystem;
    }

    public EvidenceItem collectEvidence(String evidenceId) {
        EvidenceItem evidenceItem = evidenceSystem.collectEvidence(evidenceId);
        if (evidenceItem != null) {
            threadSystem.onEvidenceCollected(evidenceId, evidenceSystem);
            timelineSystem.onThreadStateChanged(threadSystem);
            statementSystem.onEvidenceCollected(evidenceSystem);
            statementSystem.onTimelineStateChanged(timelineSystem);
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
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E05_SMART_LOCK_ENTRY_EXIT_LOG",
                "Smart-Lock Entry / Exit Log",
                "Digital / access",
                "Entry / Smart-Lock Area",
                "The partner arrived at 19:05 and exited at 19:42.",
                "The access log looks clean, but it only clears him if the victim was alive later."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E06_SCHEDULED_VICTIM_MESSAGE",
                "Scheduled Victim Message",
                "Digital / communication",
                "Office terminal",
                "A message sent at 21:07 appears to show the victim was alive, but metadata suggests it was scheduled.",
                "The message was queued before delivery, so the send time is not proof of manual activity."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E07_LAPTOP_SESSION_METADATA",
                "Laptop Session Metadata",
                "Digital / device activity",
                "Office terminal",
                "Laptop activity continued after the likely death time, but the interaction pattern looks automated.",
                "Session metadata shows repeated timed actions rather than varied manual input."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E08_ROUTER_ADMIN_ACCESS_RECORD",
                "Router / Admin Access Record",
                "Digital / network",
                "Office terminal",
                "A trusted admin-level session accessed apartment systems around the staging period.",
                "The session had privileged access to networked apartment devices, not just ordinary remote viewing."
        ));
    }

    private void seedThreads() {
        threadSystem.registerThread(new InvestigationThread(
                "T01_DEATH_WINDOW_INSTABILITY",
                "Death Window Instability",
                "When did the victim actually die?",
                "The death window was distorted. The victim likely died before the post-21:00 activity."
        ));
        threadSystem.registerThread(new InvestigationThread(
                "T02_POSTMORTEM_ACTIVITY",
                "Postmortem Activity",
                "Why did the victim appear active after death?",
                "The victim's digital activity was artificially continued after death."
        ));
        threadSystem.registerThread(new InvestigationThread(
                "T03_APARTMENT_ACCESS",
                "Apartment Access",
                "Who could affect the apartment physically or remotely?",
                "Several people had suspicious access, but the full manipulation required trusted technical privileges."
        ));
    }

    private void seedTimeline() {
        timelineSystem.registerEvent(new TimelineEvent(
                "TL01_POST_21_ACTIVITY_UNRELIABLE",
                "After 21:00",
                "Victim Activity Window",
                "Message and laptop activity initially suggest the victim was alive after 21:00.",
                "Physical evidence and environmental logs make the post-21:00 activity unreliable as proof of life."
        ));
    }

    private void seedStatements() {
        statementSystem.registerStatement(new SuspectStatement(
                "S01_PARTNER_STATEMENT",
                "Business Partner",
                "We argued, but he was alive when I left. He was already back at his laptop.",
                "His claim depends on the victim being alive after the 19:42 exit.",
                "The stabilized death-window contradiction weakens his claimed alibi."
        ));
    }
}
