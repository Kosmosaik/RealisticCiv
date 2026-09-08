# Technical Architecture

## Platform Direction

Recommended initial platform:

- Minecraft Java Edition,
- Fabric Loader,
- Fabric API,
- Java,
- Mixins only where API hooks are insufficient,
- data-driven content wherever practical.

Exact Minecraft/Fabric versions should be verified when implementation begins and then pinned for a development milestone. See `16_DEVELOPMENT_ENVIRONMENT_SETUP.md` for the recorded bootstrap baseline and setup procedure.

---

## Architecture Goals

- modular systems,
- explicit ownership of data,
- minimal hardcoded content,
- versionable content definitions,
- deterministic behavior where practical,
- multiplayer-safe server authority,
- scalable settler AI,
- efficient resource lookups,
- clear separation between gameplay logic and content data.

---

## Proposed Major Modules

```text
core/
progression/
quest_book/
materials/
recipes/
work_actions/
tools/
workstations/
world_resources/
settlers/
settlement/
logistics/
skills/
professions/
needs/
research/
knowledge/
networking/
compat/
client_ui/
```

---

## Registries

Suggested custom registries/data registries:

- MaterialRegistry,
- MaterialStateRegistry,
- ProcessRegistry,
- ToolCapabilityRegistry,
- WorkstationRegistry,
- SkillRegistry,
- ProfessionRegistry,
- TechnologyRegistry,
- QuestChapterRegistry,
- QuestNodeRegistry,
- RecipeRegistry,
- WorkActionTypeRegistry,
- ResourceTypeRegistry.

Where possible these should load from JSON or other data files rather than requiring Java changes for each content entry.

---

## Recipe Data Model

Conceptual schema:

```json
{
  "id": "mod:rough_wooden_handle",
  "inputs": [],
  "tools": [],
  "workstation": "mod:primitive_work_surface",
  "skills": {},
  "knowledge": [],
  "duration": 480,
  "outputs": [],
  "byproducts": []
}
```

Actual implementation schema should be designed carefully before content volume grows.

---

## WorkAction Architecture

Meaningful player and settler labor should execute through a common server-side action layer.

Conceptual components:

```text
WorkActionDefinition
├── action category
├── operation/recipe ID
├── inputs and reservations
├── required tools/capabilities
├── workstation/environment
├── base duration
├── movement mode
├── interruption/cancel behavior
├── relevant skill
├── outcome rules
└── outputs/byproducts

WorkActionInstance
├── actor ID
├── start/progress state
├── reserved resources
├── selected tool/workstation
└── server-authoritative status
```

Possible statuses:

```text
PENDING
RUNNING
PAUSED
INTERRUPTED
COMPLETED
FAILED
CANCELLED
```

Players start actions through UI/interactions. Settler AI starts actions through jobs/work orders. Both should resolve against the same definitions whenever practical.

### Actor Abstraction

Do not make operation logic depend directly on `ServerPlayerEntity` if settlers will perform the same work later.

Use an actor-facing capability/interface that can provide:

- actor identity,
- inventory access,
- skill values,
- knowledge access,
- position,
- stamina/energy hooks,
- tool access,
- action cancellation/interruption state.

The exact Java shape should be decided after inspecting current Fabric/Minecraft APIs, but the content model should remain actor-neutral.

### Resource Reservation

Timed work must reserve inputs server-side before it begins. Reservation logic should be reusable by:

- player crafting,
- settler work,
- workstation queues,
- logistics requests,
- automated machines.

This is both a simulation requirement and a multiplayer anti-duplication requirement.

---

## Quest Book Architecture

The Quest Book should be a data-driven presentation and guidance layer over progression rather than an independent technology authority.

Conceptual services:

```text
QuestBookService
├── chapter definitions
├── quest/node definitions
├── prerequisite graph
├── completion-condition evaluators
├── recommended-next resolver
└── synchronized progression state

ClientQuestBookState
├── selected chapter
├── graph navigation/history
├── search/filter state
└── visibility preference
```

Player visibility preference is client-side presentation state and may support:

```text
AVAILABLE_ONLY
UNLOCKED_CHAPTERS
SHOW_ALL
```

Changing this setting must never alter actual server progression.

Quest completion conditions that affect progression are evaluated server-side from authoritative simulation state. The client renders synchronized results.

Chapter/node content should be data-driven so large quest graphs can grow without one Java class per quest.

Achievements, statistics, first discoveries, historical records, and completionist tracking should not be mixed into the Quest Book data model. A separate Civilization Record system may be added later.

See `13_QUEST_BOOK_AND_PROGRESSION_UI.md` for the full player-facing design.

---

## Technology Data

