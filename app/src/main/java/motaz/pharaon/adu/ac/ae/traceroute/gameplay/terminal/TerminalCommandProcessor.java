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
        EvidenceItem collectEvidence(String evidenceId);
    }

    private final EvidenceCollector evidenceCollector;
    private final TerminalContent terminalContent;

    public TerminalCommandProcessor(GameRuntime gameRuntime) {
        this(
                gameRuntime == null ? TerminalContent.empty() : gameRuntime.getTerminalContent(),
                evidenceId -> gameRuntime == null ? null : gameRuntime.collectEvidence(evidenceId)
        );
    }

    public TerminalCommandProcessor(TerminalContent terminalContent, EvidenceCollector evidenceCollector) {
        this.terminalContent = terminalContent == null
                ? TerminalContent.empty()
                : terminalContent;
        this.evidenceCollector = evidenceCollector;
    }

    public String process(String command) {
        String normalizedCommand = command.trim().toLowerCase(Locale.US);

        if ("help".equals(normalizedCommand)) {
            return buildHelpOutput();
        }

        TerminalPanel terminalPanel = terminalContent.findPanel(normalizedCommand);
        if (terminalPanel == null) {
            return "Unknown authored command. Type HELP.";
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
        EvidenceItem evidenceItem = evidenceCollector == null
                ? null
                : evidenceCollector.collectEvidence(terminalPanel.getEvidenceId());
        if (evidenceItem == null) {
            return terminalPanel.getLabel() + " unavailable.";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(evidenceItem.getId()).append(" // ").append(evidenceItem.getTitle());

        for (String outputLine : terminalPanel.getOutputLines()) {
            builder.append('\n').append(outputLine);
        }

        return builder.toString();
    }
}
