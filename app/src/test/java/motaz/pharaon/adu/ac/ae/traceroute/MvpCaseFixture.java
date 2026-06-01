package motaz.pharaon.adu.ac.ae.traceroute;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.guidance.GuidanceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.StatementSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;

class MvpCaseFixture {
    final EvidenceSystem evidenceSystem;
    final ThreadSystem threadSystem;
    final TimelineSystem timelineSystem;
    final StatementSystem statementSystem;

    private final GuidanceSystem guidanceSystem;
    private final LaptopContent laptopContent;
    private final List<FinalTimelineEvent> finalTimelineEvents;

    private MvpCaseFixture(LaptopContent laptopContent, List<FinalTimelineEvent> finalTimelineEvents) {
        evidenceSystem = new EvidenceSystem();
        threadSystem = new ThreadSystem();
        timelineSystem = new TimelineSystem();
        statementSystem = new StatementSystem();
        guidanceSystem = new GuidanceSystem();
        this.laptopContent = laptopContent;
        this.finalTimelineEvents = finalTimelineEvents;
    }

    static MvpCaseFixture load() throws Exception {
        JSONObject root = loadCaseJson();
        MvpCaseFixture fixture = new MvpCaseFixture(
                parseLaptopContent(root.getJSONObject("laptop")),
                parseFinalTimelineEvents(root.getJSONArray("finalTimelineEvents"))
        );

        fixture.registerEvidence(root.getJSONArray("evidence"));
        fixture.registerThreads(root.getJSONArray("threads"));
        fixture.registerTimelineEvents(root.getJSONArray("timelineEvents"));
        fixture.registerFinalTimelineEvents();
        fixture.registerStatements(root.getJSONArray("statements"));

        return fixture;
    }

    EvidenceItem collect(String evidenceId) {
        EvidenceItem evidenceItem = evidenceSystem.collectEvidence(evidenceId);
        if (evidenceItem == null) {
            throw new IllegalArgumentException("Missing evidence: " + evidenceId);
        }

        threadSystem.onEvidenceCollected(evidenceId, evidenceSystem);
        timelineSystem.onThreadStateChanged(threadSystem);
        statementSystem.onEvidenceCollected(evidenceSystem);
        statementSystem.onTimelineStateChanged(timelineSystem);

        return evidenceItem;
    }

    LaptopPanelProcessor createLaptopPanelProcessor() {
        return new LaptopPanelProcessor(laptopContent, evidenceId -> {
            boolean alreadyCollected = evidenceSystem.isCollected(evidenceId);
            return new LaptopPanelProcessor.EvidenceCollectionResult(
                    collect(evidenceId),
                    alreadyCollected
            );
        });
    }

    String getCurrentLead() {
        return guidanceSystem.getCurrentLead(evidenceSystem, threadSystem, timelineSystem);
    }

    List<String> getFinalTimelineIdsInAuthoredOrder() {
        List<FinalTimelineEvent> sortedEvents = getFinalTimelineEventsSortedByOrder();
        List<String> eventIds = new ArrayList<>();

        for (FinalTimelineEvent finalTimelineEvent : sortedEvents) {
            eventIds.add(finalTimelineEvent.getId());
        }

        return eventIds;
    }

    List<String> getFinalTimelineIdsInReverseAuthoredOrder() {
        List<String> eventIds = getFinalTimelineIdsInAuthoredOrder();
        Collections.reverse(eventIds);
        return eventIds;
    }

    private static JSONObject loadCaseJson() throws Exception {
        Path appRelativePath = Paths.get("src/main/assets/data/case/mvp_case.json");
        Path rootRelativePath = Paths.get("app/src/main/assets/data/case/mvp_case.json");
        Path casePath = Files.exists(appRelativePath) ? appRelativePath : rootRelativePath;

        String json = new String(Files.readAllBytes(casePath), StandardCharsets.UTF_8);
        return new JSONObject(json);
    }

    private void registerEvidence(JSONArray array) throws Exception {
        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            evidenceSystem.registerEvidence(new EvidenceItem(
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
    }

    private void registerThreads(JSONArray array) throws Exception {
        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            threadSystem.registerThread(new InvestigationThread(
                    item.getString("id"),
                    item.getString("name"),
                    item.getString("coreQuestion"),
                    item.getString("stabilizedConclusion")
            ));
        }
    }

    private void registerTimelineEvents(JSONArray array) throws Exception {
        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            timelineSystem.registerEvent(new TimelineEvent(
                    item.getString("id"),
                    item.getString("displayTime"),
                    item.getString("title"),
                    item.getString("initialInterpretation"),
                    item.getString("resolvedInterpretation")
            ));
        }
    }

    private void registerFinalTimelineEvents() {
        for (FinalTimelineEvent finalTimelineEvent : finalTimelineEvents) {
            timelineSystem.registerFinalTimelineEvent(finalTimelineEvent);
        }
    }

    private void registerStatements(JSONArray array) throws Exception {
        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            statementSystem.registerStatement(new SuspectStatement(
                    item.getString("id"),
                    item.getString("speaker"),
                    item.getString("claim"),
                    item.getString("suspiciousDetail"),
                    item.getString("contradictionText")
            ));
        }
    }

    private List<FinalTimelineEvent> getFinalTimelineEventsSortedByOrder() {
        List<FinalTimelineEvent> sortedEvents = new ArrayList<>(finalTimelineEvents);
        Collections.sort(sortedEvents, new Comparator<FinalTimelineEvent>() {
            @Override
            public int compare(FinalTimelineEvent left, FinalTimelineEvent right) {
                return left.getOrderIndex() - right.getOrderIndex();
            }
        });
        return sortedEvents;
    }

    private static List<FinalTimelineEvent> parseFinalTimelineEvents(JSONArray array) throws Exception {
        List<FinalTimelineEvent> events = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            events.add(new FinalTimelineEvent(
                    item.getString("id"),
                    item.getInt("orderIndex"),
                    item.getString("displayTime"),
                    item.getString("title"),
                    item.getString("summary")
            ));
        }

        return events;
    }

    private static LaptopContent parseLaptopContent(JSONObject object) throws Exception {
        return new LaptopContent(
                object.getString("title"),
                parseStringArray(object.getJSONArray("introLines")),
                parseLaptopPanels(object.getJSONArray("panels"))
        );
    }

    private static List<LaptopPanel> parseLaptopPanels(JSONArray array) throws Exception {
        List<LaptopPanel> panels = new ArrayList<>();

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.getJSONObject(index);
            panels.add(new LaptopPanel(
                    item.getString("panelId"),
                    item.getString("label"),
                    item.getString("evidenceId"),
                    parseStringArray(item.getJSONArray("contentLines"))
            ));
        }

        return panels;
    }

    private static List<String> parseStringArray(JSONArray array) throws Exception {
        List<String> values = new ArrayList<>();
        if (array == null) {
            return values;
        }

        for (int index = 0; index < array.length(); index++) {
            values.add(array.getString(index));
        }

        return values;
    }
}
