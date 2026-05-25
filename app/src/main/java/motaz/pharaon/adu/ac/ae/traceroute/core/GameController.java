package motaz.pharaon.adu.ac.ae.traceroute.core;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.scene.Scene;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.WorldInvestigationScene;

    /*
     * Purpose:
     * - Central coordinator for MVP game logic.
     *
     * Responsibilities:
     * - Own the current runtime systems: rooms, evidence, threads, timeline, statements, terminal, and save state.
     * - Translate player actions into system updates.
     * - Decide when handheld screens, terminal popup, or final timeline ordering should unlock.
     * - Keep Android Fragment classes from becoming gameplay logic containers.
     *
     * Depends on:
     * - GameRepository for static content and save/load.
     * - GameStateMachine for high-level mode state.
     * - EvidenceSystem, ThreadSystem, TimelineSystem, StatementSystem, TerminalSession.
     *
     * Used by:
     * - GLRenderer for update/render coordination.
     * - GameShellFragment and handheld fragments for UI actions.
     */
public class GameController {
    private Scene worldInvestigationScene;

    public GameController(){
        worldInvestigationScene = new WorldInvestigationScene();
    }

    public void update(float deltaTime) {
        worldInvestigationScene.update(deltaTime);
    }

    public void render(RenderPipeline renderPipeline) {
        worldInvestigationScene.render(renderPipeline);
    }


}
