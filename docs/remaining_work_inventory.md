# TraceRoute Remaining Work Inventory

This is the current full remaining-work map for the TraceRoute MVP as of the latest graybox implementation pass.

## Current State

The project now has a playable graybox MVP spine:

- Data-driven `mvp_case.json` content.
- Five authored room definitions.
- Hotspots and visual debug annotations.
- Evidence collection.
- Laptop panels.
- Handheld evidence, threads, timeline, statements, and final timeline screens.
- Current-lead guidance.
- Final confrontation.
- Case completion dialog.
- Save/reset support.
- Debug overlays.
- Unit coverage for the authored demo path.

## Progress Notes

- 2026-05-27: Began the recommended manual-test support / UX cleanup pass from this file.
- 2026-05-27: Added initial laptop close/collection/read feedback, clearer handheld current-lead treatment, and a world demo/dev mode toggle. These still need manual device validation.
- 2026-06-07: Began room-art integration pass. Case JSON now points at room-specific scene image paths, and rendering falls back to `room_test.png` when a room image is missing.
- 2026-06-07: Reviewed four new room plates and aligned functional hotspots/visual annotations to the actual room compositions.
- 2026-06-07: Removed legacy OpenGL debug quads, changed evidence taps to show shell notifications instead of opening handheld, and added a visible Handheld control.
- 2026-06-07: Switched world startup to clean demo mode and routed the playable demo through the four finished room plates. The CCTV clue is temporarily reachable from Entry until a fifth room plate exists.
- 2026-06-07: Added `docs/demo_manual_checklist.md` for focused due-date manual testing.
- 2026-06-07: Hardened stale saved room state so old saves cannot reopen into the deferred CCTV room.

## MVP-Critical Work Left

- Run full manual testing on emulator/device:
  - Room navigation.
  - Every hotspot.
  - Laptop panels.
  - Handheld tabs.
  - Final timeline.
  - Confrontation.
  - Completion.
  - Reset.
- Fix touch-zone drift after manual testing, especially after real art replaces `room_test.png`.
- Make laptop UX cleaner:
  - Close/return affordance.
  - Clearer evidence-collected feedback.
  - Already-read or already-collected panel state.
- Make handheld UX cleaner:
  - Better spacing.
  - More readable mobile layouts.
  - More obvious current lead.
  - Stronger final timeline affordance.
- Make final confrontation and completion state more deliberate:
  - Decide whether confrontation can replay forever.
  - Decide whether `case complete` becomes persistent state.
- Confirm save/load on a real device:
  - Collected evidence.
  - Current room.
  - Final timeline selection/completion.
  - Reset behavior.
- Add Android instrumented or smoke tests if time allows.

## Content / Game Design Work Left

- Reconcile `TraceRoute_Project_Context.md` with the implemented case:
  - Evidence numbering differs from the original plan.
  - Some original smart-home automation content is now folded into other evidence.
- Decide whether the optional CCTV room is now officially in-scope. It is currently included and functional.
- Finalize names and labels:
  - Victim.
  - Wife.
  - Business partner.
  - Security/staff identity.
  - Case title.
  - Potential final game title.
- Tighten final timeline wording so the nine events are solvable without memorizing exact times.
- Review evidence summaries for clarity:
  - Death window.
  - Staging.
  - Access.
  - Motive.
  - Scene disturbance.
- Review suspect statement language so resolved statements are clearly distinct from contradicted statements.
- Add minimal onboarding beats:
  - Tap amber hotspots.
  - Open handheld.
  - Use the laptop.
  - Order final timeline.

## Art / Asset Work Left

- Drop actual room art into `app/src/main/assets/textures/scenes/` using the room-specific filenames:
  - Living room / body scene.
  - Entry / smart-lock area.
  - Kitchen / AC area.
  - Office / device corner.
  - CCTV / window angle.
- Create evidence thumbnails and detail images for every evidence item.
- Create UI art:
  - Handheld shell.
  - Laptop shell.
  - Final timeline controls.
  - Dialogue/completion treatment.
  - Buttons, tabs, frames, icons.
- Create a full art/asset production list later.
- Validate hotspots against final art.
- Decide final visual style:
  - Forensic cyber-noir.
  - Grounded apartment realism.
  - Stylized debug-tech.
  - Hybrid.

## Audio Work Left

- Implement `AudioManager`.
- Add ambient apartment loop.
- Add evidence discovery sound.
- Add thread stabilization sound.
- Add laptop panel/button sound.
- Add final timeline success/failure sounds.
- Add confrontation/completion stingers if desired.
- Add volume/settings controls only if scope allows.

## Technical Debt Left

- Decide what to do with empty or legacy scaffold classes:
  - `AudioManager`.
  - `PuzzleDefinition`.
  - `SceneDefinition`.
  - `EvidenceData`.
  - `OverlayController`.
  - `SceneDirector`.
  - `PuzzleStatus`.
  - Old inventory/puzzle package pieces.
- Update architecture docs:
  - Some notes still describe static content as hardcoded.
  - The current implementation is JSON-driven.
- Consider centralizing test JSON parsing with production parsing, or keep the pure-Java fixture separate.
- Add asset existence validation once real assets exist.
- Review `GameRuntime` singleton lifecycle for Android recreation edge cases.
- Review database content-version behavior when JSON changes between builds.

## Rendering / OpenGL Work Left

- Replace placeholder room rendering with final authored room images.
- Add art-aware coordinate mapping if final assets require it.
- Add transitions between fixed views if desired.
- Add shader/postprocess treatment later:
  - Scanlines.
  - Vignette.
  - Glitch.
  - Forensic overlay effects.
- Decide how much UI remains Android Views versus GL-rendered.
- Device-check aspect ratios, scaling, and touch accuracy.

## Polish / Product Work Left

- App name, launcher icon, labels, and package presentation.
- Splash/main menu if needed.
- Settings screen only if necessary:
  - Audio.
  - Reset.
  - Accessibility.
- Credits/about screen if needed for submission.
- Better error/fallback screen if case JSON fails to load.
- Remove, hide, or gate dev affordances for demo builds.
- Decide whether debug overlay visibility should persist.

## Testing Work Left

- Manual UI test checklist.
- Android instrumented smoke tests for launch and key fragments.
- SQLite save/load tests if more confidence is needed.
- Asset validation tests after real assets exist.
- Screenshot checks after art integration.
- Regression test for:
  - Complete case.
  - Close app.
  - Reopen.
  - Confirm state remains sane.

## Repository / Process Work Left

- Commit or otherwise checkpoint the current large working set.
- Update docs to reflect implemented architecture and MVP state.
- Create milestone backlog:
  - Demo-ready.
  - Art integration.
  - Audio.
  - Polish.
  - Submission/release.

## Recommended Next Coding Pass

Manual-test support plus UX cleanup:

- Laptop close/feedback.
- Handheld readability.
- Clean demo-mode/debug toggle.
- Keep the art/asset production list parked until the project is ready for that pass.
