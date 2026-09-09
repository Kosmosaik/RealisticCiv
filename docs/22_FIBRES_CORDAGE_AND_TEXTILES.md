# Fibres, Cordage, and Textiles

## Purpose

RealisticCiv must **not** treat `Plant Fibre` as a magic plant or a universal item that appears directly when ordinary vegetation is punched.

Fibre is a **processed material form** obtained from suitable biological sources. Different plants and processing methods should produce materially different fibres and cordage, with early emergency options remaining available so primitive progression does not become spawn-biome roulette.

This document records the intended progression before the first fibre content is implemented.

---

# Core Rule

The design distinction is:

```text
source plant / plant part
        ↓
harvesting
        ↓
processable stems / bark / bast
        ↓
separation / drying / retting / preparation
        ↓
fibre
        ↓
twisting / spinning / plying / braiding
        ↓
thread / cordage / rope / yarn
        ↓
cloth / nets / canvas / later composites
```

There should be **no generic naturally harvested `Plant Fibre` object** that erases the source material's identity.

---

# Earliest Emergency Route — Grass

Long grasses can be bundled, dried, twisted, and braided into useful primitive cordage. They are a valid bootstrap material, but they should be inferior to purpose-suited bast fibres.

Near-term intended route:

```text
Tall Grass
    ↓ harvest
Long Grass Stems
    ↓ prepare / dry
Prepared Grass
    ↓ twist / ply
Primitive Grass Cordage
```

Short/ordinary grass should provide little or no useful long fibre. Tall grass is the meaningful early source.

Grass cordage should be suitable for low-to-moderate demand uses such as:

- first crude hafting,
- tying bundles,
- temporary shelter bindings,
- simple traps,
- light lashing.

It should not be a universal substitute for strong rope, bowstrings, machinery belts, durable rigging, or advanced textiles.

Potential future material properties include:

- tensile strength,
- fibre length,
- flexibility,
- abrasion resistance,
- water resistance,
- durability.

Exact numeric values are balancing data and are **not locked yet**.

---

# Better Wild Fibre — Nettle

Nettle is the preferred first better wild fibre source for temperate progression.

Conceptual route:

```text
Nettle Plant
    ↓ harvest
Nettle Stalk
    ↓ strip / crush / separate
Nettle Fibre
    ↓ twist / ply
Nettle Cordage
```

Later versions may introduce more detailed retting/drying/combing stages if those stages create worthwhile gameplay rather than inventory bureaucracy.

Nettle should reward exploration because it provides stronger, more durable fibre than emergency grass cordage without requiring established agriculture.

---

# Forest Alternative — Inner Bark / Bast

Suitable trees and shrubs can provide another primitive fibre route.

Conceptual progression:

```text
Suitable Bark
    ↓ remove outer bark
Inner Bark / Bast
    ↓ soak / separate / prepare
Bast Fibre
    ↓
Bast Cordage
```

This is important for RealisticCiv's broader philosophy: a player should often have **several physically plausible ways to solve a material problem**, with different efficiency and quality, rather than one mandatory magic ingredient.

The exact tree species and bark-harvesting mechanics remain future work.

---

# Agricultural Fibre Crops

## Flax

Flax should eventually support a substantial textile-production branch:

```text
Flax Plant
    ↓ harvest
Flax Stalks
    ↓ retting
Retted Flax
    ↓ breaking / scutching
Flax Fibre
    ↓ hackling
Prepared Flax Fibre
    ↓ spinning
Flax Yarn
    ↓ weaving
Linen Cloth
```

This is intentionally much deeper than the first grass-cordage bootstrap. It belongs with settled agriculture, dedicated processing tools, and textile professions.

## Hemp

Hemp is a future strong-fibre crop suited to:

- strong cordage,
- rope,
- canvas,
- coarse textiles,
- later industrial fibre uses.

Its exact crop/processing implementation is not yet scheduled.

---

# Material Identity Must Survive Processing

The game should be able to distinguish materials such as:

