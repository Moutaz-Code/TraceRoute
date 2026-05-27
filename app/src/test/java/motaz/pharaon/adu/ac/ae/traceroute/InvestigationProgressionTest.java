package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.StatementSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvestigationProgressionTest {
    @Test
    public void deathWindowEvidenceStabilizesTimelineContradiction() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.collect("E01_BODY_CONDITION_REPORT");
        assertFalse(harness.threadSystem.getThread("T01_DEATH_WINDOW_INSTABILITY").isStabilized());
        assertTrue(harness.timelineSystem.getEvent("TL01_POST_21_ACTIVITY_UNRELIABLE").isVisible());
        assertTrue(harness.timelineSystem.getEvent("TL01_POST_21_ACTIVITY_UNRELIABLE").isContradicted());

        harness.collect("E02_THERMOSTAT_AC_LOG");
        assertTrue(harness.threadSystem.getThread("T01_DEATH_WINDOW_INSTABILITY").isStabilized());
        assertTrue(harness.timelineSystem.getEvent("TL01_POST_21_ACTIVITY_UNRELIABLE").isStabilized());
    }

    @Test
    public void coreEvidenceUnlocksFinalTimeline() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.collectAllCoreEvidence();

        assertTrue(harness.threadSystem.getThread("T01_DEATH_WINDOW_INSTABILITY").isStabilized());
        assertTrue(harness.threadSystem.getThread("T02_POSTMORTEM_ACTIVITY").isStabilized());
        assertTrue(harness.threadSystem.getThread("T03_APARTMENT_ACCESS").isStabilized());
        assertTrue(harness.threadSystem.getThread("T04_DATA_SALE_MOTIVE").isStabilized());
        assertTrue(harness.threadSystem.getThread("T05_SCENE_DISTURBANCE").isStabilized());
        assertTrue(harness.timelineSystem.isFinalTimelineUnlocked());
    }

    @Test
    public void wifeAndSecurityStatementsUnlockFromTheirEvidence() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.collect("E09_WIFE_REMOTE_ACCESS_LOG");
        harness.collect("E10_CCTV_OUTAGE_NOTE");

        assertTrue(harness.statementSystem.getStatement("S02_WIFE_STATEMENT").isVisible());
        assertTrue(harness.statementSystem.getStatement("S03_SECURITY_STATEMENT").isVisible());
    }

    @Test
    public void wifeAndSecurityStatementsResolveWhenDeathWindowStabilizes() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.collect("E09_WIFE_REMOTE_ACCESS_LOG");
        harness.collect("E10_CCTV_OUTAGE_NOTE");
        harness.collect("E01_BODY_CONDITION_REPORT");
        harness.collect("E02_THERMOSTAT_AC_LOG");

        assertTrue(harness.timelineSystem.getEvent("TL01_POST_21_ACTIVITY_UNRELIABLE").isStabilized());
        assertTrue(harness.statementSystem.getStatement("S02_WIFE_STATEMENT").isContradicted());
        assertTrue(harness.statementSystem.getStatement("S03_SECURITY_STATEMENT").isContradicted());
    }

    @Test
    public void finalTimelineRejectsWrongOrderAndAcceptsCorrectOrder() {
        ProgressionHarness harness = new ProgressionHarness();
        harness.collectAllCoreEvidence();

        harness.timelineSystem.selectFinalTimelineEvent("FT02_ARGUMENT_ESCALATES");
        harness.timelineSystem.selectFinalTimelineEvent("FT01_PARTNER_ARRIVES");

        assertFalse(harness.timelineSystem.isFinalTimelineCompleted());
        assertEquals(0, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());

        harness.timelineSystem.selectFinalTimelineEvent("FT01_PARTNER_ARRIVES");
        harness.timelineSystem.selectFinalTimelineEvent("FT02_ARGUMENT_ESCALATES");

        assertTrue(harness.timelineSystem.isFinalTimelineCompleted());
        assertEquals(2, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());
    }

    @Test
    public void finalTimelineRestoreRejectsCompletedFlagWithoutCompleteCorrectSequence() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.timelineSystem.restoreFinalTimelineState(
                Collections.singletonList("FT02_ARGUMENT_ESCALATES"),
                true
        );

        assertFalse(harness.timelineSystem.isFinalTimelineCompleted());
        assertFalse(harness.timelineSystem.isFinalTimelineUnlocked());
        assertEquals(0, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());

        harness.collectAllCoreEvidence();

        assertTrue(harness.timelineSystem.isFinalTimelineUnlocked());
        assertFalse(harness.timelineSystem.isFinalTimelineCompleted());
    }

    @Test
    public void finalTimelineRestoreRejectsCorruptSelections() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.timelineSystem.restoreFinalTimelineState(
                Arrays.asList("FT02_ARGUMENT_ESCALATES", "FT01_PARTNER_ARRIVES"),
                true
        );

        assertFalse(harness.timelineSystem.isFinalTimelineCompleted());
        assertFalse(harness.timelineSystem.isFinalTimelineUnlocked());
        assertEquals(0, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());
    }

    @Test
    public void finalTimelineRestoreAcceptsCompleteCorrectSequence() {
        ProgressionHarness harness = new ProgressionHarness();

        harness.timelineSystem.restoreFinalTimelineState(
                Arrays.asList("FT01_PARTNER_ARRIVES", "FT02_ARGUMENT_ESCALATES"),
                true
        );

        assertTrue(harness.timelineSystem.isFinalTimelineCompleted());
        assertTrue(harness.timelineSystem.isFinalTimelineUnlocked());
        assertEquals(2, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());
    }

    @Test
    public void finalTimelineRestorePreservesCoreUnlockWhenNoSelectionWasSaved() {
        ProgressionHarness harness = new ProgressionHarness();
        harness.collectAllCoreEvidence();

        harness.timelineSystem.restoreFinalTimelineState(Collections.emptyList(), false);

        assertTrue(harness.timelineSystem.isFinalTimelineUnlocked());
        assertFalse(harness.timelineSystem.isFinalTimelineCompleted());
        assertEquals(0, harness.timelineSystem.getSelectedFinalTimelineEventIds().size());
    }

    private static class ProgressionHarness {
        private final EvidenceSystem evidenceSystem;
        private final ThreadSystem threadSystem;
        private final TimelineSystem timelineSystem;
        private final StatementSystem statementSystem;

        private ProgressionHarness() {
            evidenceSystem = new EvidenceSystem();
            threadSystem = new ThreadSystem();
            timelineSystem = new TimelineSystem();
            statementSystem = new StatementSystem();

            registerEvidenceDefinitions();
            registerThreads();
            registerTimeline();
            registerStatements();
        }

        private void collectAllCoreEvidence() {
            collect("E01_BODY_CONDITION_REPORT");
            collect("E02_THERMOSTAT_AC_LOG");
            collect("E03_BROKEN_VASE_DISTURBED_TABLE");
            collect("E04_BLOOD_MARKED_COMPANY_AWARD");
            collect("E05_SMART_LOCK_ENTRY_EXIT_LOG");
            collect("E06_SCHEDULED_VICTIM_MESSAGE");
            collect("E07_LAPTOP_SESSION_METADATA");
            collect("E08_ROUTER_ADMIN_ACCESS_RECORD");
            collect("E12_ACCESS_DATA_SALE_FILE");
            collect("E13_VICTIM_DRAFT_DISCLOSURE_EMAIL");
        }

        private void collect(String evidenceId) {
            evidenceSystem.collectEvidence(evidenceId);
            threadSystem.onEvidenceCollected(evidenceId, evidenceSystem);
            timelineSystem.onThreadStateChanged(threadSystem);
            statementSystem.onEvidenceCollected(evidenceSystem);
            statementSystem.onTimelineStateChanged(timelineSystem);
        }

        private void registerEvidenceDefinitions() {
            registerEvidence("E01_BODY_CONDITION_REPORT");
            registerEvidence("E02_THERMOSTAT_AC_LOG");
            registerEvidence("E03_BROKEN_VASE_DISTURBED_TABLE");
            registerEvidence("E04_BLOOD_MARKED_COMPANY_AWARD");
            registerEvidence("E05_SMART_LOCK_ENTRY_EXIT_LOG");
            registerEvidence("E06_SCHEDULED_VICTIM_MESSAGE");
            registerEvidence("E07_LAPTOP_SESSION_METADATA");
            registerEvidence("E08_ROUTER_ADMIN_ACCESS_RECORD");
            registerEvidence("E09_WIFE_REMOTE_ACCESS_LOG");
            registerEvidence("E10_CCTV_OUTAGE_NOTE");
            registerEvidence("E12_ACCESS_DATA_SALE_FILE");
            registerEvidence("E13_VICTIM_DRAFT_DISCLOSURE_EMAIL");
        }

        private void registerEvidence(String id) {
            evidenceSystem.registerEvidence(new EvidenceItem(
                    id,
                    id,
                    "test",
                    "test",
                    "test",
                    "test"
            ));
        }

        private void registerThreads() {
            threadSystem.registerThread(new InvestigationThread(
                    "T01_DEATH_WINDOW_INSTABILITY",
                    "Death Window Instability",
                    "When did the victim actually die?",
                    "The death window was distorted."
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
                    "Who could affect the apartment?",
                    "The manipulation required trusted technical privileges."
            ));
            threadSystem.registerThread(new InvestigationThread(
                    "T04_DATA_SALE_MOTIVE",
                    "Data-Sale Motive",
                    "Why would the partner escalate?",
                    "The victim was preparing to expose the operation."
            ));
            threadSystem.registerThread(new InvestigationThread(
                    "T05_SCENE_DISTURBANCE",
                    "Scene Disturbance",
                    "Was this planned or impulsive?",
                    "The killing was impulsive, followed by a cover-up."
            ));
        }

        private void registerTimeline() {
            timelineSystem.registerEvent(new TimelineEvent(
                    "TL01_POST_21_ACTIVITY_UNRELIABLE",
                    "After 21:00",
                    "Victim Activity Window",
                    "Initial",
                    "Resolved"
            ));
            timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                    "FT01_PARTNER_ARRIVES",
                    1,
                    "19:05",
                    "Partner arrives",
                    "The partner enters."
            ));
            timelineSystem.registerFinalTimelineEvent(new FinalTimelineEvent(
                    "FT02_ARGUMENT_ESCALATES",
                    2,
                    "19:18",
                    "Argument escalates",
                    "The argument turns violent."
            ));
        }

        private void registerStatements() {
            statementSystem.registerStatement(new SuspectStatement(
                    "S01_PARTNER_STATEMENT",
                    "Business Partner",
                    "Test claim",
                    "Test detail",
                    "Test contradiction"
            ));
            statementSystem.registerStatement(new SuspectStatement(
                    "S02_WIFE_STATEMENT",
                    "Wife",
                    "Test claim",
                    "Test detail",
                    "Resolved"
            ));
            statementSystem.registerStatement(new SuspectStatement(
                    "S03_SECURITY_STATEMENT",
                    "Security",
                    "Test claim",
                    "Test detail",
                    "Resolved"
            ));
        }
    }
}
