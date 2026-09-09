# Material Identity and Properties

**Implementation note (v0.1.3):** multiple item forms can now bind to one material identity. `Flint Nodule`, `Flint Core`, `Flint Flake`, and `Flint Chips` all resolve to the same Flint material definition, while form-specific item tags control which operations accept each form.

## Purpose

RealisticCiv should model **what a material actually is** rather than inventing generic progression items such as `Tool Stone`.

This decision begins in the primitive stage and is intended to scale through metallurgy, engineering, chemistry, modern industry, and sci-fi manufacturing.

A recipe/process should preferably ask:

> Does this input have the physical/capability properties required by this operation?

rather than:

> Is this exactly the one magic item hardcoded for this recipe?

---

# Locked Decision: No Generic `Tool Stone`

There is no planned inventory item called `Tool Stone`.

The first stone resources use real material identity immediately:

| Item | Material identity | Initial role |
|---|---|---|
| `realisticciv:granite_stone` | `realisticciv:granite` | Common loose hard stone / hammerstone |
| `realisticciv:flint_nodule` | `realisticciv:flint` | High-quality knappable raw stone |

`Loose Stone` remains a useful **category/concept** for world resources, but not necessarily one generic item. For example, both Granite Stone and Flint Nodule can belong to the `realisticciv:loose_stones` tag while retaining different identities and behavior.

---

# Bootstrap Material Model

v0.1.1 establishes a small Java bootstrap model:

```text
MaterialDefinition
├── id
├── traits
├── knappability
├── edgeQuality
├── toughness
└── hardness
```

Initial traits:

```text
STONE
HAMMERSTONE
KNAPPABLE
```

These are deliberately minimal. They exist to establish the architectural direction, not to freeze the final geology/material simulation.

The bootstrap values are normalized from `0.0` to `1.0` and are subject to balancing/research later.

---

# Bootstrap Materials

## Granite

Current design intent:

- hard,
- tough,
- suitable as a hammerstone,
- poor at producing high-quality sharp flakes,
- common/general-purpose loose stone candidate.

Bootstrap values:

```text
knappability  0.15
edge quality  0.15
toughness     0.90
hardness      0.85
```

## Flint

Current design intent:

- excellent conchoidal fracture / knapping material,
- excellent sharp-edge potential,
- less tough than granite,
- important primitive cutting/tool-head resource.

Bootstrap values:

```text
knappability  0.95
edge quality  0.95
toughness     0.55
hardness      0.75
```

These numbers are gameplay bootstrap values, not claims of finalized scientific measurement.

---

# Tags Versus Material Properties

Use both levels for different purposes.

## Coarse tags/capabilities

Examples:

```text
realisticciv:loose_stones
realisticciv:hammerstones
realisticciv:knappable_stones
realisticciv:natural_branches
```

These are useful when an operation only needs a broad yes/no category.

Example:

```text
Primitive percussion knapping
requires:
  1 x #realisticciv:knappable_stones
  1 x #realisticciv:hammerstones
```

## Numeric/material properties

Later processes may need more nuance:

```text
Fine pressure flaking
requires:
  KNAPPABLE
  knappability >= 0.80
  edgeQuality >= 0.75
```

Different accepted materials can then affect:

- duration,
- waste,
- failure risk,
- edge quality,
- durability,
- output quality,
- tool suitability.

This avoids duplicating nearly identical recipes for every real material.

---

# Future Stone Materials

Potential additions include:

- chert,
- quartzite,
- obsidian,
- basalt,
- limestone,
- sandstone,
- slate,
- additional regional/geological materials.

They should be added because they create useful world/material differences, not simply to inflate item count.

---

# Scaling Beyond Stone

The same philosophy should eventually support forms such as:

```text
Copper
├── native copper
├── ore/mineral identity
├── molten copper
├── cast copper
├── wrought/work-hardened copper
├── sheet
├── rod
└── wire
```

and later:

```text
Steel
├── composition/alloy identity
├── heat-treatment state
├── stock form
├── surface/processing state
└── quality/tolerance where relevant
```

The goal is not to put every physical property on every ItemStack. The goal is to preserve enough material identity and state for realistic processes to make meaningful distinctions.

---

# Data-Driven Direction

The v0.1.1 `MaterialCatalog` is intentionally a bootstrap implementation so the first items have validated material behavior before the full data loader exists.

Long term, material definitions should become data-driven/validated where practical, for example conceptually:

```json
{
  "id": "realisticciv:flint",
  "traits": ["stone", "knappable"],
  "properties": {
    "knappability": 0.95,
    "edge_quality": 0.95,
    "toughness": 0.55,
    "hardness": 0.75
  }
}
```

The exact schema is **not locked yet**.

Operation code should therefore query a material service/capability model rather than depend directly on the bootstrap storage implementation.

---

# Current Implementation — v0.1.1

Implemented foundation:

- registered `Branch`, `Granite Stone`, and `Flint Nodule` items,
- stable item resource keys,
- RealisticCiv creative tab for development inspection,
- initial material definitions for granite and flint,
- coarse material/resource tags,
- startup validation of material mappings/required traits,
- datagen providers for English names, item models, client-item definitions, and item tags,
- original placeholder 16x16 textures for the three bootstrap items.

Not implemented yet:

- natural ground spawning,
- pickup interaction,
- knapping,
- hammerstone tool interaction,
- stone flakes,
- WorkAction integration,
- data-pack material loading,
- geological distribution.

Those belong to later roadmap milestones.


# Fibre Material Identity Direction (planned after v0.1.5)

The same material-identity principle applies to biological fibres. RealisticCiv must not collapse tall grass, nettle, bast, flax, and hemp into one magic naturally harvested `Plant Fibre` item. Source materials should be processed into fibre/cordage forms, and later operations may require material-capability thresholds such as tensile strength, fibre length, flexibility, abrasion resistance, or water resistance.

See `22_FIBRES_CORDAGE_AND_TEXTILES.md`.
