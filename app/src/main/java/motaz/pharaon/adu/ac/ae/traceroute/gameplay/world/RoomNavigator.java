package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;

public class RoomNavigator {
    /*
     * Purpose:
     * - Owns current apartment room/view and valid navigation between views.
     *
     * Responsibilities:
     * - Track the current RoomId.
     * - Move between fixed views when a navigation hotspot is tapped.
     * - Provide the active RoomDefinition to WorldInvestigationScene.
     *
     * Depends on:
     * - RoomDefinition.
     * - RoomId.
     *
     * Used by:
     * - GameController.
     * - WorldInvestigationScene.
     */

    private static final String PLACEHOLDER_ROOM_TEXTURE = "textures/scenes/room_test.png";

    private final Map<RoomId, RoomDefinition> rooms;
    private final RoomId[] roomOrder;
    private RoomId currentRoomId;

    public RoomNavigator() {
        rooms = new EnumMap<>(RoomId.class);
        roomOrder = new RoomId[] {
                RoomId.LIVING_ROOM,
                RoomId.ENTRY_SMART_LOCK,
                RoomId.KITCHEN_SIDE_AREA,
                RoomId.OFFICE_DEVICE_CORNER,
                RoomId.CCTV_WINDOW_OPTIONAL
        };

        addRoom(buildRoom(
                RoomId.LIVING_ROOM,
                "Living Room / Body Scene",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.CCTV_WINDOW_OPTIONAL,
                RoomId.ENTRY_SMART_LOCK
        ));
        addRoom(buildRoom(
                RoomId.ENTRY_SMART_LOCK,
                "Entry / Smart-Lock Area",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.LIVING_ROOM,
                RoomId.KITCHEN_SIDE_AREA
        ));
        addRoom(buildRoom(
                RoomId.KITCHEN_SIDE_AREA,
                "Kitchen / Side Area",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.ENTRY_SMART_LOCK,
                RoomId.OFFICE_DEVICE_CORNER
        ));
        addRoom(buildRoom(
                RoomId.OFFICE_DEVICE_CORNER,
                "Office / Device Corner",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.KITCHEN_SIDE_AREA,
                RoomId.CCTV_WINDOW_OPTIONAL
        ));
        addRoom(buildRoom(
                RoomId.CCTV_WINDOW_OPTIONAL,
                "CCTV / Window Angle",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.OFFICE_DEVICE_CORNER,
                RoomId.LIVING_ROOM
        ));

        currentRoomId = RoomId.LIVING_ROOM;
    }

    public RoomDefinition getCurrentRoom() {
        return rooms.get(currentRoomId);
    }

    public RoomId getCurrentRoomId() {
        return currentRoomId;
    }

    public void goToRoom(RoomId roomId) {
        if (rooms.containsKey(roomId)) {
            currentRoomId = roomId;
        }
    }

    public void goToNextRoom() {
        int currentIndex = getCurrentRoomIndex();
        int nextIndex = (currentIndex + 1) % roomOrder.length;
        currentRoomId = roomOrder[nextIndex];
    }

    public void goToPreviousRoom() {
        int currentIndex = getCurrentRoomIndex();
        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            previousIndex = roomOrder.length - 1;
        }

        currentRoomId = roomOrder[previousIndex];
    }

    private void addRoom(RoomDefinition roomDefinition) {
        rooms.put(roomDefinition.getId(), roomDefinition);
    }

    private RoomDefinition buildRoom(
            RoomId id,
            String displayName,
            String backgroundTexturePath,
            RoomId previousRoomId,
            RoomId nextRoomId
    ) {
        List<Hotspot> hotspots = new ArrayList<>();

        hotspots.add(Hotspot.navigation(
                "nav_previous",
                0.0f,
                0.0f,
                0.18f,
                1.0f,
                previousRoomId
        ));
        hotspots.add(Hotspot.navigation(
                "nav_next",
                0.82f,
                0.0f,
                0.18f,
                1.0f,
                nextRoomId
        ));

        return new RoomDefinition(id, displayName, backgroundTexturePath, hotspots);
    }

    private int getCurrentRoomIndex() {
        for (int i = 0; i < roomOrder.length; i++) {
            if (roomOrder[i] == currentRoomId) {
                return i;
            }
        }

        return 0;
    }
}
