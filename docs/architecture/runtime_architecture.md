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
