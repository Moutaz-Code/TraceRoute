package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

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
                return "Laptop panels: MESSAGES, ACTIVITY, FILES, DRAFT, CLEAR";
            case "message":
            case "messages":
                return buildMessagesOutput();
            case "activity":
            case "laptop":
                return buildLaptopOutput();
            case "file":
            case "files":
                return buildFilesOutput();
            case "draft":
            case "email":
                return buildDraftOutput();
            default:
                return "Unknown authored command. Type HELP.";
        }
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

    private String buildFilesOutput() {
        EvidenceItem evidenceItem = gameRuntime.collectEvidence("E12_ACCESS_DATA_SALE_FILE");
        if (evidenceItem == null) {
            return "Access-data files unavailable.";
        }

        return evidenceItem.getId() + "\n"
                + "File: ledger_access_export.dat\n"
                + "Status: hidden archive recovered\n"
                + "Finding: sale records connect the company to illegal apartment-system access data.";
    }

    private String buildDraftOutput() {
        EvidenceItem evidenceItem = gameRuntime.collectEvidence("E13_VICTIM_DRAFT_DISCLOSURE_EMAIL");
        if (evidenceItem == null) {
            return "Draft disclosure email unavailable.";
        }

        return evidenceItem.getId() + "\n"
                + "Draft status: unsent\n"
                + "Recipient: legal counsel / regulator\n"
                + "Finding: victim intended to expose the access-data operation.";
    }
}
