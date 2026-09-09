# RealisticCiv — Minecraft Java Total Conversion

## Project Documentation Index

This project is a large-scale Minecraft Java total-conversion mod centered on the progression of a small settlement from **bare-handed primitive survival** through **agriculture, metallurgy, industry, modern technology, automation, computing, spaceflight, and science-fiction-era technologies**.

The mod is intended to preserve Minecraft's strengths — procedural voxel worlds, exploration, construction, multiplayer, entities, and sandbox play — while replacing much of vanilla Minecraft's progression, crafting, resource processing, villager behavior, and technology model with a more realistic civilization simulation.

## Core Principles

- The player starts with effectively nothing.
- Vanilla progression shortcuts must not bypass the intended technology chain.
- Crafting should model processes, tools, workplaces, knowledge, skills, time, and material states rather than only shaped-grid recipes.
- Settlers should be simulated workers with needs, skills, professions, inventories, schedules, and jobs.
- Technological progress should change *how* work is performed, not merely unlock stronger items.
- Recipes and systems should be data-driven and scalable to thousands of items and processes.
- Realism should create meaningful gameplay choices without turning every action into tedious micromanagement.
- Assets should be reusable, modular, legally traceable, and economical to produce.
- Performance must be considered from the beginning, especially for settlement AI and logistics.
- Multiplayer is a first-class requirement: important simulation is server-authoritative and shared systems must support multiple players and settlements.
- Player crafting and settler production should execute the same underlying work-operation definitions whenever practical.
- Progression should be exposed through an expert-pack-style Quest Book that guides rather than dictates, with real cross-chapter dependencies and configurable locked-quest visibility.

## Documents

