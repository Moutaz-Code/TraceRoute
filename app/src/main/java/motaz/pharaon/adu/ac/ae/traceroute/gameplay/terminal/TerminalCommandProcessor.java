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
                return "Commands: HELP, EVIDENCE, MESSAGES, BODY, LOGS, LAPTOP, CLEAR";
            case "evidence":
                return buildEvidenceOutput();
            case "message":
            case "messages":
                return buildMessagesOutput();
            case "body":
                return buildBodyReportOutput();
            case "logs":
                return buildLogsOutput();
            case "laptop":
                return buildLaptopOutput();
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

    private String buildMessagesOutput() {
        EvidenceItem evidenceItem = gameRuntime.collectEvidence("E06_SCHEDULED_VICTIM_MESSAGE");
        if (evidenceItem == null) {
            return "Message metadata unavailable.";
        }

        return evidenceItem.getId() + "\n"
                + "Message timestamp: 21:07\n"
                + "Delivery state: sent\n"
                + "Metadata flag: scheduled before delivery\n"
                + "Finding: this message is not reliable proof the victim was alive at 21:07.";
    }

    private String buildLaptopOutput() {
        EvidenceItem evidenceItem = gameRuntime.collectEvidence("E07_LAPTOP_SESSION_METADATA");
        if (evidenceItem == null) {
            return "Laptop session metadata unavailable.";
        }

        return evidenceItem.getId() + "\n"
                + "Session activity: 21:16\n"
                + "Input pattern: repeated timed actions\n"
                + "Finding: activity pattern resembles automation, not normal human use.";
    }

    private String buildLogsOutput() {
        EvidenceItem evidenceItem = gameRuntime.collectEvidence("E08_ROUTER_ADMIN_ACCESS_RECORD");
        if (evidenceItem == null) {
            return "Router log metadata unavailable.";
        }

        return evidenceItem.getId() + "\n"
                + "Session type: trusted admin\n"
                + "Access window: staging period\n"
                + "Scope: smart-home hub / router / apartment devices\n"
                + "Finding: manipulation required privileged technical access.";
    }
}
