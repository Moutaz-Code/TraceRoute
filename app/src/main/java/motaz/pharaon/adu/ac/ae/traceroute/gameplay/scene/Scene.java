package motaz.pharaon.adu.ac.ae.traceroute.gameplay.scene;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;

public interface Scene {
    /*
     * Purpose:
     * - Runtime mode interface, not an apartment room.
     *
     * Responsibilities:
     * - Update logic for the active mode.
     * - Render through RenderPipeline.
     * - Receive normalized tap input from GameController/InputManager.
     *
     * Depends on:
     * - RenderPipeline for drawing.
     *
     * Used by:
     * - SceneDirector and GameController.
     *
     * Examples:
     * - WorldInvestigationScene for apartment inspection.
     * - TimelineOrderingScene for the final reconstruction.
     */

    void update(float deltaTime);
    void render(RenderPipeline renderer);
    void onTap(float x, float y);
}
