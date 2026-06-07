package motaz.pharaon.adu.ac.ae.traceroute.data.db;

public final class GameContract {
    /*
     * Purpose:
     * - Defines SQLite table and column names if MVP save state uses SQLite.
     *
     * Responsibilities:
     * - Keep database schema names centralized.
     * - Avoid scattering raw SQL strings throughout gameplay systems.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - GameDatabase.
     * - GameRepository.
     */

    private GameContract() {
    }

    public static final class SaveState {
        /*
         * Purpose:
         * - Key/value save table for the MVP playthrough.
         *
         * Responsibilities:
         * - Store small pieces of state without exposing table names to gameplay code.
         */

        public static final String TABLE_NAME = "save_state";
        public static final String COLUMN_KEY = "state_key";
        public static final String COLUMN_VALUE = "state_value";

        public static final String KEY_COLLECTED_EVIDENCE = "collected_evidence";
        public static final String KEY_CURRENT_ROOM = "current_room";
        public static final String KEY_SELECTED_FINAL_TIMELINE = "selected_final_timeline";
        public static final String KEY_FINAL_TIMELINE_COMPLETED = "final_timeline_completed";
        public static final String KEY_CASE_COMPLETED = "case_completed";

        private SaveState() {
        }
    }
}
