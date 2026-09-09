# Ground Resources and Bootstrap Locks

**v0.1.3 interaction extension:** Ground Resources participate in the first primitive knapping operation. A held Granite Stone can work a ground Flint Nodule; a held Flint Nodule can also be worked against a ground Granite Stone. Ordinary pickup remains the fallback when no valid operation pair matches.

**v0.1.4 timed-work extension:** a Ground Resource used by an active WorkAction remains visibly present during the timer and is logically reserved server-side against right-click collection. A ground Flint Nodule is removed only after successful completion; a ground Granite Stone used as the hammerstone remains. If the resource disappears/changes mid-action, the server cancels safely.

## Purpose

This document records the first implemented gameplay layer introduced in **v0.1.2**.

The implementation deliberately establishes reusable systems rather than one-off branch/stone hacks.

---

# Ground Resource Architecture

Natural loose resources are represented by small, non-colliding world blocks using the shared `GroundResourceBlock` implementation.

A ground resource block is **not** the inventory item itself. It is a persistent world anchor that:

- renders the canonical inventory item model through a lightweight client renderer,
- can be right-clicked with an empty or occupied hand,
- grants the canonical inventory item server-side,
- removes itself after successful collection,
- has no BlockItem / creative inventory form of its own,
- can also drop its corresponding item if broken.

Current mappings:

```text
realisticciv:ground_branch
→ realisticciv:branch

realisticciv:ground_granite_stone
→ realisticciv:granite_stone

realisticciv:ground_flint_nodule
→ realisticciv:flint_nodule
```

This pattern is intended to scale later to:

- twigs,
- bark,
- bones,
- shells,
- ore fragments,
- plant/fibre resources,
- archaeological finds,
- other loose environmental materials.

Do not create separate pickup logic for every future resource unless the resource genuinely requires unique behavior.

---

# Server Authority

The client reports the interaction, but inventory/world mutation occurs only on the logical server.

Conceptually:

```text
player interacts with ground resource
        ↓
server validates / handles interaction
        ↓
canonical ItemStack granted
        ↓
world block removed
        ↓
normal Minecraft synchronization updates clients
```

This is intentional for future multiplayer safety.

---

# Initial World Distribution

The current worldgen is intentionally simple and data-driven.

## Branch

- injected into forest biomes,
- relatively common,
- only placed on a curated forest-floor support tag.

## Granite Stone

- injected throughout the Overworld,
- less common than branches,
- support predicate limits it to plausible exposed ground surfaces.

## Flint Nodule

- injected throughout the Overworld,
- rarer than Granite Stone,
- only placed when the surface below is currently gravel/sand/red sand.

This is **bootstrap distribution**, not the final geology simulation.

Future worldgen should gradually account for geology, erosion/deposition, river systems, biome-mod compatibility, regional abundance, and surface indicators without invalidating the `GroundResourceBlock` collection architecture.

Worldgen only affects newly generated chunks. When testing changes, use a fresh world or travel into new chunks.

---

# Resource Models

As of **v0.1.2 Hotfix 3**, Ground Resources deliberately reuse their canonical inventory item models rather than maintaining separate block artwork.

Conceptually:

```text
assets/realisticciv/textures/item/branch.png
        ↓
Minecraft generated item model
        ├── inventory / held item
        └── Ground Resource renderer
```

The world renderer:

- centers the visual on the same block position as the interaction shape,
- lays the generated item model flat against the terrain,
- preserves Minecraft's subtle pixel-extrusion thickness,
- applies a stable position-derived yaw so nearby resources are not identically oriented,
- supports per-resource render scale (the Branch is intentionally larger than the stones),
- does not bob, spin, despawn, or use dropped-item physics.

The underlying blockstate uses an invisible model; a tickless `GroundResourceBlockEntity` exists only as the render anchor. It stores no duplicate item identity in NBT—the canonical pickup item and render tuning are derived from `GroundResourceBlock`.

This is the preferred architecture for future simple loose resources because one art asset can serve inventory and world presentation. Bespoke models remain appropriate later for resources whose world representation genuinely needs different geometry.

Performance note: Ground Resource block entities do not tick. Spawn density should nevertheless remain deliberate, and the renderer architecture should be profiled before greatly increasing resource counts per chunk. If this representation becomes a measurable bottleneck at larger scales, preserve the single-asset visual design while optimizing the render anchor rather than reverting to duplicated art.

