# Project Vision

## High-Level Concept

A Minecraft Java total-conversion mod where the player begins with no practical technology and gradually builds a functioning civilization with settlers.

Progression begins with gathering branches, stones, fibre, water, and food by hand. The player learns primitive toolmaking, fire, shelter construction, woodworking, agriculture, pottery, metallurgy, mechanical power, industrial manufacturing, electricity, chemistry, transport, automation, electronics, computing, spaceflight, and eventually advanced science-fiction technologies.

The project should feel like a blend of:

- Minecraft exploration and building.
- Colony-management and settler simulation.
- Realistic production-chain games.
- Survival and primitive-technology progression.
- Expert-pack-style Quest Book guidance with free chapter focus and meaningful cross-chapter gates.
- Industrial automation and logistics.
- Long-form civilization development.

The objective is not to simulate reality for its own sake. The objective is to make technological development feel earned, visible, understandable, and mechanically meaningful.

---

## Player Fantasy

The core fantasy is:

> Start with nothing. Discover how to survive. Build tools. Establish a settlement. Train specialists. Develop industries. Mechanize work. Electrify the colony. Automate production. Build advanced technology. Reach beyond the planet.

A late-game factory should feel impressive because the player remembers when a single straight branch and a sharp stone were valuable resources.

---

## Design Pillars

### 1. Technology Is a Process

Technology is not merely an unlock screen.

A technology should usually require some combination of:

- knowledge,
- tools,
- suitable materials,
- manufacturing capability,
- workstations,
- skilled workers,
- infrastructure,
- energy,
- prerequisite technologies.

Unlocking a concept makes something possible. It does not make production effortless.

### 2. Realistic Crafting Without Pointless Busywork

Production chains should model meaningful steps such as:

- harvesting,
- cutting,
- splitting,
- drying,
- crushing,
- washing,
- roasting,
- smelting,
- casting,
- forging,
- machining,
- heat treatment,
- assembly.

However, the player should not have to manually perform every microscopic manufacturing action forever. Progression should gradually shift repetitive work to settlers, machines, and automation.

### 3. Settlers Are People, Not Crafting Blocks

Settlers should have:

- health,
- hunger,
- thirst,
- energy,
- inventories,
- professions,
- skills,
- job assignments,
- schedules,
- housing,
- needs,
- work priorities.

They should physically move through the colony, collect inputs, perform jobs, transport outputs, eat, drink, rest, and improve their skills.

### 4. Progress Should Transform the Settlement

Technological advancement should physically alter everyday life.

Examples:

- Hand carrying -> basket -> cart -> wagon -> truck -> conveyor -> autonomous logistics.
- Hand-sawn wood -> pit saw -> water-powered sawmill -> powered sawmill -> automated lumber mill.
- Hand hammering -> forge -> power hammer -> machine shop -> CNC manufacturing -> robotic fabrication.
- Open fire -> charcoal -> steam -> electricity -> nuclear -> advanced power systems.

### 5. The World Is the Resource Base

The player should care where resources come from.

Different environments should provide different useful materials:

- forests,
- rivers,
- wetlands,
- mountains,
- clay deposits,
- exposed ore,
- fertile soil,
- salt,
- stone types,
- fuel sources.

### 6. Vanilla Minecraft Must Not Bypass Progression

The mod cannot coexist with the vanilla bootstrap unchanged.

Players must not be able to skip primitive progression by:

- punching trees,
- converting logs instantly to planks,
- using vanilla crafting-table recipes,
- finding advanced tools in chests,
- trading for advanced materials,
- farming iron golems,
- entering existing villages and stealing advanced workstations.

All relevant vanilla content must be audited and either removed, repurposed, gated, or integrated.

### 7. Work Takes Time

Crafting and production should not primarily be instant inventory conversions. Meaningful operations consume time and may depend on skill, tools, working conditions, and interruption state.

The same underlying work definition should be reusable by players and settlers so that a player shaping a wooden handle and a settler shaping the same handle are participating in one coherent simulation.

### 8. Multiplayer Is Architectural, Not Optional Polish

The simulation should be server-authoritative from the beginning. Players may cooperate in shared settlements, and important systems such as settlers, production, reservations, work orders, skills, and knowledge must not assume a single local player.

Knowledge should be capable of existing at multiple scopes: individual player, individual settler, settlement/civilization, and eventually written/digital records.

### 9. Data-Driven and Extensible

Content definitions should be separated from core logic wherever practical.

The project is expected to eventually contain:

- thousands of items/material states,
- many hundreds of production operations,
- numerous workstations,
- many skill types,
- settlers and professions,
- large technology graphs.

Hardcoded one-off logic should be avoided.

---

## Scope Philosophy

This is intentionally a very large long-term project, but development should proceed through small vertical slices.

Each milestone should produce a playable and testable loop.

The project should never attempt to implement the entire Stone Age, Bronze Age, Industrial Revolution, modern world, and science-fiction era at once.

---

## Non-Goals for Early Development

The first versions do not need:

- hundreds of settlers,
- genetics,
- complex social relationships,
- detailed disease simulation,
- dynamic politics,
- global trade,
- vehicles,
- advanced combat,
- spaceflight,
- complete geology,
- every real manufacturing process.

These systems may be added later if the core architecture supports them.

---

## Long-Term End State

A mature world should support a progression such as:

```text
Bare Survival
    ↓
Primitive Tools
    ↓
Fire & Shelter
    ↓
Food Security
    ↓
Pottery & Agriculture
    ↓
Advanced Woodworking
    ↓
Copper Metallurgy
    ↓
Bronze Metallurgy
    ↓
Iron & Steel
    ↓
Mechanical Power
    ↓
Early Industry
    ↓
Steam Industry
    ↓
Electricity
    ↓
Modern Chemistry
    ↓
Internal Combustion
    ↓
Mass Production
    ↓
Electronics
    ↓
Computing
    ↓
Automation & Robotics
    ↓
Advanced Energy
    ↓
Spaceflight
    ↓
Science-Fiction Technologies
```

The player should be able to point at almost any advanced machine and trace its existence backward through materials, tools, industrial processes, specialist workers, knowledge, and infrastructure.
