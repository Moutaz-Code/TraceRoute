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
    private static final String POST_21_ACTIVITY_EVENT_ID = "TL01_POST_21_ACTIVITY_UNRELIABLE";

    private final Map<String, TimelineEvent> timelineEvents;

    public TimelineSystem() {
        timelineEvents = new LinkedHashMap<>();
    }

    public void registerEvent(TimelineEvent timelineEvent) {
        timelineEvents.put(timelineEvent.getId(), timelineEvent);
    }

    public TimelineEvent getEvent(String timelineEventId) {
        return timelineEvents.get(timelineEventId);
    }

    public void onThreadStateChanged(ThreadSystem threadSystem) {
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

    public List<TimelineEvent> getVisibleTimelineEvents() {
        List<TimelineEvent> visibleEvents = new ArrayList<>();

        for (TimelineEvent timelineEvent : timelineEvents.values()) {
            if (timelineEvent.isVisible()) {
                visibleEvents.add(timelineEvent);
            }
        }

        return visibleEvents;
    }
}
