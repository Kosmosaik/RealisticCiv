# Quest Book and Progression UI

## Purpose

The Quest Book is the player's primary **progression guidance interface**.

Its job is simple:

> **Show what the player can work toward, how progression connects, why something is blocked, and where to go next when they feel lost.**

It should feel familiar to players of large expert-style Minecraft modpacks: many chapters, large node graphs, meaningful cross-chapter dependencies, and the freedom to focus on whichever currently available branch is most interesting.

The Quest Book is **not** intended to be a catch-all statistics, achievement, history, or completionist screen.

Those systems may exist later in a separate **Civilization Record** interface.

---

# Core Design Principles

## 1. Chapters Represent Fields of Progress

Progression is divided into subject-oriented chapters rather than one strictly linear era tree.

Possible chapters include:

```text
Bare-Handed Survival
Primitive Tools
Fire & Heat
Water
Food & Preservation
Shelter
Woodworking
Construction
Pottery & Ceramics
Agriculture
Animal Husbandry
Textiles
Leatherworking
Mining & Geology
Metallurgy
Mechanical Engineering
Power & Energy
Transportation
Chemistry
Medicine
Electricity
Electronics
Computing
Automation
Aerospace
Space Industry
Advanced Technology
```

A chapter can span multiple historical eras.

For example, **Woodworking** may begin with shaping branches using a stone flake and eventually contain powered sawmills, engineered timber, and CNC woodworking.

Likewise, **Metallurgy** may begin with native copper and continue through iron, steel, alloy steels, aluminium, titanium, superalloys, and advanced metallurgical processes.

Historical eras remain useful as broad labels and civilization-scale milestones, but they should not be the primary Quest Book structure.

---

## 2. Players May Focus on Any Available Chapter

The Quest Book should not force the player to complete chapters in a prescribed order.

If several chapters are available, the player should be free to focus on any of them and progress as far as current prerequisites allow.

Example:

```text
Woodworking
    ↓
Primitive Joinery
    ↓
Better Handles
    ↓
Sawing
    ↓
Metal Saw Blade
    🔒
Requires progress in Metallurgy
```

The player can push deeply into Woodworking until a real dependency requires another field.

At that point the blocked node should clearly identify the required quest or capability and allow the player to navigate directly to it.

This creates the intended expert-pack rhythm:

```text
Choose a branch
      ↓
Progress freely
      ↓
Reach a meaningful gate
      ↓
Follow prerequisite into another chapter
      ↓
Develop that field
      ↓
Return and continue
```

---

# Cross-Chapter Gating

Cross-chapter gates are one of the core progression mechanics.

A gate should exist because the underlying process actually depends on another technology, material, tool, skill, or infrastructure capability.

Avoid arbitrary requirements such as:

```text
Complete 80% of Chapter 4
```

Prefer real dependencies such as:

```text
Copper Smelting

Requires:
✓ Controlled Fire
✓ Charcoal Production
✓ Prepared Copper Ore
✗ Fired Crucible

Fired Crucible:
Pottery & Ceramics → Refractory Vessels
```

Every prerequisite shown in the Quest Book should be clickable when possible.

The player should be able to jump from:

```text
Steam Engine
→ Precision Boring
→ Metal Lathe
→ Hardened Steel
```

without manually searching through unrelated chapters.

Breadcrumb/back navigation should make it easy to return to the original goal.

---

# Quest Nodes

The player-facing Quest Book should avoid excessive quest-type labels such as "Discovery", "Milestone", "Production", or "Demonstration".

Internally, nodes may use different completion-condition types, but to the player they are simply **progression objectives**.

Examples:

```text
Find Flint or Another Knappable Material
Knapp a Sharp Flake
Produce Primitive Cordage
Assemble a Hafted Stone Axe
Establish Controlled Fire
Fire a Clay Vessel
Produce Charcoal
Smelt Copper from Ore
Construct a Bloomery
Produce an Iron Bloom
Build a Water Wheel
Machine a Precision Cylinder
Generate Electricity
```

Each quest should represent a meaningful new capability, process, concept, or step in a production chain.

Avoid filler such as:

```text
Collect 64 Fibre Items
Craft 32 Sticks
Kill 20 Animals
```

unless the quantity itself demonstrates a meaningful production capability.

The goal is not to maximize quest count. A large quest count should emerge naturally from the depth of the civilization progression.

---

# Quest Completion and the Simulation

The Quest Book should **reflect the real progression systems**, not replace them.

A quest is best understood as a visible wrapper around one or more underlying conditions.

Examples:

```text
Quest: Assemble a Stone Axe

Completion condition:
A valid stone axe assembly operation has been completed.
```

```text
Quest: Establish Copper Smelting

Completion conditions:
- settlement/player has required knowledge,
- copper ore has been successfully processed,
- a valid copper-smelting operation has completed.
```

The Quest Book should not become a second independent technology system where clicking "Claim" is what makes the world recognize progress.

Whenever practical, completion should be detected automatically from server-authoritative simulation state.

This avoids situations such as:

> The player can physically produce steel but cannot continue because they forgot to click a quest reward button.

---

# Quest Rewards

Traditional material quest rewards should generally be avoided.

Do not routinely give:

```text
8 Iron Ingots
4 Diamonds
A Free Machine
```

because these rewards can bypass the realistic production systems the mod is built around.

The normal reward for progression is:

- new capability,
- access to new processes,
- new branches becoming available,
- knowledge gained through the underlying system,
- clearer progression options.

Optional non-progression rewards may later include:

- cosmetic badges,
- small experience rewards,
- Civilization Record entries,
- UI cosmetics,
- harmless prestige markers.

No reward should undermine the material economy or skip an intended production chain.

---

# Quest Visibility Modes

Players should be able to choose how much locked progression the Quest Book reveals.

This is a **client/player preference**. It does not change the actual progression state or server rules.

Recommended modes:

## Available Only

Show:

- completed quests,
- currently available quests,
- quests already in progress where applicable.

Hide:

- locked future quests,
- locked branches beyond current progression.

This is the spoiler-light mode and gives the strongest sense of discovery.

Conceptually:

```text
✓ Completed
● Available

Locked nodes are not shown.
```

---

## Unlocked Chapters

Show all nodes belonging to chapters the player/settlement has currently revealed, including locked nodes inside those chapters.

Locked quests remain visible with prerequisite information.

Example:

```text
✓ Primitive Joinery
● Advanced Joinery
🔒 Metal Sawing Tools
🔒 Powered Sawmilling
```

This is likely a good default for players who want expert-pack-style planning without seeing the entire future technology tree immediately.

---

## Show All

Show the full defined Quest Book progression, including future chapters and locked quests.

This mode is intended for players who enjoy planning very far ahead and do not care about progression spoilers.

Example:

```text
Primitive Tools
Pottery
Metallurgy
Steam Power
Electricity
Electronics
Computing
Aerospace
Space Industry
Advanced Technology
```

Everything may be browsed, but unavailable nodes remain clearly locked and their real prerequisites still apply.

If the project later contains intentionally secret technologies, easter eggs, or discovery-only branches, those may remain hidden behind a separate secret-content rule rather than being automatically exposed by this setting.

---

# Quest States

The UI should keep states simple and immediately readable.

Recommended states:

```text
COMPLETED
AVAILABLE
IN_PROGRESS       optional where useful
LOCKED
HIDDEN            visibility/filter state rather than progression state
```

Locked quests should explain **why** they are locked.

Example:

```text
STEAM ENGINE — LOCKED

Missing prerequisites:

Metallurgy
✓ Iron Production
✗ Reliable Steel Production

Machining
✓ Drilling
✗ Precision Boring
✗ Metal Lathe

Mechanical Engineering
✓ Pistons
✓ Valves
✗ Pressure Vessel Construction
```

Each missing prerequisite should link to its relevant quest when possible.

---

# Chapter Front Pages

Each chapter should have a compact introduction explaining:

- what the field represents,
- why it matters,
- what capabilities it eventually enables,
- where the normal starting point is.

Example:

## Metallurgy

> Learn to identify, extract, refine, alloy, cast, forge, and machine metals. Metallurgy eventually enables stronger tools, machinery, transport, electrical systems, advanced structures, and modern industry.

The chapter graph might begin:

```text
Surface Minerals
      ↓
Ore Identification
      ↓
Ore Preparation
      ↓
Copper Metallurgy
      ↓
Bronze
      ↓
Iron
      ↓
Steel
      ↓
Advanced Alloys
```

Side branches can develop independently where appropriate.

---

# Recommended Next Progress

The Quest Book should include a small guidance section for players who specifically open it because they are lost.

Example:

```text
RECOMMENDED NEXT

Primitive Tools
• Assemble a hafted stone axe
  Enables reliable timber harvesting.

Fire & Heat
• Establish controlled fire
  Enables cooking and several material-processing branches.

Water
• Create a primitive water container
  Improves water transport and storage.
```

Recommendations should not force a route.

They should be derived from currently available quests and important blocked branches, ideally selecting only a few useful suggestions rather than presenting another giant list.

---

