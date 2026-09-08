# Current State and Handoff

## Purpose

This document is the **first file a new developer or GPT assistant should read after `README.md`**.

It records where the project actually is now, which design decisions are already settled, what is still open, and what the next concrete development action should be.

Do not infer implementation progress from the size of the design documentation.

---

# Current Project State

**Status:** Fabric project bootstrapped; foundation implementation is ready to begin.

At the time of this handoff:

- a Fabric 26.2 / Java 25 source project exists,
- the development client has been manually verified to launch,
- the dedicated development server has been manually verified to launch,
- the development client has been manually verified to join that server,
- local development may use `online-mode=false` in `run/server.properties` solely to allow the unauthenticated Loom dev client to connect; this file is local and excluded from source packages,
- no actual gameplay systems are implemented yet,
- the project currently contains only the cleaned Fabric baseline, architecture folders, documentation, and packaging tooling,
- the package/mod ID is currently `realisticciv` / `com.realisticciv`,
- the core design direction is substantially established,
- the next step is Phase 0 architecture followed by the primitive vertical slice.

From this point onward, the **newest complete source ZIP/repository is the primary implementation source of truth**, with the documentation traveling inside it.

---

# Immediate Next Action

The clean Minecraft Java Fabric project described in `16_DEVELOPMENT_ENVIRONMENT_SETUP.md` has now been created and its basic client/server workflow verified.

Next implement **Phase 0 — Project Foundation** from `09_IMPLEMENTATION_ROADMAP.md`.

Do **not** begin by creating large content libraries, settlers, metallurgy, or a full quest tree.

The first playable target remains:

```text
Spawn with no practical technology
        ↓
Gather branches / stones / fibre from the world
        ↓
Knapping
        ↓
Stone flake / cutting edge
        ↓
Cordage
        ↓
Haft components
        ↓
Timed Hand Crafting
        ↓
Primitive stone axe
        ↓
Fell first small tree
        ↓
Obtain first meaningful log
```

See `11_INITIAL_VERTICAL_SLICE.md`.

---

# Locked Design Decisions

These decisions are considered part of the project identity. Do not casually redesign them while implementing unrelated work.

## Core Progression

- The player starts with effectively no usable technology.
- The vanilla Minecraft bootstrap must be closed systematically.
- Punching trees must not provide the normal log progression shortcut.
- Vanilla instant log-to-plank, plank-to-stick, wooden-tool, stone-tool, crafting-table, furnace, loot, trade, and structure shortcuts must be audited and gated/replaced where necessary.
- Progression should model real dependencies between materials, tools, processes, infrastructure, skills, and knowledge.
- Historical eras are broad descriptors, not one linear global unlock number.

## Crafting and Work

- The inventory 2x2 grid becomes **Hand Crafting**, not vanilla instant crafting.
- A recognized hand-crafting recipe/operation should use an explicit **Craft** action.
- Meaningful crafting consumes time.
- Timed labor executes through a reusable server-authoritative **WorkAction** layer.
- WorkAction is not only for crafting; it should be reusable for processing, repair, machine operation, teaching, construction, and related labor.
- Inputs are validated/reserved server-side before work begins.
- Player and settler production should reuse the **same underlying operation definitions** whenever practical.
- Skills may later affect time, waste, yield, quality, failure risk, and tool wear.
- Failure should usually create inefficiency, recoverable waste, or degraded output rather than simply deleting everything.
- Not every trivial interaction should have a progress bar; timers represent meaningful labor.

## Settlers

- Settlers are simulated people, not workstation blocks.
- Settlers should have needs, inventories, jobs, professions, and skills.
- Job, profession, skill, and knowledge are separate concepts.
- Settlers should physically retrieve inputs, perform work, and deliver outputs where practical.
- Settlers should belong primarily to a **settlement**, not be hard-owned by one individual player.
- Settlement managers/indexes should prevent every settler from scanning the world every tick.

## Multiplayer

- Multiplayer is a first-class architectural requirement from day one.
- Important simulation is server-authoritative.
- Clients request actions; they do not declare authoritative results.
- Shared resources and work must use reservations/concurrency-safe logic.
- Multiple players may belong to one settlement.
- Multiple settlements must remain possible; do not assume `server == one colony`.

## Knowledge

- Knowledge is not one global boolean list.
- The architecture must support at least:
  - player personal knowledge,
  - settler personal knowledge,
  - settlement/civilization knowledge.
- Written/digital knowledge preservation may be added later.
- Technology/knowledge determines what is possible; skill determines how competently an actor can perform it.

