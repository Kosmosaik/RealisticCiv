# Current State and Handoff

## Purpose

This document is the **first file a new developer or GPT assistant should read after `README.md`**.

It records where the project actually is now, which design decisions are already settled, what is still open, and what the next concrete development action should be.

Do not infer implementation progress from the size of the design documentation.

---

# Current Project State

**Status:** v0.1.5 Hotfix 4 is the current source. Hotfix 3 compiled locally but crashed when opening the inventory because the new client accessor mixin was not registered; Hotfix 4 registers it and is pending local runtime verification.

At the time of this handoff:

- a Fabric 26.2 / Java 25 source project exists and the baseline client/server environment was previously verified by the owner,
- v0.1.1 material identity and v0.1.2 Ground Resource/bootstrap-lock behavior were manually playtested successfully, including the Ground Resource renderer and 32x32 Branch texture,
- natural Branch, Granite Stone, and Flint Nodule Ground Resources spawn and can be picked up server-authoritatively,
- the first vanilla bootstrap pass remains active: survival log breaking requires `realisticciv:felling_tools`, and vanilla planks/sticks/crafting table/furnace/wooden-tool/stone-tool recipes are disabled,
- v0.1.3 primitive knapping was manually verified: Granite Stone + Flint Nodule produces Flint Core/Flakes/Chips through a real operation definition,
- v0.1.4 established the timed server-authoritative `WorkActionManager`: reservations, stationary cancellation, action-bar progress, repeated arm swings, sounds/particles, Ground Resource participation, and server-only output resolution,
- **v0.1.5 adds the first inventory 2x2 Hand Crafting front end with an explicit Craft button**, using Fabric's screen API rather than replacing the entire vanilla inventory screen,
- the Craft button is enabled only when the current 2x2 grid matches a known `HAND_CRAFTING` operation and shows the recognized operation/base time in its tooltip,
- pressing Craft sends only an empty `StartHandCraftPayload`; the server re-reads and validates the player's real inventory menu rather than trusting client recipe/result data,
- the first Hand Crafting operation is `realisticciv:shape_wooden_haft`: one Branch + a persistent Flint Flake cutting edge → ~5 seconds of work → one Wooden Handle,
- the v0.1.5 Hotfix 2 client UI now anchors the Craft button to the actual shifted inventory window and renders the primary Hand Crafting result in the vanilla output slot as a visual preview,
- v0.1.5 Hotfix 4 registers the `AbstractContainerScreenAccessor` client mixin in `fabric.mod.json`; this is required for the shifted-inventory positioning code to safely read vanilla `leftPos` / `topPos` at runtime,
- v0.1.5 Hotfix 3 ports that output-preview rendering to the actual Minecraft/Fabric 26.2 extraction API (`GuiGraphicsExtractor` + `ScreenEvents.afterForeground`) after Hotfix 2 was found to use obsolete GUI symbols and failed `compileClientJava`,
- `realisticciv:wooden_haft` and `realisticciv:tool_hafts` now exist for future primitive-tool assembly,
- the player-facing item/operation names are now **Wooden Handle** / **Shape Wooden Handle**, while the underlying registry/operation IDs intentionally remain `realisticciv:wooden_haft` / `realisticciv:shape_wooden_haft` for compatibility,
- Hand Crafting reserves the consumed Branch, closes the inventory after server acceptance, returns the persistent cutting tool to normal inventory, and revalidates that cutting-edge capability throughout the work action,
- movement or loss of the required cutting edge cancels Hand Crafting and restores the reserved Branch,
- WorkAction presentation is now category-aware: knapping retains stone/flint feedback while Hand Crafting uses arm swings, wood-oriented sounds, Branch-derived particles, and the same action-bar progress lifecycle,
- v0.1.5 Hotfix 1 fixes the first compile pass by restoring the omitted `showStarted` / `showProgress` / `operationName` helpers and adapting Hand Crafting `ItemParticleOption` creation to Minecraft 26.2's supported `Item` constructor; gameplay design is unchanged,
- the bootstrap Hand Crafting resolver currently supports exactly one consumed input + one persistent tool with exactly two occupied 2x2 slots; richer/multi-input recipes remain future work,
- **fibre planning has been clarified before implementation:** there will be no generic naturally harvested `Plant Fibre` item; Tall Grass is the emergency source (Long Grass Stems → Prepared Grass → Primitive Grass Cordage), Nettle is the preferred first better wild fibre route, and later bast/flax/hemp retain source material identity,
- custom graphical work HUD, custom skeletal work animations, visible placed workpieces, skills, quality, failure/waste, fibre/cordage content, and the first legitimate primitive axe are not implemented yet,
- the custom RealisticCiv Quest Book remains the chosen long-term direction but is not implemented yet,
- local development may use `online-mode=false` only in the excluded dev `run/server.properties`; public/friend testing remains compatible with normal `online-mode=true`,
- open owner notes from `stuff.txt` remain relevant: investigate the client occasionally hanging on quit, fix placeholder/checkerboard Ground Resource break particles later, and revisit hand-carry/weight/bundle limits as a future inventory/logistics system,
- the newest complete source ZIP/repository is the primary implementation source of truth.

