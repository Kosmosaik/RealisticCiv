# Development Workflow

## Purpose

This file defines how implementation should be performed so the project remains maintainable as it grows from a small primitive prototype into a very large total-conversion mod.

It is also written as a handoff contract for future GPT-assisted development sessions.

---

# Source of Truth

Once a source project exists:

> **The newest complete project ZIP/repository is the implementation source of truth.**

Do not rely on remembered code from previous conversations.

Before changing code:

1. inspect the newest project recursively,
2. identify the files relevant to the requested change,
3. inspect dependent registries/data schemas/tests,
4. identify existing extension points,
5. only then edit.

Do not invent file paths, method names, or package layouts that have not been confirmed in the current project.

---

# Change Size

Prefer small, coherent milestones.

Good:

```text
v0.1.1
- block bare-hand log harvesting
- add validation for harvest capability rules
- add dedicated-server test
```

Good:

```text
v0.1.2
- add ground branch resource
- add pickup interaction
- add spawn configuration/data definition
```

Bad:

```text
"Implement the whole Stone Age, settlers, farming, pottery,
metallurgy and the quest book in one update."
```

The project is intentionally huge. Development must therefore be incremental.

---

# Audit Before Editing

For non-trivial changes, perform an implementation audit first.

Record internally or in the handoff:

- files read,
- important classes/functions/registries found,
- existing data definitions,
- dependent systems,
- save/network implications,
- multiplayer implications,
- uncertainties.

Do not rewrite working systems merely because a new implementation is easier to imagine.

---

# Architecture Rules

## Data Over Hardcoding

Gameplay content should live in definitions/configuration wherever practical.

Examples:

- process operations,
- materials,
- tool capabilities,
- quest nodes,
- technologies,
- skills,
- professions,
- world-resource spawn rules.

Java should provide reusable behavior; content files should provide the large content set.

## Explicit Ownership

Every mutable system should have clear authority/ownership.

Examples:

```text
Server              → authoritative simulation
SettlementManager   → settlement indexes/orders/reservations
WorkActionManager   → active timed work
KnowledgeService    → knowledge checks/transfers
QuestService        → authoritative quest/progression state
Client UI           → display and local presentation preferences
```

Avoid duplicated state in unrelated systems.

## No Parallel Player/Settler Crafting Systems

Do not implement one recipe engine for the player and a second fake one for settlers.

The intended shape is:

```text
Operation Definition
       ↓
WorkAction
       ↓
Actor Adapter
   ├── Player
   └── Settler
```

Actor-specific behavior can differ, but process rules should be shared.

---

# Multiplayer Development Rule

Every meaningful gameplay feature should be checked against both:

- integrated singleplayer server,
- dedicated server.

Never assume that because something works when the local player clicks it, the architecture is multiplayer-safe.

For action requests:

```text
Client intent
   ↓
Server validation
   ↓
Reservation/state mutation
   ↓
Authoritative processing
   ↓
Synchronized result
```

Validate:

- permissions,
- ownership/settlement access,
- current actor state,
- required items/tools,
- prerequisites,
- resource reservation,
- concurrent requests,
- cancellation/disconnect behavior.

---

# Testing Expectations

Every milestone should include the cheapest useful automated validation available.

At minimum:

- compile/build,
- content/data validation,
- registry validation,
- dedicated-server startup where relevant.

As systems stabilize, add tests for:

- recipe/operation parsing,
- capability requirements,
- progression gating,
- quest prerequisites,
- deterministic outcome calculations,
- reservation behavior,
- save/load migration logic.

A successful local client launch is not sufficient validation by itself.

---

# Manual Smoke Tests

Each delivered version should include a short test checklist.

Example:

```text
Fresh world
[ ] Punching a tree does not yield logs.
[ ] Branches spawn and can be picked up.
[ ] Two players cannot reserve the same input item twice.
[ ] Hand-crafting output cannot be taken instantly.
[ ] Craft starts a timed WorkAction.
[ ] Moving/cancelling behaves according to the operation rule.
[ ] Completion produces the correct output once.
[ ] Dedicated server behaves the same as integrated server.
```

Keep smoke tests focused on what changed.

---

# Versioning and Handoffs

Once code development begins, maintain at least:

```text
README.md
CHANGELOG.md
CURRENT_STATE_AND_HANDOFF.md
```

Each milestone handoff should state:

- version,
- what changed,
- tests/builds run,
- known issues,
- next planned step.

When returning a project ZIP, include the whole project rather than isolated changed files unless explicitly requested otherwise.

---

# Save Compatibility

Before persistent custom world/player/settlement data becomes important:

- add explicit data version numbers,
- centralize serialization,
- plan migration hooks,
- avoid storing implementation-specific transient state when stable IDs can be stored instead.

Do not promise save compatibility before migration infrastructure exists.

Once the project reaches user-facing releases, breaking save changes must be documented clearly.

---

# Content IDs

Use stable namespaced identifiers from the beginning.

Conceptual examples:

```text
primitivecivilization:stone_flake
primitivecivilization:assemble_stone_axe
primitivecivilization:primitive_tools
primitivecivilization:basic_knapping
```

Renaming an ID after it appears in saves/data packs can become a migration problem, so distinguish display-name changes from identifier changes.

---

# Performance Discipline

Do not optimize everything prematurely, but never design obviously unscalable loops.

Especially avoid:

```text
Every settler
× every tick
× scan every chest
× scan every workstation
× scan every possible job
```

Prefer:

- event-driven indexes,
- reservations,
- staggered updates,
- cached lookup structures,
- periodic planning rather than per-tick planning,
- simple client rendering state separate from authoritative simulation.

Stress-test settlement systems progressively as documented in the roadmap.

---

# Documentation Rule

When a design decision materially changes:

1. update the relevant design document,
2. update `14_CURRENT_STATE_AND_HANDOFF.md` if the decision is important to future implementation,
3. update the roadmap if sequencing changes,
4. record implementation changes in the changelog once code exists.

Do not allow the conversation to become the only place a major project decision exists.

---

# Asset Rule

Before adding a third-party asset:

- identify the original author/source,
- identify the exact asset license,
- confirm the license applies to the art/model/sound rather than only code,
- confirm modification and redistribution terms,
- add attribution/provenance records where required.

If license/permission is unclear, do not ship the asset.

See `07_ASSETS_ART_DIRECTION_AND_LICENSING.md`.

---

# Recommended GPT-Assisted Workflow

For each implementation request:

```text
User provides newest project ZIP
        ↓
Assistant reads project recursively
        ↓
Assistant audits relevant implementation
        ↓
Assistant performs one coherent milestone
        ↓
Assistant builds/tests/validates
        ↓
Assistant updates changelog/handoff
        ↓
Assistant returns complete updated ZIP
        ↓
User performs manual Minecraft smoke test
        ↓
Next milestone
```

If an implementation is too large to complete and validate safely in one iteration, split it before editing rather than returning a half-integrated architecture.
