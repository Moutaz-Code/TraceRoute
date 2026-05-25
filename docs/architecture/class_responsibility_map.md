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
-   touch forwarding later.

Depends on:

-   `GLRenderer`.

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
-   `GameController`.

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

## Case Logic

### `GameController`

Purpose:

-   Central coordinator for gameplay systems.

Owns:

-   evidence system,
-   thread system,
-   timeline system,
-   statement system,
-   terminal session,
-   current game state.

### `EvidenceItem`

Purpose:

-   Runtime/data representation of evidence.

### `EvidenceSystem`

Purpose:

-   Tracks collected evidence and notifies other systems.

### `InvestigationThread`

Purpose:

-   Data model for one reasoning thread.

### `ThreadSystem`

Purpose:

-   Unlocks observations and stabilized thread conclusions.

### `TimelineEvent`

Purpose:

-   Data model for apparent/true timeline events.

### `TimelineSystem`

Purpose:

-   Owns timeline reveal state and final ordering validation.

### `SuspectStatement`

Purpose:

-   Data model for a short suspect statement.

### `StatementSystem`

Purpose:

-   Owns statement unlock and contradiction state.

## Terminal Mechanic

### `TerminalFragment`

Purpose:

-   DialogFragment UI for the stylized terminal computer.

Owns:

-   terminal display widgets,
-   command input widgets later.

Depends on:

-   `TerminalSession`,
-   `TerminalCommandProcessor`.

### `TerminalSession`

Purpose:

-   Runtime state for terminal history and prompt state.

### `TerminalCommandProcessor`

Purpose:

-   Handles authored terminal commands.

Should:

-   parse simple commands,
-   return readable output,
-   trigger evidence/thread unlocks through `GameController`.

Should not:

-   become a real shell,
-   run arbitrary commands,
-   require real cybersecurity knowledge from the player.

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

Why it exists:

-   Fragments should not become giant UI classes.
-   Views are still the actual widgets the player sees and taps.

### `TerminalViewBinder`

Purpose:

-   Plain Java helper for Android Views inside `TerminalFragment`.

Owns:

-   terminal history display,
-   command input widgets,
-   command submit button wiring later.

Why it exists:

-   The terminal should feel like a stylized computer UI built from normal Android Views.
-   Terminal state and command parsing stay in `TerminalSession` and `TerminalCommandProcessor`.

## Persistence

### `DataLoader`

Purpose:

-   Loads static case content from assets or temporary hardcoded builders.

### `GameRepository`

Purpose:

-   Single access point for static content and save state.

### `GameDatabase`

Purpose:

-   SQLite helper if database CRUD is required.

### `GameContract`

Purpose:

-   SQLite table and column names.

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
