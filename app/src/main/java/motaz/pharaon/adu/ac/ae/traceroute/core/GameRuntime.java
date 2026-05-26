package motaz.pharaon.adu.ac.ae.traceroute.core;

import android.content.Context;

import motaz.pharaon.adu.ac.ae.traceroute.data.repository.GameRepository;
import motaz.pharaon.adu.ac.ae.traceroute.data.repository.GameSaveState;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.StatementSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;

public class GameRuntime {
    /*
     * Purpose:
     * - Shared runtime state for the current play session.
     *
     * Responsibilities:
     * - Own gameplay systems that must be visible to both OpenGL gameplay and Android UI.
     * - Seed temporary hardcoded MVP content until DataLoader is ready.
     * - Save/load MVP play state through GameRepository.
     * - Avoid making Fragment classes own evidence, thread, or timeline state.
     *
     * Depends on:
     * - EvidenceSystem for collected evidence state.
     * - GameRepository for persistence once Android context is available.
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
    private GameRepository gameRepository;
    private RoomId currentRoomId;
    private boolean savedStateLoaded;

    private GameRuntime(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        evidenceSystem = new EvidenceSystem();
        threadSystem = new ThreadSystem();
        timelineSystem = new TimelineSystem();
        statementSystem = new StatementSystem();
        currentRoomId = RoomId.LIVING_ROOM;
        savedStateLoaded = false;
        seedEvidence();
        seedThreads();
        seedTimeline();
        seedStatements();
        restorePlayStateIfAvailable();
    }

    public static synchronized void initialize(Context context) {
        GameRepository gameRepository = new GameRepository(context.getApplicationContext());

        if (instance == null) {
            instance = new GameRuntime(gameRepository);
        } else {
            instance.gameRepository = gameRepository;
            instance.restorePlayStateIfAvailable();
        }
    }

    public static synchronized GameRuntime getInstance() {
        if (instance == null) {
            instance = new GameRuntime(null);
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
        return collectEvidenceInternal(evidenceId, true);
    }

    public RoomId getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(RoomId currentRoomId) {
        if (currentRoomId == null) {
            return;
        }

        this.currentRoomId = currentRoomId;
        savePlayState();
    }

    public void savePlayState() {
        if (gameRepository == null) {
            return;
        }

        gameRepository.savePlayState(new GameSaveState(
                evidenceSystem.getCollectedEvidenceIds(),
                currentRoomId.name(),
                timelineSystem.getSelectedFinalTimelineEventIds(),
                timelineSystem.isFinalTimelineCompleted()
        ));
    }

    private EvidenceItem collectEvidenceInternal(String evidenceId, boolean shouldSave) {
        EvidenceItem evidenceItem = evidenceSystem.collectEvidence(evidenceId);
        if (evidenceItem != null) {
            threadSystem.onEvidenceCollected(evidenceId, evidenceSystem);
            timelineSystem.onThreadStateChanged(threadSystem);
            statementSystem.onEvidenceCollected(evidenceSystem);
            statementSystem.onTimelineStateChanged(timelineSystem);
            if (shouldSave) {
                savePlayState();
            }
        }

        return evidenceItem;
    }

    private void restorePlayStateIfAvailable() {
        if (gameRepository == null || savedStateLoaded) {
            return;
        }

        GameSaveState gameSaveState = gameRepository.loadPlayState(RoomId.LIVING_ROOM.name());
        currentRoomId = parseRoomId(gameSaveState.getCurrentRoomId());

        for (String evidenceId : gameSaveState.getCollectedEvidenceIds()) {
            collectEvidenceInternal(evidenceId, false);
        }

        timelineSystem.restoreFinalTimelineState(
                gameSaveState.getSelectedFinalTimelineEventIds(),
                gameSaveState.isFinalTimelineCompleted()
        );
        savedStateLoaded = true;
    }

    private RoomId parseRoomId(String roomId) {
        if (roomId == null) {
            return RoomId.LIVING_ROOM;
        }

        try {
            return RoomId.valueOf(roomId);
        } catch (IllegalArgumentException exception) {
            return RoomId.LIVING_ROOM;
        }
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
                "E03_BROKEN_VASE_DISTURBED_TABLE",
                "Broken Vase / Disturbed Table",
                "Physical",
                "Living Room / Body Scene",
                "There was a short localized struggle near the body.",
                "The disturbance is concentrated, suggesting an impulsive escalation rather than a long fight."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E04_BLOOD_MARKED_COMPANY_AWARD",
                "Blood-Marked Company Award",
                "Physical",
                "Living Room / Body Scene",
                "The company award is likely the murder weapon.",
                "Blood marking and impact position connect the object to the fatal struggle."
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
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E12_ACCESS_DATA_SALE_FILE",
                "Access-Data Sale File",
                "Digital / motive",
                "Office terminal",
                "Victim and partner were involved in illegal access-data activity.",
                "The file lists paid transfers tied to apartment-system access data."
        ));
        evidenceSystem.registerEvidence(new EvidenceItem(
                "E13_VICTIM_DRAFT_DISCLOSURE_EMAIL",
                "Victim Draft Disclosure Email",
                "Digital / motive",
                "Office terminal",
                "Victim intended to expose or withdraw from the illegal operation.",
                "The unsent draft names the operation and implies the partner would be legally exposed."
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
        threadSystem.registerThread(new InvestigationThread(
                "T04_DATA_SALE_MOTIVE",
                "Data-Sale Motive",
                "Why would the partner escalate to violence?",
                "The victim was preparing to expose an illegal operation the partner depended on."
        ));
        threadSystem.registerThread(new InvestigationThread(
                "T05_SCENE_DISTURBANCE",
                "Scene Disturbance",
                "Was this planned, staged, or impulsive?",
                "The killing was an impulsive escalation followed by a calculated cover-up."
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

        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT08_SCHEDULED_MESSAGE_SENDS",
                8,
                "21:07",
                "Scheduled message sends",
                "The late message is delivered, but it is no longer proof the victim was alive."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT05_PARTNER_EXITS",
                5,
                "19:42",
                "Partner exits",
                "The partner leaves after staging the apartment systems."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT03_MURDER_OCCURS",
                3,
                "19:27",
                "Murder occurs",
                "The argument turns violent in the living room."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT09_BODY_DISCOVERED",
                9,
                "22:30",
                "Body discovered",
                "Security finds the body during the welfare check."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT01_PARTNER_ARRIVES",
                1,
                "19:05",
                "Partner arrives",
                "The business partner enters for a private meeting."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT06_WIFE_REMOTE_ACCESS",
                6,
                "20:18",
                "Wife remote access",
                "The wife checks apartment systems after the victim is already dead."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT04_STAGING_BEGINS",
                4,
                "19:34",
                "AC and smart-home staging begins",
                "Cooling and automated activity start shaping the false death window."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT07_CCTV_OUTAGE",
                7,
                "20:40",
                "CCTV outage",
                "The camera outage complicates the record but does not explain the murder."
        ));
        timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                "FT02_ARGUMENT_ESCALATES",
                2,
                "19:18",
                "Argument escalates",
                "The victim says he intends to expose the access-data operation."
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
