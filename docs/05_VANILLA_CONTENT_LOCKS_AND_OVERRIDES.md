# Vanilla Content Locks and Overrides

## Purpose

The intended progression will fail if vanilla Minecraft allows the player to bypass primitive or industrial development.

Therefore vanilla content must be systematically audited and classified as:

- keep unchanged,
- keep but rebalance,
- gate behind technology,
- replace behavior,
- replace recipe,
- replace loot,
- disable,
- repurpose.

This document defines the main problem areas.

---

# Immediate Early-Game Changes

## Tree Punching

Vanilla behavior:

```text
Bare fist → Log block
```

Required behavior:

- bare-hand log harvesting should be impossible or nonproductive,
- actual felling requires suitable cutting capability,
- tree harvesting may later use whole-tree felling rather than individual log mining.

## Planks

Vanilla:

```text
1 Log → 4 Planks instantly
```

Required:

Replace with realistic timber processing stages.

Potential progression:

```text
Log
→ Split Log
→ Hewn Timber
→ Rough Board
→ Sawn Board
```

## Sticks

Vanilla:

```text
Planks → Sticks
```

Required:

Early sticks/branches come from the environment.

Manufactured dowels/rods may later be different components.

## Crafting Table

Vanilla crafting table provides enormous progression bypass potential.

Options:

- remove vanilla recipe,
- repurpose block as a later workstation,
- disable most vanilla recipes,
- replace with custom workstation system.

## Wooden Tools

Vanilla wooden tools should likely be removed or fundamentally redesigned.

A wooden pickaxe makes little sense in the intended progression.

## Stone Tools

Vanilla cobblestone tools should not be craftable through normal recipes.

Primitive stone tools should use realistic components such as:

- tool stone,
- haft,
- bindings.

---

# Furnace and Smelting

Vanilla furnace:

```text
Ore + fuel → refined metal
```

This is far too powerful for early progression.

Required changes:

- remove/gate vanilla furnace recipe,
- block vanilla ore-to-ingot recipes,
- replace with dedicated metallurgy chains,
- introduce furnace types and temperature capabilities,
- model fuel and refractory development.

---

# Mining

Vanilla mining progression must be reconsidered.

Potential early resource access:

- loose stone,
- surface deposits,
- exposed ore,
- placer deposits,
- shallow digging,
- bog iron,
- native metals.

Hard-rock mining may require later technologies:

- hammer/chisel,
- fire-setting,
- supports,
- drainage,
- hoisting,
- explosives,
- powered drilling.

---

# Structures

Generated structures may contain progression-breaking blocks or loot.

Audit:

- villages,
- mineshafts,
- shipwrecks,
- ruined portals,
- desert pyramids,
- jungle temples,
- strongholds,
- ancient cities,
- woodland mansions,
- trail ruins,
- dungeons,
- Nether structures,
- End structures.

Each should either:

- have revised loot,
- be removed,
- be re-themed,
- contain degraded/unknown technology,
- require knowledge to use recovered items.

---

# Villages

Vanilla villages are a major bypass source.

Problems:

- beds,
- advanced food,
- workstations,
- crops,
- iron golems,
- trading,
- crafted blocks.

Possible approaches:

1. Disable vanilla villages entirely.
2. Replace villages with primitive settlements.
3. Make villagers separate cultures with technology appropriate to world settings.
4. Keep villages but prevent their items from bypassing knowledge/capability requirements.

This decision can be deferred, but villages should probably be disabled during early development.

---

# Villager Trading

Trading must be audited or disabled.

Otherwise the player could obtain:

- tools,
- armor,
- glass,
- redstone items,
- enchanted items,
- advanced food.

---

# Iron Golems

Vanilla iron-golem farming is incompatible with realistic metallurgy.

Possible solutions:

- iron golems do not drop usable iron,
- disable iron golems,
- redesign drops,
- redesign village defenses.

---

# Mob Drops

Audit all mob drops that can create progression shortcuts.

Examples:

- iron equipment,
- gold equipment,
- redstone-like materials,
- gunpowder,
- enchanted items,
- bottles/potions.

---

# Nether and End

Vanilla dimensional progression should probably be heavily delayed and redesigned.

Potential requirements before Nether access:

- advanced refractory materials,
- precision construction,
- energy technology,
- scientific knowledge.

The Nether/End could eventually be reinterpreted as exotic environments rather than early-mid-game loot dimensions.

---

# Enchanting and Brewing

These systems need an explicit design decision.

Options:

- remove magic entirely,
- reinterpret systems scientifically,
- retain them but place them in a separate fantasy branch,
- postpone them until much later development.

Default recommendation: disable or ignore during initial development.

---

# Redstone

Redstone is effectively magical electricity and automation.

It must not be accessible before the project's own mechanical/electrical progression.

Options:

- disable vanilla redstone recipes,
- gate redstone behind advanced knowledge,
- reinterpret redstone as a late material,
- replace major redstone functionality with custom mechanical/electrical systems.

---

# Food

Vanilla food acquisition and cooking may trivialize survival.

Audit:

- raw food availability,
- farming speed,
- crop yields,
- cooking recipes,
- animal breeding,
- food storage,
- hunger restoration.

Detailed nutrition can be added later; early versions only need to prevent obvious progression shortcuts.

---

# Beds and Respawning

Beds may need changes if shelter and settlement development are meaningful.

Potential options:

- primitive sleeping surfaces before beds,
- bed requires textiles/carpentry,
- respawn logic separated from sleeping furniture.

---

# Armor and Weapons

Vanilla recipes should be replaced by realistic manufacturing processes.

Examples:

- hide/leather preparation,
- metal sheet production,
- forging,
- riveting,
- tailoring.

---

# Progression Audit Rule

For every vanilla feature ask:

> Can this feature provide a material, capability, block, tool, or shortcut earlier than the custom technology graph intends?

If yes, it must be modified.