---

# Vanilla Bootstrap Locks — v0.1.2 Pass

## Log Harvesting

Survival/adventure players cannot break blocks in `minecraft:logs` unless the held item belongs to:

```text
realisticciv:felling_tools
```

The tag is intentionally empty in v0.1.2. A future legitimate RealisticCiv primitive axe will enter this tag.

Creative players bypass the restriction for development/testing.

Attempting to finish breaking a log without a valid felling tool displays an action-bar message.

This avoids hardcoding a future axe ID into the log-break event.

---

# Disabled Vanilla Recipes

v0.1.2 performs the first recipe-removal pass by overriding selected vanilla recipe resources with an always-false Fabric resource condition.

Currently disabled:

- all vanilla log/stem → plank recipes,
- vanilla stick crafting recipe,
- crafting table recipe,
- furnace recipe,
- wooden axe/hoe/pickaxe/shovel/sword recipes,
- stone axe/hoe/pickaxe/shovel/sword recipes.

The override mechanism intentionally removes the recipes rather than replacing them with artificial placeholder ingredients.

This is only the **first bypass pass**. Loot, structures, villages, trades, mob drops, furnace smelting recipes, and many later vanilla systems still require the larger audit described in `05_VANILLA_CONTENT_LOCKS_AND_OVERRIDES.md` and `10_CONTENT_AUDIT_CHECKLIST.md`.

---

# Important Future Constraints

1. Do not make vanilla axes valid felling tools just to simplify implementation. RealisticCiv's own tool/process progression should determine legitimate felling capability.
2. Do not make ground resource blocks inventory-placeable unless a later design explicitly requires it.
3. Keep collection authoritative on the logical server.
4. Keep worldgen distribution separate from resource identity and pickup behavior.
5. Treat current spawn rates/surface tags as tuning values, not permanent realism assumptions.
6. When knapping is implemented, operate on `Granite Stone` / `Flint Nodule` material identities and traits rather than inventing a generic `Tool Stone`.

---

# v0.1.2 Test Checklist

Use a fresh world or new chunks.

- [ ] Branch ground resources appear in forests.
- [ ] Granite Stones appear on suitable exposed Overworld ground.
- [ ] Flint Nodules appear more rarely on gravel/sand-like surfaces.
- [ ] Ground resources are visibly small/low-profile and non-colliding.
- [ ] Right-clicking a resource gives exactly one corresponding inventory item.
- [ ] Pickup works while holding another item.
- [ ] Resource disappears for all clients after collection on a dedicated server.
- [ ] Breaking a ground resource drops the corresponding item rather than itself.
- [ ] Survival player cannot break logs without a valid felling tool.
- [ ] Creative mode can still break logs for development.
- [ ] Log → plank recipes are absent.
- [ ] Plank → stick crafting is absent.
- [ ] Vanilla wooden/stone tool recipes are absent.
- [ ] Crafting table recipe is absent.
- [ ] Furnace recipe is absent.
- [ ] Client, integrated server, and dedicated server remain stable.

## v0.1.2 visual/biome hotfix notes

Post-playtest fixes made before beginning knapping:

- Ground Resource visuals must remain centered on their interaction shape. The first implementation used a visual-only X/Z offset, causing targeting mismatch; offsets are no longer used.
- Hotfix 2 briefly introduced hand-authored 3D voxel models to add thickness. Hotfix 3 supersedes that approach: the world now renders the canonical generated item model itself, laid flat so the same icon texture supplies the silhouette and subtle extrusion thickness.
- Branches use `realisticciv:branch_rich` for biome injection. The default tag includes `#minecraft:is_forest`, `#minecraft:is_taiga`, `#minecraft:is_jungle`, swamp, and mangrove swamp. This deliberately covers vanilla wooded environments that are not members of `minecraft:is_forest` (notably taiga variants) and gives modpacks a data-driven extension point.
- Branch world rendering is intentionally scaled larger than Granite Stone / Flint Nodule so it reads as a useful branch, while selection shapes remain slightly generous for usability.
- Stable per-position yaw variation is visual only and never moves the render away from the block center.


## Art Note

- Ground Resources reuse each resource's canonical item model/texture through the custom client renderer.
- The bootstrap `Branch` currently uses an original 32x32 texture to improve legibility at the larger world scale.
- `Granite Stone` and `Flint Nodule` currently continue using original 16x16 placeholder textures.
