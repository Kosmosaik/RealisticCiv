# Realistic Crafting and Production

## Goal

Replace Minecraft's primarily instant ingredient-grid crafting model with a system that represents **materials, operations, tools, workplaces, skills, knowledge, time, risk, waste, quality, and production state**.

The design should remain understandable and playable rather than attempting a literal simulation of every physical action.

A central rule is:

> Crafting is a form of work, not an instant inventory conversion.

The same production operation should eventually be executable by either a **player** or a **settler** through the same underlying work-action system.

---

## Core Crafting Philosophy

The project should distinguish between:

- selecting or arranging a recipe,
- validating whether the operation is possible,
- reserving inputs and tools,
- spending time performing the work,
- calculating the result,
- producing outputs, byproducts, waste, skill XP, and tool wear.

This prevents the UI from becoming the simulation itself. The UI starts work; the server-side work system performs it.

---

# Hand Crafting — Reinterpret the Vanilla 2x2 Grid

The preferred initial design is to **keep the familiar 2x2 crafting area in the player inventory**, but redefine it as **Hand Crafting**.

The 2x2 grid is not a universal manufacturing system. It represents small-scale work a person can reasonably perform with carried materials and simple tools.

Appropriate examples:

- twist cordage,
- prepare tinder,
- knapp a stone,
- shape a small wooden haft,
- bind a primitive tool,
- assemble a simple tool,
- make basic bindings or bundles.

Inappropriate examples:

- log -> perfect planks,
- iron ingots + sticks -> pickaxe,
- smelting metals,
- machining precision parts,
- constructing an electric motor,
- manufacturing electronics.

These require workstations, machines, heat, power, or other infrastructure.

---

## Proposed Inventory Interaction

The player places or selects valid ingredients in the hand-crafting area.

Instead of the vanilla output immediately becoming collectible, the interface identifies the operation and presents information such as:

```text
STONE AXE ASSEMBLY

Time:        24 sec
Skill:       Primitive Toolmaking
Difficulty:  Easy
Tool:        None
Expected waste: Low

[ CRAFT ]
```

Pressing **Craft** should:

1. send a request to the server,
2. validate requirements,
3. reserve the required inputs/tools,
4. close or minimize the inventory,
5. start a timed work action,
6. show progress on the normal HUD,
7. resolve the result on the server,
8. place the outputs/byproducts into inventory or the world.

Example HUD:

```text
Assembling Stone Axe
████████████░░░░░░░░
        61%
```

This keeps Minecraft's familiar inventory interaction while removing instant crafting.

---

# WorkAction — The Universal Work Layer

Crafting should be implemented on top of a generic **WorkAction** system rather than recipe-specific timers.

Potential action categories:

```text
CRAFT
PROCESS
BUILD
HARVEST
REPAIR
DISASSEMBLE
LEARN
TEACH
OPERATE
```

Example action definition:

```text
Action:
ASSEMBLE_STONE_AXE

Base Duration:
25 seconds

Skill:
Primitive Toolmaking

Inputs:
- Stone Axe Head
- Wooden Haft
- Cordage

Required Tools:
- None

Workplace:
- Hands

Movement Mode:
- Stationary

Interruptible:
- Yes

Cancel Behavior:
- Refund unconsumed inputs
```

The important architectural goal is that later both:

```text
Player
↓
WorkAction
```

and:

```text
Settler AI
↓
WorkAction
```

execute the same underlying production definition.

This prevents a separate fake NPC crafting system from diverging from player crafting.

---

# Recipe / Operation Model

A production operation should be able to define:

- inputs,
- optional consumables,
- catalysts,
- required tools or tool capabilities,
- tool wear,
- workstation,
- skill requirements,
- knowledge requirements,
- environment requirements,
- energy/fuel requirements,
- base processing time,
- movement restrictions,
- interruption rules,
- cancellation rules,
- outputs,
- byproducts,
- waste products,
- quality rules,
- failure rules,
- XP rules,
- automation compatibility,
- batching rules.

Example:

```text
OPERATION: Shape Rough Wooden Handle

Input:
- 1 Straight Branch

Required Tool:
- CUTTING >= PRIMITIVE

Workplace:
- Primitive Work Surface

Skill:
- Woodworking

Knowledge:
- Basic Woodworking

Operations:
- debark
- trim
- shape

Base Time:
- 90 sec

Output:
- 1 Rough Wooden Handle

Byproducts:
- Wood Shavings
- Possible Wood Scrap
```

---

# Server-Authoritative Crafting

All important crafting state must be controlled by the server, including singleplayer through the integrated server.

When a player presses Craft, the server should validate:

- the recipe/action exists,
- required inputs are present,
- required tools are present and usable,
- required knowledge is known,
- skill gates are satisfied if applicable,
- workstation/environment requirements are satisfied,
- the actor is not already performing incompatible work,
- reserved items have not already been claimed by another action.

The client should never be trusted to declare that crafting succeeded.

This is required for multiplayer correctness and exploit prevention.

---

# Input Reservation

Inputs should be **reserved or transferred into an action-owned state when work begins**.

