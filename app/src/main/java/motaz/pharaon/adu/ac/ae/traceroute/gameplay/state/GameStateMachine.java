package motaz.pharaon.adu.ac.ae.traceroute.gameplay.state;

public class GameStateMachine {
    /*
     * Purpose:
     * - Small owner for high-level runtime state.
     *
     * Responsibilities:
     * - Track whether the player is in world investigation, handheld UI, final timeline ordering, or ending.
     * - Prevent scattered boolean state such as isInventoryOpen/isDialogueOpen/isPuzzleOpen.
     * - Later, enforce legal transitions if needed.
     *
     * Depends on:
     * - GameState.
     *
     * Used by:
     * - GameController.
     * - OverlayController when opening/closing handheld screens.
     */
}
