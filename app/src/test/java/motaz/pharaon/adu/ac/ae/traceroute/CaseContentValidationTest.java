package motaz.pharaon.adu.ac.ae.traceroute;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CaseContentValidationTest {
    private static final Set<String> VALID_HOTSPOT_ACTIONS = new HashSet<String>() {{
        add("INSPECT_EVIDENCE");
        add("NAVIGATE_ROOM");
        add("OPEN_HANDHELD");
        add("OPEN_LAPTOP");
        add("SHOW_FLAVOR_TEXT");
    }};

    @Test
    public void mvpCaseContentHasValidReferencesAndBounds() throws Exception {
        JSONObject root = loadCaseJson();

        Set<String> evidenceIds = collectIds(root.getJSONArray("evidence"));
        Set<String> statementIds = collectIds(root.getJSONArray("statements"));
        Set<String> roomIds = collectIds(root.getJSONArray("rooms"));

        assertFalse(evidenceIds.isEmpty());
        assertFalse(statementIds.isEmpty());
        assertFalse(roomIds.isEmpty());
        assertTrue(evidenceIds.contains("E09_WIFE_REMOTE_ACCESS_LOG"));
        assertTrue(evidenceIds.contains("E10_CCTV_OUTAGE_NOTE"));
        assertTrue(statementIds.contains("S02_WIFE_STATEMENT"));
        assertTrue(statementIds.contains("S03_SECURITY_STATEMENT"));

        validateEvidenceArtPaths(root.getJSONArray("evidence"));
        validateStatements(root.getJSONArray("statements"));
        validateRooms(root.getJSONArray("rooms"), roomIds, evidenceIds);
        validateDemoNavigationCycle(root.getJSONArray("rooms"));
        validateLaptopContent(root.getJSONObject("laptop"), evidenceIds);
        validateFinalTimelineOrder(root.getJSONArray("finalTimelineEvents"));
        validateFinalConfrontation(root.getJSONObject("finalConfrontation"));
        validateCaseCompletion(root.getJSONObject("caseCompletion"));
    }

    private JSONObject loadCaseJson() throws Exception {
        Path appRelativePath = Paths.get("src/main/assets/data/case/mvp_case.json");
        Path rootRelativePath = Paths.get("app/src/main/assets/data/case/mvp_case.json");
        Path casePath = Files.exists(appRelativePath) ? appRelativePath : rootRelativePath;

        String json = new String(Files.readAllBytes(casePath), StandardCharsets.UTF_8);
        return new JSONObject(json);
    }

    private Set<String> collectIds(JSONArray array) throws Exception {
        Set<String> ids = new HashSet<>();

        for (int index = 0; index < array.length(); index++) {
            String id = array.getJSONObject(index).getString("id");
            assertFalse("Id must not be empty.", id.trim().isEmpty());
            assertTrue("Duplicate id: " + id, ids.add(id));
        }

        return ids;
    }

    private void validateEvidenceArtPaths(JSONArray evidenceArray) throws Exception {
        for (int index = 0; index < evidenceArray.length(); index++) {
            JSONObject evidence = evidenceArray.getJSONObject(index);
            assertAssetPathPrefix(evidence, "thumbnailAssetPath", "textures/evidence/");
            assertAssetPathPrefix(evidence, "detailAssetPath", "textures/evidence/");
        }
    }

    private void validateStatements(JSONArray statements) throws Exception {
        for (int index = 0; index < statements.length(); index++) {
            JSONObject statement = statements.getJSONObject(index);
            assertFalse(statement.getString("speaker").trim().isEmpty());
            assertFalse(statement.getString("claim").trim().isEmpty());
            assertFalse(statement.getString("suspiciousDetail").trim().isEmpty());
            assertFalse(statement.getString("contradictionText").trim().isEmpty());
        }
    }

    private void validateRooms(JSONArray rooms, Set<String> roomIds, Set<String> evidenceIds) throws Exception {
        for (int index = 0; index < rooms.length(); index++) {
            JSONObject room = rooms.getJSONObject(index);

            assertAssetPathPrefix(room, "backgroundTexturePath", "textures/scenes/");
            validateHotspots(room.getJSONArray("hotspots"), roomIds, evidenceIds);
            validateVisualAnnotations(room.optJSONArray("visualAnnotations"));
        }
    }

    private void validateDemoNavigationCycle(JSONArray rooms) throws Exception {
        Map<String, JSONObject> roomById = new HashMap<>();
        for (int index = 0; index < rooms.length(); index++) {
            JSONObject room = rooms.getJSONObject(index);
            roomById.put(room.getString("id"), room);
        }

        Set<String> visitedRoomIds = new HashSet<>();
        Set<String> reachableEvidenceIds = new HashSet<>();
        String currentRoomId = "LIVING_ROOM";

        for (int step = 0; step < 6; step++) {
            JSONObject room = roomById.get(currentRoomId);
            assertTrue("Demo route room does not exist: " + currentRoomId, room != null);
            assertExistingSceneAsset(room.getString("backgroundTexturePath"), currentRoomId);
            visitedRoomIds.add(currentRoomId);
            collectEvidencePayloads(room.getJSONArray("hotspots"), reachableEvidenceIds);

            String nextRoomId = findNavigationTarget(room.getJSONArray("hotspots"), "nav_next");
            if ("LIVING_ROOM".equals(nextRoomId)) {
                break;
            }
            currentRoomId = nextRoomId;
        }

        assertTrue(visitedRoomIds.contains("LIVING_ROOM"));
        assertTrue(visitedRoomIds.contains("ENTRY_SMART_LOCK"));
        assertTrue(visitedRoomIds.contains("KITCHEN_SIDE_AREA"));
        assertTrue(visitedRoomIds.contains("OFFICE_DEVICE_CORNER"));
        assertFalse("Demo route should not require missing CCTV art.", visitedRoomIds.contains("CCTV_WINDOW_OPTIONAL"));
        assertTrue("Demo route must expose CCTV outage evidence.", reachableEvidenceIds.contains("E10_CCTV_OUTAGE_NOTE"));
    }

    private void collectEvidencePayloads(JSONArray hotspots, Set<String> evidenceIds) throws Exception {
        for (int index = 0; index < hotspots.length(); index++) {
            JSONObject hotspot = hotspots.getJSONObject(index);
            if ("INSPECT_EVIDENCE".equals(hotspot.getString("actionType"))) {
                evidenceIds.add(hotspot.getString("payloadId"));
            }
        }
    }

    private String findNavigationTarget(JSONArray hotspots, String hotspotId) throws Exception {
        for (int index = 0; index < hotspots.length(); index++) {
            JSONObject hotspot = hotspots.getJSONObject(index);
            if (hotspotId.equals(hotspot.getString("id"))) {
                return hotspot.getString("targetRoomId");
            }
        }

        throw new AssertionError("Missing navigation hotspot: " + hotspotId);
    }

    private void assertExistingSceneAsset(String assetPath, String roomId) {
        Path appRelativePath = Paths.get("src/main/assets", assetPath);
        Path rootRelativePath = Paths.get("app/src/main/assets", assetPath);
        assertTrue(
                "Demo route room " + roomId + " references missing scene asset: " + assetPath,
                Files.exists(appRelativePath) || Files.exists(rootRelativePath)
        );
    }

    private void validateHotspots(JSONArray hotspots, Set<String> roomIds, Set<String> evidenceIds) throws Exception {
        Set<String> hotspotIds = new HashSet<>();

        for (int index = 0; index < hotspots.length(); index++) {
            JSONObject hotspot = hotspots.getJSONObject(index);
            String hotspotId = hotspot.getString("id");
            String actionType = hotspot.getString("actionType");

            assertTrue("Duplicate hotspot id in room: " + hotspotId, hotspotIds.add(hotspotId));
            assertTrue("Unknown hotspot action: " + actionType, VALID_HOTSPOT_ACTIONS.contains(actionType));
            assertNormalizedRect(hotspot, "hotspot " + hotspotId);

            if ("NAVIGATE_ROOM".equals(actionType)) {
                assertTrue(
                        "Navigation target does not exist: " + hotspot.optString("targetRoomId"),
                        roomIds.contains(hotspot.getString("targetRoomId"))
                );
            } else if ("INSPECT_EVIDENCE".equals(actionType)) {
                assertTrue(
                        "Evidence payload does not exist: " + hotspot.optString("payloadId"),
                        evidenceIds.contains(hotspot.getString("payloadId"))
                );
            }
        }
    }

    private void validateVisualAnnotations(JSONArray annotations) throws Exception {
        if (annotations == null) {
            return;
        }

        Set<String> annotationIds = new HashSet<>();
        for (int index = 0; index < annotations.length(); index++) {
            JSONObject annotation = annotations.getJSONObject(index);
            String annotationId = annotation.getString("id");

            assertTrue("Duplicate visual annotation id: " + annotationId, annotationIds.add(annotationId));
            assertFalse(annotation.getString("label").trim().isEmpty());
            assertNormalizedRect(annotation, "visual annotation " + annotationId);
        }
    }

    private void validateFinalTimelineOrder(JSONArray events) throws Exception {
        Set<Integer> orderIndexes = new HashSet<>();

        for (int index = 0; index < events.length(); index++) {
            int orderIndex = events.getJSONObject(index).getInt("orderIndex");
            assertTrue("Duplicate final timeline order index: " + orderIndex, orderIndexes.add(orderIndex));
        }

        for (int expected = 1; expected <= events.length(); expected++) {
            assertTrue("Missing final timeline order index: " + expected, orderIndexes.contains(expected));
        }
    }

    private void validateLaptopContent(JSONObject laptop, Set<String> evidenceIds) throws Exception {
        assertFalse(laptop.getString("title").trim().isEmpty());
        assertStringArrayNotEmpty(laptop.getJSONArray("introLines"), "laptop intro line");

        JSONArray panels = laptop.getJSONArray("panels");
        assertTrue("Laptop needs at least one panel.", panels.length() > 0);

        Set<String> panelIds = new HashSet<>();
        for (int index = 0; index < panels.length(); index++) {
            JSONObject panel = panels.getJSONObject(index);
            String panelId = normalizePanelId(panel.getString("panelId"));

            assertFalse(panelId.isEmpty());
            assertTrue("Duplicate laptop panel id: " + panelId, panelIds.add(panelId));
            assertFalse(panel.getString("label").trim().isEmpty());
            assertTrue(
                    "Laptop panel evidence does not exist: " + panel.optString("evidenceId"),
                    evidenceIds.contains(panel.getString("evidenceId"))
            );
            assertStringArrayNotEmpty(panel.getJSONArray("contentLines"), "laptop panel content line");
        }
    }

    private void validateFinalConfrontation(JSONObject confrontation) throws Exception {
        assertFalse(confrontation.getString("id").trim().isEmpty());
        assertFalse(confrontation.getString("title").trim().isEmpty());

        JSONArray lines = confrontation.getJSONArray("lines");
        assertTrue("Final confrontation needs at least one line.", lines.length() > 0);

        for (int index = 0; index < lines.length(); index++) {
            JSONObject line = lines.getJSONObject(index);
            assertFalse(line.getString("speaker").trim().isEmpty());
            assertFalse(line.getString("text").trim().isEmpty());
        }
    }

    private void validateCaseCompletion(JSONObject caseCompletion) throws Exception {
        assertFalse(caseCompletion.getString("title").trim().isEmpty());
        assertFalse(caseCompletion.getString("body").trim().isEmpty());
    }

    private void assertAssetPathPrefix(JSONObject object, String field, String expectedPrefix) throws Exception {
        String path = object.optString(field, "");
        assertFalse(field + " must not be empty for " + object.getString("id"), path.trim().isEmpty());
        assertTrue(field + " must start with " + expectedPrefix + ": " + path, path.startsWith(expectedPrefix));
    }

    private void assertStringArrayNotEmpty(JSONArray array, String label) throws Exception {
        assertTrue(label + " array must not be empty.", array.length() > 0);

        for (int index = 0; index < array.length(); index++) {
            assertFalse(label + " must not be empty.", array.getString(index).trim().isEmpty());
        }
    }

    private String normalizePanelId(String panelId) {
        return panelId.trim().toLowerCase(Locale.US);
    }

    private void assertNormalizedRect(JSONObject object, String label) throws Exception {
        double x = object.getDouble("x");
        double y = object.getDouble("y");
        double width = object.getDouble("width");
        double height = object.getDouble("height");

        assertTrue(label + " x out of range", x >= 0.0 && x <= 1.0);
        assertTrue(label + " y out of range", y >= 0.0 && y <= 1.0);
        assertTrue(label + " width out of range", width > 0.0 && width <= 1.0);
        assertTrue(label + " height out of range", height > 0.0 && height <= 1.0);
        assertTrue(label + " extends beyond right edge", x + width <= 1.00001);
        assertTrue(label + " extends beyond bottom edge", y + height <= 1.00001);
    }
}
