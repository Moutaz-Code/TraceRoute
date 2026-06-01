<style>
@media print {

  h1, h2, h3 {
    page-break-after: avoid;
    break-after: avoid;
  }

  p, li, blockquote, pre {
    page-break-inside: avoid;
    break-inside: avoid;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    page-break-inside: auto;
    break-inside: auto;
  }

  tr {
    page-break-inside: avoid;
    break-inside: avoid;
  }

  .page-break {
    page-break-before: always;
    break-before: page;
  }
}
</style>

# TraceRoute / Ghost Signal - Project Context

Working title:

-   TraceRoute
-   Ghost Signal

Current title status:

-   Placeholder.
-   Final title not locked.

Genre:

-   Techno-noir forensic mystery.
-   Mobile detective investigation.
-   Cyber-forensics themed narrative puzzle game.

Platform and tech:

-   Android Studio.
-   Java.
-   OpenGL ES 3.0.
-   No Unity, Unreal, or Kotlin.

Team and schedule:

-   3 developers.
-   About 3 weeks total.
-   MVP first, polish afterward if time remains.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>


## Document Purpose

This document is the main design context for the project.

It should help the team keep track of:

-   the core game identity,
-   the MVP case plan,
-   the clue chain,
-   the apartment structure,
-   the handheld UI role,
-   investigation threads,
-   production scope,
-   manual implementation tasks.

The document is intentionally practical. It is meant to support design decisions and implementation, not sell the concept.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 1. Project Snapshot

## Core Game Identity

The game focuses on:

-   forensic investigation,
-   timeline reconstruction,
-   manipulated digital evidence,
-   guided deduction,
-   cyber-forensic reasoning.

The game does not focus on:

-   combat,
-   open-world exploration,
-   sandbox detective simulation,
-   hacking power fantasy,
-   complex branching outcomes,
-   freeform clue-combination systems.

The intended experience:

> A curated forensic investigation with selective deductive agency.

Player fantasy:

> You are a cyber-forensics investigator assisting authorities in solving a technologically manipulated murder.

Condensed identity:

> A guided techno-noir forensic mystery about manipulated digital continuity, unstable chronology, and the danger of trusting machine records without context.

## MVP Target

Target playtime:

-   15-20 minutes for a first-time player.

The extra time over a very short 10-15 minute slice should be used for:

-   a stronger clue chain,
-   clearer revelation pacing,
-   better understanding of why the business partner is guilty.

The extra time should not be used for:

-   more locations,
-   branching interviews,
-   multiple endings,
-   optional minigames,
-   large amounts of filler evidence.

## MVP Included Scope

The MVP should contain:

-   1 apartment environment.
-   1 complete murder case.
-   3 primary suspects.
-   1 handheld forensic interface.
-   Evidence database.
-   Investigation thread system.
-   Timeline screen.
-   Final timeline-ordering sequence.
-   Basic suspect statements.
-   Persistent save state if time allows.

## MVP Cut / Avoid List

Avoid:

-   multiple large locations,
-   fully dynamic deduction systems,
-   voice acting,
-   branching endings,
-   complex inventory systems,
-   real-time hacking gameplay,
-   procedural systems,
-   large-scale animation requirements,
-   replayability systems.

## Overall Structure

The structure is mostly linear with local freedom.

Meaning:

-   story progression is controlled,
-   evidence within a phase can be found in flexible order,
-   the player can revisit evidence freely,
-   progression gates are tied to investigation advancement,
-   the game should avoid fail states and softlocks.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 2. Design Pillars

## Central Theme

The strongest theme:

> Human memory is uncertain. Machines record precise events. But machines can be manipulated.

This should be reinforced through:

-   approximate human statements,
-   precise digital timestamps,
-   physical evidence that destabilizes the digital story,
-   machine logs that are accurate but misleading,
-   a final reconstruction that restores the true sequence.

## Tone

Target tone:

-   quiet,
-   oppressive,
-   investigative,
-   eerie,
-   melancholic,
-   tragic.

Avoid:

-   action-thriller pacing,
-   heroic triumph,
-   exaggerated villainy,
-   comedy-heavy dialogue,
-   horror jumpscare framing.

The ending should feel grounded and sad rather than victorious.

## Gameplay Philosophy

The game should feel:

-   guided but intelligent,
-   atmospheric but readable,
-   technical but accessible,
-   investigative rather than puzzle-heavy.

The player should:

-   mentally notice contradictions,
-   follow investigation threads,
-   resolve chronology uncertainty,
-   understand how digital continuity was manipulated.

The systems should:

-   support deduction,
-   avoid replacing deduction entirely,
-   avoid tedious clue-combination mechanics.

## Deduction Philosophy

Core deduction types:

-   chronological resolution,
-   manipulation detection.

Supporting deduction types:

-   evidence correlation,
-   lightweight cybersecurity analysis.

Mostly system-assisted:

-   hidden-event inference,
-   interpretive conclusions.

No explicit "combine clue A with clue B" submission gameplay for MVP.

Instead:

-   player notices contradictions,
-   evidence updates threads,
-   threads unlock new observations and questions,
-   the final timeline confirms understanding.

This is the current "soft confirmation deduction" model.

## Educational Direction

Cybersecurity knowledge should:

-   help,
-   add flavor,
-   reward attentive players,
-   never gate progress.

Cyber-forensic interactions should be:

-   readable,
-   contextualized,
-   simplified,
-   clearly labeled.

Example approach:

