# Progression and Technology

## Core Model

Progression should use a **technology graph**, not a single linear era value.

Era names are useful for UI, balance, and broad organization, but actual unlocks should depend on prerequisite concepts.

Example:

```text
Controlled Fire
├── Charcoal Production
│   └── High-Temperature Furnaces
│       └── Metallurgy
└── Pottery
    └── Crucibles
        └── Copper Smelting
```

The colony may therefore be highly advanced in one field while lacking another.

---

## Quest Book as the Progression Interface

The technology graph should be presented to the player through an expert-pack-style **Quest Book** organized into subject chapters such as Primitive Tools, Woodworking, Pottery, Metallurgy, Mechanical Power, Electricity, Computing, and Space Industry.

The Quest Book is a guidance layer over the real progression model. Players may focus on any currently available chapter and advance until they encounter a real dependency on progress in another chapter. Cross-chapter gates should correspond to genuine material, knowledge, infrastructure, tool, or capability prerequisites rather than arbitrary chapter-completion percentages.

Example:

```text
Woodworking → Metal Sawing Tools
                     🔒
              Requires Basic Ironworking
                     ↓
                 Metallurgy
```

Quest completion should normally be detected automatically from server-authoritative simulation state rather than requiring claim buttons to activate technology.

The player can choose how much locked progression is visible:

- **Available Only** — completed/currently available quests; future locked nodes hidden.
- **Unlocked Chapters** — all nodes in currently revealed chapters, including locked nodes and their prerequisites.
- **Show All** — browse the full defined progression tree, with future content visibly locked.

Achievements, statistics, historical firsts, profession records, and other completionist material should live in a separate future **Civilization Record** rather than cluttering the Quest Book.

See `13_QUEST_BOOK_AND_PROGRESSION_UI.md` for the full design.

---

## Suggested Broad Eras

These are organizational categories, not hard progression gates.

### Era 0 — Bare Survival

- hand gathering,
- surface water,
- loose natural materials,
- basic food gathering,
- no manufactured tools.

### Era 1 — Primitive Toolmaking

- hammerstones,
- identified knappable materials (flint, later chert/quartzite/obsidian etc.),
- flakes,
- primitive knives,
- cordage,
- hafted tools,
- fire-making equipment.

### Era 2 — Established Primitive Settlement

- shelters,
- controlled fire,
- hides,
- baskets,
- woodworking,
- hunting equipment,
- food preservation,
- basic storage.

### Era 3 — Neolithic / Agricultural

- cultivation,
- domestication,
- pottery,
- kilns,
- weaving,
- permanent housing,
- better carpentry.

### Era 4 — Copper / Early Metallurgy

- mining surface deposits,
- ore processing,
- charcoal,
- crucibles,
- copper smelting,
- casting,
- early metal tools.

### Era 5 — Bronze

- alloying,
- specialized furnaces,
- improved casting,
- metalworking specialization,
- stronger tools and weapons.

### Era 6 — Iron / Advanced Pre-Industrial

- bloomery iron,
- forging,
- carburization,
- early steel,
- advanced carpentry,
- larger settlements.

### Era 7 — Mechanical Power

- water wheels,
- windmills,
- gearing,
- shafts,
- belt drives,
- powered mills,
- mechanized workshops.

### Era 8 — Early Industrial

- improved furnaces,
- machine tools,
- steam engines,
- pumps,
- mechanized mining,
- standardized parts.

### Era 9 — Industrial

- blast furnaces,
- high-volume steel,
- rail,
- factories,
- precision machining,
- industrial chemistry.

### Era 10 — Electrical

- generators,
- motors,
- grids,
- batteries,
- lighting,
- electrical machinery.

### Era 11 — Modern Industrial

- internal combustion,
- petroleum refining,
- advanced alloys,
- plastics,
- modern medicine,
- mass production.

### Era 12 — Electronics and Computing

- vacuum tubes,
- semiconductors,
- printed circuits,
- integrated circuits,
- computers,
- sensors,
- automated control.

### Era 13 — Advanced Automation

- robotics,
- CNC,
- autonomous logistics,
- advanced process control,
- highly automated factories.

### Era 14 — Space Age

- rocket propulsion,
- guidance computers,
- life support,
- orbital infrastructure,
- advanced materials.

### Era 15 — Science Fiction

Potential future branches:

- fusion,
- advanced robotics,
- synthetic biology,
- exotic materials,
- advanced propulsion,
- artificial intelligence,
- off-world industry.

---

## Technology Unlock Conditions

A technology may depend on several forms of progress.

### Knowledge Prerequisites

Example:

```text
Basic Ceramics
+ Controlled Fire
+ High-Temperature Firing
→ Crucible Making
```

### Material Prerequisites

The settlement may need to have obtained or analyzed a material before related technologies become available.

### Infrastructure Prerequisites

Example:

```text
Steam Engine Design
requires:
- precision boring capability,
- pressure vessel manufacturing,
- suitable metal production.
```

### Skill Prerequisites

Some discoveries may require a sufficiently skilled worker.

### Experimentation

Certain technologies may be discovered through trial, experimentation, observation, or dedicated research work.

---

## Knowledge vs Skill vs Capability

These concepts must remain separate.

### Knowledge

What the settlement understands.

Example:

`Iron Smelting = known`

### Skill

What an individual settler can perform well.

Example:

`Anna — Smithing 42`

### Capability

What the settlement's physical infrastructure can actually produce.

Example:

The colony knows steelmaking but lacks the furnace temperature, refractory material, and fuel supply required to perform it.

---

## Progression Through Efficiency

Technological advancement should frequently improve the same process instead of simply replacing the output with a stronger tier.

Example: board production.

### Primitive

```text
Log
+ stone axe/adze
+ long manual work
→ rough boards
```

### Developed Handcraft

```text
Log
+ saw
+ sawing station
→ sawn boards
```

### Mechanical

```text
Log
+ water-powered sawmill
→ boards rapidly
```

### Industrial

```text
Log
+ powered sawmill
→ standardized lumber at high throughput
```

### Automated

```text
Log stockpile
→ automated handling
→ industrial saw line
→ sorted lumber stock
```

---

## Recommended Progression Rule

Whenever a new technology is introduced, define which of these it improves:

- new material access,
- new product access,
- increased production speed,
- reduced labor,
- reduced material waste,
- increased precision,
- increased output quality,
- reduced fuel use,
- increased scale,
- increased automation,
- improved logistics,
- safer processes.

Technological progress should preferably affect several of these.

---

## Knowledge Scopes

Technology progression must not assume that one global unlock list is sufficient. A concept may be known by an individual player, an individual settler, or established across an entire settlement. Later, knowledge may also be preserved in physical or digital records.

The technology graph defines conceptual dependencies; `12_MULTIPLAYER_AND_KNOWLEDGE.md` defines who actually possesses and can transmit that knowledge.
