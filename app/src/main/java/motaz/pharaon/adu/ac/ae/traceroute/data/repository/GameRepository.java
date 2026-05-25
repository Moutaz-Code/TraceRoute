package motaz.pharaon.adu.ac.ae.traceroute.data.repository;

public class GameRepository {
    /*
     * Purpose:
     * - Single facade for game data access.
     *
     * Responsibilities:
     * - Provide static case content to gameplay systems.
     * - Save/load playthrough state such as discovered evidence, thread conclusions, current room, and final timeline state.
     * - Keep gameplay systems from depending directly on SQLite or JSON parsing.
     *
     * Depends on:
     * - DataLoader for static content.
     * - GameDatabase for SQLite save state if SQLite is used.
     *
     * Used by:
     * - GameController.
     * - EvidenceSystem, ThreadSystem, TimelineSystem through GameController or direct injection.
     */
}
