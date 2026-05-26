package motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;

public class HotspotSystem {
    /*
     * Purpose:
     * - Performs tap-to-hotspot detection for the current apartment room/view.
     *
     * Responsibilities:
     * - Convert screen taps into room-space coordinates when needed.
     * - Find which Hotspot, if any, was tapped.
     * - Return hotspot actions to WorldInvestigationScene/GameController.
     *
     * Depends on:
     * - Hotspot.
     * - RoomDefinition.
     *
     * Used by:
     * - WorldInvestigationScene.
     */

    public Hotspot findHotspot(RoomDefinition roomDefinition, float normalizedX, float normalizedY) {
        for (Hotspot hotspot : roomDefinition.getHotspots()) {
            if (hotspot.contains(normalizedX, normalizedY)) {
                return hotspot;
            }
        }

        return null;
    }
}
