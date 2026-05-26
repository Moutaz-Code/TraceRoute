# Runtime Architecture

This is the intended architecture for the TraceRoute / Ghost Signal MVP.

The project is required to use Android Fragments, so fragments are part of the official structure rather than an optional UI choice.

## High-Level Shape

```text
MainActivity
  GameShellFragment
    WorldInvestigationFragment
      GameView
        GLRenderer
          GameController
            WorldInvestigationScene
            EvidenceSystem
            ThreadSystem
            TimelineSystem
            StatementSystem
            TerminalSession

    HandheldFragment
      HandheldController
      HandheldViewBinder
      regular Android views for evidence / threads / timeline / statements

    TerminalFragment
      TerminalViewBinder
```

## Layer Responsibilities

### Android Activity Layer

`MainActivity` should stay thin.

It should:

-   own Android startup,
-   host `GameShellFragment`,
-   apply system insets if needed.

It should not:

-   own evidence logic,
-   own room navigation,
-   parse terminal commands,
-   render OpenGL objects directly.

### Fragment UI Layer

Fragments are used where they help structure the app and satisfy the project requirement.

They are not used for every internal screen.

Important fragments:

-   `GameShellFragment`: root game shell.
-   `WorldInvestigationFragment`: hosts the OpenGL `GameView`.
-   `HandheldFragment`: one overlay for forensic device screens.
-   `TerminalFragment`: stylized terminal computer popup.

Inside `HandheldFragment`, use ordinary Android views and `HandheldController` for:

-   evidence database,
-   investigation threads,
-   timeline view,
-   suspect statements,
-   final timeline ordering.

Fragments should:

-   inflate views,
-   receive button/tap UI events,
-   call into controller/system classes,
-   display state.

Fragments should not:

-   contain the case clue chain,
-   directly modify SQLite,
-   own final deduction logic,
-   parse terminal commands.

Use plain Java classes/controllers when a feature does not need its own Android lifecycle.

### Graybox Versus Final Presentation

Current Android widgets are graybox scaffolding, not the final visual language.

Final direction:

-   use authored assets/artwork for rooms, UI frames, device controls, evidence cards, and suspect presentation,
-   avoid native Android controls when they break immersion,
-   avoid the native soft keyboard during normal gameplay,
-   replace temporary buttons/panels with stylized game UI once flow is proven,
-   keep post-processing, scanlines, vignette, and glitch treatment for a later polish stage.

Temporary Android widgets are acceptable when they help prove game flow quickly, but they should not define the final aesthetic.

### Android View Layer

Views are still central to the app.

The intended split is:

-   Fragments provide lifecycle, containment, and coarse navigation.
-   Views provide the actual UI controls and layout.
-   Plain Java controllers/view binders keep UI logic organized.

Use Android Views for:

-   handheld tabs and panels,
-   evidence lists/details,
-   thread text,
-   timeline cards,
-   suspect statement cards,
-   terminal text/input,
-   buttons and touch controls around the OpenGL view.

Do not create a new Fragment for every UI panel unless that panel needs independent lifecycle or back-stack behavior.

Helpful plain Java UI helpers:

-   `HandheldController`: selected handheld screen and UI state.
-   `HandheldViewBinder`: binds handheld Android Views.
-   `TerminalViewBinder`: binds terminal Android Views.

### OpenGL Layer

OpenGL is used for:

-   apartment background rendering,
-   hotspot highlights,
-   room transitions if time allows,
-   visual effects such as scanlines or glitch overlays.

`GameView` should:

-   extend `GLSurfaceView`,
-   configure OpenGL ES 3.0,
-   forward lifecycle and touch input.

`GLRenderer` should:

-   implement `GLSurfaceView.Renderer`,
-   initialize OpenGL state,
-   call game update/render,
-   delegate drawing to `RenderPipeline`.

### Gameplay/Core Layer

Game systems should own the actual game state.

`GameController` coordinates:

-   current room,
-   collected evidence,
-   investigation threads,
-   timeline state,
-   suspect statements,
-   terminal interactions,
-   save/load.

This keeps the MVP readable and prevents fragments from becoming giant logic classes.

## Room Model

The 4 apartment views should not be 4 Android `View` classes.

Use data definitions:

```text
RoomDefinition
  id
  displayName
  backgroundTexturePath
  hotspots
  navigationTargets
```

Required rooms:

-   `LIVING_ROOM`
-   `ENTRY_SMART_LOCK`
-   `KITCHEN_SIDE_AREA`
-   `OFFICE_DEVICE_CORNER`

Optional room:

-   `CCTV_WINDOW_OPTIONAL`

## Input And Hotspot Model

