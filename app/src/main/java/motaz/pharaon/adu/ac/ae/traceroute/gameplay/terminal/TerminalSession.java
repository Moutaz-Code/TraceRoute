package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

import java.util.ArrayList;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalContent;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.TerminalPanel;

public class TerminalSession {
    /*
     * Purpose:
     * - Runtime state for the stylized in-game terminal computer.
     *
     * Responsibilities:
     * - Store visible terminal history, current prompt state, and unlocked command availability later.
     * - Keep terminal state separate from TerminalFragment UI widgets.
     * - Allow terminal interactions to unlock evidence or thread observations through GameController.
     *
     * Depends on:
     * - TerminalCommandProcessor for command execution.
     * - GameRepository for save/load if terminal history matters.
     *
     * Used by:
     * - TerminalFragment.
     * - GameController.
     */

    private final List<String> history;
    private final TerminalContent terminalContent;

    public TerminalSession(TerminalContent terminalContent) {
        this.terminalContent = terminalContent == null
                ? TerminalContent.empty()
                : terminalContent;
        history = new ArrayList<>();
        restoreIntro();
    }

    public void submitCommand(String command, TerminalCommandProcessor commandProcessor) {
        String trimmedCommand = command == null ? "" : command.trim();
        if (trimmedCommand.isEmpty()) {
            return;
        }

        if ("clear".equalsIgnoreCase(trimmedCommand)) {
            clear();
            return;
        }

        history.add("> " + trimmedCommand);
        history.add(commandProcessor.process(trimmedCommand));
    }

    public String getVisibleHistory() {
        StringBuilder builder = new StringBuilder();

        for (String line : history) {
            builder.append(line).append('\n');
        }

        return builder.toString().trim();
    }

    public String getTitle() {
        return terminalContent.getTitle();
    }

    public String getPanelLabel(String command, String fallback) {
        TerminalPanel panel = terminalContent.findPanel(command);
        return panel == null ? fallback : panel.getLabel();
    }

    private void clear() {
        restoreIntro();
    }

    private void restoreIntro() {
        history.clear();
        history.addAll(terminalContent.getIntroLines());
    }
}
