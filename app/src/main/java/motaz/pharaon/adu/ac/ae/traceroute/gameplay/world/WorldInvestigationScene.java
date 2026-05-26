package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotActionType;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.scene.Scene;

    /*
     * Purpose:
     * - Runtime scene for apartment inspection.
     *
     * Responsibilities:
     * - Render the current fixed room background through RenderPipeline.
     * - Route taps to HotspotSystem.
     * - Ask GameController to collect evidence, navigate rooms, open handheld, or open terminal.
     * - Stay separate from Android Fragments; this is game logic/rendering mode, not UI container.
     *
     * Depends on:
     * - RoomNavigator for current room.
     * - HotspotSystem for tap detection.
     * - GameController for gameplay side effects.
     * - RenderPipeline for drawing.
     *
     * Used by:
     * - SceneDirector.
        * - GameController.
        */
public class WorldInvestigationScene implements Scene {
    private final RoomNavigator roomNavigator;
    private final HotspotSystem hotspotSystem;

    public WorldInvestigationScene() {
        roomNavigator = new RoomNavigator();
        hotspotSystem = new HotspotSystem();
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(RenderPipeline renderer) {
        RoomDefinition currentRoom = roomNavigator.getCurrentRoom();
        renderer.drawRoomBackground(currentRoom.getBackgroundTexturePath());

        float[] roomColor = getDebugRoomColor(currentRoom.getId());
        renderer.drawColorQuad(-0.92f, 0.78f, 0.22f, 0.12f, roomColor[0], roomColor[1], roomColor[2], 0.95f);

        for (Hotspot hotspot : currentRoom.getHotspots()) {
            drawHotspotDebugOverlay(renderer, hotspot);
        }
    }

    @Override
    public void onTap(float x, float y) {
        RoomDefinition currentRoom = roomNavigator.getCurrentRoom();
        Hotspot hotspot = hotspotSystem.findHotspot(currentRoom, x, y);

        if (hotspot == null) {
            return;
        }

        if (hotspot.getActionType() == HotspotActionType.NAVIGATE_ROOM) {
            roomNavigator.goToRoom(hotspot.getTargetRoomId());
        }
    }

    private void drawHotspotDebugOverlay(RenderPipeline renderer, Hotspot hotspot) {
        float ndcX = hotspot.getX() * 2.0f - 1.0f;
        float ndcY = 1.0f - ((hotspot.getY() + hotspot.getHeight()) * 2.0f);
        float ndcWidth = hotspot.getWidth() * 2.0f;
        float ndcHeight = hotspot.getHeight() * 2.0f;

        if ("nav_previous".equals(hotspot.getId())) {
            renderer.drawColorQuad(ndcX, ndcY, ndcWidth, ndcHeight, 0.15f, 0.65f, 0.95f, 0.18f);
        } else if ("nav_next".equals(hotspot.getId())) {
            renderer.drawColorQuad(ndcX, ndcY, ndcWidth, ndcHeight, 0.95f, 0.55f, 0.20f, 0.18f);
        }
    }

    private float[] getDebugRoomColor(RoomId roomId) {
        switch (roomId) {
            case ENTRY_SMART_LOCK:
                return new float[] {0.25f, 0.70f, 0.95f};
            case KITCHEN_SIDE_AREA:
                return new float[] {0.95f, 0.80f, 0.25f};
            case OFFICE_DEVICE_CORNER:
                return new float[] {0.30f, 0.95f, 0.55f};
            case CCTV_WINDOW_OPTIONAL:
                return new float[] {0.95f, 0.35f, 0.35f};
            case LIVING_ROOM:
            default:
                return new float[] {0.80f, 0.45f, 0.95f};
        }
    }
}
