package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;

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

    private HandheldController handheldController;
    private HandheldViewBinder viewBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handheld, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.handheld_close_button).setOnClickListener(closeButton -> {
            getParentFragmentManager().popBackStack();
        });

        handheldController = new HandheldController();
        viewBinder = new HandheldViewBinder(view);

        view.findViewById(R.id.handheld_evidence_tab).setOnClickListener(tab -> {
            handheldController.selectScreen(HandheldScreen.EVIDENCE);
            renderSelectedScreen();
        });

        view.findViewById(R.id.handheld_threads_tab).setOnClickListener(tab -> {
            handheldController.selectScreen(HandheldScreen.THREADS);
            renderSelectedScreen();
        });

        view.findViewById(R.id.handheld_timeline_tab).setOnClickListener(tab -> {
            handheldController.selectScreen(HandheldScreen.TIMELINE);
            renderSelectedScreen();
        });

        view.findViewById(R.id.handheld_statements_tab).setOnClickListener(tab -> {
            handheldController.selectScreen(HandheldScreen.STATEMENTS);
            renderSelectedScreen();
        });

        renderSelectedScreen();
    }

    private void renderSelectedScreen() {
        if (handheldController.getSelectedScreen() == HandheldScreen.STATEMENTS) {
            viewBinder.renderStatements(GameRuntime.getInstance().getStatementSystem().getVisibleStatements());
        } else if (handheldController.getSelectedScreen() == HandheldScreen.TIMELINE) {
            viewBinder.renderTimeline(GameRuntime.getInstance().getTimelineSystem().getVisibleTimelineEvents());
        } else if (handheldController.getSelectedScreen() == HandheldScreen.THREADS) {
            viewBinder.renderThreads(GameRuntime.getInstance().getThreadSystem().getInvestigationThreads());
        } else {
            viewBinder.renderEvidence(GameRuntime.getInstance().getEvidenceSystem().getCollectedEvidence());
        }
    }
}
