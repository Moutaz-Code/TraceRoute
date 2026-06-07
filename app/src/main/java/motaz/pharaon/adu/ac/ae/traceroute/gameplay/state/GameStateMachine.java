package motaz.pharaon.adu.ac.ae.traceroute.gameplay.state;

/*
 * Purpose:
 * - Tracks the current high-level runtime mode and enforces legal transitions.
 *
 * Used by:
 * - GameController to know which input handling and rendering path is active.
 */
public class GameStateMachine {

    private GameState currentState;

    public GameStateMachine() {
        currentState = GameState.BOOT;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    /** Transition to a new state. Returns true if the transition was accepted. */
    public boolean transitionTo(GameState newState) {
        if (newState == null || newState == currentState) {
            return false;
        }
        currentState = newState;
        return true;
    }

    public boolean is(GameState state) {
        return currentState == state;
    }

    public boolean isWorldInvestigation() {
        return currentState == GameState.WORLD_INVESTIGATION;
    }

    public boolean isHandheldOpen() {
        return currentState == GameState.HANDHELD_OPEN;
    }
}
