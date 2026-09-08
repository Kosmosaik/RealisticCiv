# Implementation Roadmap

## Roadmap Philosophy

The project is too large to build horizontally.

Every milestone should create a **small playable vertical slice** containing enough of the full architecture to validate the design.

Do not start by adding hundreds of recipes.

---

# Phase 0 — Project Foundation

## Goals

- verify current official Minecraft/Fabric/Java compatibility,
- create Fabric project using `16_DEVELOPMENT_ENVIRONMENT_SETUP.md`,
- pin the selected Minecraft/Fabric/Java versions for the milestone,
- establish package structure,
- establish data-loading architecture,
- establish validation framework,
- create developer test world/configuration,
- verify both integrated-client and dedicated-server launch paths,
- establish server-authoritative request/response conventions,
- embed the documentation set in the source project,
- establish `CHANGELOG.md` and maintain `14_CURRENT_STATE_AND_HANDOFF.md` for future handoffs.

## Deliverable

The clean mod project builds and launches on both client and dedicated server with basic registry/data validation, version control initialized, documentation included, and a current-state handoff ready for the next milestone.

---

# Phase 1 — Break the Vanilla Bootstrap

## Goals

- prevent bare-hand log harvesting,
- disable vanilla log-to-plank recipe,
- disable vanilla stick bootstrap,
- disable wooden/stone vanilla tool recipes,
- restrict crafting table progression,
- prevent obvious furnace bypasses.

## Deliverable

A new world can no longer reach normal Minecraft progression through the first few vanilla recipes.

---

# Phase 2 — Ground Resource System

## Goals

Add naturally distributed collectible resources:

- branches,
- twigs,
- loose stones,
- tool stone,
- dry grass/fibre.

Systems:

- biome/environment distribution,
- pickup interaction,
- respawn/regeneration strategy,
- resource models/textures.

## Deliverable

Player can explore and collect the raw materials necessary for primitive survival.

---

# Phase 3 — Primitive Toolmaking

## Goals

- hammerstone,
- knappable stone,
- stone flakes,
- primitive cutting tool,
- prepared fibre,
- cordage,
- haft components,
- primitive axe.

Introduce:

- hand-crafting reinterpretation of the inventory 2x2 grid,
- explicit Craft button instead of instant output pickup,
- generic server-side WorkAction system,
- timed crafting/progress HUD,
- input reservation,
- interruption/cancellation foundation,
- simple process/operation registry,
- tool capabilities,
- hand crafting restrictions,
- hooks for later skill/waste/quality calculations without requiring the full skill system yet.

## Deliverable

Player starts with nothing and can legitimately progress to a primitive axe without vanilla recipes, with each meaningful hand-crafted operation taking server-authoritative work time.

---

# Phase 3B — Quest Book Foundation

## Goals

Implement the first small version of the expert-pack-style progression interface:

- data-driven quest chapters and quest nodes,
- a graph view for progression,
- completed/available/locked states,
- automatic server-authoritative completion conditions,
- cross-chapter prerequisite links,
- clickable dependency navigation,
- player visibility preference:
  - Available Only,
  - Unlocked Chapters,
  - Show All,
- a small Recommended Next section.

Initial chapters only need to cover enough content to guide the primitive bootstrap, for example:

```text
Primitive Tools
Fire & Heat
Woodworking
```

Do not add achievements/statistics/history to this UI. Those belong to a later Civilization Record system.

## Deliverable

A player who becomes lost during the primitive bootstrap can open the Quest Book, see the currently valid progression routes, understand why a blocked quest is unavailable, jump to its prerequisite, and choose whether locked future quests are shown.

---

# Phase 4 — Primitive Wood Harvesting

## Goals

- small-tree harvesting,
- log acquisition,
- branch processing,
- split wood,
- primitive timber processing,
- first primitive work surface.

Optional later-in-phase:

- whole-tree felling behavior.

## Deliverable

The first sustainable woodworking loop.

---

# Phase 5 — Needs and Survival

## Goals

Player:

- thirst,
- hunger tuning,
- basic energy/stamina if desired,
- water sources,
- drinking,
- unsafe/clean water foundation.

Add:

- primitive fire,
- basic food cooking,
- basic shelter/sleep solution.

## Deliverable

Primitive survival becomes a coherent playable loop rather than only a crafting demo.

---

# Phase 6 — First Settler Prototype

## Goals

Create one custom Settler entity with:

- persistent identity,
- health,
- hunger,
- thirst,
- energy,
- basic inventory,
- basic UI panel.

No complex profession system yet.

## Deliverable

A persistent simulated settler exists and survives basic needs.

---

# Phase 7 — Jobs, Stockpiles, and Hauling

## Goals

- primitive stockpile,
- settlement manager,
- resource index,
- settler task selection,
- hauling job,
- reservations,
- work order prototype.

Initial job:

`Gather branches and place them in stockpile.`

## Deliverable

A settler autonomously gathers a real resource and delivers it to settlement storage.

---

# Phase 8 — Settler Crafting

## Goals

- primitive workstation,
- production work orders,
- settler input retrieval,
- settler execution through the same WorkAction/operation definitions used by players,
- crafting duration,
- output delivery,
- failure/cancellation handling.

Example:

`Produce 10 Wooden Pegs.`

## Deliverable

A settler can complete a multi-step production job using stocked materials.

---

# Phase 9 — Skills and Professions

## Goals

- skill registry,
- skill XP,
- profession registry,
- profession assignment,
- skill-based work speed,
- skill-based recipe requirements.

## Deliverable

Players and settlers can perform the same operation with different duration, waste, failure risk, tool wear, and/or quality based on competence.

---

# Phase 9B — Knowledge Foundation

## Goals

- shared KnowledgeHolder abstraction,
- player personal knowledge,
- settler personal knowledge,
- settlement-established knowledge,
- basic teaching/knowledge-transfer hook,
- persistence and multiplayer synchronization.

Full schools, books, observation learning, and knowledge loss remain later features.

## Deliverable

Knowledge can belong to different actors/scopes without being implemented as one global unlock list.

---

# Phase 10 — Primitive Settlement Gameplay

## Goals

Build the first complete mini-era:

- fire,
- shelter,
- primitive hunting/foraging,
- woodworking,
- storage,
- basic clothing/materials if useful,
- several settler roles,
- primitive technology unlocks.

## Deliverable

A satisfying primitive settlement game that could stand alone as an early-access slice.

---

# Phase 11 — Agriculture, Pottery, and Permanent Settlement

## Goals

- farming progression,
- soil/crop systems as needed,
- food storage,
- pottery,
- kiln,
- containers,
- improved housing,
- textiles.

## Deliverable

Transition from survival camp to permanent settlement.

---

# Phase 12 — Copper and Early Metallurgy

## Goals

- ore resource model,
- ore processing,
- charcoal production,
- crucibles,
- furnace temperature capability,
- copper smelting,
- casting,
- metal tools.

## Deliverable

First complete realistic metallurgy chain.

---

# Phase 13 — Bronze and Iron

## Goals

- alloying,
- tin supply,
- bronze,
- bloomery iron,
- forging,
- improved metalworking,
- blacksmith profession.

## Deliverable

Large expansion of tool capability and settlement specialization.

---

# Phase 14 — Mechanical Power

## Goals

- rotational power network,
- water wheel,
- shafts,
- gears,
- mechanical transmission,
- sawmill/mill machinery.

## Deliverable

Settlement begins replacing human labor with environmental mechanical power.

---

# Phase 15 — Early Industrialization

## Goals

- steam power,
- machine tools,
- pumps,
- precision parts,
- mechanized mining,
- improved steelmaking.

## Deliverable

First true industrial settlement.

---

# Phase 16 — Electricity

## Goals

- generation,
- wiring/network model,
- motors,
- batteries,
- lighting,
- electrical machines.

## Deliverable

Mechanical infrastructure begins converting to electrical infrastructure.

---

# Phase 17 — Modern Industry

Potential systems:

- petroleum,
- internal combustion,
- vehicles,
- industrial chemistry,
- plastics,
- advanced alloys,
- mass production.

---

# Phase 18 — Electronics and Computing

Potential systems:

- basic electronics,
- vacuum tubes,
- semiconductors,
- printed circuit boards,
- integrated circuits,
- computers,
- sensors,
- automation controllers.

---

# Phase 19 — Automation and Robotics

Potential systems:

- conveyors,
- robotic handling,
- automated factories,
- machine scheduling,
- autonomous logistics,
- reduced manual colony labor.

---

# Phase 20 — Space and Science Fiction

Potential systems:

- advanced power,
- rocket propulsion,
- spaceflight,
- orbital industry,
- advanced robotics,
- exotic materials,
- future technology branches.

---

# Cross-Cutting Workstreams

These continue throughout development.

## Content Audit

Every new era must re-check vanilla bypasses.

## Asset Pipeline

Asset provenance and reusable families are maintained continuously.

## Performance

Stress-test settlement simulation repeatedly:

- 1 settler,
- 10 settlers,
- 25 settlers,
- 50 settlers,
- 100 settlers.

Do not wait until late development to discover that colony AI architecture cannot scale.

## Save Compatibility

Version persistent data and migration strategies as systems stabilize.

## Documentation

Keep:

- CHANGELOG,
- roadmap,
- content schemas,
- asset credits,
- implementation handovers.


# Later UI Layer — Civilization Record

A separate non-critical interface may eventually track achievements, statistics, historical firsts, profession mastery, settler records, and civilization history. It should remain separate from the Quest Book so progression guidance does not become cluttered with completionist content.
