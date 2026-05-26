package motaz.pharaon.adu.ac.ae.traceroute.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;

public class GameContent {
    /*
     * Purpose:
     * - Immutable bundle of static MVP case content loaded from assets.
     *
     * Responsibilities:
     * - Keep authored case data separate from runtime save state.
     * - Provide evidence, threads, timeline events, and statements to GameRuntime.
     * - Avoid letting gameplay systems know whether content came from JSON or test builders.
     */

    private final List<EvidenceItem> evidenceItems;
    private final List<InvestigationThread> investigationThreads;
    private final List<TimelineEvent> timelineEvents;
    private final List<FinalTimelineEvent> finalTimelineEvents;
    private final List<SuspectStatement> suspectStatements;
    private final List<RoomDefinition> roomDefinitions;
    private final TerminalContent terminalContent;
    private final DialogueScript finalConfrontationScript;
    private final CaseCompletion caseCompletion;

    public GameContent(
            List<EvidenceItem> evidenceItems,
            List<InvestigationThread> investigationThreads,
            List<TimelineEvent> timelineEvents,
            List<FinalTimelineEvent> finalTimelineEvents,
            List<SuspectStatement> suspectStatements,
            List<RoomDefinition> roomDefinitions,
            TerminalContent terminalContent,
            DialogueScript finalConfrontationScript,
            CaseCompletion caseCompletion
    ) {
        this.evidenceItems = immutableCopy(evidenceItems);
        this.investigationThreads = immutableCopy(investigationThreads);
        this.timelineEvents = immutableCopy(timelineEvents);
        this.finalTimelineEvents = immutableCopy(finalTimelineEvents);
        this.suspectStatements = immutableCopy(suspectStatements);
        this.roomDefinitions = immutableCopy(roomDefinitions);
        this.terminalContent = terminalContent == null
                ? TerminalContent.empty()
                : terminalContent;
        this.finalConfrontationScript = finalConfrontationScript == null
                ? DialogueScript.empty()
                : finalConfrontationScript;
        this.caseCompletion = caseCompletion == null
                ? CaseCompletion.empty()
                : caseCompletion;
    }

    public static GameContent empty() {
        return new GameContent(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                TerminalContent.empty(),
                DialogueScript.empty(),
                CaseCompletion.empty()
        );
    }

    public List<EvidenceItem> getEvidenceItems() {
        return evidenceItems;
    }

    public List<InvestigationThread> getInvestigationThreads() {
        return investigationThreads;
    }

    public List<TimelineEvent> getTimelineEvents() {
        return timelineEvents;
    }

    public List<FinalTimelineEvent> getFinalTimelineEvents() {
        return finalTimelineEvents;
    }

    public List<SuspectStatement> getSuspectStatements() {
        return suspectStatements;
    }

    public List<RoomDefinition> getRoomDefinitions() {
        return roomDefinitions;
    }

    public TerminalContent getTerminalContent() {
        return terminalContent;
    }

    public DialogueScript getFinalConfrontationScript() {
        return finalConfrontationScript;
    }

    public CaseCompletion getCaseCompletion() {
        return caseCompletion;
    }

    private static <T> List<T> immutableCopy(List<T> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(new ArrayList<>(values));
    }
}
