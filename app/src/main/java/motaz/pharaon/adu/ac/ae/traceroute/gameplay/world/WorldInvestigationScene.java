package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;
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

    public WorldInvestigationScene() {
        roomNavigator = new RoomNavigator();
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

        // Temporary navigation hit-zone markers: tap left/right screen edges to cycle rooms.
        renderer.drawColorQuad(-0.98f, -0.15f, 0.08f, 0.30f, 0.15f, 0.65f, 0.95f, 0.35f);
        renderer.drawColorQuad(0.90f, -0.15f, 0.08f, 0.30f, 0.95f, 0.55f, 0.20f, 0.35f);
    }

    @Override
    public void onTap(float x, float y) {
        if (x <= 0.20f) {
            roomNavigator.goToPreviousRoom();
        } else if (x >= 0.80f) {
            roomNavigator.goToNextRoom();
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