-   Use labels such as `SOURCE`, `DESTINATION`, `SCHEDULED`, `ADMIN SESSION`, and `AUTOMATION TRIGGER`.
-   Avoid requiring players to understand real network forensics.
-   Let the player infer meaning from context and contradictions.

## Failure State Philosophy

No fail states for MVP.

Avoid:

-   timers,
-   stress mechanics,
-   softlocks,
-   wrong-ending branches.

Possible later addition:

-   detective score,
-   post-game rating,
-   optional clue completeness.

These are not MVP priorities.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 3. MVP Case Plan

## Case Premise

A wealthy but unpleasant tech entrepreneur is found dead in his apartment living room.

At first, digital evidence suggests he was alive after 21:00.

The truth:

-   he died earlier,
-   his business partner killed him during an argument,
-   the partner used smart-home systems and scheduled activity to make the victim appear alive longer than he was.

The mystery centers on:

-   unstable chronology,
-   artificial continuation of activity,
-   the difference between access and presence,
-   a business motive tied to illegal access-data activity.

## Player Role

The player is a cyber-forensics investigator.

The player is physically present at the apartment for scene inspection, but most interpretation happens through a handheld forensic device.

The MVP has two connected modes:

1.  Apartment inspection.
2.  Handheld analysis.

The player fantasy:

> I am not guessing the killer. I am stabilizing an unreliable timeline.

## Core Game Flow

The intended MVP flow:

1.  Enter apartment investigation view.
2.  Inspect fixed apartment views and tap hotspots.
3.  Add evidence to the handheld database.
4.  Review evidence through handheld screens.
5.  Watch investigation threads update.
6.  Unlock short suspect statements.
7.  Notice contradictions between physical evidence and digital activity.
8.  Collapse the false death window.
9.  Order the final timeline.
10. Expose the business partner.
11. End on a quiet tragic note.

## Apartment Scope

Use 4 core fixed apartment views.

A 5th view is optional if art and implementation time allow.

### View 1 - Living Room / Body Scene

Purpose:

-   Main murder scene.
-   Establishes violence, body position, and scene disturbance.
-   Starts the physical evidence side of the case.

Important hotspots:

-   Body.
-   Broken vase.
-   Disturbed side table.
-   Blood-marked glass company award.
-   Carpet or floor disturbance.

Evidence from this view:

-   Body condition report.
-   Broken vase / disturbed table.
-   Blood-marked company award.
-   Scene disturbance observation.

Design intent:

-   The room should look mostly controlled except for a localized struggle.
-   The murder should feel impulsive, not pre-planned.
-   The body scene should not be overly graphic.

### View 2 - Entry / Smart-Lock Area

Purpose:

-   Establishes access chronology.
-   Introduces the false security of precise digital logs.
-   Creates the partner's apparent alibi.

Important hotspots:

-   Apartment door.
-   Smart-lock panel.
-   Entry log display.
-   Security intercom or building access panel.

Evidence from this view:

-   Smart-lock entry/exit log.
-   Partner exit timestamp.
-   Wife remote-access timestamp, if shown through the same system.

Design intent:

-   At first, the log should seem clean and authoritative.
-   Later, the player realizes clean timestamps can still support a false story if the death window is wrong.

### View 3 - Kitchen / Side Area

Purpose:

-   Supports the AC/body temperature contradiction.
-   Gives the apartment physical depth without becoming a new location.

Important hotspots:

-   Thermostat.
-   AC vent.
-   Environmental readout.
-   Condensation or cold-air clue.

Evidence from this view:

-   Thermostat/AC log.
-   Environmental inconsistency note.

Design intent:

-   This is the first major contradiction.
-   The player should understand that the apartment was made colder after the murder.
-   The clue should be readable without medical or forensic expertise.

### View 4 - Office / Device Corner

Purpose:

-   Main digital-forensics area.
-   Connects the murder to the access-data motive.
-   Reveals automated activity and trusted technical access.

Important hotspots:

-   Laptop.
-   Router.
-   Smart-home hub.
-   Work documents.
-   Draft disclosure email.

Evidence from this view:

-   Laptop session metadata.
-   Router/admin access record.
-   Smart-home automation log.
-   Access-data sale file.
-   Victim draft disclosure email.

Design intent:

-   This view should feel like the cyber-forensics core of the case.
-   Technical details should be clearly labeled.
-   Cybersecurity knowledge should help interpretation, but never be required.

### Optional View 5 - CCTV / Window / Balcony Angle

Purpose:

-   Supports security/staff suspicion.
-   Gives a visible reason for the welfare check.
-   Adds atmosphere if time allows.

Important hotspots:

-   CCTV blind spot.
-   Building camera angle.
-   Window or balcony sightline.
-   Security notice or maintenance panel.

Evidence from this view:

-   Security CCTV outage note.
-   Welfare check context.

Design intent:

-   This view is useful but not required.
-   If time is tight, fold its evidence into the entry/smart-lock view.

## True Timeline

This is the hidden truth of the case.

The player should not see this exact version until the final timeline is stabilized.

| Time | True Event | Design Purpose |
| --- | --- | --- |
| 19:05 | Business partner arrives for a private meeting. | Places the killer inside early. |
| 19:18 | Argument escalates after victim says he wants to expose the illegal access-data operation. | Establishes motive and emotional pressure. |
| 19:27 | Partner kills victim in the living room with a heavy glass company award. | True murder time. |
| 19:34 | Partner lowers AC and begins staged smart-home activity. | Begins manipulation of body temperature and activity trail. |
| 19:38 | Partner schedules victim-like phone/laptop activity. | Creates false proof of life. |
| 19:42 | Partner exits the apartment. | Creates false alibi if death is believed to be later. |
| 20:18 | Wife remotely checks apartment systems after a previous argument. | Makes wife suspicious for a legitimate reason. |
| 20:40 | Security/staff camera outage occurs. | Makes security/staff suspicious for a legitimate reason. |
| 21:07 | Scheduled victim message sends. | Strongest false proof that victim was alive. |
| 21:16 | Automated laptop/session activity continues. | Reinforces post-death activity. |
| 22:30 | Security welfare check discovers body. | Investigation begins. |

