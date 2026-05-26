package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.ui.AssetImageLoader;

public class HandheldViewBinder {
    /*
     * Purpose:
     * - Plain Java helper for wiring Android Views inside HandheldFragment.
     *
     * Responsibilities:
     * - Find and cache handheld TextViews, Buttons, RecyclerViews, and tab controls later.
     * - Render evidence, threads, timeline, and statements into ordinary Android Views.
     * - Keep view lookup/binding code out of HandheldController's state logic.
     * - Keep the architecture balanced: Fragment for lifecycle, Views for actual UI widgets.
     *
     * Depends on:
     * - HandheldFragment's inflated layout.
     * - HandheldController for selected screen state.
     *
     * Used by:
     * - HandheldFragment.
     */

    private final View rootView;
    private final TextView titleText;
    private final TextView leadText;
    private final TextView emptyText;
    private final LinearLayout contentList;
    private final Button evidenceTab;
    private final Button threadsTab;
    private final Button timelineTab;
    private final Button statementsTab;
    private final Button finalTimelineTab;

    public interface FinalTimelineSelectionListener {
        void onFinalTimelineEventSelected(String eventId);

        void onFinalTimelineResetRequested();

        void onConfrontationRequested();
    }

    public HandheldViewBinder(View rootView) {
        this.rootView = rootView;
        titleText = rootView.findViewById(R.id.handheld_title);
        leadText = rootView.findViewById(R.id.handheld_current_lead);
        emptyText = rootView.findViewById(R.id.handheld_empty_text);
        contentList = rootView.findViewById(R.id.handheld_evidence_list);
        evidenceTab = rootView.findViewById(R.id.handheld_evidence_tab);
        threadsTab = rootView.findViewById(R.id.handheld_threads_tab);
        timelineTab = rootView.findViewById(R.id.handheld_timeline_tab);
        statementsTab = rootView.findViewById(R.id.handheld_statements_tab);
        finalTimelineTab = rootView.findViewById(R.id.handheld_final_timeline_tab);
    }

    public void renderTabState(HandheldScreen selectedScreen) {
        styleTab(evidenceTab, selectedScreen == HandheldScreen.EVIDENCE);
        styleTab(threadsTab, selectedScreen == HandheldScreen.THREADS);
        styleTab(timelineTab, selectedScreen == HandheldScreen.TIMELINE);
        styleTab(statementsTab, selectedScreen == HandheldScreen.STATEMENTS);
        styleTab(finalTimelineTab, selectedScreen == HandheldScreen.FINAL_TIMELINE);
    }

    public void renderCurrentLead(String currentLead) {
        leadText.setText("Lead: " + currentLead);
    }

    public void renderEvidence(List<EvidenceItem> collectedEvidence) {
        renderEvidence(collectedEvidence, null);
    }

    public void renderEvidence(List<EvidenceItem> collectedEvidence, String focusedEvidenceId) {
        titleText.setText("HANDHELD // EVIDENCE");
        contentList.removeAllViews();

        boolean hasEvidence = collectedEvidence != null && !collectedEvidence.isEmpty();
        emptyText.setVisibility(hasEvidence ? View.GONE : View.VISIBLE);
        emptyText.setText("No evidence collected.");

        if (!hasEvidence) {
            return;
        }

        EvidenceItem focusedEvidence = findEvidence(collectedEvidence, focusedEvidenceId);
        if (focusedEvidence != null) {
            contentList.addView(createSectionLabel("Latest inspection"));
            contentList.addView(createEvidenceView(focusedEvidence));
            contentList.addView(createSectionLabel("Collected evidence"));
        }

        for (EvidenceItem evidenceItem : collectedEvidence) {
            if (focusedEvidence != null && focusedEvidence.getId().equals(evidenceItem.getId())) {
                continue;
            }
            contentList.addView(createEvidenceView(evidenceItem));
        }
    }

