package motaz.pharaon.adu.ac.ae.traceroute.ui.terminal;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
     * - Manage terminal TextViews/EditTexts/Buttons later.
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
    private final EditText inputText;
    private final Button submitButton;

    public TerminalViewBinder(View rootView) {
        scrollView = rootView.findViewById(R.id.terminal_scroll);
        outputText = rootView.findViewById(R.id.terminal_output);
        inputText = rootView.findViewById(R.id.terminal_input);
        submitButton = rootView.findViewById(R.id.terminal_submit_button);
    }

    public void bind(TerminalSession terminalSession, TerminalCommandProcessor commandProcessor) {
        render(terminalSession);

        submitButton.setOnClickListener(view -> submitCommand(terminalSession, commandProcessor));
        inputText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                submitCommand(terminalSession, commandProcessor);
                return true;
            }

            return false;
        });
    }

    private void submitCommand(TerminalSession terminalSession, TerminalCommandProcessor commandProcessor) {
        terminalSession.submitCommand(inputText.getText().toString(), commandProcessor);
        inputText.setText("");
        render(terminalSession);
    }

    private void render(TerminalSession terminalSession) {
        outputText.setText(terminalSession.getVisibleHistory());
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }
}