## False Initial Timeline

This is what the player initially believes or is encouraged to assume.

| Time | Apparent Event | Why It Misleads |
| --- | --- | --- |
| 19:05 | Business partner arrives. | Looks routine. |
| 19:42 | Business partner exits. | Seems to clear him if death happened later. |
| 20:18 | Wife remotely accesses apartment systems. | Places her near the apparent window. |
| 20:40 | CCTV outage occurs. | Makes security/staff behavior suspicious. |
| 21:07 | Victim sends message. | Suggests victim is alive after 21:00. |
| 21:16 | Laptop activity continues. | Reinforces the false death window. |
| 22:30 | Body is discovered. | Makes the late window feel plausible. |

The player should gradually understand:

-   the message was scheduled,
-   laptop activity was automated,
-   the AC distorted physical interpretation,
-   the partner's early exit is only useful as an alibi if the false window is trusted.

## False Initial Assumption

The main false anchor:

> The victim was alive after 21:00.

This assumption is supported by:

-   scheduled message activity,
-   laptop/session activity,
-   smart-home continuity,
-   apparently clean access logs.

This assumption makes these suspects look more relevant:

-   Wife, because she accessed apartment systems at 20:18.
-   Security/staff, because the CCTV outage happened at 20:40.

The player later proves that post-21:00 activity was artificial.

Once that happens, the business partner's earlier visit becomes the key event instead of background context.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 4. Characters And Suspects

## Victim

Role:

-   Murder victim.
-   Successful but unpleasant tech entrepreneur.

Traits:

-   wealthy,
-   intelligent,
-   rude,
-   selfish,
-   cruel toward staff,
-   involved in illegal access-data activity with the business partner.

Important nuance:

-   He should be flawed, but not cartoonishly evil.
-   His planned disclosure should make sense as fear, guilt, self-preservation, or a mix of all three.

## Business Partner

Role:

-   Killer.

Public impression:

-   Technically skilled.
-   Tired and defensive.
-   Claims the meeting was tense but not violent.
-   Claims victim was alive and calm when he left.

Truth:

-   He killed the victim during an emotional escalation.
-   He used technical access to extend the appearance of victim activity.
-   He depended on the illegal access-data operation continuing.

Traits:

-   technically aware,
-   emotionally unstable,
-   financially irresponsible,
-   impulsive but intelligent,
-   dependent on illegal money flow.

Psychological arc:

-   intimidation attempt,
-   emotional escalation,
-   blunt-force killing,
-   panic,
-   calculated cover-up,
-   guilt hidden beneath rationalization,
-   numbness.

Primary lie:

> The victim was alive and stable when I left.

Evidence that breaks the lie:

-   body condition report,
-   AC log,
-   scheduled message metadata,
-   router/admin access record,
-   access-data sale file,
-   victim draft disclosure email.

## Wife

Role:

-   Suspicious but not guilty.

Public impression:

-   Strained relationship with victim.
-   Hides the severity of their argument.
-   Has remote access to apartment systems.

Truth:

-   She remotely checked the apartment after a private conflict.
-   She knew more about the victim's illegal activity than she first admits.
-   She did not enter the apartment and did not kill him.

Suspicion source:

-   Her remote access occurs inside the false death window.
-   She is evasive about the relationship and what she knew.

Resolution:

-   Her access is real, but it occurs after the true murder and does not explain the staging.

Design note:

-   She should be a believable suspect, not a fake red herring.

## Security / Staff Suspect

Role:

-   Suspicious but not guilty.

Public impression:

-   Resented the victim.
-   Had access to building systems.
-   Is connected to the CCTV outage.
-   Finds the body during the welfare check.

Truth:

-   The CCTV outage is suspicious but not the murder mechanism.
-   The staff member has motive for resentment, but not the core technical access or timing.
-   The welfare check is legitimate.

Suspicion source:

-   camera outage,
-   prior argument or mistreatment by victim,
-   building access,
-   welfare check proximity.

Resolution:

-   The outage complicates the record but does not align with the actual murder sequence.
-   Security/staff lacks the trusted technical access needed for staged device activity.

## Motive

The victim and business partner were involved in an illegal access-data operation.

Working version:

-   Their company had access to smart-building or apartment-system data.
-   They sold, exploited, or quietly shared access data for money.
-   The victim planned to withdraw, confess, or disclose the operation.
-   The partner feared legal exposure and financial collapse.

The motive should be understandable quickly:

> The victim was about to expose the illegal system that the partner depended on.

This motive connects directly to:

-   apartment systems,
-   remote access,
-   smart-home logs,
-   cyber-forensic evidence,
-   the theme of manipulated machine records.

## Murder Details

Location:

-   Living room.

Method:

-   Blunt-force trauma.

Weapon:

-   Heavy glass company award.

Scene condition:

-   Mostly orderly except for localized disturbance.

Examples:

-   knocked side table,
-   broken vase,
-   disturbed carpet,
-   short struggle signs.

Design intent:

