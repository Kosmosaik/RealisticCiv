# Primitive Knapping — v0.1.3 / v0.1.4

## Purpose

v0.1.3 introduces RealisticCiv's first actual **material-processing operation**.

The player can now turn a naturally gathered **Flint Nodule** into useful knapped material by combining it with a valid **hammerstone**. The initial hammerstone is a `Granite Stone`.

v0.1.3 proved the operation/material interaction. **v0.1.4 now routes that same operation through the first timed `WorkActionManager`**, adding reservations, progress, cancellation, arm-swing feedback, sounds, particles, and delayed server-side outputs. Skills, quality, custom skeletal animation, and dedicated Hand Crafting UI are still future work.

---

# Player Interaction

The first operation is:

```text
Granite Stone (hammerstone)
+
Flint Nodule (knapping nodule)
        ↓
Initial Flint Knapping
        ↓
1 Flint Core
2 Flint Flakes
2 Flint Chips
```

The Granite Stone is a **tool** and is not consumed in v0.1.3.

The Flint Nodule is the consumed workpiece.

The initial yield is deterministic so the interaction and operation architecture can be tested cleanly. Later skill, material quality, tool condition, failure/waste, and yield systems may alter the result.

---

# Supported Interaction Forms

## Two-Hand Knapping

The player may hold:

```text
one hand: Granite Stone
other hand: Flint Nodule
```

Then use/right-click either participating item.

The operation resolves regardless of which hand contains which item.

This makes a gathered Flint Nodule useful even after it has been picked up from the world.

## Ground-Resource Knapping

Ground Resources also participate directly.

Supported examples:

```text
Hold Granite Stone
Right-click ground Flint Nodule
```

or:

```text
Hold Flint Nodule
Right-click ground Granite Stone
```

If the Flint Nodule is the Ground Resource, the ground nodule is removed when processed.

If the Granite Stone is the Ground Resource, the granite remains because it acts as the persistent hammerstone/tool while the held Flint Nodule is consumed.

This interaction is an early bridge toward the later design where workpieces/tools can be visibly arranged on the ground, benches, or machines.

---

# New Items

## Flint Core

The remaining/prepared piece of flint after initial reduction.

It maps to the same underlying `flint` material identity as the original nodule.

The core is tagged as:

```text
realisticciv:knappable_stones
realisticciv:knapping_cores
```

Later operations will allow continued flake removal and deliberate tool-head shaping from cores.

## Flint Flake

A useful sharp flake detached from the nodule.

It is tagged as:

```text
realisticciv:stone_flakes
realisticciv:cutting_edges
```

This establishes the first usable **primitive cutting-edge capability** for later fibre/wood processing.

## Flint Chips

Small knapping waste/debitage produced during the initial reduction.

It is tagged as:

```text
realisticciv:knapping_waste
```

For now it is primarily a material/waste output. Later systems may give knapping waste secondary uses or distinguish recoverable flakes from useless debris more precisely.

---

# Material Identity

The following item forms all map to the same underlying material definition:

```text
Flint Nodule
Flint Core
Flint Flake
Flint Chips
        ↓
Material: Flint
```

Material identity and **item form** are deliberately separate.

For example, Flint as a material is highly knappable and capable of excellent edges, but only particular forms are valid inputs for particular operations.

Form/capability tags handle that distinction.

---

# Operation Architecture

v0.1.3 adds the first bootstrap operation layer under:

```text
crafting/operation/
```

Core classes:

```text
OperationDefinition
OperationCatalog
OperationCategory
OperationMatch
OperationOutputDefinition
OperationOutputRole
```

The first operation is registered as:

```text
realisticciv:initial_flint_knapping
```

Conceptually it defines:

```text
Category:
KNAPPING

Input:
#realisticciv:knapping_nodules

Tool:
#realisticciv:hammerstones

Input count:
1

Base duration (v0.1.4):
80 ticks (~4 seconds)

Outputs:
1 Flint Core      PRIMARY
2 Flint Flakes    USEFUL_BYPRODUCT
2 Flint Chips     WASTE
```

In v0.1.3 the duration was metadata only and execution was immediate. In v0.1.4 the operation duration is **80 ticks (~4 seconds)** and is executed by the generic server-side WorkAction lifecycle. See `21_WORK_ACTION_FOUNDATION.md`.

---

# Server Authority

Actual mutation happens on the logical server:

- validate operation tags,
- consume the Flint Nodule,
- preserve the Granite Stone,
- remove a ground Flint Nodule when appropriate,
- grant outputs,
- drop outputs at the player if inventory insertion fails.

The client only predicts/acknowledges a successful interaction and receives authoritative inventory/world state.

This design must remain safe for dedicated multiplayer.

---

# What Is Intentionally Not Implemented Yet

v0.1.4 now includes timed `WorkAction` execution, action-bar progress, repeated vanilla arm swings, stone sounds, and Flint Chips particles. It still does **not** yet add:

- a custom graphical WorkAction HUD,
- custom skeletal/player knapping animation,
- visible staged workpiece transformation,
- skill XP,
- skill-based yield,
- random failure,
- tool wear,
- quality,
- injury risk,
- retouching flakes,
- deliberate axe-head/point/scraper shaping,
- a dedicated ground worksite entity,
- Quest Book guidance.

These should build on the operation definition introduced here instead of replacing it with separate recipe logic.

---

# v0.1.4 Test Checklist

Run:

```powershell
.\gradlew.bat runDatagen
.\gradlew.bat build
.\gradlew.bat runClient
```

Then verify:

1. `Flint Core`, `Flint Flake`, and `Flint Chips` still exist/render correctly.
2. Hold Granite Stone in one hand and Flint Nodule in the other.
3. Start knapping and verify outputs do not appear instantly.
4. Verify work takes roughly four seconds and action-bar progress advances.
5. Verify repeated arm swings, stone-hit sounds, and Flint Chips particles occur.
6. Verify exactly one Flint Nodule is consumed only by successful completion in Survival.
7. Verify Granite Stone remains.
8. Verify output remains 1 Flint Core + 2 Flint Flakes + 2 Flint Chips.
9. Start again, move away before completion, and verify cancellation restores the held Flint Nodule.
10. Start again, remove/swap the required held hammerstone, and verify cancellation restores the held input.
11. Find a natural ground Flint Nodule, hold Granite Stone, and start work. Verify the ground nodule remains visible until success and is removed only on completion.
12. Hold a Flint Nodule and work against a natural ground Granite Stone. Verify the Granite Stone remains after success.
13. Verify ordinary Ground Resource pickup still works outside WorkActions and is blocked while the player is occupied by work.
14. Verify Branch/Granite/Flint natural spawning/rendering remain unchanged.
15. Verify vanilla log breaking and recipe locks remain active.
16. Repeat important knapping/cancellation/reservation cases on the dedicated dev server.

See `21_WORK_ACTION_FOUNDATION.md` for the expanded WorkAction-specific test checklist.

---

# Next Step

v0.1.5 has now implemented the 2x2 Hand Crafting / Craft-button foundation and wires it into the existing WorkAction manager. The next gameplay focus is the source-identified fibre/cordage bootstrap described in `22_FIBRES_CORDAGE_AND_TEXTILES.md`.

The new `Flint Flake` should become one of the first valid primitive cutting edges used by that progression.
