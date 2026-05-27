package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

import java.util.Locale;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;

public class TerminalCommandProcessor {
    /*
     * Purpose:
     * - Handles commands entered in the stylized terminal computer.
     *
     * Responsibilities:
     * - Parse simple MVP commands.
     * - Return readable terminal output.
     * - Trigger evidence/thread/timeline updates through GameController later.
     * - Keep terminal commands authored and limited, not a full shell simulation.
     *
     * Depends on:
     * - TerminalSession.
     * - GameController for gameplay side effects.
     *
     * Used by:
     * - TerminalFragment.
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

    private static final String UNKNOWN_COMMAND_RESPONSE = "Unknown authored command. Type HELP.";

    private final EvidenceCollector evidenceCollector;
    private final TerminalContent terminalContent;

    public TerminalCommandProcessor(GameRuntime gameRuntime) {
        this(
                gameRuntime == null ? TerminalContent.empty() : gameRuntime.getTerminalContent(),
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

    public TerminalCommandProcessor(TerminalContent terminalContent, EvidenceCollector evidenceCollector) {
        this.terminalContent = terminalContent == null
                ? TerminalContent.empty()
                : terminalContent;
        this.evidenceCollector = evidenceCollector;
    }

    public String process(String command) {
        String normalizedCommand = command == null
                ? ""
                : command.trim().toLowerCase(Locale.US);

        if (normalizedCommand.isEmpty()) {
            return UNKNOWN_COMMAND_RESPONSE;
        }

        if ("help".equals(normalizedCommand)) {
            return buildHelpOutput();
        }

        TerminalPanel terminalPanel = terminalContent.findPanel(normalizedCommand);
        if (terminalPanel == null) {
            return UNKNOWN_COMMAND_RESPONSE;
        }

        return buildPanelOutput(terminalPanel);
    }

    private String buildHelpOutput() {
        StringBuilder builder = new StringBuilder("Laptop panels:");
        for (TerminalPanel panel : terminalContent.getPanels()) {
            builder.append(' ').append(panel.getLabel().toUpperCase(Locale.US));
        }

        builder.append(" CLEAR");
        return builder.toString();
    }

    private String buildPanelOutput(TerminalPanel terminalPanel) {
        EvidenceCollectionResult collectionResult = evidenceCollector == null
                ? null
                : evidenceCollector.collectEvidence(terminalPanel.getEvidenceId());
        EvidenceItem evidenceItem = collectionResult == null ? null : collectionResult.getEvidenceItem();
        if (evidenceItem == null) {
            return terminalPanel.getLabel() + " unavailable.";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(evidenceItem.getId()).append(" // ").append(evidenceItem.getTitle());
        builder.append('\n').append(collectionResult.isAlreadyCollected()
                ? "Status: evidence already in handheld."
                : "Status: evidence added to handheld.");

        for (String outputLine : terminalPanel.getOutputLines()) {
            builder.append('\n').append(outputLine);
        }

        return builder.toString();
    }
}
