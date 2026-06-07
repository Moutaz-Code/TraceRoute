package motaz.pharaon.adu.ac.ae.traceroute.core;

import android.content.Context;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.GameContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.CaseCompletion;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueScript;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.repository.GameRepository;
import motaz.pharaon.adu.ac.ae.traceroute.data.repository.GameSaveState;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.guidance.GuidanceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.StatementSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final GuidanceSystem guidanceSystem;
    private List<RoomDefinition> roomDefinitions;
    private LaptopContent laptopContent;
    private DialogueScript finalConfrontationScript;
    private CaseCompletion caseCompletion;
    private GameRepository gameRepository;
    private RoomId currentRoomId;
    private boolean savedStateLoaded;
    private final Set<String> viewedRecordIds = new HashSet<>();

    private GameRuntime(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        evidenceSystem = new EvidenceSystem();
        threadSystem = new ThreadSystem();
        timelineSystem = new TimelineSystem();
        statementSystem = new StatementSystem();
        guidanceSystem = new GuidanceSystem();
        roomDefinitions = Collections.emptyList();
        laptopContent = LaptopContent.empty();
        finalConfrontationScript = DialogueScript.empty();
        caseCompletion = CaseCompletion.empty();
        currentRoomId = RoomId.LIVING_ROOM;
        savedStateLoaded = false;
        registerStaticContent(loadStaticContent(gameRepository));
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

    public static synchronized void resetPlaythrough(Context context) {
        GameRepository gameRepository = new GameRepository(context.getApplicationContext());
        gameRepository.clearPlayState();
        instance = new GameRuntime(gameRepository);
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

    public boolean isRecordViewed(String recordId) {
        return viewedRecordIds.contains(recordId);
    }

    public void markRecordViewed(String recordId) {
        viewedRecordIds.add(recordId);
    }

    public List<RoomDefinition> getRoomDefinitions() {
        return Collections.unmodifiableList(roomDefinitions);
    }

    public DialogueScript getFinalConfrontationScript() {
        return finalConfrontationScript;
    }

    public LaptopContent getLaptopContent() {
        return laptopContent;
    }

    public CaseCompletion getCaseCompletion() {
        return caseCompletion;
    }

    public String getCurrentLead() {
        return guidanceSystem.getCurrentLead(evidenceSystem, threadSystem, timelineSystem);
    }

    public EvidenceItem collectEvidence(String evidenceId) {
        return collectEvidenceInternal(evidenceId, true);
    }

    public void debugCollectAllEvidence() {
        for (String evidenceId : evidenceSystem.getAllEvidenceIds()) {
            collectEvidenceInternal(evidenceId, false);
        }

        savePlayState();
    }

    public RoomId getCurrentRoomId() {
        return currentRoomId;
    }

    public void setCurrentRoomId(RoomId currentRoomId) {
        if (!isPlayableDemoRoom(currentRoomId)) {
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
            RoomId parsedRoomId = RoomId.valueOf(roomId);
            return isPlayableDemoRoom(parsedRoomId) ? parsedRoomId : RoomId.LIVING_ROOM;
        } catch (IllegalArgumentException exception) {
            return RoomId.LIVING_ROOM;
        }
    }

    private boolean isPlayableDemoRoom(RoomId roomId) {
        if (roomId == null) {
            return false;
        }

        return roomId == RoomId.LIVING_ROOM
                || roomId == RoomId.ENTRY_SMART_LOCK
                || roomId == RoomId.KITCHEN_SIDE_AREA
                || roomId == RoomId.OFFICE_DEVICE_CORNER;
    }

    private GameContent loadStaticContent(GameRepository gameRepository) {
        if (gameRepository == null) {
            return GameContent.empty();
        }

        return gameRepository.loadGameContent();
    }

    private void registerStaticContent(GameContent gameContent) {
        for (EvidenceItem evidenceItem : gameContent.getEvidenceItems()) {
            evidenceSystem.registerEvidence(evidenceItem);
        }

        for (InvestigationThread investigationThread : gameContent.getInvestigationThreads()) {
            threadSystem.registerThread(investigationThread);
        }

        for (TimelineEvent timelineEvent : gameContent.getTimelineEvents()) {
            timelineSystem.registerEvent(timelineEvent);
        }

        for (FinalTimelineEvent finalTimelineEvent : gameContent.getFinalTimelineEvents()) {
            timelineSystem.registerFinalTimelineEvent(finalTimelineEvent);
        }

        for (SuspectStatement suspectStatement : gameContent.getSuspectStatements()) {
            statementSystem.registerStatement(suspectStatement);
        }

        roomDefinitions = new ArrayList<>(gameContent.getRoomDefinitions());
        laptopContent = gameContent.getLaptopContent();
        finalConfrontationScript = gameContent.getFinalConfrontationScript();
        caseCompletion = gameContent.getCaseCompletion();
    }
}
