# Initial Vertical Slice

## Purpose

The first implementation should prove the core concept using the smallest possible amount of content.

It should answer:

- Is replacing Minecraft's bootstrap fun?
- Is ground-resource gathering readable?
- Does realistic primitive crafting feel rewarding when meaningful operations take time?
- Does the 2x2 Hand Crafting + Craft button interaction feel natural?
- Does server-authoritative timed work behave correctly in multiplayer?
- Can the content architecture scale?
- Can the vanilla bypasses be controlled reliably?
- Can we introduce settlers later without rewriting the foundation?

---

## Scope

### Vanilla Changes

- Bare fists cannot harvest logs.
- Vanilla log → planks recipe removed.
- Vanilla plank → sticks recipe removed or irrelevant.
- Wooden tool recipes removed.
- Vanilla stone tool recipes removed.
- Crafting-table recipe disabled.
- Furnace progression blocked.

### World Resources

Add:

- Twig,
- Branch,
- Loose Stone,
- Knappable Stone,
- Dry Grass / Fibre Plant.

These spawn naturally in appropriate environments.

### Primitive Materials

Add:

- Stone Flake,
- Prepared Fibre,
- Cordage,
- Suitable Haft,
- Primitive Axe Head.

### Primitive Tools

Add:

- Hammerstone,
- Primitive Cutting Tool,
- Hafted Stone Axe.

### Processing

Implement minimal custom process/recipe system supporting:

- inputs,
- required tool capability,
- duration,
- outputs,
- basic byproducts/waste hooks,
- future skill/quality hooks.

Implement the first generic `WorkAction` flow:

```text
2x2 Hand Crafting / recognized operation
→ Craft button
→ server validates + reserves inputs
→ inventory closes/minimizes
→ HUD progress bar
→ completion/cancellation
→ server resolves output
```

The first version may use simplified outcome rules. It must not hardcode recipe-specific timers.

Workstations can remain minimal for this slice.

---

## Gameplay Flow

```text
Spawn
↓
Search surroundings
↓
Pick up branch
↓
Pick up loose stone
↓
Find knappable stone
↓
Use hammerstone to create stone flake
↓
Gather fibre
↓
Prepare fibre
↓
Make cordage
↓
Create primitive cutting tool
↓
Process branch into suitable haft
↓
Create stone axe head
↓
Haft axe head with cordage
↓
Create primitive axe
↓
Fell first small tree
↓
Obtain first log
```

That first log is the milestone.

In vanilla Minecraft the first log is nearly meaningless.

In this mod it should feel like the result of learning, exploration, material selection, and toolmaking.

---

## Minimal Architecture Required

Even this tiny slice should establish reusable systems:

### Resource Registry

Ground resource types and spawn conditions.

### Tool Capability Registry

Examples:

- HAMMERING,
- CUTTING,
- CHOPPING.

### Process Recipe Registry

Do not hardcode every primitive recipe in interaction handlers.

### WorkAction Manager

Runs timed server-authoritative work, tracks progress, reserves inputs, handles interruption, and resolves operation outcomes. Design it so a settler actor can use the same layer later.

### Hand Crafting UI Hook

Reinterpret the inventory 2x2 grid as hand crafting and add an explicit Craft action instead of vanilla instant output pickup.

### Progression Gate Service

Determines whether a vanilla/custom action is allowed.

### Data Validation

At launch, detect:

- missing item references,
- missing tool capabilities,
- invalid recipe outputs,
- duplicate IDs.

---

## Explicitly Out of Scope

Do not add yet:

- settlers,
- skills,
- professions,
- metalworking,
- agriculture,
- complex food,
- detailed temperature,
- whole-tree physics,
- villages,
- advanced world generation,
- hundreds of stone types.

---

## Success Criteria

The vertical slice is successful if:

1. A fresh player cannot use the vanilla Minecraft bootstrap.
2. All required starting materials can be found naturally.
3. The player can understand how to reach the first axe without external commands.
4. The primitive axe is the first practical way to acquire logs.
5. The entire sequence works in multiplayer/server-authoritative play, including timed WorkActions and input reservation.
6. Recipes/content are defined through reusable systems rather than one-off code.
7. Adding a second axe or another stone type would not require redesigning the architecture.
8. The first 10–20 minutes are satisfying enough to justify expanding the mod.
9. The same operation definitions can later be executed by a settler without creating a parallel crafting system.

---

## Next Slice

Once this works, the next target should be:

```text
First Log
↓
Primitive Timber Processing
↓
Primitive Work Surface
↓
Fire Making
↓
Water / Thirst
↓
Shelter
↓
Primitive Stockpile
```

Only after the survival foundation is fun should the first settler be introduced.
