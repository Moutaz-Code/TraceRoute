package motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline;

public class FinalTimelineEvent {
    /*
     * Purpose:
     * - Data model for one event in the final timeline-ordering sequence.
     *
     * Responsibilities:
     * - Store the correct final order, display time, title, and short summary.
     * - Stay separate from TimelineEvent, which is for investigation contradictions.
     *
     * Depends on:
     * - Static case content seeded by GameRuntime for the current graybox stage.
     *
     * Used by:
     * - TimelineSystem for ordering validation.
     * - HandheldFragment / HandheldViewBinder for the final timeline screen.
     */

    private final String id;
    private final int orderIndex;
    private final String displayTime;
    private final String title;
    private final String summary;

    public FinalTimelineEvent(
            String id,
            int orderIndex,
            String displayTime,
            String title,
            String summary
    ) {
        this.id = id;
        this.orderIndex = orderIndex;
        this.displayTime = displayTime;
        this.title = title;
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }
}