```text
Grass Cordage
Nettle Cordage
Bast Cordage
Flax Cordage
Hemp Cordage
```

The final naming convention is still open. We may use explicit item names or material-form presentation such as:

```text
Cordage [Grass]
Cordage [Nettle]
```

Internally, source identity should remain available for requirements, quality, and simulation.

A later operation should ideally ask for a **capability/property threshold** rather than one hardcoded cordage item.

Example concept:

```text
Crude Stone Axe Binding
requires flexible binding with modest tensile strength
→ Grass Cordage works

Demanding Bow String
requires much higher tensile quality and fibre continuity
→ Grass Cordage does not qualify
```

This follows the material-identity philosophy established in `18_MATERIAL_IDENTITY_AND_PROPERTIES.md`.

---

# Fibre Forms

Long-term forms may include:

```text
Raw Plant/Stalk
Prepared Fibre
Thread
Yarn
Cord
Cordage
Rope
Cloth
Canvas
```

Not every source needs to support every form.

Examples:

```text
Grass
→ primitive cordage

Flax
→ fibre
→ thread/yarn
→ cordage
→ linen

Hemp
→ fibre
→ cordage
→ rope
→ canvas
```

The inventory should represent **meaningful transformation stages**, not every microscopic real-world action merely for complexity's sake.

---

# Drying and Retting

Drying is a useful primitive processing concept, but the first hour of gameplay must not become passive waiting.

Possible infrastructure progression:

```text
ground / ambient drying
        ↓
Drying Rack
        ↓
covered drying area
        ↓
controlled heated drying
        ↓
industrial dryer
```

Retting should appear where materially appropriate, especially for later flax/hemp/bast processing. It does not need to block the earliest emergency grass route.

---

# Skills, Time, Waste, and Quality

Fibre processing should eventually use the same operation/WorkAction simulation as other RealisticCiv manufacturing.

Potential outcome dimensions:

- work duration,
- usable fibre yield,
- waste,
- cordage quality,
- tensile capability,
- tool wear,
- skill XP.

These are later systems. Early milestones should prove the process chain first.

---

# Quest Book Direction

A future **Fibres & Textiles** chapter can grow organically across eras:

```text
Long Grass
    ↓
Primitive Grass Cordage
    ↓
┌────────────────┬──────────────────┐
│                │                  │
Wild Bast      Nettle          Cultivated Fibre
│                │                  │
Bast Cordage  Nettle Fibre       Flax / Hemp
                 │                  ↓
             Better Cordage       Retting
                                    ↓
                                  Spinning
                                    ↓
                                   Loom
                                    ↓
                                  Linen
```

Later branches can include:

- rope making,
- nets,
- sailcloth,
- canvas,
- paper-adjacent fibre processing,
- industrial textiles,
- synthetic fibres,
- high-performance fibres,
- carbon fibre and composite materials.

---

# Near-Term Implementation Plan

The first fibre milestone after the Hand Crafting foundation should begin small:

```text
Tall Grass
→ Long Grass Stems
→ Prepared Grass
→ Primitive Grass Cordage
```

Then add a better wild route, preferably:

```text
Nettle Stalk
→ Nettle Fibre
→ Nettle Cordage
```

Flax, hemp, detailed retting, spinning, weaving, looms, and industrial textile processing belong to later milestones.

---

# Locked Decisions

- `Plant Fibre` is **not** a naturally occurring generic plant/item.
- Fibre is processed from identifiable source materials.
- Tall grass is a legitimate but low-grade emergency fibre source.
- Better wild fibre sources should exist; nettle is the preferred first candidate.
- Flax/hemp and more detailed textile processing are later progression.
- Inner-bark/bast fibre is a planned alternative path.
- Different fibre/cordage sources should retain meaningful material identity.
- A recipe/operation should eventually care about material capability where appropriate, not simply accept every cordage as identical.
- Detail should be added where it produces meaningful decisions; do not create inventory-stage clutter solely for realism.