Do not leave the same stack freely usable while a timed action is running.

Conceptually:

```text
Inventory Inputs
↓
Server Validation
↓
Action Reservation
↓
Timed Work
↓
Outcome Resolution
↓
Outputs / Waste / Returned Materials
```

This prevents:

- double crafting from the same stack,
- player/settler race conditions,
- multiplayer duplication,
- two queued actions claiming one tool or component.

The exact implementation may use item removal, reservation metadata, temporary action inventory, or another server-controlled mechanism.

---

# Time as a Crafting Resource

Crafting time should represent meaningful labor.

Do not add timers to trivial inventory operations purely for realism.

Examples:

```text
Pick up branch          -> instant
Move stack              -> instant
Tie a tiny binding      -> very short
Make 5 m cordage        -> meaningful timer
Knapp axe head          -> meaningful timer
Shape wooden handle     -> meaningful timer
Assemble axe            -> short timer
Forge large component   -> long operation
```

Technology should reduce the labor required to achieve the same result.

Example:

```text
Shape board with stone adze
120 sec

Shape board with iron plane
45 sec

Powered planer
5 sec + machine setup
```

This makes technological progression mechanically visible.

---

# Movement Modes During Work

Not every operation should restrict the actor identically.

Suggested movement modes:

```text
FREE
WALK_ONLY
STATIONARY
WORKSTATION_BOUND
```

Examples:

### Cordage

- walking slowly may be allowed,
- sprinting/combat may interrupt.

### Knapping

- actor should remain stationary.

### Lathe Operation

- actor must remain in range of the workstation.

Movement requirements belong in the operation/action definition, not hardcoded per item.

---

# Interruption and Cancellation

Work should support interruption.

Potential reasons:

- player moves away,
- player chooses cancel,
- player takes damage,
- combat begins,
- settler need becomes critical,
- workstation becomes unavailable,
- required tool breaks,
- power/fuel fails.

Suggested cancellation modes:

```text
REFUND
PARTIAL_LOSS
NO_REFUND
SAVE_PROGRESS
```

Examples:

- simple assembly may refund all ingredients,
- knapping may create chips and a damaged blank,
- interrupted heating may preserve the workpiece but lose fuel,
- long machine operations may save progress if the physical workpiece remains in the machine.

Early versions can use simplified rules while keeping this field in the architecture.

---

# Skill Effects

Skill should affect execution quality rather than function only as a binary gate.

Potential effects:

- duration,
- yield,
- waste,
- output quality,
- failure probability,
- tool wear,
- stamina/energy cost,
- access to advanced operations.

Example:

```text
Operation Difficulty: 12
Player Knapping: 4

Duration:        85 sec
Waste:           High
Failure Chance:  18%
Quality Ceiling: Crude
Tool Wear:       High
```

Compared with:

```text
Player Knapping: 30

Duration:        32 sec
Waste:           Low
Failure Chance:  <1%
Quality Ceiling: Excellent
Tool Wear:       Low
```

The exact formulas should be tunable and data-driven where practical.

---

# Failure Should Usually Produce Consequences, Not Deletion

Avoid the frustrating pattern:

```text
90 seconds of work
↓
RNG says failure
↓
Everything disappears
```

Failure should often create recoverable or degraded results.

Examples:

### Knapping Failure

```text
Flint Nodule
↓
Failed strike
↓
Flint Chips + Small Flake
```

### Cordage Failure

```text
Prepared Fibre
↓
Poor Cordage / Tangled Fibre
```

### Woodworking Failure

```text
Handle Blank
↓
Damaged Handle Blank + Wood Scrap
```

### Smithing Failure

```text
Iron Billet
↓
Misshapen Workpiece
```

Catastrophic loss should be reserved for processes where it is believable and meaningful.

---

# Waste and Byproducts

Operations may produce useful waste streams.

Examples:

```text
Woodworking
→ Wood Shavings
→ Wood Scrap

Knapping
→ Stone Chips
→ Small Flakes

Sawing
→ Sawdust

Metal Machining
→ Metal Chips

Smelting
→ Slag
```

Waste may later become useful as:

- tinder,
- fuel,
- bedding,
- compost feedstock,
- recycled metal,
- filler,
- industrial feedstock.

Do not simulate grams of every material unless that level of detail creates useful gameplay. Quantity systems should remain practical.

---

# Tool Capability

Recipes should request **tool capabilities**, not always exact item IDs.

Examples:

- cutting,
- chopping,
- sawing,
- drilling,
- hammering,
- grinding,
- measuring,
- clamping,
- heating,
- lifting.

A recipe may require:

```text
CUTTING >= PRIMITIVE
```

rather than explicitly requiring `stone_knife`.

This allows later tools to naturally replace earlier tools.

---

# Tool Quality and Efficiency

Better tools can modify:

- processing time,
- material waste,
- output quality,
- stamina cost,
- failure chance,
- tool durability consumption.

Example:

```text
Stone Adze
Time multiplier: 1.00
Waste multiplier: 1.25

Iron Adze
Time multiplier: 0.55
Waste multiplier: 0.90

Powered Planer
Time multiplier: 0.10
Waste multiplier: 0.75
```

