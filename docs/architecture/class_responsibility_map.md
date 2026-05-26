# Class Responsibility Map

This file explains the intended responsibility of the new architecture skeleton.

## App Shell

### `MainActivity`

Purpose:

-   Android entry point.

Owns:

-   activity lifecycle,
-   root fragment container.

Should not own:

-   gameplay logic,
-   evidence state,
-   room navigation,
-   terminal command handling.

### `GameShellFragment`

Purpose:

-   Root fragment for the game.

Owns:

-   world fragment container,
-   overlay fragment container,
-   terminal dialog launching.

Depends on:

-   `WorldInvestigationFragment`,
-   `HandheldFragment`,
-   `TerminalFragment`.

## World / Rendering

### `WorldInvestigationFragment`

Purpose:

-   Fragment wrapper for the OpenGL game surface.

Owns:

-   `GameView` lifecycle.

Depends on:

-   `GameView`.

### `GameView`

Purpose:

-   Android `GLSurfaceView`.

Owns:

-   OpenGL ES 3.0 setup,
-   `GLRenderer`,
-   touch forwarding to the GL/game thread.

Depends on:

-   `GLRenderer`.

Implements:

-   `GameUiEventListener` as a bridge back to Android UI.

Notes:

-   Gameplay requests arrive from the GL/game thread.
-   `GameView` posts those requests onto the Android UI thread before forwarding them to `GameShellFragment`.

### `GLRenderer`

Purpose:

-   OpenGL renderer callback bridge.

Owns:

-   surface creation,
-   viewport changes,
-   frame drawing.

Should delegate to:

-   `GameController`,
-   `RenderPipeline`.

### `WorldInvestigationScene`

Purpose:

-   Runtime scene for apartment inspection.

Owns:

-   room rendering decisions,
-   tap routing to hotspots.

Depends on:

-   `RoomNavigator`,
-   `HotspotSystem`,
-   `GameUiEventListener`.

Should:

-   handle hotspot action routing,
-   request UI surfaces through `GameUiEventListener`,
-   avoid importing Fragment classes.

### `GameUiEventListener`

Purpose:

-   Small bridge interface between gameplay actions and Android UI actions.

Owns:

-   open handheld request,
-   open terminal request,
-   evidence inspection request.

Used by:

-   `WorldInvestigationScene` to request UI,
-   `GameView` to marshal callbacks to the UI thread,
-   `GameShellFragment` to open actual fragments/dialogs.

## Rooms And Hotspots

### `RoomId`

Purpose:

-   Stable ids for fixed apartment views.

### `RoomDefinition`

Purpose:

-   Data definition for one apartment view.

Owns:

-   room id,
-   texture path,
-   hotspots,
-   navigation targets.

### `RoomNavigator`

Purpose:

-   Tracks current room and valid navigation.

### `Hotspot`

Purpose:

-   One tappable region in a room.

Owns:

-   hotspot id,
-   normalized screen-space rectangle,
-   action type,
-   optional target room id,
-   optional payload id for evidence/terminal/flavor content later.

### `HotspotActionType`

Purpose:

-   Explicit action types for tapped hotspots.

Examples:

-   inspect evidence,
-   navigate room,
-   open handheld,
-   open terminal,
-   show flavor text.

### `HotspotSystem`

Purpose:

-   Converts tap coordinates into hotspot hits.

Owns:

-   hit testing against the current room's hotspot list.

Coordinate model:

-   normalized screen space,
-   `0,0` top-left,
-   `1,1` bottom-right.

## Case Logic

### `GameController`

Purpose:

-   Central coordinator for gameplay systems.

Owns:

-   reference to `GameRuntime`,
-   active scene,
-   later: evidence system,
-   thread system,
-   timeline system,
-   statement system,
-   terminal session,
-   current game state.

### `GameRuntime`

Purpose:

-   Shared state for the current play session.

Owns:

-   `EvidenceSystem`,
-   `ThreadSystem`,
-   `TimelineSystem`,
-   `StatementSystem`,
-   current room id,
-   temporary hardcoded MVP content seed data,
-   calls to `GameRepository` for MVP save/load.

Used by:

-   `GameController` and gameplay scenes for updates,
-   `HandheldFragment` / `HandheldViewBinder` for display.

Later:

-   can be populated by `DataLoader`,
-   can move hardcoded case content into assets or JSON.

### `EvidenceItem`

Purpose:

-   Runtime/data representation of evidence.

Owns:

-   stable id,
-   title,
-   category,
-   source,
-   player-facing summary,
-   simplified technical note.

### `EvidenceSystem`

Purpose:

-   Tracks collected evidence and notifies other systems.

Owns:

-   registered evidence definitions,
-   discovered evidence ids,
-   collection lookup for handheld display.

### `InvestigationThread`

Purpose:

