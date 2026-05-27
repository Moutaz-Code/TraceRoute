package motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;

public class TimelineSystem {
    /*
     * Purpose:
     * - Owns timeline reveal state and final timeline validation.
     *
     * Responsibilities:
     * - Track which timeline events are visible, uncertain, contradicted, or stabilized.
     * - Provide timeline data to handheld UI.
     * - Validate the final timeline ordering sequence.
     *
     * Depends on:
     * - TimelineEvent data.
     * - EvidenceSystem/ThreadSystem notifications through GameController.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - HandheldFragment through HandheldController.
     * - Final timeline ordering UI.
     */

    private static final String DEATH_WINDOW_THREAD_ID = "T01_DEATH_WINDOW_INSTABILITY";
    private static final String POSTMORTEM_ACTIVITY_THREAD_ID = "T02_POSTMORTEM_ACTIVITY";
    private static final String APARTMENT_ACCESS_THREAD_ID = "T03_APARTMENT_ACCESS";
    private static final String DATA_SALE_MOTIVE_THREAD_ID = "T04_DATA_SALE_MOTIVE";
    private static final String SCENE_DISTURBANCE_THREAD_ID = "T05_SCENE_DISTURBANCE";
    private static final String POST_21_ACTIVITY_EVENT_ID = "TL01_POST_21_ACTIVITY_UNRELIABLE";

    private final Map<String, TimelineEvent> timelineEvents;
    private final Map<String, FinalTimelineEvent> finalTimelineEvents;
    private final List<String> selectedFinalTimelineEventIds;
    private boolean finalTimelineUnlocked;
    private boolean finalTimelineCompleted;
    private String finalTimelineFeedback;

    public TimelineSystem() {
        timelineEvents = new LinkedHashMap<>();
        finalTimelineEvents = new LinkedHashMap<>();
        selectedFinalTimelineEventIds = new ArrayList<>();
        finalTimelineUnlocked = false;
        finalTimelineCompleted = false;
        finalTimelineFeedback = "Stabilize the core investigation threads to unlock the final sequence.";
    }

    public void registerEvent(TimelineEvent timelineEvent) {
        timelineEvents.put(timelineEvent.getId(), timelineEvent);
    }

    public void registerFinalTimelineEvent(FinalTimelineEvent finalTimelineEvent) {
        finalTimelineEvents.put(finalTimelineEvent.getId(), finalTimelineEvent);
    }

    public TimelineEvent getEvent(String timelineEventId) {
        return timelineEvents.get(timelineEventId);
    }

    public void onThreadStateChanged(ThreadSystem threadSystem) {
        updateInvestigationTimeline(threadSystem);
        updateFinalTimelineUnlock(threadSystem);
    }

    public List<TimelineEvent> getVisibleTimelineEvents() {
        List<TimelineEvent> visibleEvents = new ArrayList<>();

        for (TimelineEvent timelineEvent : timelineEvents.values()) {
            if (timelineEvent.isVisible()) {
                visibleEvents.add(timelineEvent);
            }
        }

        return visibleEvents;
    }

    public boolean isFinalTimelineUnlocked() {
        return finalTimelineUnlocked;
    }

    public boolean isFinalTimelineCompleted() {
        return finalTimelineCompleted;
    }

    public String getFinalTimelineFeedback() {
        return finalTimelineFeedback;
    }

    public void selectFinalTimelineEvent(String eventId) {
        if (!finalTimelineUnlocked || finalTimelineCompleted) {
            return;
        }

        if (!finalTimelineEvents.containsKey(eventId) || selectedFinalTimelineEventIds.contains(eventId)) {
            return;
        }

        selectedFinalTimelineEventIds.add(eventId);

        if (selectedFinalTimelineEventIds.size() == finalTimelineEvents.size()) {
            if (isSelectedOrderCorrect()) {
                finalTimelineCompleted = true;
                finalTimelineFeedback = "Timeline stabilized. The partner killed earlier, staged continuity, and left behind the false death window.";
            } else {
                selectedFinalTimelineEventIds.clear();
                finalTimelineFeedback = "Sequence unstable. Try again from the earliest confirmed event.";
            }
        } else {
            finalTimelineFeedback = "Selected "
                    + selectedFinalTimelineEventIds.size()
                    + " of "
                    + finalTimelineEvents.size()
                    + " events.";
        }
    }

    public void clearFinalTimelineSelection() {
        if (finalTimelineCompleted) {
            return;
        }

        selectedFinalTimelineEventIds.clear();
        finalTimelineFeedback = "Sequence cleared. Start from the earliest confirmed event.";
    }

    public List<FinalTimelineEvent> getSelectedFinalTimelineEvents() {
        List<FinalTimelineEvent> selectedEvents = new ArrayList<>();

        for (String eventId : selectedFinalTimelineEventIds) {
            FinalTimelineEvent finalTimelineEvent = finalTimelineEvents.get(eventId);
            if (finalTimelineEvent != null) {
                selectedEvents.add(finalTimelineEvent);
            }
        }

        return selectedEvents;
    }

