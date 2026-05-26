package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal.TerminalCommandProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TerminalCommandProcessorTest {
    @Test
    public void authoredPanelCommandCollectsLinkedEvidenceAndReturnsOutput() {
        Set<String> collectedEvidenceIds = new LinkedHashSet<>();
        TerminalCommandProcessor processor = new TerminalCommandProcessor(
                buildTerminalContent(),
                evidenceId -> {
                    collectedEvidenceIds.add(evidenceId);
                    return new EvidenceItem(
                            evidenceId,
                            "Scheduled Victim Message",
                            "Digital",
                            "Terminal",
                            "Summary",
                            "Note"
                    );
                }
        );

        String output = processor.process("message");

        assertTrue(collectedEvidenceIds.contains("E06_SCHEDULED_VICTIM_MESSAGE"));
        assertTrue(output.contains("E06_SCHEDULED_VICTIM_MESSAGE // Scheduled Victim Message"));
        assertTrue(output.contains("Metadata flag: scheduled before delivery"));
    }

    @Test
    public void helpListsAuthoredPanels() {
        TerminalCommandProcessor processor = new TerminalCommandProcessor(
                buildTerminalContent(),
                evidenceId -> null
        );

        assertEquals("Laptop panels: MESSAGES CLEAR", processor.process("help"));
    }

    @Test
    public void unknownCommandDoesNotCollectEvidence() {
        Set<String> collectedEvidenceIds = new LinkedHashSet<>();
        TerminalCommandProcessor processor = new TerminalCommandProcessor(
                buildTerminalContent(),
                evidenceId -> {
                    collectedEvidenceIds.add(evidenceId);
                    return null;
                }
        );

        String output = processor.process("weather");

        assertTrue(collectedEvidenceIds.isEmpty());
        assertEquals("Unknown authored command. Type HELP.", output);
    }

    private TerminalContent buildTerminalContent() {
        return new TerminalContent(
                "Test Laptop",
                Collections.singletonList("Intro"),
                Collections.singletonList(new TerminalPanel(
                        "messages",
                        "Messages",
                        "E06_SCHEDULED_VICTIM_MESSAGE",
                        Collections.singletonList("message"),
                        Arrays.asList(
                                "Message timestamp: 21:07",
                                "Metadata flag: scheduled before delivery"
                        )
                ))
        );
    }
}
