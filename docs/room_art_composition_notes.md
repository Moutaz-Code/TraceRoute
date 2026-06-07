# Room Art Composition Notes

Use this as the implementation reference for the current flat room plates.

## Shared Composition

- All current room plates are 1672x941, effectively 16:9.
- Style is consistent: front-facing fixed room, dark side walls, central practical light, sketch/painted texture.
- Important objects should stay away from the extreme left/right edge because those are still room-navigation zones.
- Flat PNGs are acceptable for the current demo. No separated object layers are required.

## Living Room

| Element | Approx Area |
|---|---|
| Body | foreground right/bottom |
| Disturbed/broken object | left side table |
| Company award | shelf above sofa |
| Sofa | center back wall |

Implementation note: the body overlaps the right navigation edge visually, so hotspot priority must let evidence beat navigation.

## Entry

| Element | Approx Area |
|---|---|
| Door / smart-lock area | left wall/door area |
| Remote-access/log object | center console |
| Coat rack/bags | mid-left wall |
| Side shelf | right side |

Implementation note: smart-lock evidence is currently mapped to the left door/control area; wife remote access is mapped to the console object.

## Kitchen

| Element | Approx Area |
|---|---|
| Kitchen counter | center/right horizontal counter |
| Refrigerator | left center |
| Thermostat / AC clue | right wall control plus leaning vent/panel |

Implementation note: the thermostat/AC hotspot is intentionally tall so tapping either the right wall control or the lower vent/panel works.

## Office

| Element | Approx Area |
|---|---|
| Laptop/computer access | central monitor cluster |
| Router/admin record | tower/network hardware under desk |
| Papers/files | right desk/cabinet |

Implementation note: laptop opening is mapped to the monitor cluster, not the whole desk, to avoid accidental opens.

## Missing / Deferred

`cctv_window_angle.png` has not been added yet. The app currently falls back to `room_test.png` for that room.

For the current demo route, the player cycles through the four finished rooms only:

```text
Living Room -> Entry -> Kitchen -> Office -> Living Room
```

The CCTV outage clue is temporarily collectible from the Entry room's side-shelf/security notice area so the case remains completable without a fifth room plate.