-   Data model for one reasoning thread.

### `ThreadSystem`

Purpose:

-   Unlocks observations and stabilized thread conclusions.

Owns:

-   active investigation thread definitions,
-   evidence-to-observation progression,
-   stabilized thread state.

Current implemented thread:

-   `T01_DEATH_WINDOW_INSTABILITY`
-   `T02_POSTMORTEM_ACTIVITY`
-   `T03_APARTMENT_ACCESS`
-   `T04_DATA_SALE_MOTIVE`
-   `T05_SCENE_DISTURBANCE`

Current progression:

-   `E01_BODY_CONDITION_REPORT` unlocks the first observation,
-   `E02_THERMOSTAT_AC_LOG` unlocks the second observation,
-   collecting both stabilizes the thread.
-   `E03_BROKEN_VASE_DISTURBED_TABLE` unlocks a Scene Disturbance observation.
-   `E04_BLOOD_MARKED_COMPANY_AWARD` unlocks a Scene Disturbance observation.
-   collecting both scene-disturbance clues stabilizes the thread.
-   `E06_SCHEDULED_VICTIM_MESSAGE` unlocks the first Postmortem Activity observation.
-   `E07_LAPTOP_SESSION_METADATA` unlocks the second Postmortem Activity observation.
-   collecting both Postmortem Activity clues stabilizes the thread.
-   `E05_SMART_LOCK_ENTRY_EXIT_LOG` unlocks the first Apartment Access observation.
-   `E08_ROUTER_ADMIN_ACCESS_RECORD` unlocks the second Apartment Access observation.
-   collecting both Apartment Access clues stabilizes the thread.
-   `E12_ACCESS_DATA_SALE_FILE` unlocks the first Data-Sale Motive observation.
-   `E13_VICTIM_DRAFT_DISCLOSURE_EMAIL` unlocks the second Data-Sale Motive observation.
-   collecting both motive clues stabilizes the thread.

### `TimelineEvent`

Purpose:

-   Data model for apparent/true timeline events.

Owns:

-   stable id,
-   displayed time/window,
-   title,
-   initial interpretation,
-   resolved interpretation,
-   visible/contradicted/stabilized state.

### `FinalTimelineEvent`

Purpose:

-   Data model for one event in the final timeline-ordering sequence.

Owns:

-   stable id,
-   correct order index,
-   displayed time,
-   title,
-   short player-facing summary.

Why it is separate from `TimelineEvent`:

-   `TimelineEvent` represents investigation contradictions and uncertainty.
-   `FinalTimelineEvent` represents the final solved sequence the player orders.

### `TimelineSystem`

Purpose:

-   Owns timeline reveal state and final ordering validation.

Owns:

-   timeline event definitions,
-   visible timeline state,
-   thread-to-timeline reveal rules.
-   final timeline event definitions,
-   selected final timeline sequence,
-   final timeline unlock/completion state,
-   final timeline validation feedback.

Current implemented event:

-   `TL01_POST_21_ACTIVITY_UNRELIABLE`

Current trigger:

-   `T01_DEATH_WINDOW_INSTABILITY` reveals and stabilizes the first contradiction.

Current final timeline unlock:

-   requires all five core threads to stabilize:
-   `T01_DEATH_WINDOW_INSTABILITY`
-   `T02_POSTMORTEM_ACTIVITY`
-   `T03_APARTMENT_ACCESS`
-   `T04_DATA_SALE_MOTIVE`
-   `T05_SCENE_DISTURBANCE`

Current final timeline behavior:

-   the handheld shows scrambled final events,
-   the player taps events into the selected sequence,
-   `TimelineSystem` validates the order,
-   a wrong full sequence clears and asks the player to try again,
-   a correct full sequence marks the timeline completed.

### `SuspectStatement`

Purpose:

-   Data model for a short suspect statement.

Owns:

-   stable id,
-   speaker,
-   claim,
-   suspicious detail,
-   contradiction text,
-   visible/contradicted state.

### `StatementSystem`

Purpose:

-   Owns statement unlock and contradiction state.

Owns:

-   registered suspect statements,
-   evidence-to-statement unlock rules,
-   timeline-to-contradiction rules.

Current implemented statement:

-   `S01_PARTNER_STATEMENT`

Current progression:

-   `E05_SMART_LOCK_ENTRY_EXIT_LOG` unlocks the partner statement,
-   stabilized post-21:00 timeline contradiction marks the statement contradicted.

## Terminal Mechanic

### `TerminalFragment`

Purpose:

-   DialogFragment UI for the graybox victim laptop.

Owns:

-   laptop display widgets,
-   authored laptop panel buttons.

Depends on:

-   `TerminalSession`,
-   `TerminalCommandProcessor`.

### `TerminalSession`

Purpose:

-   Runtime state for victim laptop history/panel output.

