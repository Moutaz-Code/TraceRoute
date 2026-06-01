package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MvpCaseFlowTest {
    @Test
    public void authoredMvpCaseCompletesThroughExpectedDemoPath() throws Exception {
        MvpCaseFixture fixture = MvpCaseFixture.load();
        LaptopPanelProcessor laptopPanelProcessor = fixture.createLaptopPanelProcessor();

        collectWorldEvidence(fixture);
        openLaptopPanels(laptopPanelProcessor);

        assertCoreThreadsStabilized(fixture);
        assertStatementsResolved(fixture);
        assertTrue(fixture.timelineSystem.isFinalTimelineUnlocked());

        for (String eventId : fixture.getFinalTimelineIdsInReverseAuthoredOrder()) {
            fixture.timelineSystem.selectFinalTimelineEvent(eventId);
        }

        assertFalse(fixture.timelineSystem.isFinalTimelineCompleted());
        assertEquals(0, fixture.timelineSystem.getSelectedFinalTimelineEventIds().size());

        for (String eventId : fixture.getFinalTimelineIdsInAuthoredOrder()) {
            fixture.timelineSystem.selectFinalTimelineEvent(eventId);
        }

        assertTrue(fixture.timelineSystem.isFinalTimelineCompleted());
        assertEquals(
                fixture.getFinalTimelineIdsInAuthoredOrder().size(),
                fixture.timelineSystem.getSelectedFinalTimelineEventIds().size()
        );
        assertEquals("Confront Marcus Hale with the completed timeline.", fixture.getCurrentLead());
    }

    private void collectWorldEvidence(MvpCaseFixture fixture) {
        fixture.collect("E01_BODY_CONDITION_REPORT");
        fixture.collect("E02_THERMOSTAT_AC_LOG");
        fixture.collect("E03_BROKEN_VASE_DISTURBED_TABLE");
        fixture.collect("E04_BLOOD_MARKED_COMPANY_AWARD");
        fixture.collect("E05_SMART_LOCK_ENTRY_EXIT_LOG");
        fixture.collect("E09_WIFE_REMOTE_ACCESS_LOG");
        fixture.collect("E10_CCTV_OUTAGE_NOTE");
        fixture.collect("E08_ROUTER_ADMIN_ACCESS_RECORD");
    }

    private void openLaptopPanels(LaptopPanelProcessor laptopPanelProcessor) {
        assertTrue(laptopPanelProcessor.openPanel("messages").contains("E06_SCHEDULED_VICTIM_MESSAGE"));
        assertTrue(laptopPanelProcessor.openPanel("activity").contains("E07_LAPTOP_SESSION_METADATA"));
        assertTrue(laptopPanelProcessor.openPanel("files").contains("E12_ACCESS_DATA_SALE_FILE"));
        assertTrue(laptopPanelProcessor.openPanel("draft").contains("E13_VICTIM_DRAFT_DISCLOSURE_EMAIL"));
    }

    private void assertCoreThreadsStabilized(MvpCaseFixture fixture) {
        assertTrue(fixture.threadSystem.getThread("T01_DEATH_WINDOW_INSTABILITY").isStabilized());
        assertTrue(fixture.threadSystem.getThread("T02_POSTMORTEM_ACTIVITY").isStabilized());
        assertTrue(fixture.threadSystem.getThread("T03_APARTMENT_ACCESS").isStabilized());
        assertTrue(fixture.threadSystem.getThread("T04_DATA_SALE_MOTIVE").isStabilized());
        assertTrue(fixture.threadSystem.getThread("T05_SCENE_DISTURBANCE").isStabilized());
    }

    private void assertStatementsResolved(MvpCaseFixture fixture) {
        assertTrue(fixture.statementSystem.getStatement("S01_PARTNER_STATEMENT").isVisible());
        assertTrue(fixture.statementSystem.getStatement("S01_PARTNER_STATEMENT").isContradicted());
        assertTrue(fixture.statementSystem.getStatement("S02_WIFE_STATEMENT").isVisible());
        assertTrue(fixture.statementSystem.getStatement("S02_WIFE_STATEMENT").isContradicted());
        assertTrue(fixture.statementSystem.getStatement("S03_SECURITY_STATEMENT").isVisible());
        assertTrue(fixture.statementSystem.getStatement("S03_SECURITY_STATEMENT").isContradicted());
    }
}