Touch input flows through the OpenGL game path:

```text
GameView
  GLRenderer
    GameController
      WorldInvestigationScene
        HotspotSystem
```

`GameView` converts Android touch coordinates into normalized screen coordinates:

-   `x = 0.0` at the left edge.
-   `x = 1.0` at the right edge.
-   `y = 0.0` at the top edge.
-   `y = 1.0` at the bottom edge.

For the current MVP stage, hotspots use this same normalized screen-space coordinate model.

Reasons:

-   it matches Android touch input directly,
-   it is easy to author and debug,
-   it works well with fixed-view room navigation,
-   it keeps the first hotspot system simple.

Later, if art-specific precision is needed, add a conversion layer from normalized screen coordinates into room-image coordinates. Do not put that conversion in fragments.

Current hotspot actions:

-   `NAVIGATE_ROOM`
-   `INSPECT_EVIDENCE`
-   `OPEN_HANDHELD`
-   `OPEN_TERMINAL`
-   `SHOW_FLAVOR_TEXT`

The first implemented action is `NAVIGATE_ROOM`.

The handheld device is opened from a bottom-center hotspot strip, inspired by games where a player raises a device from the bottom of the screen. This should remain the primary world-to-handheld interaction:

-   the world stays visible until the player taps the strip,
-   the strip opens `HandheldFragment`,
-   Android UI owns the actual overlay,
-   gameplay only requests the overlay through `GameUiEventListener`.

The OpenGL/game layer should not directly open fragments. Requests flow through:

```text
WorldInvestigationScene
  GameUiEventListener
    GameView
      GameShellFragment
```

This keeps gameplay logic separate from Android UI mechanics while still allowing hotspots to open major UI surfaces.

Implemented hotspot action examples:

-   side zones navigate between rooms,
-   bottom-center strip opens the handheld,
-   living-room evidence hotspot requests evidence inspection,
-   office hotspot opens the terminal dialog.

## Runtime State

`GameRuntime` is the current shared play-session state object.

It exists because:

-   OpenGL gameplay runs through `GameController` and `WorldInvestigationScene`,
-   Android UI fragments need to display the same evidence/thread/timeline state,
-   fragments should not own gameplay state directly,
-   the project does not yet have full repository/save-state plumbing.

Current runtime flow for evidence:

```text
WorldInvestigationScene
  EvidenceSystem.collectEvidence(id)
    GameRuntime
      EvidenceSystem

HandheldFragment
  HandheldViewBinder
    GameRuntime
      EvidenceSystem.getCollectedEvidence()
```

This is acceptable for the early MVP foundation. Later, `GameRuntime` can be backed by `GameRepository`, `DataLoader`, and save/load persistence.

Implemented evidence items include:

-   `E01_BODY_CONDITION_REPORT`
-   `E02_THERMOSTAT_AC_LOG`
-   `E05_SMART_LOCK_ENTRY_EXIT_LOG`
-   `E06_SCHEDULED_VICTIM_MESSAGE`
-   `E07_LAPTOP_SESSION_METADATA`
-   `E08_ROUTER_ADMIN_ACCESS_RECORD`

The first evidence display surface is:

-   `HandheldFragment` evidence list.

## Investigation Thread Loop

The first implemented deduction loop is:

```text
Collect E01_BODY_CONDITION_REPORT
  ThreadSystem unlocks Death Window observation 1

Collect E02_THERMOSTAT_AC_LOG
  ThreadSystem unlocks Death Window observation 2

If both are collected
  Death Window Instability becomes stabilized
```

This creates the first playable version of the intended "soft confirmation deduction" model:

-   the player discovers clues in the world,
-   the system records them as evidence,
-   investigation threads update automatically,
-   the handheld explains the evolving line of reasoning.

The handheld currently has working tabs:

-   Evidence
-   Threads
-   Timeline

Later tabs should include:

-   Terminal entry point if desired.

## Timeline Loop

The first implemented timeline event is:

-   `TL01_POST_21_ACTIVITY_UNRELIABLE`

The current timeline flow is:

```text
EvidenceSystem
  collects body report / AC log
    ThreadSystem
      updates Death Window Instability
        TimelineSystem
          reveals and stabilizes first contradiction
```

Current behavior:

-   once the Death Window thread has at least one observation, the timeline contradiction becomes visible,
-   once the Death Window thread is stabilized, the timeline contradiction is marked stabilized,
-   the handheld Timeline tab displays the initial interpretation and current resolved interpretation.

This timeline screen is not the final ordering puzzle yet. It is the investigation timeline: a readable place for contradictions, uncertain windows, and stabilized chronology claims.

## Suspect Statement Loop

