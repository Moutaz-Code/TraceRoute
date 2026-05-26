package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;

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
    private final TextView emptyText;
    private final LinearLayout contentList;

    public HandheldViewBinder(View rootView) {
        this.rootView = rootView;
        titleText = rootView.findViewById(R.id.handheld_title);
        emptyText = rootView.findViewById(R.id.handheld_empty_text);
        contentList = rootView.findViewById(R.id.handheld_evidence_list);
    }

    public void renderEvidence(List<EvidenceItem> collectedEvidence) {
        titleText.setText("HANDHELD // EVIDENCE");
        contentList.removeAllViews();

        boolean hasEvidence = collectedEvidence != null && !collectedEvidence.isEmpty();
        emptyText.setVisibility(hasEvidence ? View.GONE : View.VISIBLE);
        emptyText.setText("No evidence collected.");

        if (!hasEvidence) {
            return;
        }

        for (EvidenceItem evidenceItem : collectedEvidence) {
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
}