1. [01_PROJECT_VISION.md](01_PROJECT_VISION.md) — vision, pillars, scope, player fantasy, design principles.
2. [02_PROGRESSION_AND_TECHNOLOGY.md](02_PROGRESSION_AND_TECHNOLOGY.md) — primitive-to-sci-fi progression model and technology graph.
3. [03_REALISTIC_CRAFTING_AND_PRODUCTION.md](03_REALISTIC_CRAFTING_AND_PRODUCTION.md) — materials, processes, tools, workstations, recipes, quality, and manufacturing.
4. [04_SETTLERS_AND_COLONY_SIMULATION.md](04_SETTLERS_AND_COLONY_SIMULATION.md) — settler needs, jobs, skills, professions, AI, schedules, logistics, and settlement management.
5. [05_VANILLA_CONTENT_LOCKS_AND_OVERRIDES.md](05_VANILLA_CONTENT_LOCKS_AND_OVERRIDES.md) — what vanilla systems must be blocked, replaced, gated, or redesigned.
6. [06_WORLD_RESOURCES_AND_EARLY_GAME.md](06_WORLD_RESOURCES_AND_EARLY_GAME.md) — loose resources, ground litter, primitive gathering, first tools, water, fire, and initial survival loop.
7. [07_ASSETS_ART_DIRECTION_AND_LICENSING.md](07_ASSETS_ART_DIRECTION_AND_LICENSING.md) — asset strategy, licensing policy, reuse rules, modular art families, and provenance tracking.
8. [08_TECHNICAL_ARCHITECTURE.md](08_TECHNICAL_ARCHITECTURE.md) — Fabric-oriented architecture, registries, data-driven content, AI managers, persistence, and performance constraints.
9. [09_IMPLEMENTATION_ROADMAP.md](09_IMPLEMENTATION_ROADMAP.md) — phased development plan from prototype to long-term civilization simulation.
10. [10_CONTENT_AUDIT_CHECKLIST.md](10_CONTENT_AUDIT_CHECKLIST.md) — audit checklist for vanilla bypasses, systems, recipes, loot, structures, and progression leaks.
11. [11_INITIAL_VERTICAL_SLICE.md](11_INITIAL_VERTICAL_SLICE.md) — the smallest playable prototype that validates the core architecture and gameplay loop.
12. [12_MULTIPLAYER_AND_KNOWLEDGE.md](12_MULTIPLAYER_AND_KNOWLEDGE.md) — server authority, multiplayer settlements, knowledge scopes, teaching, permissions, and civilization knowledge preservation.
13. [13_QUEST_BOOK_AND_PROGRESSION_UI.md](13_QUEST_BOOK_AND_PROGRESSION_UI.md) — expert-pack-style progression chapters, major/minor quest graphs, cross-chapter gates, visibility modes, guidance, and visual layout direction.
14. [14_CURRENT_STATE_AND_HANDOFF.md](14_CURRENT_STATE_AND_HANDOFF.md) — current implementation status, locked decisions, open questions, immediate next action, and cold-handoff instructions.
15. [15_DEVELOPMENT_WORKFLOW.md](15_DEVELOPMENT_WORKFLOW.md) — source-of-truth rules, audit-first development, multiplayer validation, testing, versioning, documentation, and GPT-assisted workflow.
16. [16_DEVELOPMENT_ENVIRONMENT_SETUP.md](16_DEVELOPMENT_ENVIRONMENT_SETUP.md) — Windows/Fabric development environment, provisional toolchain baseline, bootstrap procedure, client/server validation, and upgrade policy.
17. [17_VISUAL_CRAFTING_AND_ANIMATION.md](17_VISUAL_CRAFTING_AND_ANIMATION.md) — visible workpieces, reusable animation families, ground worksites, workstations, machines, and synchronized work visuals.
18. [18_MATERIAL_IDENTITY_AND_PROPERTIES.md](18_MATERIAL_IDENTITY_AND_PROPERTIES.md) — material-first resource identity, stone traits/properties, capability tags, and the no-generic-Tool-Stone decision.
19. [19_GROUND_RESOURCES_AND_BOOTSTRAP_LOCKS.md](19_GROUND_RESOURCES_AND_BOOTSTRAP_LOCKS.md) — implemented v0.1.2 ground-resource architecture, natural distribution, pickup behavior, and first vanilla progression locks.
20. [20_PRIMITIVE_KNAPPING.md](20_PRIMITIVE_KNAPPING.md) — primitive knapping operation, held/ground interactions, flint forms/capabilities, and the v0.1.4 transition from instant execution to timed WorkAction.
21. [21_WORK_ACTION_FOUNDATION.md](21_WORK_ACTION_FOUNDATION.md) — first timed server WorkAction lifecycle, reservations, progress, cancellation, knapping animation/sounds/particles, and multiplayer authority.
22. [22_FIBRES_CORDAGE_AND_TEXTILES.md](22_FIBRES_CORDAGE_AND_TEXTILES.md) — planned source-identified grass/nettle/bast/flax/hemp fibre, cordage, drying/retting, textile progression, and material-quality direction.
23. [23_HAND_CRAFTING_UI.md](23_HAND_CRAFTING_UI.md) — v0.1.5 inventory 2x2 Hand Crafting Craft-button UI, server validation/networking, first Wooden Handle operation, and WorkAction integration.
24. [references/README.md](references/README.md) — Quest Book structural reference and generated early/mid-game UI concepts.


## Recommended Reading Order for a New Handoff

A new developer or GPT assistant should begin with:

```text
README.md
→ 14_CURRENT_STATE_AND_HANDOFF.md
→ 15_DEVELOPMENT_WORKFLOW.md
→ 16_DEVELOPMENT_ENVIRONMENT_SETUP.md
→ 08_TECHNICAL_ARCHITECTURE.md
→ 11_INITIAL_VERTICAL_SLICE.md
→ relevant system-specific design documents
```

Do not assume that design depth implies implementation progress. `14_CURRENT_STATE_AND_HANDOFF.md` records the actual current state.

## Project Name

**RealisticCiv** is the current working project/mod name and `realisticciv` is the current implementation ID. The final public-facing name may still change before release; if the mod ID changes after persistent saves/content exist, treat that as a migration concern rather than a cosmetic rename.