The first implemented suspect statement is:

-   `S01_PARTNER_STATEMENT`

The first statement unlock clue is:

-   `E05_SMART_LOCK_ENTRY_EXIT_LOG`

Current statement flow:

```text
Collect E05_SMART_LOCK_ENTRY_EXIT_LOG
  StatementSystem unlocks S01_PARTNER_STATEMENT

Stabilize TL01_POST_21_ACTIVITY_UNRELIABLE
  StatementSystem marks S01_PARTNER_STATEMENT contradicted
```

This keeps statements lightweight:

-   no branching interview tree,
-   no dialogue minigame,
-   one readable claim,
-   one suspicious detail,
-   one contradiction state.

The handheld currently has four working tabs:

-   Evidence
-   Threads
-   Timeline
-   Statements

## Terminal Mechanic

The terminal is a major mechanic.

It should be treated as:

-   a `DialogFragment` for UI,
-   a `TerminalSession` for runtime state,
-   a `TerminalCommandProcessor` for authored command handling.

It should not become:

-   a full shell simulation,
-   a freeform parser,
-   a separate game mode disconnected from evidence and threads.

The terminal should support authored interactions like:

-   inspect logs,
-   query device activity,
-   reveal metadata,
-   unlock evidence,
-   update investigation threads.

Current MVP terminal boundary:

-   terminal commands are authored, not freeform,
-   unknown commands return a readable fallback,
-   commands do not execute real device or shell operations,
-   command results may read `GameRuntime` state,
-   later commands may unlock evidence/thread observations through gameplay systems.
-   the terminal should not use the native Android soft keyboard during normal play.

Implemented starter commands:

-   `HELP`
-   `EVIDENCE`
-   `MESSAGES`
-   `BODY`
-   `LOGS`
-   `LAPTOP`
-   `CLEAR`

`TerminalSession` owns visible history. `TerminalCommandProcessor` owns command interpretation. `TerminalViewBinder` owns Android view binding.

For the current graybox, terminal commands are exposed as ordinary Android buttons. Later, replace these with authored game-art buttons, a custom diegetic keypad, or an asset-backed terminal interface. Do not return to a native text-entry keyboard unless there is a deliberate accessibility/debug mode.

The `MESSAGES` command currently discovers:

-   `E06_SCHEDULED_VICTIM_MESSAGE`

The `LAPTOP` command currently discovers:

-   `E07_LAPTOP_SESSION_METADATA`

These clues advance the `Postmortem Activity` thread. Collecting both stabilizes the thread.

The `LOGS` command currently discovers:

-   `E08_ROUTER_ADMIN_ACCESS_RECORD`

This supports Revelation Beat 3: trusted technical access. When combined with `E05_SMART_LOCK_ENTRY_EXIT_LOG`, it stabilizes the `Apartment Access` thread.

## Revelation Beat 3: Trusted Technical Access

The current Apartment Access flow is:

```text
Collect E05_SMART_LOCK_ENTRY_EXIT_LOG
  Apartment Access observes physical entry/exit timing

Run LOGS in terminal
  Collect E08_ROUTER_ADMIN_ACCESS_RECORD
  Apartment Access observes privileged technical access

If both are collected
  Apartment Access stabilizes
```

Design meaning:

-   simple presence is no longer the only question,
-   remote or system-level manipulation becomes central,
-   suspicion starts shifting toward someone with trusted technical familiarity.

## Data And Persistence

Separate static content from save state.

Static content:

-   room definitions,
-   evidence definitions,
-   thread definitions,
-   timeline events,
-   suspect statements,
-   terminal command scripts.

Save state:

-   collected evidence ids,
-   unlocked thread observations,
-   stabilized threads,
-   unlocked statements,
-   current room,
-   final timeline completion.

For MVP, static content can start hardcoded or loaded from assets.

For save state:

-   `SharedPreferences` is enough technically.
-   Use SQLite through `GameDatabase` if the course requires CRUD/database practice.

All persistence should be accessed through `GameRepository`.

## Implementation Order

1.  Make `MainActivity` host `GameShellFragment`.
2.  Make `WorldInvestigationFragment` host `GameView`.
3.  Render one background through OpenGL.
4.  Add `RoomDefinition` and switch between 4 room ids.
5.  Add `Hotspot` rectangles and tap detection.
6.  Add evidence collection through `EvidenceSystem`.
7.  Add `HandheldFragment` with evidence screen.
8.  Add thread/timeline/statement screens.
9.  Add `TerminalFragment` and terminal command state.
10. Add final timeline ordering inside `HandheldFragment` or as a simple overlay view.
11. Add save/load.
12. Polish visuals/audio.