-   The violence should feel emotionally impulsive.
-   The cover-up should feel more calculated than the murder.
-   The killer should be technically competent, but not a genius mastermind.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 5. Evidence And Clue Chain

Use 13-15 meaningful evidence items.

Avoid filler collectibles.

Each evidence item should do at least one of the following:

-   destabilize the timeline,
-   support a suspect,
-   clear a suspect,
-   reveal the manipulation method,
-   establish motive.

## Evidence Summary Table

| ID | Evidence | Category | Source View | Main Thread | Purpose |
| --- | --- | --- | --- | --- | --- |
| E01 | Body Condition Report | Physical / forensic | Living room | Death Window Instability | Suggests death happened earlier than apparent activity. |
| E02 | Thermostat / AC Log | Digital / environmental | Kitchen / side area | Death Window Instability | Shows unusual cooling after the real murder time. |
| E03 | Broken Vase / Disturbed Table | Physical | Living room | Scene Disturbance | Supports short localized struggle. |
| E04 | Blood-Marked Company Award | Physical | Living room | Scene Disturbance / Motive | Identifies likely weapon and links symbolically to company. |
| E05 | Smart-Lock Entry / Exit Log | Digital / access | Entry | Apartment Access | Shows partner arrived at 19:05 and exited at 19:42. |
| E06 | Scheduled Victim Message | Digital / communication | Handheld / device data | Postmortem Activity | False proof of life at 21:07. |
| E07 | Laptop Session Metadata | Digital / device activity | Office | Postmortem Activity | Reveals automated activity pattern. |
| E08 | Router / Admin Access Record | Digital / network | Office | Apartment Access | Shows trusted technical access around staging period. |
| E09 | Smart-Home Automation Log | Digital / smart-home | Office or thermostat | Postmortem Activity | Connects AC, lighting/device activity, and staged continuity. |
| E10 | Wife Remote-Access Record | Digital / access | Entry or handheld | Apartment Access | Makes wife suspicious but later explainable. |
| E11 | Security CCTV Outage Note | Digital / security | Optional CCTV or entry | Apartment Access | Creates security/staff suspicion. |
| E12 | Access-Data Sale File | Digital / motive | Office | Data-Sale Motive | Establishes illegal business activity. |
| E13 | Victim Draft Disclosure Email | Digital / motive | Office laptop | Data-Sale Motive | Shows victim intended to expose or withdraw. |
| E14 | Partner Statement | Testimonial | Statement screen | Access / Death Window | Contains the key lie. |
| E15 | Security / Staff Statement | Testimonial | Statement screen | Apartment Access | Explains outage and welfare check. |

## Evidence Details

### E01 - Body Condition Report

Player-facing meaning:

-   The body condition does not cleanly match a death after 21:00.

Design role:

-   First physical clue that the apparent timeline is wrong.

### E02 - Thermostat / AC Log

Player-facing meaning:

-   AC was lowered unusually after the true murder time.

Design role:

-   Main first contradiction.
-   Shows that machine records are precise but can be used to mislead.

### E03 - Broken Vase / Disturbed Table

Player-facing meaning:

-   There was a short localized struggle.

Design role:

-   Supports impulsive violence rather than a clean planned execution.

### E04 - Blood-Marked Company Award

Player-facing meaning:

-   The company award is likely the murder weapon.

Design role:

-   Links the murder weapon symbolically to the company and partner relationship.

### E05 - Smart-Lock Entry / Exit Log

Player-facing meaning:

-   Partner arrived at 19:05 and exited at 19:42.

Design role:

-   Initially appears to clear the partner.
-   Later becomes damning once death is moved earlier.

### E06 - Scheduled Victim Message

Player-facing meaning:

-   A message sent at 21:07 appears to show the victim was alive, but metadata suggests it was scheduled.

Design role:

-   Strong false proof of life.

### E07 - Laptop Session Metadata

Player-facing meaning:

-   Laptop activity continued after the likely death time, but the interaction pattern looks automated.

Design role:

-   Reinforces that the victim's digital presence was artificially continued.

### E08 - Router / Admin Access Record

Player-facing meaning:

-   A trusted admin-level session accessed apartment systems around the staging period.

Design role:

-   Shows that manipulation required technical familiarity and privileged access.

### E09 - Smart-Home Automation Log

Player-facing meaning:

-   Several actions were automated or triggered in sequence after the murder.

Design role:

-   Connects AC manipulation, lighting/device activity, and staged continuity.

### E10 - Wife Remote-Access Record

Player-facing meaning:

-   Wife remotely accessed apartment systems at 20:18.

Design role:

-   Makes wife suspicious, then helps show the difference between access and murder opportunity.

### E11 - Security CCTV Outage Note

Player-facing meaning:

-   A camera outage occurred at 20:40.

Design role:

-   Creates security/staff suspicion.
-   Later becomes secondary because the true murder happened earlier.

### E12 - Access-Data Sale File

Player-facing meaning:

-   Victim and partner were involved in illegal access-data activity.

Design role:

-   Establishes the case's cyber-forensic motive.

### E13 - Victim Draft Disclosure Email

Player-facing meaning:

-   Victim intended to expose or withdraw from the illegal operation.

Design role:

-   Gives the partner a clear reason to confront and silence the victim.

### E14 - Partner Statement

Player-facing meaning:

-   Partner claims the victim was alive, calm, and working when he left.

Design role:

-   Contains the key lie.

### E15 - Security / Staff Statement

Player-facing meaning:

-   Security/staff explains the outage and welfare check.

Design role:

-   Gives a suspicious but ultimately non-killer explanation.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 6. Investigation Threads And Revelation Pacing

