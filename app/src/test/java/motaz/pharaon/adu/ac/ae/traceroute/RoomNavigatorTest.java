package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomNavigator;

import static org.junit.Assert.assertEquals;

public class RoomNavigatorTest {
    @Test
    public void demoRouteSkipsDeferredCctvRoom() {
        RoomNavigator navigator = new RoomNavigator(Arrays.asList(
                room(RoomId.LIVING_ROOM),
                room(RoomId.ENTRY_SMART_LOCK),
                room(RoomId.KITCHEN_SIDE_AREA),
                room(RoomId.OFFICE_DEVICE_CORNER),
                room(RoomId.CCTV_WINDOW_OPTIONAL)
        ));

        assertEquals(RoomId.LIVING_ROOM, navigator.getCurrentRoomId());

        navigator.goToNextRoom();
        assertEquals(RoomId.ENTRY_SMART_LOCK, navigator.getCurrentRoomId());

        navigator.goToNextRoom();
        assertEquals(RoomId.KITCHEN_SIDE_AREA, navigator.getCurrentRoomId());

        navigator.goToNextRoom();
        assertEquals(RoomId.OFFICE_DEVICE_CORNER, navigator.getCurrentRoomId());

        navigator.goToNextRoom();
        assertEquals(RoomId.LIVING_ROOM, navigator.getCurrentRoomId());
    }

    @Test
    public void directNavigationIgnoresDeferredCctvRoom() {
        RoomNavigator navigator = new RoomNavigator(Arrays.asList(
                room(RoomId.LIVING_ROOM),
                room(RoomId.CCTV_WINDOW_OPTIONAL)
        ));

        navigator.goToRoom(RoomId.CCTV_WINDOW_OPTIONAL);

        assertEquals(RoomId.LIVING_ROOM, navigator.getCurrentRoomId());
    }

    private RoomDefinition room(RoomId roomId) {
        return new RoomDefinition(
                roomId,
                roomId.name(),
                "",
                "",
                "textures/scenes/room_test.png",
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
