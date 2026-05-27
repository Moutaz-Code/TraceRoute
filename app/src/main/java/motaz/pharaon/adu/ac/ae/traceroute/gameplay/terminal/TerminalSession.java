package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    private final Set<String> readPanelCommands;
    private final TerminalContent terminalContent;

    public TerminalSession(TerminalContent terminalContent) {
        this.terminalContent = terminalContent == null
                ? TerminalContent.empty()
                : terminalContent;
        history = new ArrayList<>();
        readPanelCommands = new LinkedHashSet<>();
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

        TerminalPanel terminalPanel = terminalContent.findPanel(trimmedCommand);
        if (terminalPanel != null) {
            readPanelCommands.add(terminalPanel.getCommand());
        }
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

    public boolean isPanelRead(String command) {
        TerminalPanel panel = terminalContent.findPanel(command);
        return panel != null && readPanelCommands.contains(panel.getCommand());
    }

    private void clear() {
        restoreIntro();
    }

    private void restoreIntro() {
        history.clear();
        history.addAll(terminalContent.getIntroLines());
    }
}