# Blocked Progress Guidance

A useful companion section may show major branches the player is currently trying to access or is close to accessing.

Example:

```text
BLOCKED PROGRESS

Woodworking
Metal Sawing Tools
→ Requires Basic Ironworking

Metallurgy
Copper Smelting
→ Requires Fired Crucible

Pottery
Kiln Construction
→ Requires Improved Fire Control
```

This is guidance, not an additional objective system.

---

# Dependency and Recipe Navigation

The Quest Book should integrate closely with the realistic production system.

From a quest or item/process entry, the player should eventually be able to navigate to related information such as:

- prerequisites,
- required tools,
- required workstation,
- relevant skills,
- relevant knowledge,
- input materials,
- output materials,
- upstream production chain,
- downstream uses.

Example:

```text
STEEL PLATE

Possible routes:

Route A — Hand Forging
Iron/Steel Billet
→ Reheat
→ Hammer/Forge
→ Plate

Route B — Powered Hammer
Billet
→ Powered Forging
→ Plate

Route C — Rolling Mill
Billet/Slab
→ Rolling Mill
→ Standardized Plate
```

The Quest Book does not need to become the complete recipe browser itself, but cross-links should make the progression and recipe systems feel like one coherent interface.

---

# Multiplayer and Settlement Progress

Quest progression should be compatible with cooperative settlements from the beginning.

Many progression quests will reflect **settlement-level capability** rather than requiring every player to repeat the same industrial milestones.

Example:

```text
River Camp

Copper Smelting: COMPLETE
```

If one authorized member of the settlement legitimately establishes copper smelting, other members should normally see that civilization-level progression as complete.

Personal skill remains separate.

Example:

```text
Settlement knows Copper Smelting.

Alexander — Smithing 4
Anna — Smithing 37
```

The settlement may therefore have access to a process even though not every player or settler can personally perform it efficiently.

Player-specific quests may exist where genuinely appropriate, but the Quest Book should avoid forcing every co-op player to repeat settlement infrastructure merely to clear their personal UI.

See `12_MULTIPLAYER_AND_KNOWLEDGE.md` for knowledge ownership and settlement sharing rules.

---

# Quest Book vs Civilization Record

These interfaces should remain conceptually separate.

## Quest Book

Purpose:

> **Where can we go next?**

Contains:

- progression chapters,
- quest graphs,
- available objectives,
- cross-chapter gates,
- prerequisite explanations,
- recommended next steps,
- progression navigation.

---

## Civilization Record — Later Feature

Purpose:

> **What have we accomplished?**

Potential contents:

- achievements,
- statistics,
- first discoveries,
- first successful production events,
- civilization history,
- settler records,
- profession mastery,
- production totals,
- historical dates,
- completionist goals.

Example:

```text
First controlled fire:
Day 1 — Alexander

First copper produced:
Day 38 — Anna

First steam engine:
Day 291

Highest Carpentry:
Erik — 74
```

This may eventually provide considerable visual flavor and historical identity, but it should not clutter the Quest Book or be required for normal progression.

---

# Visual Structure and Layout Direction

The Quest Book should lean much more toward a **classic expert-modpack node canvas** than a linear checklist or card feed.

The current structural reference is the kind of dense, dependency-driven layout seen in expert packs such as GregTech: New Horizons.

The project should not copy another mod's art or UI assets, but the following structural ideas are intentionally adopted:

- persistent chapter list/sidebar,
- large pannable/zoomable quest canvas,
- obvious dependency lines between nodes,
- compact minor quests,
- visually dominant major progression quests,
- branching and converging dependency routes,
- immediately readable completed/available/locked states,
- selected-quest detail panel or equivalent focused information view.

See the concept/reference images under `references/`.

---

## Major vs Minor Quests

Not every progression node should have equal visual weight.

### Minor Quest

Represents a smaller enabling step such as:

```text
Prepare Grass / Fibre
Knapp Stone Flake
Shape Wooden Haft
Make Clay Tuyere
Forge Nails
```

Minor nodes should remain compact so large graphs can show many dependencies without becoming visually exhausting.

### Major Quest

Represents a major capability shift, chapter landmark, or technological breakthrough such as:

```text
Stone Axe
Controlled Fire
Pottery Firing
Copper Smelting
Bloomery Iron
Water Power
Steam Engine
Electric Generation
Internal Combustion
Integrated Circuits
Orbital Flight
```

Major nodes should be visually larger or more strongly framed so a player can glance at a dense chapter and immediately understand the important progression landmarks.

The distinction is primarily **presentation and navigation**, not a separate reward economy.

---

