# WorkAction Foundation — v0.1.4

## Purpose

v0.1.4 converts the first primitive knapping operation from an instant item conversion into RealisticCiv's first **timed, server-authoritative WorkAction**.

This milestone is intentionally small in content but important architecturally. The same runtime layer should later drive:

- Hand Crafting,
- workstation crafting,
- sawing/carving/assembly,
- smithing,
- machine operation,
- repairs,
- and eventually settler production.

The goal is not to create a Flint-specific timer. The goal is to prove a reusable work lifecycle.

---

# Player Experience

Initial Flint Knapping now behaves approximately as:

```text
Granite Stone + Flint Nodule
        ↓
Start Knapping Flint
        ↓
~4 seconds of work
        ↓
repeated arm strikes
stone-hit sounds
Flint Chips particles
HUD/action-bar progress
        ↓
server resolves outputs
        ↓
1 Flint Core
2 Flint Flakes
2 Flint Chips
```

The operation uses the existing `realisticciv:initial_flint_knapping` definition. Its base duration is now **80 ticks (~4 seconds at 20 TPS)**.

---

# WorkAction Runtime

The first implementation lives under:

```text
crafting/workaction/
├── ActiveWorkAction.java
└── WorkActionManager.java
```

`ActiveWorkAction` stores short-lived runtime state such as:

- actor/player UUID,
- `OperationDefinition`,
- starting `ServerLevel`,
- starting position,
- elapsed/duration ticks,
- animation/tool hand,
- reserved inventory input,
- optional Ground Resource participant,
- whether the Ground Resource is the input or the tool.

`WorkActionManager` owns active actions and advances them once per server tick.

This state is intentionally **not persistent save data** in v0.1.4. Current actions are short-lived and are cancelled/restored when the player leaves or the server shuts down.

---

# Server Authority

The logical server owns:

- action start validation,
- input reservation,
- active-action state,
- elapsed work time,
- cancellation,
- target validation,
- output creation,
- sound/particle event broadcast,
- Ground Resource removal on successful completion.

The client does not decide when knapping finishes or which outputs exist.

This remains true in singleplayer because Minecraft's integrated server is still the authoritative simulation side.

---

# Input Reservation

For a held Flint Nodule:

```text
player inventory
      ↓ action starts
1 Flint Nodule removed/reserved by server
      ↓
work proceeds
```

If the action succeeds, the reserved input stays consumed and outputs are granted.

If the action is cancelled, the reserved input is returned to inventory. If inventory insertion fails, it is dropped at the player.

Creative mode does not consume the held input.

## Ground Input

If the Flint Nodule is a natural Ground Resource, it remains visibly present while work is in progress.

It is logically reserved and removed only when the action completes successfully.

This is the first step toward visually persistent workpieces on the ground/workbenches/machines.

## Ground Tool

If the Granite Stone is the Ground Resource, it remains in place as the persistent tool. The held Flint Nodule is reserved and consumed only on successful work completion.

---

# Ground Resource Reservation

A Ground Resource participating in an active WorkAction is registered in a server-side reservation map.

Right-click collection/processing attempts against a reserved Ground Resource are denied with feedback rather than allowing two players to claim the same object simultaneously.

This first implementation does not yet synchronize a special visual reservation marker to clients and does not fully prevent another player from physically breaking the block. If a reserved Ground Resource disappears, the WorkAction notices during validation and cancels safely.

More comprehensive shared-resource reservation should evolve as workstations/logistics are introduced.

---

# Stationary Work and Cancellation

Primitive knapping currently uses a stationary-work rule.

The action stores the player's start position. If the player moves more than approximately **0.15 blocks** from that position before completion, the server cancels the action.

The action also cancels if:

- the player dies,
- the player changes level/dimension,
- a required held hammerstone is no longer in the expected hand,
- a required Ground Resource disappears or changes.

On normal cancellation, reserved held input is restored.

The system currently uses one generic `Work cancelled.` message. More specific cancellation reasons can be added later.

---

# Progress Feedback

v0.1.4 intentionally uses a simple action-bar HUD rather than building the final custom work HUD immediately.

