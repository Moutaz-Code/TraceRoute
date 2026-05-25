package motaz.pharaon.adu.ac.ae.traceroute.core;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.WorldInvestigationScene;

public class GameController {
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
    private WorldInvestigationScene worldInvestigationScene; //hmmmmmmmmmmmmmmm, rethink this
}
