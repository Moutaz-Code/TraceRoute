package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.statement.SuspectStatement;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.FinalTimelineEvent;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineEvent;

public class HandheldViewBinder {
    /*
     * Purpose:
     * - Plain Java helper for wiring Android Views inside HandheldFragment.
     *
     * Responsibilities:
     * - Find and cache handheld TextViews, Buttons, and tab controls.
     * - Render evidence, threads, timeline, and statements into ordinary Android Views.
     * - List view = one styled record row per record; detail view = a 2-column
     *   key/value table (think JSON) with a single title header.
     * - Keep view lookup/binding code out of HandheldController's state logic.
     *
     * Styling:
     * - All look (font, size, colour, background, padding) comes from
     *   @style/handheld_record on the inflated row/table layouts. Only the
     *   data-driven variants (new / important / accent +/-) are applied here,
     *   as one-line background/text-colour swaps.
     *
     * Depends on:
     * - HandheldFragment's inflated layout.
     * - view_handheld_record_row / view_handheld_table_row layouts.
     *
     * Used by:
     * - HandheldFragment.
     */

    private final View rootView;
    private final TextView leadText;
    private final LinearLayout contentList;
    private final TextView evidenceTab;
    private final TextView threadsTab;
    private final TextView timelineTab;
    private final TextView statementsTab;
    private final TextView finalTimelineTab;

    public interface FinalTimelineSelectionListener {
        void onFinalTimelineEventSelected(String eventId);

        void onFinalTimelineResetRequested();

        void onConfrontationRequested();
    }

    public interface RecordListener {
        // True until the player has opened this record at least once.
        boolean isNew(String recordId);

        // Called when a list row is tapped, to open that record's detail view.
        void onRecordSelected(String recordId);
    }

    public HandheldViewBinder(View rootView) {
        this.rootView = rootView;
        leadText = rootView.findViewById(R.id.handheld_current_lead);
        contentList = rootView.findViewById(R.id.handheld_display_content_list);
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
        leadText.setText(currentLead);
    }

    public void renderEvidence(List<EvidenceItem> collectedEvidence, String openRecordId, RecordListener listener) {
        contentList.removeAllViews();

        if (collectedEvidence == null || collectedEvidence.isEmpty()) {
            contentList.addView(getPlaceholderText("No evidence collected."));
            return;
        }

        for (EvidenceItem evidenceItem : collectedEvidence) {
            // Detail view: the one opened record's table.
            if (evidenceItem.getId().equals(openRecordId)) {
                renderEvidenceDetail(evidenceItem);
                return;
            }
        }

        // List view: one tappable title per record.
        for (EvidenceItem evidenceItem : collectedEvidence) {
            addRecordRow(evidenceItem.getId(), evidenceItem.getTitle(), listener);
        }
    }

    public void renderThreads(List<InvestigationThread> investigationThreads, String openRecordId, RecordListener listener) {
        contentList.removeAllViews();

        if (investigationThreads == null || investigationThreads.isEmpty()) {
            contentList.addView(getPlaceholderText("No investigation threads active."));
            return;
        }

        for (InvestigationThread investigationThread : investigationThreads) {
            if (investigationThread.getId().equals(openRecordId)) {
                renderThreadDetail(investigationThread);
                return;
            }
        }

        for (InvestigationThread investigationThread : investigationThreads) {
            addRecordRow(investigationThread.getId(), investigationThread.getName(), listener);
        }
    }

    public void renderTimeline(List<TimelineEvent> timelineEvents, String openRecordId, RecordListener listener) {
        contentList.removeAllViews();

        if (timelineEvents == null || timelineEvents.isEmpty()) {
            contentList.addView(getPlaceholderText("No timeline contradictions visible."));
            return;
        }

        for (TimelineEvent timelineEvent : timelineEvents) {
            if (timelineEvent.getId().equals(openRecordId)) {
                renderTimelineDetail(timelineEvent);
                return;
            }
        }

        for (TimelineEvent timelineEvent : timelineEvents) {
            addRecordRow(
                    timelineEvent.getId(),
                    timelineEvent.getDisplayTime() + "  " + timelineEvent.getTitle(),
                    listener);
        }
    }

    public void renderStatements(List<SuspectStatement> suspectStatements, String openRecordId, RecordListener listener) {
        contentList.removeAllViews();

        if (suspectStatements == null || suspectStatements.isEmpty()) {
            contentList.addView(getPlaceholderText("No suspect statements unlocked."));
            return;
        }

        for (SuspectStatement suspectStatement : suspectStatements) {
            if (suspectStatement.getId().equals(openRecordId)) {
                renderStatementDetail(suspectStatement);
                return;
            }
        }

        for (SuspectStatement suspectStatement : suspectStatements) {
            addRecordRow(suspectStatement.getId(), suspectStatement.getSpeaker(), listener);
        }
    }

    private void addRecordRow(String recordId, String title, RecordListener listener) {
        boolean isNew = listener.isNew(recordId);
        TextView row = inflateRow();
        row.setText((isNew ? "! " : "") + title);
        if (isNew) {
            row.setBackgroundResource(R.color.handheld_record_bg_new);
        }
        row.setOnClickListener(view -> listener.onRecordSelected(recordId));
        contentList.addView(row);
    }

