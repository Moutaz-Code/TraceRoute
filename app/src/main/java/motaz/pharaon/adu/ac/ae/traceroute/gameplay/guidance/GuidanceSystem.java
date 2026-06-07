package motaz.pharaon.adu.ac.ae.traceroute.gameplay.guidance;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.InvestigationThread;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation.ThreadSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline.TimelineSystem;

public class GuidanceSystem {
    private static final String DEATH_WINDOW_THREAD_ID = "T01_DEATH_WINDOW_INSTABILITY";
    private static final String POSTMORTEM_ACTIVITY_THREAD_ID = "T02_POSTMORTEM_ACTIVITY";
    private static final String APARTMENT_ACCESS_THREAD_ID = "T03_APARTMENT_ACCESS";
    private static final String DATA_SALE_MOTIVE_THREAD_ID = "T04_DATA_SALE_MOTIVE";
    private static final String SCENE_DISTURBANCE_THREAD_ID = "T05_SCENE_DISTURBANCE";

    public String getCurrentLead(
            EvidenceSystem evidenceSystem,
            ThreadSystem threadSystem,
            TimelineSystem timelineSystem
    ) {
        if (timelineSystem != null && timelineSystem.isFinalTimelineCompleted()) {
            return "Confront Marcus Hale (business partner).";
        }

        if (timelineSystem != null && timelineSystem.isFinalTimelineUnlocked()) {
            return "Order the final sequence in the handheld final timeline.";
        }

        if (evidenceSystem == null || evidenceSystem.getCollectedEvidenceIds().isEmpty()) {
            return "Look around, explore the rooms.";
        }

        if (!isThreadStabilized(threadSystem, DEATH_WINDOW_THREAD_ID)) {
            return "Compare the body condition with the AC log.";
        }

        if (!isThreadStabilized(threadSystem, POSTMORTEM_ACTIVITY_THREAD_ID)) {
            return "Search through laptop in signs of postmortem activity.";
        }

        if (!isThreadStabilized(threadSystem, APARTMENT_ACCESS_THREAD_ID)) {
            return "Check smart-lock and router records for access.";
        }

        if (!isThreadStabilized(threadSystem, DATA_SALE_MOTIVE_THREAD_ID)) {
            return "Find motive – search through personal correspondence";
        }

        if (!isThreadStabilized(threadSystem, SCENE_DISTURBANCE_THREAD_ID)) {
            return "Inspect the living-room disturbance and weapon.";
        }

        return "Order the final sequence in the handheld final timeline.";
    }

    private boolean isThreadStabilized(ThreadSystem threadSystem, String threadId) {
        if (threadSystem == null) {
            return false;
        }

        InvestigationThread investigationThread = threadSystem.getThread(threadId);
        return investigationThread != null && investigationThread.isStabilized();
    }
}
