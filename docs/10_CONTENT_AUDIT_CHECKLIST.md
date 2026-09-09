# Content Audit Checklist

Use this checklist whenever a major progression milestone is added.

The core question is:

> Can the player acquire a capability earlier than intended through any vanilla or custom shortcut?

---

## Current Audit Status — v0.1.2

This checklist is intentionally broader than the current implementation. In v0.1.2, only the **first bootstrap pass** is complete: survival log breaking is gated, the first natural ground resources exist, and the obvious plank/stick/crafting-table/furnace/wooden-tool/stone-tool recipes are disabled. Checked entries below mean that specific bootstrap concern has an implemented first pass; they do **not** mean the entire surrounding category is permanently finished.

## Resource Acquisition

- [ ] Bare-hand block drops audited.
- [x] Tree harvesting audited. — v0.1.2 first pass: survival log breaking requires `realisticciv:felling_tools`.
- [ ] Leaf drops audited.
- [ ] Ground-resource availability balanced. — initial v0.1.2 generation exists; balancing still pending playtest.
- [ ] Stone acquisition audited. — initial Granite Stone / Flint Nodule surface acquisition exists; full stone/mining rules pending.
- [ ] Ore acquisition audited.
- [ ] Clay/sand/gravel acquisition audited.
- [ ] Water acquisition audited.

---

## Recipes

- [ ] Inventory 2×2 recipes audited. — first bootstrap recipes disabled; full Hand Crafting replacement pending.
- [ ] Crafting-table recipes audited. — crafting-table bootstrap recipe disabled; full recipe audit pending.
- [ ] Furnace recipes audited. — furnace bootstrap recipe disabled; furnace/smelting system audit pending.
- [ ] Blast-furnace recipes audited.
- [ ] Smoker recipes audited.
- [ ] Stonecutter recipes audited.
- [ ] Smithing-table recipes audited.
- [ ] Brewing recipes audited.
- [ ] Special vanilla recipes audited.

---

## Tools

- [x] Wooden tools disabled/reworked. — vanilla wooden tool recipes disabled in v0.1.2.
- [x] Stone tools disabled/reworked. — vanilla stone tool recipes disabled in v0.1.2.
- [ ] Iron tools gated.
- [ ] Diamond tools gated.
- [ ] Netherite tools gated.
- [ ] Shears gated.
- [ ] Buckets gated.
- [ ] Flint and steel gated.
- [ ] Fishing rod progression checked.
- [ ] Bow/crossbow progression checked.

---

## Workstations

- [ ] Crafting table. — crafting recipe disabled; later workstation/interaction behavior still pending.
- [ ] Furnace. — crafting recipe disabled; later workstation/interaction behavior still pending.
- [ ] Blast furnace.
- [ ] Smoker.
- [ ] Stonecutter.
- [ ] Anvil.
- [ ] Smithing table.
- [ ] Grindstone.
- [ ] Loom.
- [ ] Cartography table.
- [ ] Brewing stand.
- [ ] Enchanting table.

---

## Loot

- [ ] Dungeon chests.
- [ ] Village chests.
- [ ] Mineshaft chests.
- [ ] Shipwrecks.
- [ ] Buried treasure.
- [ ] Ruined portals.
- [ ] Desert pyramids.
- [ ] Jungle temples.
- [ ] Strongholds.
- [ ] Ancient cities.
- [ ] Woodland mansions.
- [ ] Trail ruins.
- [ ] Nether structures.
- [ ] End structures.

---

## Villagers

- [ ] Vanilla villager spawning decision.
- [ ] Villager trades.
- [ ] Profession workstations.
- [ ] Village crops.
- [ ] Village food.
- [ ] Village beds.
- [ ] Village crafted blocks.
- [ ] Wandering trader.

---

## Mobs

- [ ] Iron golem drops.
- [ ] Zombie equipment drops.
- [ ] Skeleton equipment drops.
- [ ] Piglin/bartering systems.
- [ ] Witch drops.
- [ ] Creeper/gunpowder progression.
- [ ] Enderman drops.
- [ ] Blaze drops.
- [ ] Other progression-relevant drops.

---

## Structures and Dimensions

- [ ] Nether portal creation.
- [ ] Nether access.
- [ ] Stronghold access.
- [ ] End portal access.
- [ ] End progression.

---

## Redstone and Automation

- [ ] Redstone dust acquisition.
- [ ] Pistons.
- [ ] Hoppers.
- [ ] Observers.
- [ ] Comparators.
- [ ] Repeaters.
- [ ] Dispensers/droppers.
- [ ] Minecart automation.

---

## Food and Survival

- [ ] Crop acquisition.
- [ ] Crop growth speed.
- [ ] Crop yields.
- [ ] Animal breeding.
- [ ] Fishing.
- [ ] Cooking.
- [ ] Food preservation.
- [ ] Hunger balance.
- [ ] Water/thirst balance.

---

## Player Death / Recovery

- [ ] Respawn mechanics.
- [ ] Bed access.
- [ ] Keep-inventory assumptions.
- [ ] Death recovery does not duplicate valuable technology/resources.

---

## Settler Bypasses

- [ ] Settlers obey the same technology gates as player production.
- [ ] Settlers cannot craft unavailable recipes.
- [ ] Settlers cannot pull items from inaccessible containers.
- [ ] Settler work does not create resources from nothing.
- [ ] Hauling respects actual inventory and stockpiles.

---

## Compatibility

- [ ] External mods cannot trivially reintroduce vanilla recipes in official packs.
- [ ] Datapacks are considered.
- [ ] Server config can enforce intended progression.
