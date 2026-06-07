package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.evidence.EvidenceItem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotActionType;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.scene.Scene;

    /*
     * Purpose:
     * - Runtime scene for apartment inspection.
     *
     * Responsibilities:
     * - Render the current fixed room background through RenderPipeline.
     * - Route taps to HotspotSystem.
     * - Ask GameController to collect evidence, navigate rooms, open handheld, or open the laptop.
     * - Stay separate from Android Fragments; this is game logic/rendering mode, not UI container.
     *
     * Depends on:
     * - RoomNavigator for current room.
     * - HotspotSystem for tap detection.
     * - GameController for gameplay side effects.
     * - RenderPipeline for drawing.
     *
     * Used by:
     * - SceneDirector.
        * - GameController.
        */
public class WorldInvestigationScene implements Scene {
    private final GameRuntime gameRuntime;
    private final GameUiEventListener uiEventListener;
    private final RoomNavigator roomNavigator;
    private final HotspotSystem hotspotSystem;

    public WorldInvestigationScene(GameRuntime gameRuntime, GameUiEventListener uiEventListener) {
        this.gameRuntime = gameRuntime;
        this.uiEventListener = uiEventListener;
        roomNavigator = new RoomNavigator(gameRuntime.getRoomDefinitions());
        roomNavigator.goToRoom(gameRuntime.getCurrentRoomId());
        hotspotSystem = new HotspotSystem();
        publishRoomContext();
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void render(RenderPipeline renderer) {
        RoomDefinition currentRoom = roomNavigator.getCurrentRoom();
        renderer.drawRoomBackground(currentRoom.getBackgroundTexturePath());
    }

    @Override
    public void onTap(float x, float y) {
        RoomDefinition currentRoom = roomNavigator.getCurrentRoom();
        Hotspot hotspot = hotspotSystem.findHotspot(currentRoom, x, y);

        if (hotspot == null) {
            return;
        }

        if (hotspot.getActionType() == HotspotActionType.NAVIGATE_ROOM) {
            roomNavigator.goToRoom(hotspot.getTargetRoomId());
            gameRuntime.setCurrentRoomId(roomNavigator.getCurrentRoomId());
            publishRoomContext();
        } else if (hotspot.getActionType() == HotspotActionType.OPEN_HANDHELD) {
            if (uiEventListener != null) {
                uiEventListener.onOpenHandheldRequested();
            }
        } else if (hotspot.getActionType() == HotspotActionType.OPEN_LAPTOP) {
            if (uiEventListener != null) {
                uiEventListener.onOpenLaptopRequested();
            }
        } else if (hotspot.getActionType() == HotspotActionType.INSPECT_EVIDENCE) {
            boolean alreadyCollected = gameRuntime.getEvidenceSystem().isCollected(hotspot.getPayloadId());
            EvidenceItem evidenceItem = gameRuntime.collectEvidence(hotspot.getPayloadId());
            if (uiEventListener != null) {
                String evidenceId = evidenceItem != null ? evidenceItem.getId() : hotspot.getPayloadId();
                uiEventListener.onEvidenceInspectionRequested(evidenceId, alreadyCollected);
            }
        }
    }

    private void publishRoomContext() {
        if (uiEventListener == null) {
            return;
        }

        RoomDefinition currentRoom = roomNavigator.getCurrentRoom();
        uiEventListener.onRoomContextChanged(
                currentRoom.getDisplayName(),
                currentRoom.getDebugContextText(),
                currentRoom.getDebugHotspotHint()
        );
    }

}