## Dependency Lines

Lines between nodes are essential information, not decoration.

They should communicate:

- direct prerequisite relationships,
- convergence of several requirements into one major quest,
- branches opened by a completed capability,
- cross-chapter dependencies where practical.

A dense chapter may contain many lines, so readability matters.

Potential visual conventions may include:

```text
completed dependency    → strong/bright line
available dependency    → normal highlighted line
locked dependency       → muted/dim line
cross-chapter link      → distinct marker/portal/edge link
```

Exact colors are not locked yet and should be tested for readability and accessibility.

Avoid decorative connections that do not correspond to actual progression relationships.

---

## Canvas Behavior

The central graph should support enough navigation for very large late-game chapters.

Desired capabilities:

- pan/drag,
- zoom,
- focus selected quest,
- jump to prerequisite,
- jump back to originating quest,
- search,
- optionally center on next recommended quest,
- remember the player's last view per chapter where practical.

A chapter with dozens or hundreds of nodes should feel explorable rather than squeezed into one static screen.

---

## Chapter Sidebar

The chapter list should remain quickly accessible while browsing the graph.

A chapter entry may indicate:

- icon,
- title,
- whether it is newly available,
- broad completion/progress indication if useful,
- locked/unrevealed state according to the player's visibility mode.

Avoid turning the sidebar into a second statistics dashboard.

Its primary job is navigation.

---

## Selected Quest Details

Selecting a quest should expose only the information needed to understand and pursue it.

Possible information:

```text
Title
Short explanation
Current state
Completion requirement
Missing prerequisites
What important branches it unlocks
Track / focus controls
Navigation to prerequisite
```

Detailed material recipes may link to a future recipe/process browser rather than forcing every manufacturing detail into the quest panel.

---

## Visual References

The current documentation bundle contains:

```text
references/gtnh_quest_book_reference.png
references/quest_book_early_concept.png
references/quest_book_midgame_metallurgy_concept.png
```

`gtnh_quest_book_reference.png` is a user-provided structural reference and is **not a distributable project asset**.

The two project concept images are visual direction only. They do not freeze exact fonts, colors, dimensions, icons, or layout proportions.

The implementation should preserve the underlying goals even if the final Minecraft UI differs substantially:

> **Dense but readable progression, clear major/minor hierarchy, and dependencies visible at a glance.**

---

# Data-Driven Requirements

Quest chapters and nodes should be data-driven wherever practical.

Conceptual chapter definition:

```json
{
  "id": "realisticciv:woodworking",
  "title": "Woodworking",
  "description": "...",
  "icon": "...",
  "visibility": "..."
}
```

Conceptual quest definition:

```json
{
  "id": "realisticciv:stone_axe",
  "chapter": "realisticciv:primitive_tools",
  "title": "A Hafted Stone Axe",
  "description": "...",
  "parents": [
    "realisticciv:cordage",
    "realisticciv:stone_axe_head",
    "realisticciv:wooden_haft"
  ],
  "completion": {
    "type": "operation_completed",
    "operation": "realisticciv:assemble_stone_axe"
  }
}
```

The final schema will depend on implementation details, but adding or reorganizing large amounts of progression content should not require editing Java classes for every quest.

---

# Technical Authority

Quest completion and unlock conditions that affect progression must be evaluated by the server.

The client may:

- render the graph,
- apply the player's visibility preference,
- navigate/search/filter,
- display synchronized completion/lock state.

The client must not decide that a quest is completed or unlock a capability.

This preserves multiplayer correctness and prevents progression desynchronization or trivial client-side cheating.

---

# Initial Implementation Scope

The full Quest Book does not need to exist before the first primitive gameplay prototype.

The first implementation should validate the architecture with a very small set of chapters, for example:

```text
Primitive Tools
Fire & Heat
Woodworking
```

The first graph can contain only enough quests to guide:

```text
Ground Resources
→ Knapping
→ Cutting Edge
→ Long Grass / viable fibre source
→ Prepared Fibre Material
→ Cordage
→ Hafted Stone Axe
→ Timber
```

Requirements for the first Quest Book implementation:

- chapter tabs/list,
- node graph,
- completed/available/locked states,
- prerequisite links,
- automatic server-side completion,
- quest visibility preference,
- basic recommended-next guidance,
- data-driven chapter/node definitions.

The same framework can then scale to hundreds or thousands of progression nodes without redesigning the fundamental UI model.

The fibre branch must follow the source-identified design in `22_FIBRES_CORDAGE_AND_TEXTILES.md`; the Quest Book should not imply that `Plant Fibre` is a generic naturally harvested resource.
