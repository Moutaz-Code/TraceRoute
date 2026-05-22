package motaz.pharaon.adu.ac.ae.traceroute.event;

public enum GameEventType {
    HOTSPOT_TRIGGERED,    // Hotspot System → State Machine, Dialogue, Repository
    DIALOGUE_CONCLUDED,   // Dialogue → State Machine
    PUZZLE_SOLVED,        // Puzzle System → State Machine, Repository
    EVIDENCE_COLLECTED,   // Hotspot System → Repository, Inventory UI
    SCENE_TRANSITION,     // State Machine → Scene Director, Input Manager
}
