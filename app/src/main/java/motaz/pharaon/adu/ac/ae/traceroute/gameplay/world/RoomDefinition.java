package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;

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
    private final String debugContextText;
    private final String debugHotspotHint;
    private final String backgroundTexturePath;
    private final List<Hotspot> hotspots;

    public RoomDefinition(
            RoomId id,
            String displayName,
            String debugContextText,
            String debugHotspotHint,
            String backgroundTexturePath,
            List<Hotspot> hotspots
    ) {
        this.id = id;
        this.displayName = displayName;
        this.debugContextText = debugContextText;
        this.debugHotspotHint = debugHotspotHint;
        this.backgroundTexturePath = backgroundTexturePath;
        this.hotspots = Collections.unmodifiableList(new ArrayList<>(hotspots));
    }

    public RoomId getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDebugContextText() {
        return debugContextText;
    }

    public String getDebugHotspotHint() {
        return debugHotspotHint;
    }

    public String getBackgroundTexturePath() {
        return backgroundTexturePath;
    }

    public List<Hotspot> getHotspots() {
        return hotspots;
    }
}
