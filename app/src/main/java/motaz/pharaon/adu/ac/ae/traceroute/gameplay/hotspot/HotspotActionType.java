package motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot;

public enum HotspotActionType {
    /*
     * Purpose:
     * - Describes what a tapped hotspot should do.
     *
     * Responsibilities:
     * - Keep hotspot effects explicit and easy to route.
     * - Avoid encoding behavior through loose strings.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - Hotspot.
     * - HotspotSystem.
     * - WorldInvestigationScene.
     */

    INSPECT_EVIDENCE,
    NAVIGATE_ROOM,
    OPEN_HANDHELD,
    OPEN_LAPTOP,
    SHOW_FLAVOR_TEXT
}
