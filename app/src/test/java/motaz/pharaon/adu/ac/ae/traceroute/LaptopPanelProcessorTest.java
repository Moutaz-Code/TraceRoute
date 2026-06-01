package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LaptopPanelProcessorTest {
    @Test
    public void openingPanelCollectsLinkedEvidenceAndReturnsContent() {
        Set<String> collectedEvidenceIds = new LinkedHashSet<>();
        LaptopPanelProcessor processor = new LaptopPanelProcessor(
                buildLaptopContent(),
                evidenceId -> {
                    collectedEvidenceIds.add(evidenceId);
                    return new LaptopPanelProcessor.EvidenceCollectionResult(
                            new EvidenceItem(
                                    evidenceId,
                                    "Scheduled Victim Message",
                                    "Digital",
                                    "Office laptop",
                                    "Summary",
                                    "Note"
                            ),
                            false
                    );
                }
        );

        String output = processor.openPanel("messages");

        assertTrue(collectedEvidenceIds.contains("E06_SCHEDULED_VICTIM_MESSAGE"));
        assertTrue(output.contains("E06_SCHEDULED_VICTIM_MESSAGE // Scheduled Victim Message"));
        assertTrue(output.contains("Status: evidence added to handheld."));
        assertTrue(output.contains("Metadata flag: scheduled before delivery"));
    }

    @Test
    public void openingPanelCanReportAlreadyCollectedEvidence() {
        LaptopPanelProcessor processor = new LaptopPanelProcessor(
                buildLaptopContent(),
                evidenceId -> new LaptopPanelProcessor.EvidenceCollectionResult(
                        new EvidenceItem(
                                evidenceId,
                                "Scheduled Victim Message",
                                "Digital",
                                "Office laptop",
                                "Summary",
                                "Note"
                        ),
                        true
                )
        );

        String output = processor.openPanel("messages");

        assertTrue(output.contains("Status: evidence already in handheld."));
    }

    @Test
    public void unknownPanelDoesNotCollectEvidence() {
        Set<String> collectedEvidenceIds = new LinkedHashSet<>();
        LaptopPanelProcessor processor = new LaptopPanelProcessor(
                buildLaptopContent(),
                evidenceId -> {
                    collectedEvidenceIds.add(evidenceId);
                    return new LaptopPanelProcessor.EvidenceCollectionResult(null, false);
                }
        );

        String output = processor.openPanel("weather");

        assertTrue(collectedEvidenceIds.isEmpty());
        assertEquals("This laptop panel is unavailable.", output);
    }

    @Test
    public void nullAndBlankPanelIdsReturnUnavailableWithoutCollectingEvidence() {
        Set<String> collectedEvidenceIds = new LinkedHashSet<>();
        LaptopPanelProcessor processor = new LaptopPanelProcessor(
                buildLaptopContent(),
                evidenceId -> {
                    collectedEvidenceIds.add(evidenceId);
                    return new LaptopPanelProcessor.EvidenceCollectionResult(null, false);
                }
        );

        assertEquals("This laptop panel is unavailable.", processor.openPanel(null));
        assertEquals("This laptop panel is unavailable.", processor.openPanel(""));
        assertEquals("This laptop panel is unavailable.", processor.openPanel("   "));
        assertTrue(collectedEvidenceIds.isEmpty());
    }

    private LaptopContent buildLaptopContent() {
        return new LaptopContent(
                "Test Laptop",
                Collections.singletonList("Intro"),
                Collections.singletonList(new LaptopPanel(
                        "messages",
                        "Messages",
                        "E06_SCHEDULED_VICTIM_MESSAGE",
                        Arrays.asList(
                                "Message timestamp: 21:07",
                                "Metadata flag: scheduled before delivery"
                        )
                ))
        );
    }
}