## Thread Model

Use semi-explicit investigation threads.

Threads should not feel like ordinary objectives. They should feel like evolving lines of reasoning.

Each thread can have:

-   unresolved observations,
-   suspicious evidence,
-   one stabilized conclusion.

Progression is primarily thread-resolution based.

## Thread 1 - Death Window Instability

Core question:

> When did the victim actually die?

Starts when:

-   Body condition report is discovered.

Advances with:

-   Thermostat/AC log.
-   Smart-home automation log.
-   Partner statement contradiction.

Stabilized conclusion:

-   The death window was distorted. The victim likely died before the post-21:00 activity.

## Thread 2 - Postmortem Activity

Core question:

> Why did the victim appear active after death?

Starts when:

-   Scheduled victim message is discovered.

Advances with:

-   Laptop session metadata.
-   Smart-home automation log.
-   Router/admin access record.

Stabilized conclusion:

-   The victim's digital activity was artificially continued after death.

## Thread 3 - Apartment Access

Core question:

> Who could affect the apartment physically or remotely?

Starts when:

-   Smart-lock entry/exit log is discovered.

Advances with:

-   Wife remote-access record.
-   Router/admin access record.
-   Security CCTV outage note.

Stabilized conclusion:

-   Several people had suspicious access, but the full manipulation required trusted technical privileges.

## Thread 4 - Data-Sale Motive

Core question:

> Why would the partner escalate to violence?

Starts when:

-   Access-data sale file is discovered.

Advances with:

-   Victim draft disclosure email.
-   Blood-marked company award.
-   Partner statement.

Stabilized conclusion:

-   The victim was preparing to expose an illegal operation the partner depended on.

## Thread 5 - Scene Disturbance

Core question:

> Was this planned, staged, or impulsive?

Starts when:

-   Broken vase/disturbed table is found.

Advances with:

-   Blood-marked company award.
-   Body condition report.

Stabilized conclusion:

-   The killing was an impulsive escalation followed by a calculated cover-up.

## Revelation Beat 1 - AC / Body Contradiction

Player discovers:

-   Body condition report.
-   Thermostat/AC log.

Realization:

-   The death window is less stable than it first appeared.

Effect:

-   The player begins to doubt the late death assumption.

## Revelation Beat 2 - Message / Laptop Contradiction

Player discovers:

-   Scheduled victim message.
-   Laptop session metadata.

Realization:

-   Victim activity after 21:00 may not prove the victim was alive.

Effect:

-   Postmortem Activity becomes central.

## Revelation Beat 3 - Trusted Technical Access

Player discovers:

-   Router/admin access record.
-   Smart-home automation log.

Realization:

-   The manipulation required someone who understood the victim's systems and had privileged access.

Effect:

-   Suspicion shifts away from simple physical access and toward technical familiarity.

## Revelation Beat 4 - Data-Sale Motive

Player discovers:

-   Access-data sale file.
-   Victim draft disclosure email.

Realization:

-   The partner had a direct reason to stop the victim.

Effect:

-   The partner is no longer just a visitor. He becomes the strongest suspect.

## Revelation Beat 5 - Final Timeline Stabilization

Player has enough evidence to order:

-   arrival,
-   argument,
-   murder,
-   staging,
-   exit,
-   false activity,
-   discovery.

Realization:

-   The partner killed earlier than the apparent death window and used technology to extend the victim's presence.

Effect:

-   Final confrontation/reveal unlocks.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 7. Player Systems And UI

## Fragment Requirement

Android Fragments are required for this project.

Use fragments meaningfully, but not for every small UI panel.

Recommended fragment usage:

-   `MainActivity` hosts `GameShellFragment`.
-   `GameShellFragment` hosts the world fragment and overlay fragments.
-   `WorldInvestigationFragment` contains the OpenGL `GameView`.
-   `HandheldFragment` contains evidence, threads, timeline, and statements screens using regular internal views/controllers.
-   `LaptopFragment` is a popup/dialog fragment for the stylized victim laptop.

Avoid making every handheld tab its own fragment unless there is a concrete lifecycle reason.

Do not put the case logic directly inside fragments.

Fragments should:

-   inflate UI,
-   display current state,
-   receive Android UI input,
-   call controller/system classes.

Gameplay systems should:

-   own evidence state,
-   own thread progression,
-   own timeline state,
-   open authored laptop panels,
-   save/load progress.

Views are still important.

Use Android Views inside fragments for:

-   handheld tabs,
-   evidence lists,
-   thread panels,
-   timeline cards,
-   suspect statement cards,
-   laptop screen text and panel buttons,
-   buttons around the OpenGL surface.

Recommended split:

-   Fragments = lifecycle and container structure.
-   Views = actual UI widgets.
-   Controllers/view binders = UI state and widget wiring.
-   Gameplay systems = case logic and progression.

## Core Screens

Major screens:

-   World Investigation View.
-   Handheld Device Main Menu.
-   Evidence Database.
-   Investigation Threads Screen.
-   Timeline Screen.
-   Suspect Statements Screen.
-   Final Timeline Ordering Screen.
-   Stylized Laptop Popup.

Possible later screen:

-   Reconstruction screen.

For MVP, reconstructions should remain lightweight and should not become a major implementation risk.

## World Investigation View

Purpose:

-   apartment interaction,
-   fixed-view navigation,
-   hotspot inspection,
-   evidence discovery.

Interaction style:

-   point-and-click / tap-to-inspect,
-   influenced by Cube Escape style room interaction,
-   no free movement FPS gameplay.

