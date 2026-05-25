package motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence;

public class EvidenceSystem {
    /*
     * Purpose:
     * - Owns evidence collection state for the current playthrough.
     *
     * Responsibilities:
     * - Track discovered evidence ids.
     * - Notify ThreadSystem/TimelineSystem when important evidence is collected.
     * - Provide evidence lists to handheld UI.
     * - Avoid SQLite details; persistence goes through GameRepository.
     *
     * Depends on:
     * - EvidenceItem.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - WorldInvestigationScene.
     * - HandheldFragment through HandheldController.
     */
}
