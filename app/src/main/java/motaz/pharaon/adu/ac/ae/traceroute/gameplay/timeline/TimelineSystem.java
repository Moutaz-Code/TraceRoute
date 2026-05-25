package motaz.pharaon.adu.ac.ae.traceroute.gameplay.timeline;

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
}