Player should be able to:

-   move between fixed apartment views,
-   tap hotspots,
-   collect evidence,
-   open the handheld device,
-   revisit already discovered evidence.

## Handheld Device

The handheld is the main analysis surface.

It should contain:

-   Evidence database.
-   Investigation threads.
-   Timeline screen.
-   Suspect statements.
-   Final timeline ordering.
-   Laptop access point.

Visual direction:

-   grounded DS/Gameboy-like device layer,
-   retro-futuristic forensic aesthetic,
-   readable before stylish.

Priority order:

1.  Readability.
2.  Clear categories.
3.  Obvious new-item feedback.
4.  Light atmosphere.
5.  Visual polish.

Avoid spending too much time on:

-   complex animations,
-   draggable windows,
-   deeply nested menus,
-   decorative UI that makes text hard to read.

## Victim Laptop

The victim laptop is a major mechanic.

It should be presented as:

-   a stylized popup on the phone,
-   an Android `DialogFragment`,
-   connected to the handheld/device layer,
-   a small set of authored panels (Messages, Activity, Files, Draft), not a command line or shell.

The laptop can be used to:

-   open recovered messages,
-   review device/session activity,
-   open recovered files and the draft email,
-   reveal metadata,
-   unlock evidence,
-   update investigation threads.

The laptop should not:

-   accept typed commands,
-   simulate a shell or run real device operations,
-   require real cybersecurity expertise,
-   replace the evidence/thread/timeline structure.

Interaction model:

-   the player taps a panel button,
-   the panel opens its authored content on the laptop screen,
-   opening a panel collects the linked evidence,
-   a Clear button returns the screen to the laptop's starting view.

Implementation split:

-   `LaptopFragment` owns the popup UI.
-   `LaptopSession` owns the visible laptop screen state.
-   `LaptopPanelProcessor` opens authored panels and returns their content.
-   `GameController` receives any evidence/thread/timeline unlocks caused by opening panels.

## Evidence Database

Stores:

-   physical evidence,
-   digital evidence,
-   testimonial evidence,
-   analytical observations.

Evidence categories:

-   Physical.
-   Digital.
-   Testimonial.
-   Analytical.

Each evidence entry should show:

-   title,
-   category,
-   source,
-   player-facing summary,
-   simplified technical note,
-   linked thread if relevant.

## Timeline Screen

Timeline philosophy:

-   investigation-centric,
-   not full minute-by-minute simulation.

Timeline should focus on:

-   unresolved chronology,
-   suspicious activity,
-   contradictions,
-   uncertain intervals.

Precision model:

-   physical evidence is broad or estimated,
-   human statements are approximate,
-   machine evidence is precise,
-   machine evidence can still mislead.

The timeline should help players compare:

-   approximate death window,
-   precise device logs,
-   suspect access events,
-   staged post-death activity.

## Suspect Statements

Use short statement cards or calls.

Do not implement branching interviews for MVP.

Each suspect needs:

-   1 short initial statement,
-   1 contradiction or suspicious detail,
-   1 resolution.

### Wife Statement

Initial claim:

> I checked the system because he was ignoring me. I did not go there.

Suspicious detail:

-   She hides the severity of the argument.
-   She does not immediately admit how much she knew about the illegal operation.

Resolution:

-   Her access is real, but it occurs after the true murder and does not explain the staging.

### Security / Staff Statement

Initial claim:

> The camera issue was maintenance. I checked on him because the system flagged irregular activity.

Suspicious detail:

-   They disliked the victim.
-   The outage timing looks convenient.

Resolution:

-   The outage complicates the record but does not align with the actual murder sequence.

### Business Partner Statement

Initial claim:

> We argued, but he was alive when I left. He was already back at his laptop.

Suspicious detail:

-   His statement depends on the victim being alive after the meeting.
-   His technical familiarity matches the staging method.

Resolution:

-   His statement collapses once post-meeting digital activity is shown to be artificial.

## Handler Prompts

The handler should guide lightly.

The handler should not feel like:

-   a quest marker system,
-   a tutorial voice,
-   a replacement for player reasoning.

Use 3-5 short prompts.

Possible prompts:

-   "Body condition and apartment temperature are not lining up. Check the environmental logs."
-   "The message helps the timeline, but only if it was sent manually."
-   "Remote access alone does not prove presence. Look for who could automate the sequence."
-   "The access-data file gives motive. Now test it against the timeline."
-   "The late activity is staged. Rebuild the sequence from the earliest confirmed event."

## Final Player Action

The finale is a timeline-ordering sequence.

The player orders these events:

1.  Partner arrives.
2.  Argument escalates.
3.  Murder occurs.
4.  AC/smart-home manipulation begins.
5.  Partner exits.
6.  Wife remote access.
7.  CCTV outage.
8.  Scheduled message sends.
9.  Body discovered.

Optional if implementation allows:

-   Include "automated laptop activity continues" as a 10th event.

The final timeline should not require exact minute memorization.

The player should understand relative order:

-   murder before staged activity,
-   staging before partner exit,
-   partner exit before wife/security suspicious events,
-   scheduled message after death,
-   body discovery last.

## Suggested Data Records For Later Implementation

These are design structures, not required Java schemas yet.

### Evidence Record

-   `id`
-   `title`
-   `category`
-   `source_view`
-   `unlock_condition`
-   `thread`
-   `player_text`
-   `technical_note`

### Timeline Event Record

-   `id`
-   `display_time`
-   `truth_time`
-   `certainty`
-   `source_evidence`
-   `initial_interpretation`
-   `resolved_interpretation`

