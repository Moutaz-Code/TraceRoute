package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

import java.util.List;
import java.util.Locale;

import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
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

    private final GameRuntime gameRuntime;

    public TerminalCommandProcessor(GameRuntime gameRuntime) {
        this.gameRuntime = gameRuntime;
    }

    public String process(String command) {
        String normalizedCommand = command.trim().toLowerCase(Locale.US);

        switch (normalizedCommand) {
            case "help":
                return "Commands: HELP, EVIDENCE, BODY, LOGS, CLEAR";
            case "evidence":
                return buildEvidenceOutput();
            case "body":
                return buildBodyReportOutput();
            case "logs":
                return "Router logs locked. Office terminal access confirmed, but no router evidence has been authored yet.";
            default:
                return "Unknown authored command. Type HELP.";
        }
    }

    private String buildEvidenceOutput() {
        List<EvidenceItem> collectedEvidence = gameRuntime.getEvidenceSystem().getCollectedEvidence();
        if (collectedEvidence.isEmpty()) {
            return "No collected evidence in handheld database.";
        }

        StringBuilder builder = new StringBuilder("Collected evidence:");
        for (EvidenceItem evidenceItem : collectedEvidence) {
            builder.append('\n')
                    .append("- ")
                    .append(evidenceItem.getId())
                    .append(" // ")
                    .append(evidenceItem.getTitle());
        }

        return builder.toString();
    }

    private String buildBodyReportOutput() {
        if (!gameRuntime.getEvidenceSystem().isCollected("E01_BODY_CONDITION_REPORT")) {
            return "Body report not collected. Inspect the living room scene first.";
        }

        return "E01_BODY_CONDITION_REPORT\n"
                + "Finding: body condition conflicts with activity after 21:00.\n"
                + "Note: death window requires review against apartment temperature logs.";
    }
}
