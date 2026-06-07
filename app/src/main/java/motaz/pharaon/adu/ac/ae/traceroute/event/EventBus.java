package motaz.pharaon.adu.ac.ae.traceroute.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;

// Abstract shape — not implementation
interface GameEventListener {
    void onEvent(GameEvent event);
}

class EventBus {
    /*
     * Purpose:
     * - Lightweight publish/subscribe event dispatcher for gameplay events.
     *
     * Responsibilities:
     * - Allow gameplay systems to subscribe to specific event types.
     * - Post events from any thread and dispatch them on the main thread.
     * - Keep gameplay systems decoupled from each other.
     *
     * Used by:
     * - GameController for cross-system notifications.
     * - WorldInvestigationScene for hotspot results.
     */

    private static final EventBus INSTANCE = new EventBus();

    private final Map<GameEventType, List<GameEventListener>> listeners;
    private final Handler mainThreadHandler;

    private EventBus() {
        listeners = new EnumMap<>(GameEventType.class);
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static EventBus getInstance() {
        return INSTANCE;
    }

    public synchronized void subscribe(GameEventType type, GameEventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public synchronized void unsubscribe(GameEventType type, GameEventListener listener) {
        List<GameEventListener> typeListeners = listeners.get(type);
        if (typeListeners != null) {
            typeListeners.remove(listener);
        }
    }

    public void post(GameEvent event) {
        mainThreadHandler.post(() -> {
            List<GameEventListener> typeListeners;
            synchronized (this) {
                List<GameEventListener> raw = listeners.get(event.getType());
                typeListeners = raw == null ? null : new ArrayList<>(raw);
            }
            if (typeListeners != null) {
                for (GameEventListener listener : typeListeners) {
                    listener.onEvent(event);
                }
            }
        });
    }
}