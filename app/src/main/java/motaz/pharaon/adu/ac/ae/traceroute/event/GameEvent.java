package motaz.pharaon.adu.ac.ae.traceroute.event;

public class GameEvent {
    /*
     * Purpose:
     * - Carries event type and an optional string payload between systems.
     *
     * Used by:
     * - EventBus subscribers and publishers.
     */

    private final GameEventType type;
    private final String payloadId;

    public GameEvent(GameEventType type) {
        this(type, null);
    }

    public GameEvent(GameEventType type, String payloadId) {
        this.type = type;
        this.payloadId = payloadId;
    }

    public GameEventType getType() {
        return type;
    }

    public String getPayloadId() {
        return payloadId;
    }
}
