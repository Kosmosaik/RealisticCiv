# World Resources and Early Game

## Early-Game Goal

The first phase should teach the player that the environment contains resources *before* manufactured tools exist.

The player should not begin by destroying blocks with bare hands.

Instead, the opening loop is observation, gathering, selection, and primitive processing.

---

## Ground Resources

Small physical resource objects should appear throughout the world.

Potential examples:

- twigs,
- branches,
- deadwood,
- bark,
- dry grass,
- fibre plants,
- loose stones,
- hammerstones,
- knappable stone nodules,
- clay deposits,
- edible plants,
- mushrooms,
- shells,
- bones later.

They may be represented as:

- small blocks,
- low-profile models,
- collectible entities,
- clustered ground-cover features.

---

## Implementation Status — v0.1.2

A reusable `GroundResourceBlock` system now exists for the first three loose resources:

- Branch,
- Granite Stone,
- Flint Nodule.

They appear as small non-colliding world objects and are collected through server-authoritative right-click pickup. Their current JSON-defined surface distribution is intentionally simple: branches favor forest biomes, Granite Stones use broad exposed Overworld surfaces, and Flint Nodules are rarer and restricted to gravel/sand-like support surfaces.

This is a bootstrap implementation, not the final geology/ecology model. See `19_GROUND_RESOURCES_AND_BOOTSTRAP_LOCKS.md`.



### Fibre Resource Clarification

`fibre resources` in this document means **real source vegetation/materials**, not a generic `Plant Fibre` ground pickup. The planned emergency route starts from Tall Grass and produces Long Grass Stems, then Prepared Grass, then Primitive Grass Cordage. Nettle is the preferred first better wild source; suitable inner bark/bast and cultivated flax/hemp are later alternatives. See `22_FIBRES_CORDAGE_AND_TEXTILES.md`.

## Biome/Environment Distribution

### Forests

Common:

- branches,
- twigs,
- bark,
- deadwood,
- fungi,
- fibre resources.

### Riverbanks

Common:

- rounded stones,
- gravel,
- clay,
- certain knappable stones,
- reeds/fibres.

### Rocky Terrain

Common:

- loose stone,
- exposed geological resources,
- ore indicators.

### Grassland

Common:

- grasses,
- fibres,
- seeds,
- occasional branches.

### Wetlands

Potential:

- reeds,
- clay,
- peat,
- bog iron later.

---

## Stone Selection and Material Identity

Not every stone should automatically create a sharp tool, and RealisticCiv should **not** introduce a generic item named `Tool Stone`.

Stone resources have material identity from the beginning. The first bootstrap materials are:

- **Granite Stone** — common loose stone; suitable as a hammerstone/general-purpose hard stone, but poor for making sharp flakes.
- **Flint Nodule** — high-quality knappable stone suitable for flakes and later knapped tool heads.

Future materials may include:

- chert,
- obsidian,
- quartzite,
- basalt,
- limestone,
- additional regional/geological stone types.

Operations should ask for capabilities/properties such as `HAMMERSTONE`, `KNAPPABLE`, edge quality, toughness, or minimum knappability rather than requiring one artificial all-purpose stone item. Broad tags such as `loose_stones` are categories; they do not erase the underlying material identity.

See `18_MATERIAL_IDENTITY_AND_PROPERTIES.md`.

---

## Primitive Knapping

Initial process:

```text
Knappable Material (e.g. Flint Nodule)
+ Hammerstone (e.g. Granite Stone)
→ Stone Flake
```

Possible outputs:

- sharp flake,
- core,
- stone waste.

More advanced knapping later:

- blades,
- points,
- axe heads,
- scraper heads.

---

## Fibre and Cordage

Example chain:

```text
Fibre Plant
→ Raw Fibre
→ Prepared Fibre
→ Twisted Cordage
```

Cordage enables:

- hafting,
- baskets,
- traps,
- bows,
- shelters,
- fishing equipment.

---

## First Cutting Tool

Example:

```text
Sharp Stone Flake
+ Simple Grip / Binding
→ Primitive Cutting Tool
```

This unlocks better processing of:

- bark,
- fibre,
- branches,
- hides later,
- food.

---

## First Axe

Suggested structure:

```text
Stone Axe Head
+ Suitable Haft
+ Cordage
→ Hafted Stone Axe
```

The first axe allows the player to begin harvesting small trees and useful timber.

---

## Tree Progression

Potential progression:

### No Tool

- collect fallen wood,
- break very small twigs only.

### Primitive Cutting Tool

- process branches,
- strip bark,
- cut saplings.

### Primitive Axe

- fell small trees,
- process logs slowly.

### Improved Axe

- fell larger trees more efficiently.

Later:

- saws,
- logging tools,
- powered saws,
- mechanized forestry.

---

## Water

Thirst creates an immediate survival need.

Potential early sources:

- rivers,
- lakes,
- ponds,
- rain collection later,
- springs later.

Water quality can eventually include:

- clean,
- questionable,
- contaminated,
- boiled,
- filtered.

The first implementation may use a simpler clean/unsafe model.

---

## Fire

Fire is a major early milestone.

Possible progression:

```text
Dry Tinder
+ Kindling
+ Fire-Making Tool
→ Controlled Fire
```

Potential fire-making technologies:

- friction fire,
- percussion sparks,
- later matches/lighters.

Fire unlocks:

- warmth,
- cooking,
- water boiling,
- charcoal,
- pottery later,
- metallurgy later.

---

## Early Shelter

Primitive construction could initially use:

- branches,
- fibre bindings,
- leaves/thatch,
- hides later.

The goal is not an architectural simulator at first. Shelter should primarily establish the settlement and provide beds/storage/work areas.

---

## Intended First 30–60 Minutes

```text
Spawn
↓
Survey terrain
↓
Locate water
↓
Collect branches / grass / stones
↓
Find a suitable knappable material (initially Flint Nodule)
↓
Make first stone flake
↓
Prepare fibre
↓
Make cordage
↓
Create primitive cutting tool
↓
Process better branches/materials
↓
Make primitive axe
↓
Fell first small tree
↓
Acquire usable timber
↓
Create fire-making equipment
↓
Establish controlled fire
↓
Build basic shelter / stockpile
```

No vanilla crafting table, pickaxe, furnace, or instant planks should be required or available during this loop.
