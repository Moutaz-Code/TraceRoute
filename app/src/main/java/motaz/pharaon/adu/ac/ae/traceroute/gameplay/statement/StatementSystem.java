package motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;

public class StatementSystem {
    /*
     * Purpose:
     * - Owns suspect statement unlocks and resolution state.
     *
     * Responsibilities:
     * - Unlock wife, security/staff, and business partner statements as evidence appears.
     * - Mark statements contradicted or resolved.
     * - Provide statement data to handheld UI.
     *
     * Depends on:
     * - SuspectStatement data.
     * - EvidenceSystem/ThreadSystem notifications through GameController.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - HandheldFragment through HandheldController.
     */

    private static final String SMART_LOCK_LOG_ID = "E05_SMART_LOCK_ENTRY_EXIT_LOG";
    private static final String PARTNER_STATEMENT_ID = "S01_PARTNER_STATEMENT";
    private static final String POST_21_ACTIVITY_EVENT_ID = "TL01_POST_21_ACTIVITY_UNRELIABLE";

    private final Map<String, SuspectStatement> statements;

    public StatementSystem() {
        statements = new LinkedHashMap<>();
    }

    public void registerStatement(SuspectStatement suspectStatement) {
        statements.put(suspectStatement.getId(), suspectStatement);
    }

    public void onEvidenceCollected(EvidenceSystem evidenceSystem) {
        SuspectStatement partnerStatement = statements.get(PARTNER_STATEMENT_ID);
        if (partnerStatement == null) {
            return;
        }

        if (evidenceSystem.isCollected(SMART_LOCK_LOG_ID)) {
            partnerStatement.setVisible(true);
        }
    }

    public void onTimelineStateChanged(TimelineSystem timelineSystem) {
        SuspectStatement partnerStatement = statements.get(PARTNER_STATEMENT_ID);
        TimelineEvent post21Activity = timelineSystem.getEvent(POST_21_ACTIVITY_EVENT_ID);

        if (partnerStatement == null || post21Activity == null) {
            return;
        }

        if (partnerStatement.isVisible() && post21Activity.isStabilized()) {
            partnerStatement.setContradicted(true);
        }
    }

    public List<SuspectStatement> getVisibleStatements() {
        List<SuspectStatement> visibleStatements = new ArrayList<>();

        for (SuspectStatement statement : statements.values()) {
            if (statement.isVisible()) {
                visibleStatements.add(statement);
            }
        }

        return visibleStatements;
    }
}