### Investigation Thread Record

-   `id`
-   `name`
-   `observations`
-   `unlock_triggers`
-   `stabilized_conclusion`

### Statement Record

-   `speaker`
-   `claim`
-   `unlock_condition`
-   `contradicted_by`
-   `resolution`

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 8. Art, Audio, And Presentation

## Visual Direction

High-level style:

-   retro-futuristic forensic aesthetic,
-   CRT displays,
-   scanlines,
-   analog distortion,
-   glitch artifacts,
-   wireframe reconstructions,
-   low-resolution forensic simulations.

Avoid:

-   sleek sci-fi holograms,
-   overly glossy UI,
-   unreadable decorative effects,
-   excessive visual noise.

## UI Visual Language

The interface should feel:

-   functional,
-   slightly worn,
-   technical,
-   subdued,
-   readable.

Use visual polish only after the case flow works.

Prioritize:

-   clear text,
-   obvious tabs/categories,
-   strong contrast,
-   consistent evidence icons,
-   simple new-item indicators.

## Reconstruction Presentation

Reconstruction sequences are:

-   atmospheric,
-   lightweight,
-   contextual,
-   supportive.

They are not major puzzle pillars for MVP.

Possible lightweight reconstruction types:

-   timeline alignment,
-   smart-home sequence ordering,
-   metadata consistency check,
-   CCTV continuity repair,
-   audio fragment ordering.

For MVP, prefer final timeline ordering as the main reconstruction-like interaction.

## Audio Direction

Audio should support a quiet forensic mood.

Use:

-   ambient electronic soundscapes,
-   degraded recordings,
-   static interference,
-   mechanical UI sounds,
-   low-pressure investigation ambience.

MVP audio priorities:

1.  Ambient background loop.
2.  Evidence discovery sound.
3.  Thread update sound.
4.  Timeline stabilization sound.
5.  Optional subtle glitch/static effects.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 9. Production Plan

## Team Division

### Developer 1 - Systems / UI

Primary responsibility:

-   handheld interface,
-   evidence database,
-   thread progression,
-   timeline system,
-   save/load.

### Developer 2 - Gameplay / World

Primary responsibility:

-   apartment interaction,
-   fixed-view navigation,
-   hotspot system,
-   evidence collection,
-   final timeline ordering.

### Developer 3 - Content / Presentation

Primary responsibility:

-   evidence writing,
-   statements,
-   audio,
-   placeholder art,
-   visual polish,
-   environmental storytelling.

All developers:

-   integration,
-   bug fixing,
-   playtesting,
-   scope cuts.

## Suggested Production Order

1.  Write final evidence text and thread updates.
2.  Build a paper prototype of the full case flow.
3.  Create placeholder apartment views.
4.  Implement fixed-view navigation and hotspots.
5.  Implement evidence collection.
6.  Implement handheld evidence database.
7.  Implement investigation thread updates.
8.  Implement suspect statements.
9.  Implement timeline screen.
10. Implement final timeline-ordering sequence.
11. Add save/load.
12. Add art/audio polish.
13. Playtest and cut anything unclear or slow.

## Main Production Risks

### Risk 1 - Overcomplicated Deduction Logic

Danger:

-   trying to support too many valid reasoning paths.

Avoid by:

-   controlled thread progression,
-   curated contradictions,
-   limited manual deductions.

### Risk 2 - UI Complexity

Danger:

-   spending too much time on windows, transitions, data management, and navigation flow.

Avoid by:

-   making the handheld simple,
-   prioritizing text clarity,
-   limiting screen count,
-   adding polish after the full loop works.

### Risk 3 - Content Explosion

Danger:

-   too many evidence items,
-   too many logs,
-   too many messages,
-   too much dialogue.

Avoid by:

-   keeping 13-15 evidence items,
-   using short statements,
-   making every clue serve the core timeline reversal.

### Risk 4 - Reconstruction Scope Creep

Danger:

-   turning reconstructions into expensive custom puzzle systems.

Avoid by:

-   using final timeline ordering as the main reconstruction,
-   adding optional reconstruction only if the main flow is complete.

### Risk 5 - Environment Scope

Danger:

-   adding too many apartment views or locations.

Avoid by:

-   keeping 4 core fixed views,
-   treating the 5th view as optional,
-   folding optional evidence into existing views when needed.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 10. Manual To-Do List

This list is for manual implementation and production tracking.

## Design Tasks

-   [ ] Write a full case document using the true timeline above.
-   [ ] Finalize names for the victim, wife, business partner, and security/staff suspect.
-   [ ] Decide whether the optional fifth apartment view is included.
-   [ ] Finalize the 4 required apartment views.
-   [ ] Decide the exact location of each evidence item.
-   [ ] Write final text for all 13-15 evidence items.
-   [ ] Write one short statement for each suspect.
-   [ ] Write 3-5 handler prompts.
-   [ ] Define which evidence unlocks each investigation thread update.
-   [ ] Define when each suspect statement unlocks.
-   [ ] Define the exact final timeline-ordering event list.
-   [ ] Decide whether laptop activity is included as a separate final timeline event.
-   [ ] Write the final reveal sequence.
-   [ ] Write the short ending text.

## Content Tasks

