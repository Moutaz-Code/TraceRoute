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
        Hotspot bestHotspot = null;

        for (Hotspot hotspot : roomDefinition.getHotspots()) {
            if (hotspot.contains(normalizedX, normalizedY)) {
                if (isBetterMatch(hotspot, bestHotspot)) {
                    bestHotspot = hotspot;
                }
            }
        }

        return bestHotspot;
    }

    private boolean isBetterMatch(Hotspot candidate, Hotspot currentBest) {
        if (currentBest == null) {
            return true;
        }

        int candidatePriority = getActionPriority(candidate.getActionType());
        int currentPriority = getActionPriority(currentBest.getActionType());
        if (candidatePriority != currentPriority) {
            return candidatePriority < currentPriority;
        }

        return getArea(candidate) < getArea(currentBest);
    }

    private int getActionPriority(HotspotActionType actionType) {
        if (actionType == HotspotActionType.INSPECT_EVIDENCE
                || actionType == HotspotActionType.OPEN_LAPTOP) {
            return 0;
        }

        if (actionType == HotspotActionType.OPEN_HANDHELD) {
            return 1;
        }

        if (actionType == HotspotActionType.NAVIGATE_ROOM) {
            return 2;
        }

        return 3;
    }

    private float getArea(Hotspot hotspot) {
        return hotspot.getWidth() * hotspot.getHeight();
    }
}
