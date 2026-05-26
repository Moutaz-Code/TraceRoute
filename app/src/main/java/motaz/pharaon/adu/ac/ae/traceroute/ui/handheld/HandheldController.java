package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

public class HandheldController {
    /*
     * Purpose:
     * - Plain Java controller for the handheld overlay's internal screens.
     *
     * Responsibilities:
     * - Track which handheld screen/tab is selected.
     * - Ask GameController for evidence, thread, timeline, and statement state.
     * - Keep handheld UI behavior out of Fragment lifecycle methods.
     * - Let HandheldFragment remain the required Android fragment wrapper, not the whole UI brain.
     *
     * Depends on:
     * - HandheldScreen.
     * - GameController when implemented.
     *
     * Used by:
     * - HandheldFragment.
     */

    private HandheldScreen selectedScreen;

    public HandheldController() {
        selectedScreen = HandheldScreen.EVIDENCE;
    }

    public HandheldScreen getSelectedScreen() {
        return selectedScreen;
    }

    public void selectScreen(HandheldScreen selectedScreen) {
        this.selectedScreen = selectedScreen;
    }
}