See `21_WORK_ACTION_FOUNDATION.md`, `22_FIBRES_CORDAGE_AND_TEXTILES.md`, and `23_HAND_CRAFTING_UI.md`.

---

# Immediate Next Action

Verify v0.1.5 locally from the project root:

```powershell
.\gradlew.bat runDatagen
.\gradlew.bat build
.\gradlew.bat runClient
```

Verify at minimum:

1. previous timed Flint knapping still works unchanged,
2. the player inventory shows a **Craft** button beside the 2x2 crafting grid,
3. the button remains disabled for invalid/empty combinations,
4. putting one Branch + one Flint Flake in any two 2x2 slots enables the button,
5. its tooltip identifies **Shape Wooden Handle**, ~5.0 seconds, consumed input, and retained tool,
6. pressing Craft causes the server to accept the request and close the inventory,
7. one Branch is reserved/removed while the Flint Flake is retained,
8. shaping takes ~5 seconds with action-bar progress, arm swings, wood sounds, and small Branch-derived particles,
9. exactly one Wooden Handle is produced on success,
10. moving during shaping cancels it and restores the Branch,
11. dropping/removing the required Flint Flake during shaping cancels and restores the Branch,
12. invalid 2x2 arrangements never grant an output,
13. existing Ground Resources/bootstrap locks still work.

Then verify the same Hand Crafting flow on the dedicated dev server:

```powershell
.\gradlew.bat runServer
```

If v0.1.5 passes, commit it to `develop` and proceed to **v0.1.6 — Fibre/Cordage Bootstrap**. The first intended route is:

```text
Tall Grass
→ Long Grass Stems
→ Prepared Grass
→ Primitive Grass Cordage
```

A better wild-fibre route (preferably Nettle Stalk → Nettle Fibre → Nettle Cordage) should follow soon after. Do **not** introduce a generic naturally harvested `Plant Fibre` item. See `22_FIBRES_CORDAGE_AND_TEXTILES.md`.

The overall first playable target remains:

```text
Spawn with no practical technology
        ↓
Gather branches / material-identified stones
        ↓
Initial knapping ✓
        ↓
Flint Flake / cutting edge ✓
        ↓
Timed WorkAction knapping ✓
        ↓
2x2 Hand Crafting / Craft button ✓
        ↓
Wooden Handle shaping ✓
        ↓
Grass / better fibre processing
        ↓
Cordage
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

## Fibres and Cordage

- `Plant Fibre` is not a naturally harvested universal resource.
- Fibre must be processed from identifiable source material.
- Tall grass is the emergency/low-grade bootstrap fibre source.
- Nettle is the preferred first better wild fibre source.
- Bast/inner bark, flax, and hemp are planned later alternatives/progression.
- Different cordage sources should retain material identity and can eventually qualify differently for operations based on strength/flexibility/durability properties.
- Do not add extra processing-stage items unless they create meaningful gameplay decisions.

## Materials and Resource Identity

- Material identity begins immediately; do not use a generic `Tool Stone` inventory item.
- `Granite Stone` and `Flint Nodule` are the first concrete stone resources.
- Granite is the bootstrap hammerstone/general hard stone; flint is the bootstrap high-quality knappable stone.
- Broad tags (`loose_stones`, `hammerstones`, `knappable_stones`) classify real materials without replacing their identity.
- Operations should increasingly query material traits/properties/capabilities rather than hardcode one exact accepted item when multiple materials could realistically work.
- The exact long-term JSON/material-property schema remains provisional, but callers should be designed so the bootstrap `MaterialCatalog` can later be replaced/expanded without rewriting every operation.

See `18_MATERIAL_IDENTITY_AND_PROPERTIES.md`.

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
- The core Quest Book is implemented by RealisticCiv itself; FTB Quests/other quest frameworks are not required dependencies.
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
10. Establish package/module/data-validation foundation. **Done in bootstrap/v0.1.1.**
11. Register first material-identity content and datagen. **Done in v0.1.1; local verification pending.**
12. Run datagen/build/client/server verification.
13. Only then begin Phase 1 gameplay changes.
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
