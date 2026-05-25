package motaz.pharaon.adu.ac.ae.traceroute.data.db;

public class GameDatabase {
    /*
     * Purpose:
     * - SQLite save-state helper, if SQLite is required for the project.
     *
     * Responsibilities:
     * - Eventually extend SQLiteOpenHelper.
     * - Create tables for discovered evidence, thread state, current room, and final timeline completion.
     * - Keep CRUD details behind GameRepository.
     *
     * Depends on:
     * - GameContract.
     * - Android SQLite APIs when implemented.
     *
     * Used by:
     * - GameRepository.
     *
     * Note:
     * - For a tiny MVP, SharedPreferences is technically enough.
     * - Use SQLite here if the course or team specifically wants database CRUD practice.
     */
}