Technology definitions should contain:

- ID,
- name,
- prerequisites,
- category,
- unlock effects,
- discoverability rules,
- optional era label.

Avoid scattering checks such as:

```java
if (technologyLevel >= 4)
```

through gameplay code.

Instead ask a centralized technology/capability service.

---

## Entity Data

Settlers require persistent synchronized data.

Potential components:

```text
SettlerIdentity
SettlerNeeds
SettlerSkills
SettlerProfession
SettlerInventory
SettlerSchedule
SettlerJobState
SettlerResidence
SettlerAttributes
```

Avoid one monolithic settler class containing every system.

---

## Settlement-Level Simulation

Central manager:

```text
SettlementManager
├── settler index
├── stockpile index
├── resource index
├── workplace index
├── housing index
├── work orders
├── logistics requests
├── reservations
└── technology state
```

This avoids expensive repeated world scans.

---

## AI Architecture

Settlers should not independently search every block/entity every tick.

Recommended pattern:

1. Settlement system maintains indexed opportunities.
2. Settler requests next suitable task.
3. Task reserves required resources/workplace.
4. Settler executes task state machine.
5. Task completes/fails/cancels.
6. Indexes update only when relevant state changes.

Potential task state machine:

```text
Acquire Job
→ Reserve Inputs
→ Travel to Input
→ Collect
→ Travel to Workstation
→ Work
→ Collect Output
→ Deliver Output
→ Complete
```

---

## Tick Budgeting

Expensive simulation should be staggered.

Examples:

- hunger/thirst do not need updates every tick,
- job evaluation can run periodically,
- stockpile indexing should be event-driven where possible,
- path recalculation should be minimized,
- unloaded settlements may use simplified simulation later.

---

## Multiplayer Authority

Important gameplay simulation should run server-side:

- needs,
- inventory,
- job state,
- production,
- technology,
- work orders,
- loot/progression gates,
- WorkAction progress/outcomes,
- player/settler/settlement knowledge,
- reservations and shared-work conflicts.

Clients receive synchronized state for rendering and UI. Client packets request actions; they do not declare authoritative outcomes.

---

## Content Locks

A centralized progression/capability layer should determine:

- whether a block can be harvested,
- whether an item can be used,
- whether a recipe is executable,
- whether loot is allowed,
- whether a workstation can operate,
- whether a settler can perform an operation.

This avoids one-off bypass fixes throughout the codebase.

---

## Vanilla Integration Strategy

Use the least invasive method available, in this order:

1. tags/data files,
2. Fabric events/APIs,
3. custom registries/recipes,
4. loot replacement/modification,
5. custom entities/blocks,
6. Mixins for necessary vanilla behavior changes.

Mixins should not become the default solution for every feature.

---

## Persistence

Persistent world data should include:

- settlement IDs,
- settlement membership,
- technology/knowledge state,
- player knowledge where applicable,
- settler state and knowledge,
- active/resumable work state where required,
- work orders,
- custom world-resource state where necessary.

Saved data format should tolerate future schema evolution.

---

## Testing Strategy

At minimum:

- unit tests for data validation where practical,
- automated registry/content validation,
- recipe dependency validation,
- missing-reference detection,
- development commands for spawning/test setup,
- server/client smoke tests,
- performance tests with increasing settler counts.

---

## Mod Compatibility

Do not make broad compatibility promises early.

A total conversion that changes recipes, villager systems, loot, mining, and progression will conflict with many conventional mods.

Later compatibility layers may support selected mods or APIs.

Initial goal should be a coherent standalone mod environment.

---

## Related Design

See `12_MULTIPLAYER_AND_KNOWLEDGE.md` for knowledge scopes, cooperative settlement ownership, permissions, teaching, and multiplayer concurrency rules.

See `13_QUEST_BOOK_AND_PROGRESSION_UI.md` for progression chapters, quest graph behavior, cross-chapter gating, visibility preferences, and guidance UI.

---

## Visual Work / Animation Layer

Crafting/processing visuals are a client presentation layer over authoritative `WorkAction` state.

Do not make recipe success depend on a custom animation existing.

Planned client-facing concerns include:

```text
client/
├── ui/
├── render/
├── animation/
└── networking/
```

Worksites/workstations should render reserved logical inputs rather than spawning ordinary dropped item entities for visual placement.

Operations may later provide optional visual metadata for:

- actor animation family,
- workpiece layout,
- staged workpiece states,
- particle/sound cues,
- machine animation.

Nearby clients reconstruct visuals from synchronized action state. The server remains authoritative over reservation, progress, interruption, outcomes, and outputs.

See `17_VISUAL_CRAFTING_AND_ANIMATION.md`.
