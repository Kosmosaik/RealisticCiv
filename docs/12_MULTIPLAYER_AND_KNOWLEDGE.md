# Multiplayer and Knowledge

## Goal

Multiplayer must be treated as a first-class architectural requirement from the beginning rather than a later compatibility feature.

The core principle is:

> The server owns the civilization simulation. Players send requests and receive synchronized state.

This applies even in singleplayer through Minecraft's integrated logical server.

---

# Multiplayer Design Principles

- Important simulation state is authoritative on the server.
- Clients never decide that a craft, unlock, assignment, transfer, or production action succeeded.
- Settlers belong to settlements rather than directly to one player whenever practical.
- Multiple players may cooperate in one settlement.
- Multiple settlements should be possible on one server.
- Permission systems should control who can modify shared colony state.
- Simultaneous actions must be validated and resolved safely.
- Knowledge must not be modeled as one global boolean list.

---

# Knowledge Scopes

Knowledge should exist at multiple scopes.

## Player Knowledge

Represents what an individual player character personally understands.

Examples:

- Stone Knapping,
- Fire Making,
- Basic Carpentry,
- Copper Smelting.

Personal skills remain separate from personal knowledge.

A player may know how a process works while still being unskilled at performing it.

---

## Settler Knowledge

Represents knowledge possessed by a specific settler.

Example:

```text
Anna

Knowledge:
- Advanced Joinery
- Wheel Construction

Skills:
Carpentry 73
Woodworking 58
```

This allows settlers to teach, learn, specialize, and preserve knowledge across generations.

---

## Settlement / Civilization Knowledge

Represents knowledge that has become established within a settlement.

Example:

```text
River Camp

Established Knowledge:
✓ Fire Making
✓ Stone Knapping
✓ Basic Carpentry
✓ Pottery
✗ Copper Smelting
```

Settlement knowledge can control what the colony may:

- request through work orders,
- construct,
- teach formally,
- display in recipe/workstation interfaces,
- organize as established production.

It does not automatically mean every individual has high personal skill.

---

## Documented Knowledge

Later technologies should allow knowledge to exist independently of a living person.

Potential progression:

```text
Oral Teaching
↓
Apprenticeship
↓
Clay Tablet / Marked Record
↓
Papyrus / Parchment
↓
Books
↓
Printing
↓
Technical Manuals
↓
Libraries
↓
Schools / Universities
↓
Digital Databases
↓
Networked Information Systems
```

A knowledge document should be capable of teaching or restoring knowledge under suitable conditions.

This makes information preservation a real civilization technology.

---

# Knowledge Is Not Skill

These concepts must remain distinct.

```text
Knowledge
= understands that an operation/process exists and how it is conceptually performed

Skill
= competence gained by actually performing or training the work
```

Example:

```text
Settlement knows Copper Smelting.

Player A:
Copper Smelting knowledge ✓
Smelting skill 3

Settler Anna:
Copper Smelting knowledge ✓
Smelting skill 47
```

Both can understand the process, but Anna performs it far more efficiently and reliably.

---

# Knowledge Propagation

Potential ways knowledge can move between actors:

- direct teaching,
- apprenticeship,
- assisting another worker,
- observing work,
- practicing an available process,
- reading a suitable document,
- formal schooling,
- technical training,
- digital learning systems later.

Early versions do not need every propagation method.

The architecture should support them without redesigning knowledge storage.

---

# Player -> Settler Teaching

Example:

```text
Player discovers Copper Smelting
↓
Player performs process
↓
Player teaches Anna
↓
Anna gains Copper Smelting knowledge
↓
Anna practices and raises Smelting skill
```

This lets player discovery become operational colony knowledge through people.

---

# Settler -> Player Teaching

Players should also be able to learn from skilled settlers.

Example:

```text
Anna
Master Carpenter
Carpentry 73
Wheel Construction ✓

Player
Wheel Construction ✗

Interact -> Learn
↓
Training / apprenticeship
↓
Player learns Wheel Construction
```

This gives skilled settlers value beyond labor output.

---

# Knowledge Loss and Preservation

A later optional/hardcore rule may allow knowledge to disappear if every living/documented holder is lost.

Example:

```text
Only Anna knows Advanced Forge Welding
↓
Anna dies before training anyone
↓
Knowledge may be lost locally
```

But if the settlement owns:

```text
Treatise on Forge Welding
```

another worker may eventually relearn it.

The exact severity should be configurable. The architecture should preserve the possibility.

---

# Multiplayer Settlements

