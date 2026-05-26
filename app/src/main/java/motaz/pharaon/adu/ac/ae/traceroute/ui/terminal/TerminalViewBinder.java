package motaz.pharaon.adu.ac.ae.traceroute.ui.terminal;

import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal.TerminalCommandProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal.TerminalSession;

public class TerminalViewBinder {
    /*
     * Purpose:
     * - Plain Java helper for wiring Android Views inside TerminalFragment.
     *
     * Responsibilities:
     * - Manage terminal TextViews and authored command buttons.
     * - Render terminal history and prompt text.
     * - Forward entered commands to TerminalCommandProcessor through TerminalFragment.
     * - Keep the stylized terminal as real Android UI, not OpenGL text unless time allows.
     *
     * Depends on:
     * - TerminalFragment's inflated layout.
     * - TerminalSession for terminal display state.
     *
     * Used by:
     * - TerminalFragment.
     */

    private final ScrollView scrollView;
    private final TextView outputText;
    private final Button helpButton;
    private final Button evidenceButton;
    private final Button messagesButton;
    private final Button bodyButton;
    private final Button logsButton;
    private final Button laptopButton;
    private final Button clearButton;

    public TerminalViewBinder(View rootView) {
        scrollView = rootView.findViewById(R.id.terminal_scroll);
        outputText = rootView.findViewById(R.id.terminal_output);
        helpButton = rootView.findViewById(R.id.terminal_command_help);
        evidenceButton = rootView.findViewById(R.id.terminal_command_evidence);
        messagesButton = rootView.findViewById(R.id.terminal_command_messages);
        bodyButton = rootView.findViewById(R.id.terminal_command_body);
        logsButton = rootView.findViewById(R.id.terminal_command_logs);
        laptopButton = rootView.findViewById(R.id.terminal_command_laptop);
        clearButton = rootView.findViewById(R.id.terminal_command_clear);
    }

    public void bind(TerminalSession terminalSession, TerminalCommandProcessor commandProcessor) {
        render(terminalSession);

        helpButton.setOnClickListener(view -> submitCommand("help", terminalSession, commandProcessor));
        evidenceButton.setOnClickListener(view -> submitCommand("evidence", terminalSession, commandProcessor));
        messagesButton.setOnClickListener(view -> submitCommand("messages", terminalSession, commandProcessor));
        bodyButton.setOnClickListener(view -> submitCommand("body", terminalSession, commandProcessor));
        logsButton.setOnClickListener(view -> submitCommand("logs", terminalSession, commandProcessor));
        laptopButton.setOnClickListener(view -> submitCommand("laptop", terminalSession, commandProcessor));
        clearButton.setOnClickListener(view -> submitCommand("clear", terminalSession, commandProcessor));
    }

    private void submitCommand(
            String command,
            TerminalSession terminalSession,
            TerminalCommandProcessor commandProcessor
    ) {
        terminalSession.submitCommand(command, commandProcessor);
        render(terminalSession);
    }

    private void render(TerminalSession terminalSession) {
        outputText.setText(terminalSession.getVisibleHistory());
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
