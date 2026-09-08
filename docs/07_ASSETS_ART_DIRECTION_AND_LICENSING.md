# Assets, Art Direction, and Licensing

## Goal

The project may eventually require thousands of items and hundreds of visible machines/workstations. Asset production must therefore be scalable and legally clean.

---

## Core Rule

**Free to download does not mean free to reuse.**

Assets from other mods, modpacks, websites, or repositories must not be copied into this project unless their license explicitly permits reuse or the original author grants permission.

Permission to include a mod in a modpack does not automatically allow extracting its textures/models for another mod.

---

## Preferred Asset Sources

Order of preference:

1. Original project assets.
2. CC0 / public-domain assets.
3. CC BY assets.
4. Other clearly permissive licenses.
5. Assets with explicit written author permission.
6. Avoid uncertain or all-rights-reserved material.

---

## License Tracking

Every third-party asset should record:

- asset name,
- source,
- creator,
- license,
- version/date retrieved,
- whether modified,
- required attribution,
- redistribution conditions.

Recommended documents:

```text
ASSET_CREDITS.md
ASSET_LICENSES.md
```

---

## Recommended Directory Strategy

```text
assets/
├── original/
├── generated/
├── third_party/
└── vanilla_references/
```

Third-party assets should never be mixed into untracked project-original folders.

---

## Vanilla Minecraft Assets

Where useful, reference existing vanilla assets at runtime rather than copying them into the mod.

Examples of useful vanilla visual vocabulary:

- logs,
- planks,
- sticks,
- ingots,
- nuggets,
- leather,
- string,
- common foods,
- block materials.

Custom gameplay still requires careful handling of Mojang's usage terms, but runtime references are preferable to redistributing large copied asset collections.

---

## Asset Family Strategy

Thousands of gameplay definitions must **not** mean thousands of individually designed models.

Use reusable visual families.

### Ore Family

Shared form:

`ore_chunk`

Material variants:

- copper,
- iron,
- tin,
- lead,
- nickel.

### Powder Family

Shared form:

`material_powder`

Variants:

- charcoal dust,
- crushed ore,
- limestone powder,
- clay powder.

### Metal Stock Family

Reusable models:

- billet,
- bar,
- rod,
- sheet,
- wire,
- plate,
- pipe.

Material appearance is applied through texture variants.

---

## Detail Budget

### Low Asset Priority

Simple inventory materials:

- nails,
- rivets,
- screws,
- dusts,
- fibres,
- strips,
- plates,
- rods,
- billets.

### Medium Priority

Recognizable hand tools:

- stone axe,
- bow drill,
- auger,
- saw,
- hammer,
- tongs.

### High Priority

Major world machines and milestones:

- bloomery,
- waterwheel,
- forge,
- lathe,
- steam engine,
- blast furnace,
- generator,
- engine,
- CNC machine,
- industrial robot,
- advanced reactor.

Detailed modeling effort should be concentrated here.

---

## Modular Machine Components

Create reusable components for world models:

```text
machine_parts/
├── frames/
├── legs/
├── shafts/
├── gears/
├── belts/
├── pulleys/
├── motors/
├── pipes/
├── tanks/
├── valves/
├── gauges/
├── handles/
└── furnace_parts/
```

This allows machines to share a coherent visual language.

---

## Art Direction

Default recommendation:

- visually compatible with Minecraft,
- readable silhouettes,
- restrained texture noise,
- recognizable materials,
- modest model complexity,
- major technology milestones visibly distinct.

A consistent 16×16 or 32×32 texture philosophy should be chosen early.

---

## Generated Assets

Original generated pixel art and model definitions can be used where practical, especially for simple material icons and components.

Generated assets should still be reviewed for:

- visual consistency,
- readability,
- accidental similarity to existing third-party work,
- technical correctness.

---

## Author Permission

When an ideal asset exists under a restrictive license, contact the author and request explicit permission.

Record permission in project documentation.

Never rely on informal assumptions such as:

- "everyone uses it",
- "it's in a public modpack",
- "the mod is free",
- "the repository is public".
