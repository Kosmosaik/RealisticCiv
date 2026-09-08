# Settlers and Colony Simulation

## Vision

Settlers should replace the role of passive vanilla villagers with simulated colony members who work, learn, consume resources, and physically participate in production.

The recommended architecture is a **custom Settler entity/system**, rather than permanently relying on invasive modifications to the vanilla Villager class.

Vanilla villagers may still exist separately or potentially be recruitable/conversible into settlers.

---

## Core Settler Data

Each settler should be capable of storing:

### Identity

- name,
- age later,
- sex/gender if eventually useful,
- traits later,
- family/relationships later.

### Needs

Initial:

- health,
- hunger,
- thirst,
- energy.

Possible later additions:

- body temperature,
- morale,
- comfort,
- hygiene,
- social needs.

### Work Data

- profession,
- skills,
- assigned job,
- current task,
- job priorities,
- workplace,
- residence,
- inventory,
- carrying capacity.

---

## Skills

Skills represent individual competence.

Potential skills include:

- foraging,
- hunting,
- fishing,
- farming,
- animal handling,
- knapping,
- woodworking,
- carpentry,
- mining,
- masonry,
- pottery,
- smelting,
- smithing,
- machining,
- mechanics,
- chemistry,
- medicine,
- electronics,
- computing.

Skill list should be data-driven.

---

## Profession vs Skill vs Job

These must be different concepts.

### Profession

Long-term specialization.

Example: `Blacksmith`

### Skill

Actual ability.

Example: `Smithing 42`

### Job Assignment

What the settler is currently expected to do.

Example: `Operate Bloomery`

A blacksmith may temporarily mine, haul, or build if reassigned.

---

## Profession Examples

Primitive / early:

- gatherer,
- hunter,
- fisher,
- toolmaker,
- potter,
- farmer,
- woodworker.

Metal ages:

- miner,
- charcoal burner,
- smelter,
- blacksmith,
- carpenter,
- mason.

Industrial:

- machinist,
- mechanic,
- foundry worker,
- engineer,
- chemist,
- electrician.

Modern / advanced:

- electronics technician,
- programmer,
- robotics technician,
- materials scientist,
- aerospace engineer.

---

## Needs Behavior

Needs must affect AI priorities.

Example thirst thresholds:

```text
50–100: normal
25–49: mild penalties
10–24: strong work penalty
1–9: abandon non-critical work and seek water
0: dehydration damage
```

The exact values should be tuned rather than hardcoded into design assumptions.

A settler may interrupt work, satisfy an urgent need, then return to the previous job.

---

## Daily Schedule

Settlers should eventually support schedules.

Primitive example:

```text
sunrise   wake / eat
morning   work
midday    eat / drink
later     work
sunset    return to settlement
night     social / rest / sleep
```

Industrial settlement schedules may later support work shifts.

---

## Inventory and Carrying

Settlers should physically carry materials rather than teleport resources.

Potential progression:

```text
Hands
→ Basket
→ Backpack
→ Handcart
→ Wheelbarrow
→ Wagon
→ Truck
→ Forklift
→ Conveyor Network
→ Autonomous Logistics Vehicle
```

Carrying capacity may use:

- item slots,
- mass,
- bulk,
- or a hybrid system.

The exact model should remain configurable.

---

## Job Flow

Example work order:

```text
Produce 20 Wooden Pegs
```

A worker should:

1. acquire the job,
2. determine required inputs,
3. request missing resources,
4. reserve inputs,
5. travel to stockpile,
6. collect inputs,
7. travel to workstation,
8. perform production,
9. carry output to destination stockpile,
10. mark order progress.

---

## Settlement Manager

Performance requires shared settlement-level data.

Do **not** let every settler repeatedly scan the entire loaded world.

The settlement should maintain centralized registries such as:

```text
SettlementManager
├── Settler Registry
├── Stockpile Registry
├── Resource Index
├── Workstation Registry
├── Housing Registry
├── Job Queue
├── Work Order Registry
├── Logistics Requests
└── Reservations
```

Settlers query this system for useful work.

---

## Resource Requests

Example:

```text
Work Order: Wooden Wheel
Missing: 6 Seasoned Boards
```

The colony creates a demand request.

Possible outcomes:

- hauler retrieves boards from another stockpile,
- carpenter produces boards,
- lumber worker requests logs,
- logger creates timber supply.

This forms a production and logistics network.

---

## Reservations

Resources and workplaces should be reservable so multiple settlers do not attempt to use the same item or workstation simultaneously.

Reservation system should handle:

- input stacks,
- workstation slots,
- storage capacity,
- haul destinations,
- beds,
- special tools.

---

## Skill Progression

Skills increase through actual work.

Example:

```text
Knapping stone flake → Knapping XP
Forge iron bar → Smithing XP
Build timber frame → Carpentry XP
Operate lathe → Machining XP
```

Skill gains may depend on:

- recipe difficulty,
- time spent,
- success/failure,
- instructor/training systems later.

---

## Traits — Later Feature

Potential examples:

- strong,
- hardy,
- quick learner,
- patient,
- clumsy,
- inventive.

Traits should modify existing systems rather than introduce excessive special cases.

---

## Longer-Term Social Simulation

Possible future additions:

- morale,
- relationships,
- families,
- children,
- aging,
- education,
- injuries,
- disease,
- personal preferences,
- leadership,
- migration.

These should not be required for the first colony prototype.
---

## Shared Work-Action Execution

Settlers should not receive a separate simplified crafting simulation if a real player can perform the same operation.

Whenever practical:

```text
Production Operation
├── Player-controlled WorkAction
└── Settler-controlled WorkAction
```

Both use the same:

- inputs,
- tool capabilities,
- workstation requirements,
- base duration,
- knowledge requirements,
- skill formulas,
- output/byproduct rules,
- failure/waste rules,
- XP rules.

The difference is who selects and controls the action.

This keeps player crafting and colony production mechanically consistent.

---

## Knowledge

Settlers should eventually hold personal knowledge independently of their skills.

A settler may:

- know a process but be inexperienced at it,
- learn from a player,
- teach a player,
- teach another settler,
- learn through apprenticeship or observation,
- preserve rare knowledge for the settlement.

Settlement-level and multiplayer knowledge rules are defined in `12_MULTIPLAYER_AND_KNOWLEDGE.md`.

