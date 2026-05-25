package motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence;

public class EvidenceItem {
    /*
     * Purpose:
     * - Runtime representation of an evidence item the player can collect or inspect.
     *
     * Responsibilities:
     * - Identify evidence by stable id such as E01_BODY_CONDITION.
     * - Store player-facing summary, category, and linked thread ids.
     * - Avoid owning UI rendering or database writes directly.
     *
     * Depends on:
     * - Static content loaded by DataLoader or hardcoded MVP content.
     *
     * Used by:
     * - EvidenceSystem.
     * - Handheld evidence screen.
     * - GameRepository for persistence.
     */
}
