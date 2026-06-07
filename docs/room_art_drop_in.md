 # Room Art Drop-In

Use this file for the due-tomorrow room art pass. No UI art and no separated prop layers are required.

## Required Location

Place room images here:

```text
app/src/main/assets/textures/scenes/
```

## Filenames

Use these exact filenames:

```text
living_room.png
entry_smart_lock.png
kitchen_side_area.png
office_device_corner.png
cctv_window_angle.png
```

The app can run if any of these are missing. Missing room images fall back to `room_test.png`.
For the current due-date demo, `cctv_window_angle.png` is deferred and the CCTV clue is reachable from the Entry room instead.

## Minimum Room Checklist

| File | Must Show |
|---|---|
| `living_room.png` | couch, table/side table, dead body, blood pool, bloody weapon/object, disturbed/broken object |
| `entry_smart_lock.png` | front door, smart-lock/keypad, small entry surface/shelf |
| `kitchen_side_area.png` | kitchen counter/cabinets, thermostat, AC vent |
| `office_device_corner.png` | desk, laptop/computer, router/modem, loose cables/papers |
| `cctv_window_angle.png` | window or hallway view, CCTV camera/monitor, small outage note/screen |

## Export Guidance

- Export flat PNGs for now.
- Keep composition front-facing and readable on a phone screen.
- Keep important tappable objects away from the extreme left/right edges because those edges are navigation touch zones.
- Do not spend time separating assets unless there is spare time after the demo works.