Owns:

-   visible terminal history,
-   command submission history updates,
-   clear behavior.

### `TerminalCommandProcessor`

Purpose:

-   Handles authored victim-laptop panel commands.

Should:

-   parse simple commands,
-   return readable output,
-   trigger evidence/thread unlocks through `GameController`.

Should not:

-   become a real shell,
-   run arbitrary commands,
-   require real cybersecurity knowledge from the player.

Current starter commands:

-   `MESSAGES`
-   `ACTIVITY` / `LAPTOP`
-   `FILES`
-   `DRAFT`
-   `CLEAR`

Current gameplay side effect:

-   `MESSAGES` collects `E06_SCHEDULED_VICTIM_MESSAGE`.
-   `LAPTOP` collects `E07_LAPTOP_SESSION_METADATA`.
-   `FILES` collects `E12_ACCESS_DATA_SALE_FILE`.
-   `DRAFT` collects `E13_VICTIM_DRAFT_DISCLOSURE_EMAIL`.

Note:

-   `E08_ROUTER_ADMIN_ACCESS_RECORD` is now collected from a separate office router/admin hotspot, not from the victim laptop.

## Handheld UI

### `HandheldFragment`

Purpose:

-   Fragment wrapper for the forensic handheld overlay.

Owns:

-   the handheld root view,
-   basic Android lifecycle for the overlay.

Should delegate to:

-   `HandheldController` for tab/screen state.
-   Gameplay systems for actual evidence/thread/timeline data.

### `HandheldScreen`

Purpose:

-   Enum for handheld tabs.

### `HandheldController`

Purpose:

-   Plain Java controller for internal handheld screens.

Owns:

-   selected tab/screen,
-   requests for evidence/thread/timeline/statement data.

Current screens:

-   Evidence,
-   Threads,
-   Timeline,
-   Statements,
-   Final Timeline.

Why not fragments for every tab:

-   The project must show meaningful fragment usage, but every handheld tab does not need its own lifecycle.
-   Keeping handheld tabs as ordinary views/controllers is simpler for MVP.

### `HandheldViewBinder`

Purpose:

-   Plain Java helper for Android Views inside `HandheldFragment`.

Owns:

-   view lookup,
-   simple render/bind methods,
-   tab button wiring later.

Currently renders:

-   collected evidence cards,
-   investigation thread cards,
-   stabilized thread conclusion text,
-   visible timeline contradiction cards,
-   suspect statement cards.
-   final timeline ordering cards and selectable event buttons.

Why it exists:

-   Fragments should not become giant UI classes.
-   Views are still the actual widgets the player sees and taps.

### `TerminalViewBinder`

Purpose:

-   Plain Java helper for Android Views inside `TerminalFragment`.

Owns:

-   victim laptop output display,
-   authored panel buttons.

Currently binds:

-   output history text,
-   authored laptop panel buttons,
-   no native Android soft keyboard.

Why it exists:

-   The laptop should feel like a stylized computer UI built from normal Android Views.
-   Terminal state and command parsing stay in `TerminalSession` and `TerminalCommandProcessor`.

## Persistence

### `DataLoader`

Purpose:

-   Loads static case content from assets or temporary hardcoded builders.

### `GameRepository`

Purpose:

-   Single access point for static content and save state.

Owns:

-   `GameDatabase`,
-   conversion between `GameSaveState` and stored SQLite values.

Current behavior:

-   saves collected evidence ids,
-   saves current room id,
-   saves selected final timeline ids,
-   saves final timeline completion.

### `GameSaveState`

Purpose:

-   Plain Java save-state transfer object.

Owns:

-   collected evidence ids,
-   current room id,
-   selected final timeline event ids,
-   final timeline completion flag.

### `GameDatabase`

Purpose:

-   Basic SQLite helper for MVP save state.

Owns:

-   database name/version,
-   creation of the `save_state` table,
-   basic CRUD helpers.

Current CRUD surface:

-   `putValue` creates or updates one key/value row,
-   `getValue` reads one key/value row,
-   `clearSaveState` deletes all save rows.

### `GameContract`

Purpose:

-   SQLite table and column names.

Owns:

-   `save_state` table name,
-   key/value column names,
-   save keys for evidence, room, final timeline selection, and final timeline completion.

## Deprecated / Old Scaffold

The following names came from early scaffolding and should not receive new logic:

-   `ExplorationScene`
-   `PuzzleScene`
-   `gameplay.puzzle.EvidenceSystem`
-   old `InventoryUI` / `DialogueView` style classes unless repurposed deliberately.

Preferred replacements:

-   `WorldInvestigationScene`
-   final timeline ordering inside handheld UI/controller
-   `gameplay.evidence.EvidenceSystem`
-   `HandheldFragment` plus `HandheldController` for evidence/statements/timeline UI.
