package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;

public class HandheldFragment extends Fragment {
    /*
     * Purpose:
     * - Root overlay fragment for the player's handheld forensic device.
     *
     * Responsibilities:
     * - Host evidence, threads, timeline, statements, and terminal entry points in one fragment.
     * - Present Android UI over the OpenGL world.
     * - Route handheld tab/screen changes through ordinary views/controllers, not one fragment per tab.
     *
     * Depends on:
     * - HandheldController when implemented.
     * - EvidenceSystem, ThreadSystem, TimelineSystem, and StatementSystem through GameController.
     * - TerminalFragment as a popup launched from shell or handheld.
     *
     * Used by:
     * - GameShellFragment.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handheld, container, false);
    }
}
