package motaz.pharaon.adu.ac.ae.traceroute.data.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameSaveState {
    /*
     * Purpose:
     * - Data transfer object for persisted MVP play state.
     *
     * Responsibilities:
     * - Carry collected evidence ids, current room, and final timeline state.
     * - Keep SQLite row format out of gameplay systems.
     *
     * Depends on:
     * - Plain Java collections only.
     *
     * Used by:
     * - GameRepository for save/load.
     * - GameRuntime for restoring gameplay state.
     */

    private final List<String> collectedEvidenceIds;
    private final String currentRoomId;
    private final List<String> selectedFinalTimelineEventIds;
    private final boolean finalTimelineCompleted;

    public GameSaveState(
            List<String> collectedEvidenceIds,
            String currentRoomId,
            List<String> selectedFinalTimelineEventIds,
            boolean finalTimelineCompleted
    ) {
        this.collectedEvidenceIds = new ArrayList<>(collectedEvidenceIds);
        this.currentRoomId = currentRoomId;
        this.selectedFinalTimelineEventIds = new ArrayList<>(selectedFinalTimelineEventIds);
        this.finalTimelineCompleted = finalTimelineCompleted;
    }

    public static GameSaveState empty(String defaultRoomId) {
        return new GameSaveState(
                Collections.emptyList(),
                defaultRoomId,
                Collections.emptyList(),
                false
        );
    }

    public List<String> getCollectedEvidenceIds() {
        return new ArrayList<>(collectedEvidenceIds);
    }

    public String getCurrentRoomId() {
        return currentRoomId;
    }

    public List<String> getSelectedFinalTimelineEventIds() {
        return new ArrayList<>(selectedFinalTimelineEventIds);
    }

    public boolean isFinalTimelineCompleted() {
        return finalTimelineCompleted;
    }
}
