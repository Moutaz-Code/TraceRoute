package motaz.pharaon.adu.ac.ae.traceroute.ui.shell;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.ui.handheld.HandheldFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.terminal.TerminalFragment;
import motaz.pharaon.adu.ac.ae.traceroute.ui.world.WorldInvestigationFragment;

public class GameShellFragment extends Fragment implements GameUiEventListener {
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

    private static final String HANDHELD_TAG = "handheld";
    private static final String TERMINAL_TAG = "terminal";

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
        FragmentManager fragmentManager = getChildFragmentManager();
        if (fragmentManager.findFragmentByTag(HANDHELD_TAG) != null) {
            return;
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.overlay_fragment_container, new HandheldFragment(), HANDHELD_TAG)
                .addToBackStack(HANDHELD_TAG)
                .commit();
    }

    public void openTerminal() {
        if (getChildFragmentManager().findFragmentByTag(TERMINAL_TAG) != null) {
            return;
        }

        new TerminalFragment().show(getChildFragmentManager(), TERMINAL_TAG);
    }

    @Override
    public void onOpenHandheldRequested() {
        openHandheld();
    }

    @Override
    public void onOpenTerminalRequested() {
        openTerminal();
    }

    @Override
    public void onEvidenceInspectionRequested(String evidenceId) {
        if (!isAdded()) {
            return;
        }

        Toast.makeText(
                requireContext(),
                "Evidence inspected: " + evidenceId,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onRoomContextChanged(String roomName, String contextText, String hotspotHint) {
        // Room context labels are owned by WorldInvestigationFragment.
    }
}