-   [ ] Write the living room/body scene description.
-   [ ] Write the entry/smart-lock view description.
-   [ ] Write the kitchen/side-area view description.
-   [ ] Write the office/device-corner view description.
-   [ ] Write the optional CCTV/window/balcony view description if included.
-   [ ] Create a hotspot list for each apartment view.
-   [ ] Write player-facing evidence text for each evidence item.
-   [ ] Write simplified technical notes for each digital evidence item.
-   [ ] Write observation text for each thread update.
-   [ ] Write stabilized conclusion text for each thread.
-   [ ] Write wife statement text.
-   [ ] Write security/staff statement text.
-   [ ] Write business partner statement text.
-   [ ] Write final accusation/reveal text.
-   [ ] Check that all technical language is understandable without cybersecurity expertise.

## UI / UX Tasks

-   [ ] Sketch the world investigation screen.
-   [ ] Sketch fixed-view apartment navigation.
-   [ ] Sketch hotspot feedback.
-   [ ] Sketch evidence discovery feedback.
-   [ ] Sketch handheld main menu.
-   [ ] Sketch evidence database screen.
-   [ ] Sketch investigation threads screen.
-   [ ] Sketch timeline screen.
-   [ ] Sketch suspect statement screen.
-   [ ] Sketch final timeline-ordering interaction.
-   [ ] Decide how new evidence is marked in the handheld.
-   [ ] Decide how thread updates are shown.
-   [ ] Decide how handler prompts appear without feeling intrusive.
-   [ ] Decide how completed/stabilized threads are visually distinguished.
-   [ ] Confirm all text areas are readable on a phone screen.

## Code Planning Tasks

-   [ ] Decide whether MVP content is hardcoded first or loaded from JSON assets.
-   [ ] Define Java data models for evidence.
-   [ ] Define Java data models for timeline events.
-   [ ] Define Java data models for investigation threads.
-   [ ] Define Java data models for suspect statements.
-   [ ] Implement fixed-view apartment navigation.
-   [ ] Implement hotspot detection.
-   [ ] Implement evidence collection.
-   [ ] Implement handheld evidence database.
-   [ ] Implement thread progress tracking.
-   [ ] Implement suspect statement unlocks.
-   [ ] Implement simple timeline screen.
-   [ ] Implement final timeline-ordering sequence.
-   [ ] Implement save/load for discovered evidence.
-   [ ] Implement save/load for thread states.
-   [ ] Implement save/load for final timeline completion.
-   [ ] Keep reconstruction mechanics lightweight unless core flow is already complete.

## Art / Audio Tasks

-   [ ] Produce placeholder art for the living room/body scene.
-   [ ] Produce placeholder art for the entry/smart-lock view.
-   [ ] Produce placeholder art for the kitchen/side-area view.
-   [ ] Produce placeholder art for the office/device-corner view.
-   [ ] Produce placeholder art for the optional fifth view if included.
-   [ ] Mark hotspot regions on each apartment view.
-   [ ] Create a basic handheld UI frame.
-   [ ] Create simple evidence category icons.
-   [ ] Create a simple thread update visual treatment.
-   [ ] Add ambient background loop.
-   [ ] Add evidence discovery sound.
-   [ ] Add thread update sound.
-   [ ] Add timeline stabilization sound.
-   [ ] Add glitch/scanline treatment only after the full case flow works.

## Testing Tasks

-   [ ] Paper-test the clue chain before coding the full flow.
-   [ ] Confirm a player can finish in 15-20 minutes.
-   [ ] Confirm the AC/body contradiction is understandable.
-   [ ] Confirm the scheduled message clue is understandable.
-   [ ] Confirm the laptop automation clue is understandable.
-   [ ] Confirm wife is suspicious for a legitimate reason.
-   [ ] Confirm security/staff is suspicious for a legitimate reason.
-   [ ] Confirm the partner reveal is supported by motive, access, and timing.
-   [ ] Confirm the final timeline can be solved from discovered evidence.
-   [ ] Confirm no cybersecurity knowledge is required beyond reading clear labels.
-   [ ] Confirm there are no softlocks in evidence collection.
-   [ ] Confirm the player always has a reasonable next step.
-   [ ] Confirm the final reveal tone feels tragic rather than triumphant.

<div class="page-break" style="page-break-before: always; break-before: page;"></div>

# 11. MVP Acceptance Checklist

The MVP is acceptable when:

-   [ ] The game contains one complete murder case.
-   [ ] The apartment has 4 working core views.
-   [ ] The player can collect all required evidence.
-   [ ] The handheld shows evidence, threads, statements, and timeline.
-   [ ] The death window contradiction is clear.
-   [ ] Post-death digital activity is clearly revealed as artificial.
-   [ ] The partner's motive is understandable.
-   [ ] The wife and security/staff are suspicious but fairly resolved.
-   [ ] The final timeline ordering works.
-   [ ] The case can be completed in 15-20 minutes.
-   [ ] The ending has a quiet tragic tone.

## Open Design Items

These still need final decisions or content writing:

-   final game title,
-   character names,
-   exact apartment layout drawings,
-   final hotspot positions,
-   final evidence text,
-   final suspect statement text,
-   exact thread update text,
-   final reveal writing,
-   visual style guide,
-   audio asset list,
-   save/progression implementation details.

## Scope Guardrails

Protect:

-   one coherent case,
-   clear clue chain,
-   readable evidence,
-   strong timeline reversal,
-   simple handheld flow,
-   tragic ending tone.

Cut first if time gets tight:

1.  Optional 5th apartment view.
2.  Extra reconstruction sequence.
3.  Decorative handheld animation.
4.  Extra evidence flavor text.
5.  Non-essential audio polish.

Do not cut:

-   AC/body contradiction,
-   scheduled message,
-   laptop/session automation,
-   smart-lock access log,
-   access-data motive,
-   final timeline ordering.
