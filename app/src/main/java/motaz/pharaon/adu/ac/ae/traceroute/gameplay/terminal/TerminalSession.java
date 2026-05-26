package motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal;

import java.util.ArrayList;
import java.util.List;

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

    public TerminalSession() {
        history = new ArrayList<>();
        history.add("TRACE/TERM v0.1");
        history.add("Type HELP for available authored commands.");
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

    private void clear() {
        history.clear();
        history.add("TRACE/TERM v0.1");
    }
}
