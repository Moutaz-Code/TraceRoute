package motaz.pharaon.adu.ac.ae.traceroute.gameplay.world;

import java.util.ArrayList;
import java.util.Collections;
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
        this(null);
    }

    public RoomNavigator(List<RoomDefinition> loadedRooms) {
        rooms = new EnumMap<>(RoomId.class);
        roomOrder = new RoomId[] {
                RoomId.LIVING_ROOM,
                RoomId.ENTRY_SMART_LOCK,
                RoomId.KITCHEN_SIDE_AREA,
                RoomId.OFFICE_DEVICE_CORNER,
                RoomId.CCTV_WINDOW_OPTIONAL
        };

        if (loadedRooms != null && !loadedRooms.isEmpty()) {
            for (RoomDefinition roomDefinition : loadedRooms) {
                addRoom(roomDefinition);
            }
            currentRoomId = RoomId.LIVING_ROOM;
            return;
        }

        addRoom(buildRoom(
                RoomId.LIVING_ROOM,
                "Living Room / Body Scene",
                "Victim: Adrian Voss. Physical clues define the real violence.",
                "Body / vase / company award",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.CCTV_WINDOW_OPTIONAL,
                RoomId.ENTRY_SMART_LOCK
        ));
        addRoom(buildRoom(
                RoomId.ENTRY_SMART_LOCK,
                "Entry / Smart-Lock Area",
                "Visitor log: Marcus Hale entered 19:05 and exited 19:42.",
                "Smart-lock access log",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.LIVING_ROOM,
                RoomId.KITCHEN_SIDE_AREA
        ));
        addRoom(buildRoom(
                RoomId.KITCHEN_SIDE_AREA,
                "Kitchen / Side Area",
                "Environmental systems contradict the apparent late death window.",
                "Thermostat / AC log",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.ENTRY_SMART_LOCK,
                RoomId.OFFICE_DEVICE_CORNER
        ));
        addRoom(buildRoom(
                RoomId.OFFICE_DEVICE_CORNER,
                "Office / Device Corner",
                "Victim laptop and network devices expose staging and motive.",
                "Victim laptop / router record",
                PLACEHOLDER_ROOM_TEXTURE,
                RoomId.KITCHEN_SIDE_AREA,
                RoomId.CCTV_WINDOW_OPTIONAL
        ));
        addRoom(buildRoom(
                RoomId.CCTV_WINDOW_OPTIONAL,
                "CCTV / Window Angle",
                "Building security context. Useful later for staff suspicion.",
                "CCTV outage placeholder",
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
            String debugContextText,
            String debugHotspotHint,
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
        hotspots.add(Hotspot.openHandheld(
                "open_handheld_bottom_strip",
                0.34f,
                0.88f,
                0.32f,
                0.10f
        ));

        if (id == RoomId.LIVING_ROOM) {
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_body_condition",
                    0.36f,
                    0.38f,
                    0.12f,
                    0.28f,
                    "E01_BODY_CONDITION_REPORT"
            ));
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_broken_vase",
                    0.50f,
                    0.42f,
                    0.12f,
                    0.22f,
                    "E03_BROKEN_VASE_DISTURBED_TABLE"
            ));
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_company_award",
                    0.64f,
                    0.42f,
                    0.12f,
                    0.22f,
                    "E04_BLOOD_MARKED_COMPANY_AWARD"
            ));
        }

        if (id == RoomId.KITCHEN_SIDE_AREA) {
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_thermostat_ac_log",
                    0.44f,
                    0.26f,
                    0.18f,
                    0.26f,
                    "E02_THERMOSTAT_AC_LOG"
            ));
        }

        if (id == RoomId.ENTRY_SMART_LOCK) {
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_smart_lock_log",
                    0.42f,
                    0.28f,
                    0.18f,
                    0.30f,
                    "E05_SMART_LOCK_ENTRY_EXIT_LOG"
            ));
        }

        if (id == RoomId.OFFICE_DEVICE_CORNER) {
            hotspots.add(Hotspot.openLaptop(
                    "open_victim_laptop",
                    0.42f,
                    0.26f,
                    0.20f,
                    0.28f
            ));
            hotspots.add(Hotspot.inspectEvidence(
                    "inspect_router_admin_record",
                    0.66f,
                    0.28f,
                    0.14f,
                    0.24f,
                    "E08_ROUTER_ADMIN_ACCESS_RECORD"
            ));
        }

        return new RoomDefinition(
                id,
                displayName,
                debugContextText,
                debugHotspotHint,
                backgroundTexturePath,
                hotspots,
                Collections.emptyList()
        );
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
