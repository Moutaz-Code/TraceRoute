package motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline;

public class TimelineEvent {
    /*
     * Purpose:
     * - Data model for one apparent or true timeline event.
     *
     * Responsibilities:
     * - Store event id, displayed time, truth time, certainty, and linked evidence later.
     * - Support both investigation timeline and final ordering sequence.
     *
     * Depends on:
     * - Static content from DataLoader.
     *
     * Used by:
     * - TimelineSystem.
     * - HandheldFragment through HandheldController.
     * - Final timeline ordering UI.
     */

    private final String id;
    private final String displayTime;
    private final String title;
    private final String initialInterpretation;
    private final String resolvedInterpretation;
    private boolean visible;
    private boolean contradicted;
    private boolean stabilized;

    public TimelineEvent(
            String id,
            String displayTime,
            String title,
            String initialInterpretation,
            String resolvedInterpretation
    ) {
        this.id = id;
        this.displayTime = displayTime;
        this.title = title;
        this.initialInterpretation = initialInterpretation;
        this.resolvedInterpretation = resolvedInterpretation;
        visible = false;
        contradicted = false;
        stabilized = false;
    }

    public String getId() {
        return id;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public String getTitle() {
        return title;
    }

    public String getInitialInterpretation() {
        return initialInterpretation;
    }

    public String getResolvedInterpretation() {
        return resolvedInterpretation;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isContradicted() {
        return contradicted;
    }

    public void setContradicted(boolean contradicted) {
        this.contradicted = contradicted;
    }

    public boolean isStabilized() {
        return stabilized;
    }

    public void setStabilized(boolean stabilized) {
        this.stabilized = stabilized;
    }
}
