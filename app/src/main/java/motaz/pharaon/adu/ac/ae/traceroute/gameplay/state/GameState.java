package motaz.pharaon.adu.ac.ae.traceroute.gameplay.state;

enum GameState {
    MAIN_MENU,
    EXPLORING,       // point-and-click scene navigation
    DIALOGUE,        // dialogue overlay active
    PUZZLE,          // puzzle scene replacing exploration
    EVIDENCE_BOARD,  // inventory overlay active
    CASE_SOLVED      // terminal state
}
