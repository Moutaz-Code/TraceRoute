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
    private static final String WIFE_REMOTE_ACCESS_LOG_ID = "E09_WIFE_REMOTE_ACCESS_LOG";
    private static final String CCTV_OUTAGE_NOTE_ID = "E10_CCTV_OUTAGE_NOTE";
    private static final String PARTNER_STATEMENT_ID = "S01_PARTNER_STATEMENT";
    private static final String WIFE_STATEMENT_ID = "S02_WIFE_STATEMENT";
    private static final String SECURITY_STATEMENT_ID = "S03_SECURITY_STATEMENT";
    private static final String POST_21_ACTIVITY_EVENT_ID = "TL01_POST_21_ACTIVITY_UNRELIABLE";

    private final Map<String, SuspectStatement> statements;

    public StatementSystem() {
        statements = new LinkedHashMap<>();
    }

    public void registerStatement(SuspectStatement suspectStatement) {
        statements.put(suspectStatement.getId(), suspectStatement);
    }

    public void onEvidenceCollected(EvidenceSystem evidenceSystem) {
        if (evidenceSystem == null) {
            return;
        }

        if (evidenceSystem.isCollected(SMART_LOCK_LOG_ID)) {
            setStatementVisible(PARTNER_STATEMENT_ID);
        }

        if (evidenceSystem.isCollected(WIFE_REMOTE_ACCESS_LOG_ID)) {
            setStatementVisible(WIFE_STATEMENT_ID);
        }

        if (evidenceSystem.isCollected(CCTV_OUTAGE_NOTE_ID)) {
            setStatementVisible(SECURITY_STATEMENT_ID);
        }
    }

    public void onTimelineStateChanged(TimelineSystem timelineSystem) {
        if (timelineSystem == null) {
            return;
        }

        TimelineEvent post21Activity = timelineSystem.getEvent(POST_21_ACTIVITY_EVENT_ID);

        if (post21Activity == null || !post21Activity.isStabilized()) {
            return;
        }

        setStatementContradictedIfVisible(PARTNER_STATEMENT_ID);
        setStatementContradictedIfVisible(WIFE_STATEMENT_ID);
        setStatementContradictedIfVisible(SECURITY_STATEMENT_ID);
    }

    public SuspectStatement getStatement(String statementId) {
        return statements.get(statementId);
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

    private void setStatementVisible(String statementId) {
        SuspectStatement statement = statements.get(statementId);
        if (statement != null) {
            statement.setVisible(true);
        }
    }

    private void setStatementContradictedIfVisible(String statementId) {
        SuspectStatement statement = statements.get(statementId);
        if (statement != null && statement.isVisible()) {
            statement.setContradicted(true);
        }
    }
}
