package motaz.pharaon.adu.ac.ae.traceroute.core;

public interface GameUiEventListener {
    /*
     * Purpose:
     * - Bridge gameplay events from the OpenGL/game thread back to Android UI.
     *
     * Responsibilities:
     * - Let gameplay request major UI surfaces without importing Fragment classes.
     * - Keep fragment opening in the Android UI layer.
     * - Support world hotspots such as open handheld, open terminal, and inspect evidence.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - GameView to marshal requests onto the Android UI thread.
     * - GLRenderer and GameController to pass requests from gameplay.
     * - GameShellFragment to perform the actual UI action.
     */

    void onOpenHandheldRequested();

    void onOpenTerminalRequested();

    void onEvidenceInspectionRequested(String evidenceId);
}
