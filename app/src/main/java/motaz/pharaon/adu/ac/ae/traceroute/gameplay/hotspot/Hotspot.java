package motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;

public class Hotspot {
    /*
     * Purpose:
     * - Defines one tappable region inside a fixed apartment room/view.
     *
     * Responsibilities:
     * - Store hotspot id and rectangle bounds.
     * - Point to an action such as inspect evidence, navigate room, open handheld entry, or show flavor text.
     * - Stay data-like; actual effects are handled by HotspotSystem/GameController.
     *
     * Depends on:
     * - RoomDefinition data.
     *
     * Used by:
     * - HotspotSystem.
     * - WorldInvestigationScene.
     */

    private final String id;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private final HotspotActionType actionType;
    private final RoomId targetRoomId;
    private final String payloadId;

    public Hotspot(
            String id,
            float x,
            float y,
            float width,
            float height,
            HotspotActionType actionType,
            RoomId targetRoomId,
            String payloadId
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.actionType = actionType;
        this.targetRoomId = targetRoomId;
        this.payloadId = payloadId;
    }

    public static Hotspot navigation(
            String id,
            float x,
            float y,
            float width,
            float height,
            RoomId targetRoomId
    ) {
        return new Hotspot(
                id,
                x,
                y,
                width,
                height,
                HotspotActionType.NAVIGATE_ROOM,
                targetRoomId,
                null
        );
    }

    public boolean contains(float tapX, float tapY) {
        return tapX >= x
                && tapX <= x + width
                && tapY >= y
                && tapY <= y + height;
    }

    public String getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public HotspotActionType getActionType() {
        return actionType;
    }

    public RoomId getTargetRoomId() {
        return targetRoomId;
    }

    public String getPayloadId() {
        return payloadId;
    }
}