Example:

```text
Knapping Flint  [#####-----]  50%
```

This proves synchronized work progress with minimal client-specific UI infrastructure.

A later milestone may replace/augment this with a dedicated visual progress bar while keeping the same server WorkAction state.

---

# Knapping Presentation

The WorkAction presentation layer is intentionally simple but visible.

During knapping the server periodically triggers:

- a player hand swing,
- `STONE_HIT` sounds with slight pitch variation,
- item particles using the RealisticCiv `Flint Chips` item art.

At completion it triggers:

- a stronger stone-break sound,
- a larger final Flint Chips particle burst,
- completion feedback.

For Ground Resource knapping, effects are positioned near the visible ground object.

For two-hand inventory knapping, effects appear in front of the player.

These are **bootstrap animations/effects**, not the final custom knapping animation. They can later be replaced by a dedicated animation family without changing operation execution.

---

# One WorkAction Per Player

A player may currently have only one active WorkAction.

Attempting to start another timed operation while already working is rejected.

The occupied state should eventually also gate other incompatible interactions more generally as more work systems are added.

---

# Current Scope / Limitations

v0.1.4 does **not** yet implement:

- the 2x2 Hand Crafting Craft button,
- a custom graphical WorkAction HUD,
- custom skeletal/player knapping animation,
- partial workpiece visual stages,
- skill speed modifiers,
- skill XP,
- failure probability,
- variable yield,
- material waste calculation,
- tool durability/wear,
- quality,
- injuries,
- pause/resume/save-progress behavior,
- settler actors,
- workstation-bound actions,
- fully generalized multi-input reservations.

Those systems should extend this foundation rather than bypass it.

---

# v0.1.4 Test Checklist

Run:

```powershell
.\gradlew.bat runDatagen
.\gradlew.bat build
.\gradlew.bat runClient
```

Verify:

1. Granite Stone + Flint Nodule no longer resolves instantly.
2. Knapping takes about four seconds.
3. The action bar shows operation name, progress bar, and percentage.
4. The player's arm repeatedly swings during the action.
5. Each strike produces stone-hit sound with slight variation.
6. Each strike produces small Flint Chips particles.
7. Outputs appear only after the timer completes.
8. Successful output remains:
   - 1 Flint Core,
   - 2 Flint Flakes,
   - 2 Flint Chips.
9. Granite Stone is not consumed.
10. Moving away during knapping cancels the action.
11. A held Flint Nodule reserved at action start is returned after cancellation.
12. Changing/removing the held hammerstone cancels the action and returns reserved input.
13. Hold Granite Stone and right-click a natural ground Flint Nodule:
    - the nodule remains visible during work,
    - particles/sounds occur near it,
    - it disappears only on successful completion.
14. Hold Flint Nodule and work against a natural ground Granite Stone:
    - the Granite Stone remains after completion,
    - the held nodule is reserved during work.
15. Right-clicking another ordinary Ground Resource while actively working does not allow free concurrent collection.
16. Ordinary Ground Resource pickup works normally when no WorkAction is active.
17. Existing bootstrap locks remain active.
18. Repeat important cases on the dedicated development server and, if possible, observe another player performing knapping.

---

# Next Step

v0.1.5 now implements the intended **Hand Crafting UI foundation**:

```text
inventory 2x2 Hand Crafting
        ↓
recognized operation
        ↓
Craft button
        ↓
server validation / reservation
        ↓
existing WorkActionManager
        ↓
progress / outcome
```

The Hand Crafting UI must start WorkActions; it must not introduce a separate crafting-timer system.


# v0.1.5 Extension — Hand Crafting

v0.1.5 proves that `WorkActionManager` is not knapping-specific. The inventory 2x2 Hand Crafting button resolves `shape_wooden_haft` and starts the same server runtime. The consumed Branch is reserved; the persistent Flint Flake is returned to normal inventory when the inventory closes and is then revalidated by capability throughout the action. Hand Crafting uses its own wood-oriented pulse/completion sounds and particles while preserving the same progress/cancellation/output lifecycle.

See `23_HAND_CRAFTING_UI.md`.