    public List<String> getSelectedFinalTimelineEventIds() {
        return new ArrayList<>(selectedFinalTimelineEventIds);
    }

    public void restoreFinalTimelineState(List<String> selectedEventIds, boolean completed) {
        selectedFinalTimelineEventIds.clear();
        finalTimelineCompleted = false;
        boolean alreadyUnlockedByCoreState = finalTimelineUnlocked;

        boolean restoreInputValid = selectedEventIds != null;
        if (selectedEventIds != null) {
            for (String eventId : selectedEventIds) {
                if (!finalTimelineEvents.containsKey(eventId) || selectedFinalTimelineEventIds.contains(eventId)) {
                    restoreInputValid = false;
                    continue;
                }
                selectedFinalTimelineEventIds.add(eventId);
            }
        } else {
            restoreInputValid = false;
        }

        restoreInputValid = restoreInputValid && isSelectedOrderPrefixCorrect();
        boolean restoreCompleted = completed
                && restoreInputValid
                && !finalTimelineEvents.isEmpty()
                && selectedFinalTimelineEventIds.size() == finalTimelineEvents.size()
                && isSelectedOrderCorrect();

        if (restoreCompleted) {
            finalTimelineCompleted = true;
            finalTimelineUnlocked = true;
            finalTimelineFeedback = "Timeline stabilized. The partner killed earlier, staged continuity, and left behind the false death window.";
        } else if (restoreInputValid && !completed && !selectedFinalTimelineEventIds.isEmpty()) {
            finalTimelineUnlocked = true;
            finalTimelineFeedback = "Selected "
                    + selectedFinalTimelineEventIds.size()
                    + " of "
                    + finalTimelineEvents.size()
                    + " events.";
        } else {
            selectedFinalTimelineEventIds.clear();
            finalTimelineUnlocked = alreadyUnlockedByCoreState;
            finalTimelineFeedback = alreadyUnlockedByCoreState
                    ? "Core threads stabilized. Order the final sequence."
                    : "Stabilize the core investigation threads to unlock the final sequence.";
        }
    }

    public List<FinalTimelineEvent> getAvailableFinalTimelineEvents() {
        List<FinalTimelineEvent> availableEvents = new ArrayList<>();

        if (finalTimelineCompleted) {
            return availableEvents;
        }

        for (FinalTimelineEvent finalTimelineEvent : finalTimelineEvents.values()) {
            if (!selectedFinalTimelineEventIds.contains(finalTimelineEvent.getId())) {
                availableEvents.add(finalTimelineEvent);
            }
        }

        return availableEvents;
    }

    private void updateInvestigationTimeline(ThreadSystem threadSystem) {
        InvestigationThread deathWindowThread = threadSystem.getThread(DEATH_WINDOW_THREAD_ID);
        TimelineEvent post21ActivityEvent = timelineEvents.get(POST_21_ACTIVITY_EVENT_ID);

        if (deathWindowThread == null || post21ActivityEvent == null) {
            return;
        }

        if (!deathWindowThread.getObservations().isEmpty()) {
            post21ActivityEvent.setVisible(true);
            post21ActivityEvent.setContradicted(true);
        }

        if (deathWindowThread.isStabilized()) {
            post21ActivityEvent.setStabilized(true);
        }
    }

    private void updateFinalTimelineUnlock(ThreadSystem threadSystem) {
        boolean allCoreThreadsStabilized =
                isThreadStabilized(threadSystem, DEATH_WINDOW_THREAD_ID)
                        && isThreadStabilized(threadSystem, POSTMORTEM_ACTIVITY_THREAD_ID)
                        && isThreadStabilized(threadSystem, APARTMENT_ACCESS_THREAD_ID)
                        && isThreadStabilized(threadSystem, DATA_SALE_MOTIVE_THREAD_ID)
                        && isThreadStabilized(threadSystem, SCENE_DISTURBANCE_THREAD_ID);

        if (allCoreThreadsStabilized && !finalTimelineUnlocked) {
            finalTimelineUnlocked = true;
            finalTimelineFeedback = "Core threads stabilized. Order the final sequence.";
        }
    }

    private boolean isThreadStabilized(ThreadSystem threadSystem, String threadId) {
        InvestigationThread investigationThread = threadSystem.getThread(threadId);
        return investigationThread != null && investigationThread.isStabilized();
    }

    private boolean isSelectedOrderCorrect() {
        for (int index = 0; index < selectedFinalTimelineEventIds.size(); index++) {
            FinalTimelineEvent finalTimelineEvent = finalTimelineEvents.get(selectedFinalTimelineEventIds.get(index));
            if (finalTimelineEvent == null || finalTimelineEvent.getOrderIndex() != index + 1) {
                return false;
            }
        }

        return true;
    }

    private boolean isSelectedOrderPrefixCorrect() {
        for (int index = 0; index < selectedFinalTimelineEventIds.size(); index++) {
            FinalTimelineEvent finalTimelineEvent = finalTimelineEvents.get(selectedFinalTimelineEventIds.get(index));
            if (finalTimelineEvent == null || finalTimelineEvent.getOrderIndex() != index + 1) {
                return false;
            }
        }

        return true;
    }
}
