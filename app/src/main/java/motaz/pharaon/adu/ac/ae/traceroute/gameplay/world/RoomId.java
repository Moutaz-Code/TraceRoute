package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

public enum RoomId {
    /*
     * Purpose:
     * - Stable identifiers for fixed apartment views.
     *
     * Responsibilities:
     * - Avoid stringly typed room navigation.
     * - Match the MVP case plan's 4 required views plus optional fifth view.
     *
     * Depends on:
     * - None.
     *
     * Used by:
     * - RoomDefinition.
     * - RoomNavigator.
     * - WorldInvestigationScene.
     */

    LIVING_ROOM,
    ENTRY_SMART_LOCK,
    KITCHEN_SIDE_AREA,
    OFFICE_DEVICE_CORNER,
    CCTV_WINDOW_OPTIONAL
}
