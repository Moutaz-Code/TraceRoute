package motaz.pharaon.adu.ac.ae.traceroute.data.loader;

public class DataLoader {
    /*
     * Purpose:
     * - Loads static case content from assets or temporary hardcoded builders.
     *
     * Responsibilities:
     * - Load room definitions, evidence definitions, timeline events, thread definitions, and statements.
     * - Keep static content separate from save-state persistence.
     * - Hide JSON parsing details from gameplay systems when JSON is added.
     *
     * Depends on:
     * - Android AssetManager when implemented.
     * - data.model classes.
     *
     * Used by:
     * - GameController during startup.
     * - GameRepository if static content loading is centralized there.
     */
}
