package motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;

public class LaptopPanelProcessor {
    /*
     * Purpose:
     * - Opens an authored panel on the victim laptop when the player taps its button.
     *
     * Responsibilities:
     * - Look up the authored panel by id and return its readable content.
     * - Collect the evidence linked to that panel through GameRuntime.
     * - Stay limited to authored panels; the laptop is not a shell and runs no commands.
     *
     * Depends on:
     * - LaptopContent for the authored panels.
     * - An EvidenceCollector (GameRuntime) for gameplay side effects.
     *
     * Used by:
     * - LaptopSession when a panel button is opened.
     */

    public interface EvidenceCollector {
        EvidenceCollectionResult collectEvidence(String evidenceId);
    }

    public static class EvidenceCollectionResult {
        private final EvidenceItem evidenceItem;
        private final boolean alreadyCollected;

        public EvidenceCollectionResult(EvidenceItem evidenceItem, boolean alreadyCollected) {
            this.evidenceItem = evidenceItem;
            this.alreadyCollected = alreadyCollected;
        }

        public EvidenceItem getEvidenceItem() {
            return evidenceItem;
        }

        public boolean isAlreadyCollected() {
            return alreadyCollected;
        }
    }

    private static final String PANEL_UNAVAILABLE_RESPONSE = "This laptop panel is unavailable.";

    private final EvidenceCollector evidenceCollector;
    private final LaptopContent laptopContent;

    public LaptopPanelProcessor(GameRuntime gameRuntime) {
        this(
                gameRuntime == null ? LaptopContent.empty() : gameRuntime.getLaptopContent(),
                evidenceId -> {
                    if (gameRuntime == null) {
                        return new EvidenceCollectionResult(null, false);
                    }

                    boolean alreadyCollected = gameRuntime.getEvidenceSystem().isCollected(evidenceId);
                    return new EvidenceCollectionResult(
                            gameRuntime.collectEvidence(evidenceId),
                            alreadyCollected
                    );
                }
        );
    }

    public LaptopPanelProcessor(LaptopContent laptopContent, EvidenceCollector evidenceCollector) {
        this.laptopContent = laptopContent == null
                ? LaptopContent.empty()
                : laptopContent;
        this.evidenceCollector = evidenceCollector;
    }

    public String openPanel(String panelId) {
        LaptopPanel laptopPanel = laptopContent.findPanel(panelId);
        if (laptopPanel == null) {
            return PANEL_UNAVAILABLE_RESPONSE;
        }

        return buildPanelOutput(laptopPanel);
    }

    private String buildPanelOutput(LaptopPanel laptopPanel) {
        EvidenceCollectionResult collectionResult = evidenceCollector == null
                ? null
                : evidenceCollector.collectEvidence(laptopPanel.getEvidenceId());
        EvidenceItem evidenceItem = collectionResult == null ? null : collectionResult.getEvidenceItem();
        if (evidenceItem == null) {
            return laptopPanel.getLabel() + " unavailable.";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(evidenceItem.getId()).append(" // ").append(evidenceItem.getTitle());
        builder.append('\n').append(collectionResult.isAlreadyCollected()
                ? "Status: evidence already in handheld."
                : "Status: evidence added to handheld.");

        for (String contentLine : laptopPanel.getContentLines()) {
            builder.append('\n').append(contentLine);
        }

        return builder.toString();
    }
}
