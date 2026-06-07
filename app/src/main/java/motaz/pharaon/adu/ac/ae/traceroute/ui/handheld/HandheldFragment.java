package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.audio.AudioManager;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;
import motaz.pharaon.adu.ac.ae.traceroute.ui.dialogue.DialogueFragment;

public class HandheldFragment extends Fragment {
    /*
     * Purpose:
     * - Root overlay fragment for the player's handheld forensic device.
     *
     * Responsibilities:
     * - Host evidence, threads, timeline, statements, and laptop entry points in one fragment.
     * - Present Android UI over the OpenGL world.
     * - Route handheld tab/screen changes through ordinary views/controllers, not one fragment per tab.
     *
     * Depends on:
     * - HandheldController when implemented.
     * - EvidenceSystem, ThreadSystem, TimelineSystem, and StatementSystem through GameController.
     * - LaptopFragment as a popup launched from shell or handheld.
     *
     * Used by:
     * - GameShellFragment.
     */

    private static final String ARG_FOCUSED_EVIDENCE_ID = "focused_evidence_id";

    private HandheldController handheldController;
    private HandheldViewBinder viewBinder;
    private String focusedEvidenceId;
    private static final String DIALOGUE_TAG = "final_confrontation";

    private final HandheldViewBinder.RecordListener recordListener = new HandheldViewBinder.RecordListener() {
        @Override
        public boolean isNew(String recordId) {
            return !GameRuntime.getInstance().isRecordViewed(recordId);
        }

        @Override
        public void onRecordSelected(String recordId) {
            GameRuntime.getInstance().markRecordViewed(recordId);
            handheldController.openRecord(recordId);
            renderSelectedScreen();
        }
    };

    public static HandheldFragment newInstance(String focusedEvidenceId) {
        HandheldFragment fragment = new HandheldFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_FOCUSED_EVIDENCE_ID, focusedEvidenceId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handheld, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            focusedEvidenceId = getArguments().getString(ARG_FOCUSED_EVIDENCE_ID);
        }

        view.findViewById(R.id.handheld_close_button).setOnClickListener(closeButton -> {
            getParentFragmentManager().popBackStack();
        });

        handheldController = new HandheldController();
        if (focusedEvidenceId != null && !focusedEvidenceId.trim().isEmpty()) {
            handheldController.selectScreen(HandheldScreen.EVIDENCE);
            handheldController.openRecord(focusedEvidenceId);
            GameRuntime.getInstance().markRecordViewed(focusedEvidenceId);
        }
        viewBinder = new HandheldViewBinder(view);

        // Go Back
        view.findViewById(R.id.handheld_back_button).setOnClickListener(backButton -> {
            if (handheldController.closeRecord()) {
                renderSelectedScreen();
            } else {
                getParentFragmentManager().popBackStack();
            }
        });

        // Up / Down
        ScrollView displayScroll = view.findViewById(R.id.handheld_display_scroll);
        view.findViewById(R.id.handheld_up_button).setOnClickListener(upButton ->
                displayScroll.smoothScrollBy(0, -displayScroll.getHeight() / 2));
        view.findViewById(R.id.handheld_down_button).setOnClickListener(downButton ->
                displayScroll.smoothScrollBy(0, displayScroll.getHeight() / 2));

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

        view.findViewById(R.id.handheld_final_timeline_tab).setOnClickListener(tab -> {
            handheldController.selectScreen(HandheldScreen.FINAL_TIMELINE);
            renderSelectedScreen();
        });

        renderSelectedScreen();
    }

    private void renderSelectedScreen() {
        viewBinder.renderTabState(handheldController.getSelectedScreen());
        renderCurrentLead();

        String openRecordId = handheldController.getOpenRecordId();

        if (handheldController.getSelectedScreen() == HandheldScreen.FINAL_TIMELINE) {
            renderFinalTimeline();
        } else if (handheldController.getSelectedScreen() == HandheldScreen.STATEMENTS) {
            viewBinder.renderStatements(
                    GameRuntime.getInstance().getStatementSystem().getVisibleStatements(),
                    openRecordId,
                    recordListener
            );
        } else if (handheldController.getSelectedScreen() == HandheldScreen.TIMELINE) {
            viewBinder.renderTimeline(
                    GameRuntime.getInstance().getTimelineSystem().getVisibleTimelineEvents(),
                    openRecordId,
                    recordListener
            );
        } else if (handheldController.getSelectedScreen() == HandheldScreen.THREADS) {
            viewBinder.renderThreads(
                    GameRuntime.getInstance().getThreadSystem().getInvestigationThreads(),
                    openRecordId,
                    recordListener
            );
        } else {
            viewBinder.renderEvidence(
                    GameRuntime.getInstance().getEvidenceSystem().getCollectedEvidence(),
                    openRecordId,
                    recordListener
            );
        }
    }

    private void renderFinalTimeline() {
        TimelineSystem timelineSystem = GameRuntime.getInstance().getTimelineSystem();
        renderCurrentLead();
        viewBinder.renderFinalTimeline(
                timelineSystem.getSelectedFinalTimelineEvents(),
                timelineSystem.getAvailableFinalTimelineEvents(),
                timelineSystem.isFinalTimelineUnlocked(),
                timelineSystem.isFinalTimelineCompleted(),
                timelineSystem.getFinalTimelineFeedback(),
                new HandheldViewBinder.FinalTimelineSelectionListener() {
                    @Override
                    public void onFinalTimelineEventSelected(String eventId) {
                        boolean wasCompleted = timelineSystem.isFinalTimelineCompleted();
                        timelineSystem.selectFinalTimelineEvent(eventId);
                        if (!wasCompleted && timelineSystem.isFinalTimelineCompleted()) {
                            AudioManager.getInstance().playTimelineSuccess();
                        }
                        GameRuntime.getInstance().savePlayState();
                        renderFinalTimeline();
                    }

                    @Override
                    public void onFinalTimelineResetRequested() {
                        timelineSystem.clearFinalTimelineSelection();
                        GameRuntime.getInstance().savePlayState();
                        renderFinalTimeline();
                    }

                    @Override
                    public void onConfrontationRequested() {
                        AudioManager.getInstance().playDeviceOpen();
                        if (getParentFragmentManager().findFragmentByTag(DIALOGUE_TAG) == null) {
                            new DialogueFragment().show(getParentFragmentManager(), DIALOGUE_TAG);
                        }
                    }
                }
        );
    }

    private void renderCurrentLead() {
        viewBinder.renderCurrentLead(GameRuntime.getInstance().getCurrentLead());
    }
}
