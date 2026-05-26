package motaz.pharaon.adu.ac.ae.traceroute.ui.terminal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal.TerminalCommandProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.terminal.TerminalSession;

public class TerminalFragment extends DialogFragment {
    /*
     * Purpose:
     * - Stylized computer terminal popup used by the player on their phone.
     *
     * Responsibilities:
     * - Display terminal output and command/input UI.
     * - Forward commands to TerminalCommandProcessor.
     * - Return results that can unlock evidence, logs, or thread updates.
     * - Remain a Fragment/DialogFragment because terminal interaction is a major required mechanic.
     *
     * Depends on:
     * - TerminalSession for terminal state.
     * - TerminalCommandProcessor for command handling.
     * - GameController or GameRepository later for evidence/thread unlocks.
     *
     * Used by:
     * - GameShellFragment.
     * - HandheldFragment.
     */

    private TerminalSession terminalSession;
    private TerminalCommandProcessor commandProcessor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_terminal, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        terminalSession = new TerminalSession(GameRuntime.getInstance().getTerminalContent());
        commandProcessor = new TerminalCommandProcessor(GameRuntime.getInstance());

        TerminalViewBinder viewBinder = new TerminalViewBinder(view);
        viewBinder.bind(terminalSession, commandProcessor);
    }
}
