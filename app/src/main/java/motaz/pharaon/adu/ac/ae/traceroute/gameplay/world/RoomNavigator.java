package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import java.util.EnumMap;
import java.util.Map;

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
    private RoomId currentRoomId;

    public RoomNavigator() {
        rooms = new EnumMap<>(RoomId.class);

        addRoom(new RoomDefinition(
                RoomId.LIVING_ROOM,
                "Living Room / Body Scene",
                PLACEHOLDER_ROOM_TEXTURE
        ));
        addRoom(new RoomDefinition(
                RoomId.ENTRY_SMART_LOCK,
                "Entry / Smart-Lock Area",
                PLACEHOLDER_ROOM_TEXTURE
        ));
        addRoom(new RoomDefinition(
                RoomId.KITCHEN_SIDE_AREA,
                "Kitchen / Side Area",
                PLACEHOLDER_ROOM_TEXTURE
        ));
        addRoom(new RoomDefinition(
                RoomId.OFFICE_DEVICE_CORNER,
                "Office / Device Corner",
                PLACEHOLDER_ROOM_TEXTURE
        ));
        addRoom(new RoomDefinition(
                RoomId.CCTV_WINDOW_OPTIONAL,
                "CCTV / Window Angle",
                PLACEHOLDER_ROOM_TEXTURE
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

    private void addRoom(RoomDefinition roomDefinition) {
        rooms.put(roomDefinition.getId(), roomDefinition);
    }
}
