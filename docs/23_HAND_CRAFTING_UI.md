# Hand Crafting UI Foundation — v0.1.5

## Purpose

v0.1.5 connects the familiar vanilla inventory **2x2 crafting grid** to RealisticCiv's existing server-authoritative `WorkAction` system.

The grid is now the first front end for **Hand Crafting**: small operations that can plausibly be performed with carried materials/tools and without a dedicated workstation.

This milestone does **not** replace every vanilla crafting behavior yet and is not the final crafting UI. It establishes the architecture and interaction pattern.

---

# Player Flow

The first real Hand Crafting operation is:

```text
Branch
+ Flint Flake (persistent cutting edge)
        ↓
[ Craft ]
        ↓
inventory closes
        ↓
Shape Wooden Handle — ~5 seconds
        ↓
arm/tool work pulses
wood sounds + small particles
        ↓
Wooden Handle
```

The Branch is consumed/reserved. The Flint Flake is a tool/capability and is retained.

---

# UI Behavior

When the vanilla player inventory opens:

- the existing 2x2 grid remains in place,
- RealisticCiv adds a **Craft** button adjacent to it,
- the button is enabled only when the current grid matches a known `HAND_CRAFTING` operation,
- hovering the button shows the operation name, base work time, and that the tool is retained,
- invalid combinations leave the button disabled.

The UI does not manufacture the result itself.

---

# Server Authority

Pressing Craft sends an intentionally empty request payload:

```text
client: "attempt Hand Crafting from my current 2x2 grid"
        ↓
server re-reads actual inventory menu
        ↓
server resolves operation
        ↓
server validates input/tool/tags/counts
        ↓
server reserves consumed input
        ↓
server starts WorkAction
        ↓
server closes inventory
```

The client does **not** send a trusted recipe ID, output, item count, slot result, duration, or progress value.

This is important for multiplayer and anti-duplication design.

---

# Shared Operation Architecture

v0.1.5 adds shared Hand Crafting resolution rather than implementing one button-specific recipe.

```text
HandCraftingResolver
        ↓
OperationDefinition
        ↓
WorkActionManager
```

The current bootstrap resolver supports exactly:

- one consumed input,
- one persistent tool/capability,
- exactly two occupied 2x2 slots.

The operation itself remains UI-independent.

The first definition is:

```text
realisticciv:shape_wooden_haft
Category: HAND_CRAFTING
Input: realisticciv:natural_branches
Tool: realisticciv:cutting_edges
Input count: 1
Base duration: 100 ticks (~5 s)
Output: 1 Wooden Handle
```

This is deliberately useful future content rather than a disposable test recipe.

---

# Recipe vs Operation Direction

Long-term, the design distinguishes two concepts:

```text
Recipe / matching definition
→ what material/tool arrangement qualifies?

Operation definition
→ what work is actually performed?
```

v0.1.5 still uses the small bootstrap `OperationDefinition` schema directly for matching. Once recipes become richer, the matching layer can evolve without replacing `WorkAction`.

---

# WorkAction Changes

The WorkAction runtime now supports a persistent tool capability that may live anywhere in the player's normal inventory after Hand Crafting begins.

This is needed because:

1. the Flint Flake begins inside the 2x2 crafting grid,
2. the server reserves the Branch,
3. the inventory screen closes,
4. vanilla returns the persistent Flint Flake from the crafting grid to normal inventory,
5. the WorkAction continues only while the player still possesses an accepted cutting edge.

If the player removes/drops the required cutting edge during work, or moves beyond the stationary threshold, the action cancels and the reserved Branch is restored.

---

# Presentation

v0.1.5 reuses the same lightweight visual philosophy as timed knapping:

- action-bar progress,
- repeated arm swings,
- wooden impact/shaping sounds,
- small Branch-derived item particles,
- completion sound/particles.

These are placeholders for richer visual crafting. The operation and WorkAction layers must remain valid when custom carving animations, visible workpieces, tables, and settler execution are added later.

---

# New Content

v0.1.5 adds:

