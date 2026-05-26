package motaz.pharaon.adu.ac.ae.traceroute.ui.handheld;

public enum HandheldScreen {
    /*
     * Purpose:
     * - Stable identifiers for handheld UI tabs/screens.
     *
     * Responsibilities:
     * - Avoid stringly typed tab names.
     * - Let HandheldFragment switch screens cleanly.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - HandheldFragment.
     * - OverlayController when implemented.
     */

    EVIDENCE,
    THREADS,
    TIMELINE,
    STATEMENTS,
    FINAL_TIMELINE,
    TERMINAL
}