A server should not assume one global colony.

Conceptually:

```text
Server
├── Settlement A
│   ├── players
│   ├── settlers
│   ├── knowledge
│   ├── stockpiles
│   ├── work orders
│   └── territory
├── Settlement B
│   └── ...
└── Independent players
```

Players may later:

- create settlements,
- join settlements,
- leave settlements,
- merge settlements,
- trade between settlements,
- exchange knowledge,
- migrate settlers,
- form alliances.

These are later features, but data ownership must not prevent them.

---

# Settler Ownership

Avoid a hard model such as:

```text
settler.owner = playerUUID
```

Prefer:

```text
settler.settlementId = RiverCamp
```

with settlement permissions determining who may issue orders.

This makes cooperative multiplayer natural.

---

# Settlement Permissions

Potential permission examples:

- view settlement information,
- assign settler jobs,
- create work orders,
- modify stockpile rules,
- modify build plans,
- spend restricted resources,
- recruit settlers,
- exile settlers,
- manage members,
- share or trade knowledge.

Roles might eventually include:

```text
Owner
Administrator
Member
Guest
```

Do not hardcode the final permission model before gameplay proves what is needed.

---

# Shared Work and Player-Settler Interaction

The common WorkAction architecture should later enable cooperative operations.

Examples:

### Heavy Carrying

```text
Large Log
Requires 2 carriers

Player + Settler
or
Player + Player
or
Settler + Settler
```

### Pit Saw

```text
Sawyer above
Sawyer below
```

### Construction

Multiple actors can contribute labor to a large build task.

### Training

Teacher and learner may both be assigned to one teaching WorkAction.

These features are not required early, but the action architecture should not assume every action has exactly one isolated actor forever.

---

# Multiplayer Concurrency

Two players may interact with the same shared object at nearly the same time.

Example:

```text
Player A -> Assign Erik as Carpenter
Player B -> Assign Erik as Miner
```

The server must:

1. receive requests,
2. validate permissions,
3. validate current state,
4. apply one authoritative result,
5. reject or supersede conflicting requests according to rules,
6. synchronize the resulting state to relevant clients.

The same applies to:

- crafting inputs,
- stockpiles,
- machines,
- work orders,
- technology unlocks,
- settler assignments,
- shared inventories.

---

# Server-Owned Simulation State

Server-authoritative state should include at minimum:

- settler needs,
- settler inventories,
- settler jobs,
- player WorkActions,
- settler WorkActions,
- stockpile contents,
- reservations,
- work orders,
- player knowledge,
- settler knowledge,
- settlement knowledge,
- skills,
- production outcomes,
- progression gates.

Clients receive only what is required for rendering, interfaces, prediction where safe, and feedback.

---

# Persistence

Knowledge and settlement data must survive restarts.

Persistent identities should use stable IDs rather than display names.

Potential structures:

```text
SettlementId
Player UUID
Settler UUID
Knowledge ID
Skill ID
WorkOrder ID
```

Saved data should be versioned to allow future schema migrations.

---

# Recommended Architecture

Conceptually:

```text
ServerCivilizationManager
├── SettlementManager
├── KnowledgeManager
├── WorkActionManager
├── PlayerStateManager
├── SettlerManager
├── WorkOrderManager
├── ReservationManager
└── ResourceManager
```

Knowledge should use a shared abstraction such as:

```text
KnowledgeHolder
```

with implementations/storage for:

```text
PlayerKnowledge
SettlerKnowledge
SettlementKnowledge
KnowledgeDocument
```

This is preferable to starting with a player-only `unlockedTechnologies` list and rewriting it when settlers need the same concepts.

---

# Player Death

The exact death model can be decided later, but architecture should distinguish:

- player-character personal skill,
- player personal knowledge,
- settlement-established knowledge,
- documented knowledge.

A possible default direction:

```text
Death may remove/reduce personal expertise,
but established settlement knowledge survives.
```

This can make settlers and written knowledge important to civilization continuity without making ordinary multiplayer deaths unbearably punitive.

---

# Early Implementation Rules

Even before multiplayer-specific gameplay exists:

1. run important logic server-side,
2. test both integrated and dedicated server,
3. never trust a client Craft/Assign/Unlock request,
4. identify actors with stable IDs,
5. avoid direct player ownership assumptions,
6. keep knowledge scope extensible,
7. reserve resources before timed work begins,
8. ensure reconnects restore authoritative state.

Following these rules from v0.1 is much cheaper than retrofitting them later.
