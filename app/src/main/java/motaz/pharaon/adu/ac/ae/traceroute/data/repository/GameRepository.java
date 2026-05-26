package motaz.pharaon.adu.ac.ae.traceroute.data.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.data.db.GameContract;
import motaz.pharaon.adu.ac.ae.traceroute.data.db.GameDatabase;

public class GameRepository {
    /*
     * Purpose:
     * - Single facade for game data access.
     *
     * Responsibilities:
     * - Provide static case content to gameplay systems.
     * - Save/load playthrough state such as discovered evidence, thread conclusions, current room, and final timeline state.
     * - Keep gameplay systems from depending directly on SQLite or JSON parsing.
     *
     * Depends on:
     * - DataLoader for static content.
     * - GameDatabase for SQLite save state if SQLite is used.
     *
     * Used by:
     * - GameController.
     * - EvidenceSystem, ThreadSystem, TimelineSystem through GameController or direct injection.
     */

    private static final String LIST_SEPARATOR = "|";
    private static final String TRUE_VALUE = "true";

    private final GameDatabase gameDatabase;

    public GameRepository(Context context) {
        gameDatabase = new GameDatabase(context.getApplicationContext());
    }

    public GameSaveState loadPlayState(String defaultRoomId) {
        String collectedEvidence = gameDatabase.getValue(
                GameContract.SaveState.KEY_COLLECTED_EVIDENCE,
                ""
        );
        String currentRoom = gameDatabase.getValue(
                GameContract.SaveState.KEY_CURRENT_ROOM,
                defaultRoomId
        );
        String selectedFinalTimeline = gameDatabase.getValue(
                GameContract.SaveState.KEY_SELECTED_FINAL_TIMELINE,
                ""
        );
        String finalTimelineCompleted = gameDatabase.getValue(
                GameContract.SaveState.KEY_FINAL_TIMELINE_COMPLETED,
                "false"
        );

        return new GameSaveState(
                splitList(collectedEvidence),
                currentRoom,
                splitList(selectedFinalTimeline),
                TRUE_VALUE.equals(finalTimelineCompleted)
        );
    }

    public void savePlayState(GameSaveState gameSaveState) {
        gameDatabase.putValue(
                GameContract.SaveState.KEY_COLLECTED_EVIDENCE,
                joinList(gameSaveState.getCollectedEvidenceIds())
        );
        gameDatabase.putValue(
                GameContract.SaveState.KEY_CURRENT_ROOM,
                gameSaveState.getCurrentRoomId()
        );
        gameDatabase.putValue(
                GameContract.SaveState.KEY_SELECTED_FINAL_TIMELINE,
                joinList(gameSaveState.getSelectedFinalTimelineEventIds())
        );
        gameDatabase.putValue(
                GameContract.SaveState.KEY_FINAL_TIMELINE_COMPLETED,
                gameSaveState.isFinalTimelineCompleted() ? "true" : "false"
        );
    }

    public void clearPlayState() {
        gameDatabase.clearSaveState();
    }

    private String joinList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < values.size(); index++) {
            if (index > 0) {
                stringBuilder.append(LIST_SEPARATOR);
            }
            stringBuilder.append(values.get(index));
        }

        return stringBuilder.toString();
    }

    private List<String> splitList(String storedValue) {
        if (storedValue == null || storedValue.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String[] parts = storedValue.split("\\|");
        List<String> values = new ArrayList<>();
        for (String part : parts) {
            if (!part.trim().isEmpty()) {
                values.add(part);
            }
        }

        return values;
    }
}