    public void renderThreads(List<InvestigationThread> investigationThreads) {
        titleText.setText("HANDHELD // THREADS");
        contentList.removeAllViews();

        boolean hasThreads = investigationThreads != null && !investigationThreads.isEmpty();
        emptyText.setVisibility(hasThreads ? View.GONE : View.VISIBLE);
        emptyText.setText("No investigation threads active.");

        if (!hasThreads) {
            return;
        }

        for (InvestigationThread investigationThread : investigationThreads) {
            contentList.addView(createThreadView(investigationThread));
        }
    }

    public void renderTimeline(List<TimelineEvent> timelineEvents) {
        titleText.setText("HANDHELD // TIMELINE");
        contentList.removeAllViews();

        boolean hasTimelineEvents = timelineEvents != null && !timelineEvents.isEmpty();
        emptyText.setVisibility(hasTimelineEvents ? View.GONE : View.VISIBLE);
        emptyText.setText("No timeline contradictions visible.");

        if (!hasTimelineEvents) {
            return;
        }

        for (TimelineEvent timelineEvent : timelineEvents) {
            contentList.addView(createTimelineView(timelineEvent));
        }
    }

    public void renderStatements(List<SuspectStatement> suspectStatements) {
        titleText.setText("HANDHELD // STATEMENTS");
        contentList.removeAllViews();

        boolean hasStatements = suspectStatements != null && !suspectStatements.isEmpty();
        emptyText.setVisibility(hasStatements ? View.GONE : View.VISIBLE);
        emptyText.setText("No suspect statements unlocked.");

        if (!hasStatements) {
            return;
        }

        for (SuspectStatement suspectStatement : suspectStatements) {
            contentList.addView(createStatementView(suspectStatement));
        }
    }

    public void renderFinalTimeline(
            List<FinalTimelineEvent> selectedEvents,
            List<FinalTimelineEvent> availableEvents,
            boolean unlocked,
            boolean completed,
            String feedback,
            FinalTimelineSelectionListener selectionListener
    ) {
        titleText.setText("HANDHELD // FINAL TIMELINE");
        contentList.removeAllViews();
        emptyText.setVisibility(View.GONE);

        if (!unlocked) {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(feedback);
            return;
        }

        contentList.addView(createFinalTimelineStatusView(feedback, completed));

        boolean hasSelectedEvents = selectedEvents != null && !selectedEvents.isEmpty();
        if (hasSelectedEvents) {
            TextView selectedHeader = createSectionLabel("Selected sequence");
            contentList.addView(selectedHeader);

            for (int index = 0; index < selectedEvents.size(); index++) {
                contentList.addView(createSelectedFinalTimelineEventView(index + 1, selectedEvents.get(index)));
            }
        }

        if (!completed) {
            Button resetButton = new Button(rootView.getContext());
            resetButton.setText("Reset Sequence");
            resetButton.setOnClickListener(view -> selectionListener.onFinalTimelineResetRequested());
            contentList.addView(resetButton);

            TextView availableHeader = createSectionLabel("Tap events in true order");
            contentList.addView(availableHeader);

            for (FinalTimelineEvent availableEvent : availableEvents) {
                contentList.addView(createAvailableFinalTimelineEventButton(availableEvent, selectionListener));
            }
        } else {
            Button confrontButton = new Button(rootView.getContext());
            confrontButton.setText("Confront Partner");
            confrontButton.setAllCaps(false);
            confrontButton.setOnClickListener(view -> selectionListener.onConfrontationRequested());
            contentList.addView(confrontButton);
        }
    }

    private View createEvidenceView(EvidenceItem evidenceItem) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(Color.rgb(10, 25, 29));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        TextView title = new TextView(rootView.getContext());
        title.setText(evidenceItem.getTitle());
        title.setTextColor(Color.rgb(214, 245, 227));
        title.setTextSize(16);
        card.addView(title);

        TextView metadata = new TextView(rootView.getContext());
        metadata.setText(evidenceItem.getCategory() + " // " + evidenceItem.getSource());
        metadata.setTextColor(Color.rgb(159, 216, 180));
        metadata.setTextSize(12);
        card.addView(metadata);

        card.addView(createEvidenceImageView(evidenceItem));

        TextView summary = new TextView(rootView.getContext());
        summary.setText(evidenceItem.getPlayerSummary());
        summary.setTextColor(Color.rgb(230, 238, 232));
        summary.setTextSize(14);
        summary.setPadding(0, 12, 0, 0);
        card.addView(summary);

