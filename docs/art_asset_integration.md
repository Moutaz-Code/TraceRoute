# Art Asset Integration

This project is set up so final art can be dropped into `app/src/main/assets/` and referenced from `app/src/main/assets/data/case/mvp_case.json`.

## Scene Backgrounds

Room backgrounds are OpenGL textures loaded from:

```text
app/src/main/assets/textures/scenes/
```

Each room uses:

```json
"backgroundTexturePath": "textures/scenes/room_test.png"
```

Replace that path with the final room image path when ready. Missing scene textures fall back to a dark background instead of crashing the app.

Suggested final naming:

```text
textures/scenes/living_room.png
textures/scenes/entry_smart_lock.png
textures/scenes/kitchen_side_area.png
textures/scenes/office_device_corner.png
textures/scenes/cctv_window_angle.png
```

## Evidence Art

Evidence images are Android UI assets loaded from:

```text
app/src/main/assets/textures/evidence/
```

Each evidence item supports:

```json
"thumbnailAssetPath": "textures/evidence/e01_body_condition_thumb.png",
"detailAssetPath": "textures/evidence/e01_body_condition_detail.png"
```

The handheld evidence card prefers `detailAssetPath`, then falls back to `thumbnailAssetPath`, then shows a dark placeholder area if neither file exists.

Suggested sizes:

- Thumbnail: `512x288` or similar 16:9 crop.
- Detail: `1024x576` or similar 16:9 crop.
- Use PNG for UI screenshots, reports, annotated evidence, and transparent cutouts.
- Use JPG/WebP for large painted/photo-style room backgrounds if file size matters.

## Debug Layout

Functional touchzones and non-functional visual labels are still JSON-driven:

```json
"hotspots": [],
"visualAnnotations": []
```

When final art arrives, update those normalized `x`, `y`, `width`, and `height` values to align with the image composition. Coordinates are normalized from `0.0` to `1.0` across the screen.
