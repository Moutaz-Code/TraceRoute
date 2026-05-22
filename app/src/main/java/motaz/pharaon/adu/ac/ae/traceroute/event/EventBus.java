package motaz.pharaon.adu.ac.ae.traceroute.event;

// Abstract shape — not implementation
interface GameEventListener {
    void onEvent(GameEvent event);
}

class EventBus {
    void subscribe(GameEventType type, GameEventListener listener) {}
    void post(GameEvent event) {}  // called from any thread, dispatches on main
}