    private void renderEvidenceDetail(EvidenceItem evidenceItem) {
        addHeader(evidenceItem.getTitle());
        addKeyValue("Source", evidenceItem.getSource());
        addKeyValue("Summary", evidenceItem.getPlayerSummary());
        addKeyValue("Note", evidenceItem.getTechnicalNote());
    }

    private void renderThreadDetail(InvestigationThread investigationThread) {
        addHeader(investigationThread.getName());
        addKeyValue("Question", investigationThread.getCoreQuestion());
        for (String observation : investigationThread.getObservations()) {
            addKeyValue("Observation", observation);
        }
        if (investigationThread.isStabilized()) {
            addKeyValue("Conclusion", investigationThread.getStabilizedConclusion(),
                    R.color.handheld_record_bg_accent_positive);
        } else {
            addKeyValue("Conclusion", "???");
        }
    }

    private void renderTimelineDetail(TimelineEvent timelineEvent) {
        addHeader(timelineEvent.getTitle());
        addKeyValue("Time", timelineEvent.getDisplayTime());
        addKeyValue("Initial", timelineEvent.getInitialInterpretation());
        addKeyValue("Current", timelineEvent.getResolvedInterpretation(),
                timelineEvent.isStabilized() ? R.color.handheld_record_bg_accent_positive : 0);
    }

    private void renderStatementDetail(SuspectStatement suspectStatement) {
        addHeader(suspectStatement.getSpeaker());
        addKeyValue("Claim", suspectStatement.getClaim());
        addKeyValue("Detail", suspectStatement.getSuspiciousDetail());
        if (suspectStatement.isContradicted()) {
            addKeyValue("Status", suspectStatement.getContradictionText(),
                    R.color.handheld_record_bg_accent_negative);
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
        contentList.removeAllViews();

        if (!unlocked) {
            contentList.addView(getPlaceholderText(feedback));
            return;
        }

        TextView status = addHeader(feedback);
        if (completed) {
            tint(status, R.color.handheld_record_bg_accent_positive);
        }

        boolean hasSelectedEvents = selectedEvents != null && !selectedEvents.isEmpty();
        if (hasSelectedEvents) {
            addHeader("Selected sequence");
            for (int index = 0; index < selectedEvents.size(); index++) {
                FinalTimelineEvent event = selectedEvents.get(index);
                addKeyValue(String.valueOf(index + 1), event.getDisplayTime() + " // " + event.getTitle());
            }
        }

        if (!completed) {
            TextView resetButton = createButton("Reset Sequence");
            tintImportant(resetButton);
            resetButton.setOnClickListener(view -> selectionListener.onFinalTimelineResetRequested());
            contentList.addView(resetButton);

            addHeader("Tap events in true order");
            for (FinalTimelineEvent availableEvent : availableEvents) {
                contentList.addView(createAvailableFinalTimelineEventButton(availableEvent, selectionListener));
            }
        } else {
            TextView confrontButton = createButton("Confront Partner");
            tintImportant(confrontButton);
            confrontButton.setOnClickListener(view -> selectionListener.onConfrontationRequested());
            contentList.addView(confrontButton);
        }
    }


    private TextView inflateRow() {
        return (TextView) LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.view_handheld_record_row, contentList, false);
    }

    private TextView addHeader(String text) {
        TextView header = inflateRow();
        header.setText(text);
        tintImportant(header);
        contentList.addView(header);
        return header;
    }

    private void addKeyValue(String key, String value) {
        addKeyValue(key, value, 0);
    }

    private void addKeyValue(String key, String value, int valueBgColorRes) {
        View row = LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.view_handheld_table_row, contentList, false);

        ((TextView) row.findViewById(R.id.handheld_table_key)).setText(key);

        TextView valueCell = row.findViewById(R.id.handheld_table_value);
        valueCell.setText(value);
        if (valueBgColorRes != 0) {
            valueCell.setBackgroundResource(valueBgColorRes);
        }

        contentList.addView(row);
    }

    private View getPlaceholderText(String message) {
        TextView placeholder = inflateRow();
        placeholder.setText(message);
        return placeholder;
    }

    private View createAvailableFinalTimelineEventButton(
            FinalTimelineEvent finalTimelineEvent,
            FinalTimelineSelectionListener selectionListener
    ) {
        TextView button = createButton(finalTimelineEvent.getDisplayTime() + " // " + finalTimelineEvent.getTitle());
        button.setOnClickListener(view -> selectionListener.onFinalTimelineEventSelected(finalTimelineEvent.getId()));
        return button;
    }

    private TextView createButton(String text) {
        TextView button = (TextView) LayoutInflater.from(rootView.getContext())
                .inflate(R.layout.view_handheld_button, contentList, false);
        button.setText(text);
        return button;
    }

    private void tint(TextView cell, int backgroundColorRes) {
        cell.setBackgroundResource(backgroundColorRes);
    }

    private void tintImportant(TextView cell) {
        cell.setBackgroundResource(R.color.handheld_record_bg_important);
        cell.setTextColor(ContextCompat.getColor(rootView.getContext(), R.color.handheld_record_txt_important));
    }

    private void styleTab(TextView button, boolean selected) {
        button.setTextAppearance(selected
                ? R.style.handheld_display_element_active
                : R.style.handheld_display_element_inactive);
        button.setBackgroundColor(Color.parseColor(selected ? "#455445" : "#506250"));
    }
}