---

# Material States

Materials should change through processing instead of jumping directly from raw resource to finished component.

### Wood Example

```text
Tree
→ Log
→ Split Log
→ Hewn Timber
→ Rough Board
→ Sawn Board
→ Seasoned Board
→ Shaped Component
```

### Iron Example

```text
Iron-bearing material
→ Crushed Ore
→ Washed Ore
→ Roasted Ore
→ Furnace Charge
→ Bloom
→ Consolidated Bloom
→ Iron Billet
→ Bar / Plate / Rod
→ Machined Component
```

### Fibre Example

```text
Fibre Plant
→ Raw Fibre
→ Cleaned Fibre
→ Prepared Fibre
→ Cordage
→ Thread
→ Yarn
→ Fabric
```

---

# Workstation Progression

Suggested progression:

```text
Hands
↓
Primitive Work Surface
↓
Chopping Block
↓
Woodworking Bench
↓
Carpenter Bench
↓
Primitive Forge
↓
Smithing Workshop
↓
Mechanical Workshop
↓
Machine Shop
↓
Electrical Workbench
↓
Chemical Laboratory
↓
Electronics Bench
↓
Cleanroom / Precision Fabrication
↓
Automated Manufacturing Cells
```

Workstations should define available capabilities, not merely provide a different-looking crafting grid.

---

# Recipe Discovery and UI Evolution

The 2x2 hand-crafting arrangement can remain useful for primitive crafting where physical arrangement is intuitive.

It should **not** remain the only recipe interface forever.

As recipe count grows, interfaces may evolve toward:

- recognized operations from placed ingredients,
- recipe search/filtering,
- selecting a known operation,
- automatic ingredient matching,
- workstation-specific operation lists,
- work orders and queues.

The player should not be expected to memorize a shaped crafting pattern for a carburetor, gearbox, electric motor, or circuit board.

---

# Batch Crafting and Queues

Repeated production should not require repeated clicking.

Hand/workstation crafting should eventually support options such as:

```text
Craft 1
Craft 5
Craft 10
Craft All
```

and possibly queued work:

```text
Cordage x5
Wooden Peg x20
Handle x3
```

Each unit or batch still consumes real work time and follows reservation rules.

Later, settlers and machines should be the preferred solution for large repetitive production.

---

# Four Production Modes

Long-term production can be understood as four broad modes.

| Mode | Example | Human labor |
|---|---|---|
| Hand Crafting | Cordage, knapping, simple assembly | Actor performs the whole operation |
| Workstation Crafting | Carpentry, pottery, smithing | Actor works at dedicated station |
| Machine Operation | Lathe, drill press, power hammer | Actor operates powered equipment |
| Automated Processing | Furnace line, mill, factory cell | Worker mainly loads, supervises, or is removed entirely |

Civilization progression should gradually move production from the top of this table toward the bottom.

---

# Player and Settler Parity

A major design rule:

> If a player and a settler can perform the same real-world process, they should use the same operation definition whenever practical.

Example:

```text
Operation: make_wooden_peg

Player selects operation
→ Player WorkAction

Settler receives work order
→ Settler WorkAction
```

Both should use the same:

- inputs,
- tools,
- workstation requirements,
- base duration,
- skill rules,
- outcome calculation,
- byproducts,
- XP rules.

Only actor control differs.

---

# Work Orders

Production should support work orders instead of requiring the player to manually click every recipe.

Example:

```text
Work Order:
Produce 20 Wooden Pegs

Status:
Waiting for 2 Wood Billets
```

The logistics system then generates demand for the missing inputs.

---

# Dependency Resolution

The game should eventually be capable of tracing a requested product backward through intermediate components.

Example:

```text
Wooden Wheel
├── Hub
├── Spokes x12
├── Rim Segments x6
└── Fastening / Joinery
```

The system should know whether each subcomponent is:

- already in storage,
- craftable locally,
- missing raw materials,
- blocked by knowledge,
- blocked by skills,
- blocked by tools,
- blocked by infrastructure.

---

# Automation Progression

Production should evolve through stages.

### Manual

Player or settler performs each operation.

### Assisted

Machines reduce labor but require an operator.

### Semi-Automated

Machine performs a production cycle while worker loads/unloads it.

### Automated

Machine can receive inputs and dispatch outputs automatically.

### Integrated Factory

Multiple machines, buffers, conveyors, robots, and control systems form production lines.

---

# Recipe Design Checklist

A realistic recipe should answer:

1. What is the object physically made from?
2. What intermediate forms must those materials take?
3. What processes create those forms?
4. What tools or machines perform those processes?
5. What knowledge is required?
6. What skill is relevant?
7. What energy or fuel is required?
8. How long does the work take?
9. What happens if work is interrupted?
10. What byproducts or waste result?
11. How can low skill affect the outcome without becoming frustrating?
12. How does later technology improve the same process?
13. Can both players and settlers use the same operation definition?
14. Can the operation eventually be batched or automated?
