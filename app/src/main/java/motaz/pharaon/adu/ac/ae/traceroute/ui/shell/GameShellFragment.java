package motaz.pharaon.adu.ac.ae.traceroute.ui.shell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.ui.handheld.HandheldFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.terminal.TerminalFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.world.WorldInvestigationFragment;

public class GameShellFragment extends Fragment {
    /*
     * Purpose:
     * - Root fragment for the playable game shell.
     *
     * Responsibilities:
     * - Host the world investigation fragment.
     * - Host overlay fragments such as handheld screens.
     * - Open the terminal popup as a DialogFragment.
     * - Keep MainActivity thin and fragment-mandate compliant.
     *
     * Depends on:
     * - WorldInvestigationFragment for OpenGL apartment interaction.
     * - HandheldFragment for evidence/threads/timeline UI.
     * - TerminalFragment for the stylized computer terminal popup.
     *
     * Used by:
     * - MainActivity.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_shell, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.world_fragment_container, new WorldInvestigationFragment())
                    .commit();
        }
    }

    public void openHandheld() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.overlay_fragment_container, new HandheldFragment())
                .addToBackStack("handheld")
                .commit();
    }

    public void openTerminal() {
        new TerminalFragment().show(getChildFragmentManager(), "terminal");
    }
}
