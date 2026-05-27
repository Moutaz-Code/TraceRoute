package motaz.pharaon.adu.ac.ae.traceroute.ui.terminal;

import android.graphics.Color;
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
    private final TextView titleText;
    private final TextView outputText;
    private final Button closeButton;
    private final Button messagesButton;
    private final Button laptopButton;
    private final Button clearButton;
    private final Button filesButton;
    private final Button draftButton;

    public TerminalViewBinder(View rootView) {
        scrollView = rootView.findViewById(R.id.terminal_scroll);
        titleText = rootView.findViewById(R.id.terminal_title);
        outputText = rootView.findViewById(R.id.terminal_output);
        closeButton = rootView.findViewById(R.id.terminal_close_button);
        messagesButton = rootView.findViewById(R.id.terminal_command_messages);
        laptopButton = rootView.findViewById(R.id.terminal_command_laptop);
        clearButton = rootView.findViewById(R.id.terminal_command_clear);
        filesButton = rootView.findViewById(R.id.terminal_command_files);
        draftButton = rootView.findViewById(R.id.terminal_command_draft);
    }

    public void bind(
            TerminalSession terminalSession,
            TerminalCommandProcessor commandProcessor,
            Runnable closeCallback
    ) {
        titleText.setText(terminalSession.getTitle());
        closeButton.setAllCaps(false);
        closeButton.setOnClickListener(view -> closeCallback.run());
        render(terminalSession);

        messagesButton.setOnClickListener(view -> submitCommand("messages", terminalSession, commandProcessor));
        laptopButton.setOnClickListener(view -> submitCommand("laptop", terminalSession, commandProcessor));
        clearButton.setOnClickListener(view -> submitCommand("clear", terminalSession, commandProcessor));
        filesButton.setOnClickListener(view -> submitCommand("files", terminalSession, commandProcessor));
        draftButton.setOnClickListener(view -> submitCommand("draft", terminalSession, commandProcessor));
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
        renderPanelButton(messagesButton, terminalSession, "messages", "Messages");
        renderPanelButton(laptopButton, terminalSession, "activity", "Activity");
        renderPanelButton(filesButton, terminalSession, "files", "Files");
        renderPanelButton(draftButton, terminalSession, "draft", "Draft");
        styleUtilityButton(clearButton);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void renderPanelButton(
            Button button,
            TerminalSession terminalSession,
            String command,
            String fallback
    ) {
        boolean read = terminalSession.isPanelRead(command);
        button.setAllCaps(false);
        button.setText(terminalSession.getPanelLabel(command, fallback) + (read ? " (read)" : ""));
        button.setTextColor(read ? Color.rgb(5, 8, 10) : Color.rgb(214, 245, 227));
        button.setBackgroundColor(read ? Color.rgb(245, 218, 122) : Color.rgb(18, 35, 39));
    }

    private void styleUtilityButton(Button button) {
        button.setAllCaps(false);
        button.setTextColor(Color.rgb(214, 245, 227));
        button.setBackgroundColor(Color.rgb(20, 39, 45));
    }
}
