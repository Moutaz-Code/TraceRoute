package motaz.pharaon.adu.ac.ae.traceroute;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotSystem;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;

import static org.junit.Assert.assertEquals;

public class HotspotSystemTest {
    @Test
    public void evidenceTakesPriorityWhenItOverlapsNavigation() {
        RoomDefinition roomDefinition = new RoomDefinition(
                RoomId.LIVING_ROOM,
                "Living Room",
                "",
                "",
                "textures/scenes/living_room.png",
                Arrays.asList(
                        Hotspot.navigation("nav_next", 0.80f, 0.0f, 0.20f, 1.0f, RoomId.ENTRY_SMART_LOCK),
                        Hotspot.inspectEvidence("inspect_body", 0.70f, 0.70f, 0.25f, 0.25f, "E01_BODY_CONDITION_REPORT")
                ),
                Collections.emptyList()
        );

        Hotspot hotspot = new HotspotSystem().findHotspot(roomDefinition, 0.86f, 0.86f);

        assertEquals("inspect_body", hotspot.getId());
    }

    @Test
    public void smallerHotspotWinsWhenPriorityMatches() {
        RoomDefinition roomDefinition = new RoomDefinition(
                RoomId.OFFICE_DEVICE_CORNER,
                "Office",
                "",
                "",
                "textures/scenes/office_device_corner.png",
                Arrays.asList(
                        Hotspot.inspectEvidence("inspect_large", 0.20f, 0.20f, 0.60f, 0.60f, "E08_ROUTER_ADMIN_ACCESS_RECORD"),
                        Hotspot.inspectEvidence("inspect_small", 0.40f, 0.40f, 0.10f, 0.10f, "E08_ROUTER_ADMIN_ACCESS_RECORD")
                ),
                Collections.emptyList()
        );

        Hotspot hotspot = new HotspotSystem().findHotspot(roomDefinition, 0.45f, 0.45f);

        assertEquals("inspect_small", hotspot.getId());
    }
}
