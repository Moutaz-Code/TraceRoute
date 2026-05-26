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
        renderer.drawDebugQuad(-0.25f, -0.25f, 0.5f, 0.5f);
        renderer.drawColorQuad(-0.5f, -0.5f, 0.5f, 0.5f, 0.7f, 0.8f, 0.25f, 1.0f);
    }

    @Override
    public void onTap(float x, float y) {
    }
}
