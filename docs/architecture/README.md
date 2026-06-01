# Architecture Notes

This folder holds technical design notes for the Android/Java/OpenGL ES 3.0 MVP.

Start here:

-   [Runtime Architecture](runtime_architecture.md)
-   [Class Responsibility Map](class_responsibility_map.md)

Current architectural direction:

-   Android Fragments are mandatory and should be used meaningfully, but not for every small UI panel.
-   OpenGL rendering lives inside `WorldInvestigationFragment` through `GameView`.
-   Apartment rooms are data definitions, not Android `View` classes.
-   Hotspots are normalized screen-space rectangles owned by room definitions.
-   The handheld opens from a bottom-center world hotspot, with Android UI opened through `GameUiEventListener`.
-   The handheld is one fragment-based Android UI overlay with ordinary internal views/controllers.
-   The stylized victim laptop is a major mechanic and uses `LaptopFragment`.
-   Game logic should live in gameplay/core systems, not inside fragment classes.