## Quest Book

- The Quest Book exists to answer **"Where can I go next and why am I blocked?"**
- It should feel like a classic expert-modpack quest graph.
- Chapters are subject/domain oriented and may span multiple historical eras.
- Players may focus on any available/unlocked chapter and progress until a real cross-chapter dependency blocks them.
- Gates should correspond to real simulation dependencies rather than arbitrary chapter percentages.
- Major and minor quests should be visually distinguishable.
- Dependency lines should make the graph structure obvious.
- Missing prerequisites should be directly navigable/clickable where possible.
- Quest completion should normally observe server-authoritative simulation state rather than rely on manual reward claiming.
- Material quest rewards that bypass progression should generally be avoided.
- The Quest Book must not become an achievement/statistics/history dump.
- A future **Civilization Record** may contain achievements, records, firsts, statistics, and completionist material.
- Locked-quest visibility is a per-player display preference with at least:
  - Available Only,
  - Unlocked Chapters,
  - Show All.
- Visibility settings never change actual progression authority.

## Architecture

- Prefer modular, data-driven systems.
- Do not hardcode thousands of recipes/quests/technologies directly into Java classes.
- Avoid throwaway prototypes that require rewriting core systems later.
- Content schemas should be validated at load time.
- Exact persistent formats should be versioned once saves exist.

## Assets

- Do not copy assets from other mods merely because they are downloadable.
- Reuse requires an appropriate license or explicit permission.
- Track provenance/credits for third-party assets.
- Prefer reusable visual families for mundane material forms.
- Spend detailed art effort primarily on important tools, machines, structures, and progression landmarks.

---

# Deliberately Open / Provisional Decisions

These are not locked yet and may be changed after prototypes provide evidence.

- Final public-facing project/mod name (current working name: RealisticCiv).
- Whether the current `realisticciv` mod ID / `com.realisticciv` package should remain permanent before public release.
- Final project/source-code license. **The current CC0 file is inherited from the Fabric template and is not yet an intentional licensing decision.**
- Future Minecraft/Fabric upgrade targets after the currently pinned 26.2 bootstrap.
- Final recipe/operation JSON schema.
- Exact skill list and skill progression formulas.
- Exact hunger/thirst/energy tuning.
- Exact item quality model.
- Exact failure/waste formulas.
- Exact geology/material granularity.
- Whether whole-tree felling is included in the first timber milestone or later.
- Exact quest-node sizing/color language and final art style.
- Exact chapter list; the current list is structural guidance, not content freeze.
- Exact research/discovery mechanics.
- Exact settler recruitment/origin mechanics.
- Exact player-death knowledge consequences.
- Exact scope and timing of the Civilization Record.

When changing one of these, record the decision in the relevant design document and in the project changelog once code development begins.

---

# First Development Sequence

A new implementation session should proceed in this order:

```text
1. Read README + this handoff.
2. Read DEVELOPMENT_WORKFLOW.
3. Read DEVELOPMENT_ENVIRONMENT_SETUP.
4. Read TECHNICAL_ARCHITECTURE.
5. Read INITIAL_VERTICAL_SLICE.
6. Read VANILLA_CONTENT_LOCKS_AND_OVERRIDES.
7. Create/import clean Fabric project.
8. Confirm client launch.
9. Confirm dedicated-server launch.
10. Establish package/module/data-validation foundation.
11. Create baseline build/test workflow.
12. Only then begin Phase 1 gameplay changes.
```

Before implementing any system, read the directly related design document rather than relying on a summary from this file.

---

# Handoff Rule for Future Sessions

When a new source ZIP exists, the recommended instruction to a fresh GPT/developer is:

> Read the entire newest project ZIP recursively before making changes. Treat that ZIP as the only implementation source of truth. Read the project handoff and relevant design documents. Audit existing code and anchors before editing. Do not guess missing files or recreate already implemented systems from memory.

If documentation and code disagree, inspect the newest changelog/handoff and implementation. Resolve the discrepancy explicitly rather than silently choosing one.

---

# What Success Looks Like for the First Real Milestone

The first milestone is not "a lot of content".

It is successful when:

- the game launches on client and dedicated server,
- vanilla bootstrap shortcuts are closed enough for the slice,
- starting resources exist in-world,
- the player can reach a primitive axe without vanilla crafting shortcuts,
- meaningful hand crafting uses server-authoritative timed WorkActions,
- inputs cannot be duplicated through concurrent requests,
- data definitions are validated and extensible,
- the architecture can later accept settlers without a second parallel crafting implementation,
- the first log feels earned.
