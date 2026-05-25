package motaz.pharaon.adu.ac.ae.traceroute.gameplay.investigation;

public class ThreadSystem {
    /*
     * Purpose:
     * - Owns investigation thread progress for the current playthrough.
     *
     * Responsibilities:
     * - Unlock thread observations when relevant evidence is collected.
     * - Mark threads stabilized when their required clue chain is complete.
     * - Provide readable thread state to the handheld UI.
     *
     * Depends on:
     * - InvestigationThread data.
     * - EvidenceSystem events or GameController notifications.
     * - GameRepository for save/load.
     *
     * Used by:
     * - GameController.
     * - HandheldFragment through HandheldController.
     */
}
