package motaz.pharaon.adu.ac.ae.traceroute.gameplay.state;

public enum GameState {
    /*
     * Purpose:
     * - High-level mode enum for the MVP runtime.
     *
     * Responsibilities:
     * - Describe what kind of interaction is currently active.
     * - Stay broad; detailed case progress belongs to evidence/thread/timeline systems.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - GameStateMachine.
     * - GameController.
     */

    BOOT,
    WORLD_INVESTIGATION,
    HANDHELD_OPEN,
    TIMELINE_ORDERING,
    ENDING
}
