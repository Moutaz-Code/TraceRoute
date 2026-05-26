package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

public class RoomDefinition {
    /*
     * Purpose:
     * - Data definition for one fixed apartment view.
     *
     * Responsibilities:
     * - Store room id, display name, background texture path, hotspot ids, and navigation targets.
     * - Represent "living room" or "office" as data, not as Android View subclasses.
     * - Stay free of rendering, touch handling, and persistence code.
     *
     * Depends on:
     * - RoomId.
     * - Hotspot data when implemented.
     *
     * Used by:
     * - RoomNavigator.
     * - HotspotSystem.
     * - WorldInvestigationScene.
     * - DataLoader.
     */

    private final RoomId id;
    private final String displayName;
    private final String backgroundTexturePath;

    public RoomDefinition(RoomId id, String displayName, String backgroundTexturePath) {
        this.id = id;
        this.displayName = displayName;
        this.backgroundTexturePath = backgroundTexturePath;
    }

    public RoomId getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBackgroundTexturePath() {
        return backgroundTexturePath;
    }
}