- `realisticciv:wooden_haft`
- original 32x32 Wooden Handle texture
- `realisticciv:tool_hafts` capability/form tag
- `realisticciv:shape_wooden_haft` Hand Crafting operation
- client inventory Craft button
- serverbound Hand Crafting request payload
- shared Hand Crafting resolver/service

---

# Known Scope Limits

Not implemented yet:

- skill requirements,
- difficulty display/calculation,
- success/failure/waste,
- tool durability/wear,
- batch Hand Crafting,
- multi-input/multi-tool recipes,
- custom graphical progress bar,
- custom player animation,
- visible workpiece placed on the ground/table,
- settlers using Hand Crafting operations,
- full replacement/audit of every remaining vanilla 2x2 recipe,
- full layout integration with every vanilla Recipe Book/narrow-screen inventory shift; the v0.1.5 button is positioned for the normal centered inventory layout and will be generalized if playtesting exposes a conflict,
- plant fibre/cordage content.

Those are intentional later steps.

---

# Multiplayer Rules

- The logical server owns matching and validation.
- The logical server owns reservation and output.
- The client button is only a request surface.
- One active WorkAction per player remains enforced.
- Tool capability is revalidated during work.
- Movement cancellation remains server-owned.

The same behavior should work in singleplayer (integrated server) and dedicated multiplayer.

---

# Test Checklist

1. Put one Branch and one Flint Flake into any two slots of the 2x2 inventory crafting grid.
2. Confirm the Craft button becomes enabled.
3. Hover it and confirm it identifies **Shape Wooden Handle** and ~5.0 s.
4. Press Craft.
5. Confirm the inventory closes.
6. Confirm the Branch is reserved/removed and the Flint Flake is retained.
7. Remain stationary and verify progress, arm swings, wood sounds, and particles.
8. Confirm exactly one Wooden Handle is produced after completion.
9. Repeat and move during work; verify cancellation restores the Branch.
10. Repeat and remove/drop the Flint Flake during work; verify cancellation restores the Branch.
11. Try invalid 2x2 combinations and confirm Craft stays disabled.
12. Test on a dedicated server to confirm the same behavior.

---

# Next Milestone

After v0.1.5 verification, the intended next gameplay milestone is **v0.1.6 — Fibre/Cordage Bootstrap**.

The planned first path is:

```text
Tall Grass
→ Long Grass Stems
→ Prepared Grass
→ Primitive Grass Cordage
```

See `22_FIBRES_CORDAGE_AND_TEXTILES.md`.


## Hotfix 2 UI Refinements

- The Hand Crafting **Craft** button is now positioned relative to the real inventory GUI `leftPos` / `topPos`, so it follows the inventory correctly when the vanilla Recipe Book panel opens or the screen layout shifts.
- When the 2x2 grid resolves to a valid RealisticCiv Hand Crafting operation, the primary result now appears directly in the normal vanilla output slot area as a **visual preview**.
- Hovering the preview uses the item's normal tooltip. Crafting itself still begins only through the explicit **Craft** button so the server-authoritative timed WorkAction flow remains unchanged.
- The underlying operation and item IDs remain `realisticciv:shape_wooden_haft` and `realisticciv:wooden_haft`, but the player-facing names are now **Shape Wooden Handle** and **Wooden Handle**.


## Hotfix 3 Compile/API Correction

Hotfix 2 accidentally used pre-26.2 GUI names (`GuiGraphics` and `ScreenEvents.afterRender`). Minecraft 26.2 uses the extraction-based `GuiGraphicsExtractor` pipeline, and Fabric 0.159.0+26.2 exposes extraction events such as `ScreenEvents.afterForeground`. Hotfix 3 updates the result-slot preview to that API while preserving the intended UI behavior.


## Hotfix 4 Runtime Fix

Hotfix 3 compiled under Minecraft 26.2 but crashed as soon as the inventory opened because `HandCraftingInventoryUi` attempted to cast `InventoryScreen` to `AbstractContainerScreenAccessor` before that accessor mixin had ever been registered with Fabric Loader. Hotfix 4 adds the client-only mixin configuration to `fabric.mod.json`, making the accessor available at runtime. The unused template example mixins were removed at the same time.