        TextView technicalNote = new TextView(rootView.getContext());
        technicalNote.setText(evidenceItem.getTechnicalNote());
        technicalNote.setTextColor(Color.rgb(139, 178, 155));
        technicalNote.setTextSize(13);
        technicalNote.setPadding(0, 10, 0, 0);
        card.addView(technicalNote);

        return card;
    }

    private View createEvidenceImageView(EvidenceItem evidenceItem) {
        ImageView imageView = new ImageView(rootView.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(Color.rgb(18, 35, 39));

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(132)
        );
        imageParams.setMargins(0, dp(12), 0, 0);
        imageView.setLayoutParams(imageParams);

        String preferredPath = evidenceItem.getDetailAssetPath();
        if (!AssetImageLoader.loadInto(imageView, preferredPath)) {
            AssetImageLoader.loadInto(imageView, evidenceItem.getThumbnailAssetPath());
        }

        return imageView;
    }

    private EvidenceItem findEvidence(List<EvidenceItem> evidenceItems, String evidenceId) {
        if (evidenceItems == null || evidenceId == null || evidenceId.trim().isEmpty()) {
            return null;
        }

        for (EvidenceItem evidenceItem : evidenceItems) {
            if (evidenceId.equals(evidenceItem.getId())) {
                return evidenceItem;
            }
        }

        return null;
    }

    private void styleTab(Button button, boolean selected) {
        button.setAllCaps(false);
        button.setTextColor(selected ? Color.rgb(5, 8, 10) : Color.rgb(214, 245, 227));
        button.setBackgroundColor(selected ? Color.rgb(245, 218, 122) : Color.rgb(18, 35, 39));
    }

    private int dp(int value) {
        float density = rootView.getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }

    private View createThreadView(InvestigationThread investigationThread) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(Color.rgb(14, 22, 34));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        TextView title = new TextView(rootView.getContext());
        title.setText(investigationThread.getName());
        title.setTextColor(Color.rgb(214, 245, 227));
        title.setTextSize(16);
        card.addView(title);

        TextView question = new TextView(rootView.getContext());
        question.setText(investigationThread.getCoreQuestion());
        question.setTextColor(Color.rgb(159, 216, 180));
        question.setTextSize(13);
        card.addView(question);

        for (String observation : investigationThread.getObservations()) {
            TextView observationText = new TextView(rootView.getContext());
            observationText.setText("- " + observation);
            observationText.setTextColor(Color.rgb(230, 238, 232));
            observationText.setTextSize(13);
            observationText.setPadding(0, 10, 0, 0);
            card.addView(observationText);
        }

        if (investigationThread.isStabilized()) {
            TextView conclusion = new TextView(rootView.getContext());
            conclusion.setText("STABILIZED: " + investigationThread.getStabilizedConclusion());
            conclusion.setTextColor(Color.rgb(245, 218, 122));
            conclusion.setTextSize(13);
            conclusion.setPadding(0, 12, 0, 0);
            card.addView(conclusion);
        }

        return card;
    }

    private View createTimelineView(TimelineEvent timelineEvent) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(Color.rgb(26, 20, 32));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        TextView time = new TextView(rootView.getContext());
        time.setText(timelineEvent.getDisplayTime());
        time.setTextColor(Color.rgb(245, 218, 122));
        time.setTextSize(13);
        card.addView(time);

        TextView title = new TextView(rootView.getContext());
        title.setText(timelineEvent.getTitle());
        title.setTextColor(Color.rgb(214, 245, 227));
        title.setTextSize(16);
        title.setPadding(0, 6, 0, 0);
        card.addView(title);

        TextView initial = new TextView(rootView.getContext());
        initial.setText("Initial: " + timelineEvent.getInitialInterpretation());
        initial.setTextColor(Color.rgb(230, 238, 232));
        initial.setTextSize(13);
        initial.setPadding(0, 12, 0, 0);
        card.addView(initial);

        TextView resolved = new TextView(rootView.getContext());
        resolved.setText("Current: " + timelineEvent.getResolvedInterpretation());
        resolved.setTextColor(timelineEvent.isStabilized()
                ? Color.rgb(245, 218, 122)
                : Color.rgb(159, 216, 180));
        resolved.setTextSize(13);
        resolved.setPadding(0, 10, 0, 0);
        card.addView(resolved);

        return card;
    }

    private View createStatementView(SuspectStatement suspectStatement) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(Color.rgb(28, 23, 19));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        TextView speaker = new TextView(rootView.getContext());
        speaker.setText(suspectStatement.getSpeaker());
        speaker.setTextColor(Color.rgb(214, 245, 227));
        speaker.setTextSize(16);
        card.addView(speaker);

        TextView claim = new TextView(rootView.getContext());
        claim.setText("\"" + suspectStatement.getClaim() + "\"");
        claim.setTextColor(Color.rgb(230, 238, 232));
        claim.setTextSize(14);
        claim.setPadding(0, 12, 0, 0);
        card.addView(claim);

        TextView suspiciousDetail = new TextView(rootView.getContext());
        suspiciousDetail.setText("Note: " + suspectStatement.getSuspiciousDetail());
        suspiciousDetail.setTextColor(Color.rgb(159, 216, 180));
        suspiciousDetail.setTextSize(13);
        suspiciousDetail.setPadding(0, 10, 0, 0);
        card.addView(suspiciousDetail);

        if (suspectStatement.isContradicted()) {
            TextView contradiction = new TextView(rootView.getContext());
            contradiction.setText("STATUS: " + suspectStatement.getContradictionText());
            contradiction.setTextColor(Color.rgb(245, 218, 122));
            contradiction.setTextSize(13);
            contradiction.setPadding(0, 12, 0, 0);
            card.addView(contradiction);
        }

        return card;
    }

    private View createFinalTimelineStatusView(String feedback, boolean completed) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(completed ? Color.rgb(23, 34, 20) : Color.rgb(24, 27, 34));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 16);
        card.setLayoutParams(cardParams);

        TextView status = new TextView(rootView.getContext());
        status.setText(feedback);
        status.setTextColor(completed ? Color.rgb(245, 218, 122) : Color.rgb(214, 245, 227));
        status.setTextSize(14);
        card.addView(status);

        return card;
    }

    private TextView createSectionLabel(String label) {
        TextView textView = new TextView(rootView.getContext());
        textView.setText(label);
        textView.setTextColor(Color.rgb(159, 216, 180));
        textView.setTextSize(13);
        textView.setPadding(0, 12, 0, 8);
        return textView;
    }

    private View createSelectedFinalTimelineEventView(int selectedOrder, FinalTimelineEvent finalTimelineEvent) {
        LinearLayout card = new LinearLayout(rootView.getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 16);
        card.setBackgroundColor(Color.rgb(21, 26, 28));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 12);
        card.setLayoutParams(cardParams);

        TextView title = new TextView(rootView.getContext());
        title.setText(selectedOrder + ". " + finalTimelineEvent.getDisplayTime() + " // " + finalTimelineEvent.getTitle());
        title.setTextColor(Color.rgb(214, 245, 227));
        title.setTextSize(15);
        card.addView(title);

        TextView summary = new TextView(rootView.getContext());
        summary.setText(finalTimelineEvent.getSummary());
        summary.setTextColor(Color.rgb(159, 216, 180));
        summary.setTextSize(13);
        summary.setPadding(0, 8, 0, 0);
        card.addView(summary);

        return card;
    }

    private View createAvailableFinalTimelineEventButton(
            FinalTimelineEvent finalTimelineEvent,
            FinalTimelineSelectionListener selectionListener
    ) {
        Button button = new Button(rootView.getContext());
        button.setText(finalTimelineEvent.getDisplayTime() + " // " + finalTimelineEvent.getTitle());
        button.setAllCaps(false);
        button.setOnClickListener(view -> selectionListener.onFinalTimelineEventSelected(finalTimelineEvent.getId()));

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, 0, 0, 10);
        button.setLayoutParams(buttonParams);

        return button;
    }
}
