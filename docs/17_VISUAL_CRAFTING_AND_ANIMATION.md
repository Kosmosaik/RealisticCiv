# Visual Crafting, Worksites, and Animation

## Purpose

Crafting and processing should increasingly become **visible work performed in the world**, not only inventory/menu interactions.

The underlying simulation remains server-authoritative through `WorkAction`. Visuals are a presentation layer driven by that authoritative action state.

The system must work even when no custom animation exists yet; visual fidelity should be incremental rather than a prerequisite for adding content.

---

## Core Principle

A meaningful operation may visually coordinate several independent presentation systems:

```text
WorkAction
├── actor animation
├── held-tool animation / transform
├── visible workpiece(s)
├── workstation or machine animation
├── staged visual state changes
├── particles
└── sound
```

The server decides whether the action is valid, reserves inputs, advances authoritative progress, and produces outcomes. Clients render nearby actions from synchronized state.

---

## Physical Workpieces

Do not rely on normal dropped `ItemEntity` objects for crafting components placed on a table, machine, anvil, or ground worksite.

Instead, the logical worksite/workstation should own reserved inputs/slots while the client renderer displays those ItemStacks or dedicated models at defined positions.

Benefits:

- no accidental pickup,
- no despawn behavior,
- no hopper interaction unless explicitly designed,
- no multiplayer race over visible components,
- deterministic placement,
- recipe-specific staged visuals.

Example:

```text
Primitive Work Surface
slot 0: wooden haft
slot 1: stone axe head
slot 2: cordage

Visual stage 1: components laid separately
Visual stage 2: head positioned on haft
Visual stage 3: binding wrapped
Visual stage 4: finished axe
```

---

## Ground Worksites

Some primitive processes should not require an invented crafting-table block.

A temporary or lightweight **ground worksite** may anchor state and rendering at a world position while appearing visually as materials laid on the ground.

Candidate early uses:

- stone knapping,
- primitive assembly,
- fibre preparation,
- hide processing,
- fire-starting preparation.

Example knapping flow:

```text
place knappable stone
      ↓
place/use hammerstone
      ↓
actor crouches / works low to ground
      ↓
repeated strike animation
      ↓
stone-chip particles + sound
      ↓
workpiece visual changes
      ↓
flake / core / waste produced
```

---

## Reusable Animation Families

Do not create one bespoke animation per recipe.

Operations should reference reusable action/animation families such as:

```text
KNAP
CHOP
CARVE
SAW
HAMMER
CHISEL
FILE
HAND_DRILL
BIND
ASSEMBLE
STIR
GRIND
POUR
SHAPE_CLAY
SEW
WEAVE
SMITH
USE_TONGS
TURN_CRANK
OPERATE_LEVER
MEASURE
INSPECT
SOLDER
WELD
```

Thousands of operations can reuse a much smaller library of high-quality animations.

The same animation family should be usable by players and settlers wherever their rigs/poses permit it.

---

## Progressive Fidelity

Use three broad visual-fidelity levels.

### Level 1 — Generic

- visible workpiece,
- reused vanilla/generic pose,
- sound,
- particles,
- HUD progress.

Use this as the default fallback so content is never blocked by missing animation assets.

### Level 2 — Specialized

- purpose-specific animation family,
- visible tool interaction,
- multiple workpiece states,
- better placement/pose alignment.

Use for common but important craft families such as sawing, smithing, pottery, and assembly.

### Level 3 — Showcase

- custom actor animation,
- custom machine animation,
- multiple staged models/components,
- richer particles/sounds,
- possibly synchronized multiple actors.

Reserve for important progression landmarks and major machines.

Examples:

- bloomery operation,
- waterwheel machinery,
- powered lathe,
- steam engine,
- engine assembly,
- generator,
- industrial production equipment.

---

## Vanilla Animations as Prototype Fallback

Early versions should reuse vanilla-compatible poses/arm motions where practical rather than blocking development on custom animation tooling.

For example, first-pass knapping may combine:

- crouching,
- repeated hand/held-item motion,
- hammerstone in hand,
- flint rendered on the ground,
- chips/particles,
- impact sound.

Custom animations can replace the generic presentation later without changing the operation definition.

---

## Visual Stages

Operations may define optional visual stages based on progress.

Conceptually:

```text
0–20%   components laid out
20–55%  first assembly/processing step
55–85%  intermediate state
85–100% finishing step
100%    completed output
```

Stages may alter:

- rendered input/model,
- item transforms/positions,
- tool pose,
- particles,
- sound loop,
- machine moving parts.

The actual schema should remain data-driven and optional.

---

## Workstations and Machines

A workstation should be a physical workspace, not merely a GUI block.

Examples:

- a carpenter's bench displays the board being cut,
- an anvil displays the hot workpiece being struck,
- a pottery station displays the clay form,
- a lathe renders stock between chuck and support while it rotates,
- an assembly stand visibly gains components as construction proceeds.

Machine animation and actor animation should be separate so a machine can continue running when its process does not require constant manual operation.

---

## Detailed Assembly

This visual system should scale to the project's highly detailed BOM/manufacturing philosophy.

Complex machines or vehicles may visibly progress through subassemblies rather than appearing instantly at completion.

Example engine assembly:

```text
engine block
   ↓
main bearings placed
   ↓
crankshaft installed
   ↓
bearing caps / fasteners installed
   ↓
pistons / rods installed
   ↓
head / valvetrain / covers installed
   ↓
completed engine assembly
```

The exact amount of visual detail is content-dependent; the simulation/recipe tree may be more detailed than the rendered intermediate models.

---

## Settlers

Settlers should use the same underlying operation definitions and, where possible, the same animation families as players.

A settlement should therefore visibly look active:

- carpenters saw timber,
- smiths hammer workpieces,
- potters shape clay,
- machinists operate machine tools,
- mechanics assemble components.

The visual system must not create a separate fake settler-only crafting simulation.

---

## Multiplayer

The server synchronizes enough action state for nearby clients to render the same operation consistently.

Conceptual synchronized state:

```text
actor ID
operation ID
workstation/worksite position
start/progress state
selected visual stage
relevant tool/workpiece identifiers
```

Clients render animations but never decide success, output, resource consumption, or authoritative completion.

Late-joining or newly nearby clients must be able to reconstruct the current visual state from server data rather than relying only on a one-time "start animation" packet.

---

## Architecture Direction

Operation definitions should eventually support optional visual metadata approximately like:

```text
OperationDefinition
├── simulation data
│   ├── inputs
│   ├── outputs
│   ├── tools
│   ├── skills
│   ├── workstation
│   ├── duration
│   └── outcome rules
└── optional visual data
    ├── actor animation family
    ├── workpiece layout
    ├── visual stages
    ├── particles
    ├── sounds
    └── machine animation
```

Never make successful simulation depend on a particular animation being present.

---

## Implementation Order

Do not attempt the full animation system in the first gameplay milestone.

Recommended order:

1. server-authoritative `WorkAction`,
2. HUD progress/cancel feedback,
3. visible static workpiece on a simple worksite/workstation,
4. generic/reused actor animation,
5. particles and sounds,
6. reusable animation families,
7. staged workpiece visuals,
8. animated machines,
9. synchronized multi-actor work,
10. showcase assembly sequences.

This preserves development velocity while keeping the architecture ready for rich visual crafting later.
