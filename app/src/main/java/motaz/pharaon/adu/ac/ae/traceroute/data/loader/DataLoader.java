package motaz.pharaon.adu.ac.ae.traceroute.data.loader;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.GameContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.CaseCompletion;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueLine;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.DialogueScript;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotActionType;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.VisualAnnotation;

public class DataLoader {
    /*
     * Purpose:
     * - Loads static case content from assets.
     *
     * Responsibilities:
     * - Load evidence definitions, timeline events, thread definitions, and statements.
     * - Keep static content separate from save-state persistence.
     * - Hide JSON parsing details from gameplay systems when JSON is added.
     *
     * Depends on:
     * - Android AssetManager.
     * - data.model classes.
     *
     * Used by:
     * - GameController during startup.
     * - GameRepository if static content loading is centralized there.
     */

    private static final String MVP_CASE_PATH = "data/case/mvp_case.json";

    private final Context context;

    public DataLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    public GameContent loadGameContent() {
        try {
            JSONObject root = new JSONObject(readAsset(MVP_CASE_PATH));
            return new GameContent(
                    parseEvidence(root.getJSONArray("evidence")),
                    parseThreads(root.getJSONArray("threads")),
                    parseTimelineEvents(root.getJSONArray("timelineEvents")),
                    parseFinalTimelineEvents(root.getJSONArray("finalTimelineEvents")),
                    parseStatements(root.getJSONArray("statements")),
                    parseRooms(root.getJSONArray("rooms")),
                    parseTerminalContent(root.getJSONObject("terminal")),
                    parseDialogueScript(root.getJSONObject("finalConfrontation")),
                    parseCaseCompletion(root.getJSONObject("caseCompletion"))
            );
        } catch (IOException | JSONException exception) {
            throw new IllegalStateException("Unable to load case content from " + MVP_CASE_PATH, exception);
        }
    }

    private List<EvidenceItem> parseEvidence(JSONArray array) throws JSONException {
        List<EvidenceItem> evidenceItems = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            evidenceItems.add(new EvidenceItem(
                    item.getString("id"),
                    item.getString("title"),
                    item.getString("category"),
                    item.getString("source"),
                    item.getString("playerSummary"),
                    item.optString("technicalNote", ""),
                    item.optString("thumbnailAssetPath", ""),
                    item.optString("detailAssetPath", "")
            ));
        }

        return evidenceItems;
    }

    private List<InvestigationThread> parseThreads(JSONArray array) throws JSONException {
        List<InvestigationThread> threads = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            threads.add(new InvestigationThread(
                    item.getString("id"),
                    item.getString("name"),
                    item.getString("coreQuestion"),
                    item.getString("stabilizedConclusion")
            ));
        }

        return threads;
    }

    private List<TimelineEvent> parseTimelineEvents(JSONArray array) throws JSONException {
        List<TimelineEvent> timelineEvents = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            timelineEvents.add(new TimelineEvent(
                    item.getString("id"),
                    item.getString("displayTime"),
                    item.getString("title"),
                    item.getString("initialInterpretation"),
                    item.getString("resolvedInterpretation")
            ));
        }

        return timelineEvents;
    }

    private List<FinalTimelineEvent> parseFinalTimelineEvents(JSONArray array) throws JSONException {
        List<FinalTimelineEvent> finalTimelineEvents = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            finalTimelineEvents.add(new FinalTimelineEvent(
                    item.getString("id"),
                    item.getInt("orderIndex"),
                    item.getString("displayTime"),
                    item.getString("title"),
                    item.getString("summary")
            ));
        }

        return finalTimelineEvents;
    }

    private List<SuspectStatement> parseStatements(JSONArray array) throws JSONException {
        List<SuspectStatement> statements = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            statements.add(new SuspectStatement(
                    item.getString("id"),
                    item.getString("speaker"),
                    item.getString("claim"),
                    item.getString("suspiciousDetail"),
                    item.getString("contradictionText")
            ));
        }

        return statements;
    }

    private List<RoomDefinition> parseRooms(JSONArray array) throws JSONException {
        List<RoomDefinition> rooms = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            rooms.add(new RoomDefinition(
                    RoomId.valueOf(item.getString("id")),
                    item.getString("displayName"),
                    item.getString("debugContextText"),
                    item.getString("debugHotspotHint"),
                    item.optString("backgroundTexturePath", ""),
                    parseHotspots(item.getJSONArray("hotspots")),
                    parseVisualAnnotations(item.optJSONArray("visualAnnotations"))
            ));
        }

        return rooms;
    }

    private DialogueScript parseDialogueScript(JSONObject object) throws JSONException {
        JSONArray linesArray = object.getJSONArray("lines");
        List<DialogueLine> lines = new ArrayList<>();

        for (int index = 0; index < linesArray.length(); index++) {
            JSONObject line = linesArray.getJSONObject(index);
            lines.add(new DialogueLine(
                    line.getString("speaker"),
                    line.getString("text")
            ));
        }

        return new DialogueScript(
                object.getString("id"),
                object.getString("title"),
                lines
        );
    }

    private CaseCompletion parseCaseCompletion(JSONObject object) throws JSONException {
        return new CaseCompletion(
                object.getString("title"),
                object.getString("body")
        );
    }

    private TerminalContent parseTerminalContent(JSONObject object) throws JSONException {
        return new TerminalContent(
                object.getString("title"),
                parseStringArray(object.getJSONArray("introLines")),
                parseTerminalPanels(object.getJSONArray("panels"))
        );
    }

    private List<TerminalPanel> parseTerminalPanels(JSONArray array) throws JSONException {
        List<TerminalPanel> panels = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            panels.add(new TerminalPanel(
                    item.getString("command"),
                    item.getString("label"),
                    item.getString("evidenceId"),
                    parseStringArray(item.optJSONArray("aliases")),
                    parseStringArray(item.getJSONArray("outputLines"))
            ));
        }

        return panels;
    }

    private List<String> parseStringArray(JSONArray array) throws JSONException {
        List<String> values = new ArrayList<>();
        if (array == null) {
            return values;
        }

        for (int index = 0; index < array.length(); index++) {
            values.add(array.getString(index));
        }

        return values;
    }

    private List<Hotspot> parseHotspots(JSONArray array) throws JSONException {
        List<Hotspot> hotspots = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            HotspotActionType actionType = HotspotActionType.valueOf(item.getString("actionType"));
            RoomId targetRoomId = null;
            if (item.has("targetRoomId") && !item.isNull("targetRoomId")) {
                targetRoomId = RoomId.valueOf(item.getString("targetRoomId"));
            }

            hotspots.add(new Hotspot(
                    item.getString("id"),
                    (float) item.getDouble("x"),
                    (float) item.getDouble("y"),
                    (float) item.getDouble("width"),
                    (float) item.getDouble("height"),
                    actionType,
                    targetRoomId,
                    item.optString("payloadId", null)
            ));
        }

        return hotspots;
    }

    private List<VisualAnnotation> parseVisualAnnotations(JSONArray array) throws JSONException {
        List<VisualAnnotation> annotations = new ArrayList<>();
        if (array == null) {
            return annotations;
        }

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            annotations.add(new VisualAnnotation(
                    item.getString("id"),
                    item.getString("label"),
                    (float) item.getDouble("x"),
                    (float) item.getDouble("y"),
                    (float) item.getDouble("width"),
                    (float) item.getDouble("height")
            ));
        }

        return annotations;
    }

    private String readAsset(String assetPath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        try (InputStream inputStream = context.getAssets().open(assetPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     inputStream,
                     StandardCharsets.UTF_8
             ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
        }

        return stringBuilder.toString();
    }
}
