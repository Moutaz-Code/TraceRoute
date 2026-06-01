package motaz.pharaon.adu.ac.ae.traceroute.core;

public class OverlayController {
    /*
     * Purpose:
     * - Coordinates which fragment-based overlay is visible.
     *
     * Responsibilities:
     * - Keep overlay decisions separate from MainActivity and gameplay systems.
     * - Request opening/closing handheld screens, laptop popup, and final timeline ordering.
     * - Later, provide a single place to enforce "only one major overlay at a time."
     *
     * Depends on:
     * - GameStateMachine for current high-level mode.
     * - GameShellFragment or FragmentManager adapter when implemented.
     *
     * Used by:
     * - GameController.
     * - GameShellFragment.
     */
}
