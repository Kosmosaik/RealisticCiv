# Changelog

## 0.1.5 Hotfix 4 — Inventory Accessor Mixin Registration

### Fixed

- Fixed a client crash when opening the inventory after Hotfix 3. `HandCraftingInventoryUi` casts the vanilla `InventoryScreen` to `AbstractContainerScreenAccessor` so the Craft button can follow the inventory's true `leftPos` / `topPos`, but the new client accessor mixin configuration had not been registered in `fabric.mod.json`.
- Registered `realisticciv.client.mixins.json` as a client-only mixin configuration so the accessor is actually applied at runtime before the inventory UI code uses it.

### Cleanup

- Removed the unused template `ExampleMixin` / `ExampleClientMixin` scaffolding and the now-unreferenced main example mixin config to keep the project mixin setup explicit.

### Validation Note

- The project owner had already confirmed Hotfix 3 compiled successfully; the failure occurred only when opening the inventory at runtime. Hotfix 4 specifically addresses that runtime mixin-registration defect.

---
## 0.1.5 Hotfix 3 — Minecraft 26.2 GUI extraction API compile fix

### Fixed

- Corrected the v0.1.5 Hotfix 2 output-preview implementation for Minecraft 26.2's extraction-based GUI API.
- Replaced the removed `GuiGraphics` type with `GuiGraphicsExtractor`.
- Replaced the unavailable `ScreenEvents.afterRender(...)` hook with the current Fabric 26.2 `ScreenEvents.afterForeground(...)` extraction event.
- Updated preview drawing to the 26.2 `GuiGraphicsExtractor.item(...)` and `setTooltipForNextFrame(...)` APIs.

### Notes

- This hotfix is intended to preserve Hotfix 2 behavior: Recipe Book-aware Craft-button positioning, Wooden Handle naming/art, and the visual result-slot preview.
- The project owner's local Java 25 `runDatagen` / `build` remains the definitive compilation check.

---
## 0.1.5 Hotfix 2 — Wooden Handle rename, UI placement, and output preview

### Changed

- Renamed the player-facing item name **Wooden Haft** to **Wooden Handle** while intentionally preserving the existing internal registry IDs (`realisticciv:wooden_haft` and `realisticciv:shape_wooden_haft`) for compatibility.
- Replaced the first Wooden Haft placeholder art with a new original 32x32 **Wooden Handle** texture that reads more clearly as a carved tool handle and less like food.
- Anchored the Hand Crafting **Craft** button to the real inventory GUI `leftPos`/`topPos`, so it now follows the inventory when the vanilla Recipe Book opens or the screen layout shifts.
- Added an in-GUI Hand Crafting **output preview** in the vanilla crafting result slot. When the 2x2 grid contains a valid RealisticCiv Hand Crafting operation, the primary output item now appears directly in the output slot area and shows its normal item tooltip on hover.

### Notes

- The output slot preview is currently visual guidance only; the actual server-authoritative crafting still begins through the **Craft** button.
- This keeps the familiar vanilla layout while making the result more legible to the player.

---
## 0.1.5 Hotfix 1 — Java 25 / Minecraft 26.2 Compile Fix

### Fixed

- Restored the `showStarted`, `showProgress`, and `operationName` WorkAction helper methods that were accidentally omitted during the v0.1.5 refactor.
- Updated Hand Crafting item-particle creation for the Minecraft 26.2 API: `ItemParticleOption` now receives the backing `Item` from the reserved `ItemStack` instead of the unsupported `ItemStack` constructor.
- This hotfix is compilation-only; Hand Crafting behavior, timings, reservations, UI, and outputs are otherwise unchanged from v0.1.5.

### Verification

Run `runDatagen` and `build` first. If compilation succeeds, continue with the v0.1.5 Hand Crafting playtest checklist.

---
## 0.1.5 — Hand Crafting UI Foundation

### Added

- Reinterpreted the player's familiar inventory 2x2 crafting area as the first RealisticCiv **Hand Crafting** front end by adding an explicit `Craft` button beside the grid.
- Added client-side Hand Crafting matching so the Craft button enables only for recognized `HAND_CRAFTING` operations and provides an operation/time tooltip.
- Added `StartHandCraftPayload` and server networking registration. The payload intentionally carries no trusted recipe/result data; the logical server resolves and validates the player's actual 2x2 grid.
- Added shared `HandCraftingResolver`, `HandCraftingMatch`, and server-side `HandCraftingService`.
- Added the first real Hand Crafting operation: `realisticciv:shape_wooden_haft`. One Branch is shaped with a persistent Flint Flake cutting edge over 100 ticks (~5 seconds).
- Added `realisticciv:wooden_haft` with an original 32x32 item texture.
- Added `realisticciv:tool_hafts` for future primitive-tool assembly.
- Added `docs/22_FIBRES_CORDAGE_AND_TEXTILES.md`, explicitly planning source-identified grass/nettle/bast/flax/hemp fibre progression instead of a generic naturally harvested `Plant Fibre` item.
- Added `docs/23_HAND_CRAFTING_UI.md`.

### Changed

- Extended `WorkAction` tool validation so a persistent Hand Crafting tool can move from the 2x2 grid back into normal player inventory when the server closes the inventory screen. The required capability continues to be revalidated during work.
- Generalized the WorkAction pulse/completion presentation beyond knapping: Hand Crafting now uses arm swings, wood-working sounds, Branch-derived particles, action-bar progress, movement cancellation, and server-only completion.
- Reserved Hand Crafting input is restored if work cancels before completion.
- The first fibre/cordage roadmap now begins with Tall Grass → Long Grass Stems → Prepared Grass → Primitive Grass Cordage, followed by a better wild Nettle route; flax/hemp/bast and textile-industry depth remain later progression.

### Architecture

- Hand Crafting does not create a second timer/crafting engine. It resolves into existing `OperationDefinition` data and executes through the existing server-authoritative `WorkActionManager`.
- The UI is a request/presentation layer only. The server controls operation matching, consumed input reservation, tool validation, timing, cancellation, and outputs.
- The bootstrap resolver currently supports exactly one consumed input plus one persistent tool in the 2x2 grid; richer recipe matching can evolve separately from the WorkAction runtime.

### Testing Note

Run `runDatagen`, `build`, and `runClient`, then test Branch + Flint Flake in the inventory 2x2 grid. Verify the Craft button, server-owned inventory close/start, ~5 second shaping action, output, movement cancellation, cutting-edge removal cancellation, and invalid-grid behavior. Repeat on a dedicated server.

---
## 0.1.4 — Timed WorkAction + Physical Knapping

### Added

- Added the first generic server-authoritative `WorkActionManager` and runtime `ActiveWorkAction` state under `crafting/workaction/`.
- Added timed server-tick execution for the existing `realisticciv:initial_flint_knapping` operation.
- Added action-owned input reservation for held consumed inputs; reserved inputs are restored when work is cancelled.
- Added Ground Resource reservations so a Flint Nodule/Granite Stone being used by an active WorkAction cannot be right-click-collected by another player during the action.
- Added stationary-work cancellation, held-tool validation, ground-target validation, disconnect cleanup, and server-shutdown cleanup.
- Added simple action-bar work progress showing operation name, a 10-segment progress bar, and percentage.
- Added repeated knapping presentation during work: arm swings, stone-hit sounds with pitch variation, and `Flint Chips` item particles.
- Added stronger completion sound/particle feedback.
- Added `docs/21_WORK_ACTION_FOUNDATION.md`.

### Changed

- `Initial Flint Knapping` now takes 80 ticks (~4 seconds at 20 TPS) instead of resolving immediately.
- Primitive knapping now delegates execution to the generic WorkAction layer; `PrimitiveKnappingService` is only an interaction adapter.
- A natural ground Flint Nodule remains visibly present during knapping and is removed only after successful completion.
- Held Flint Nodule input is reserved at action start and restored if the player moves, loses the required hammerstone, changes level, dies, disconnects, or otherwise invalidates the action.
- Players are prevented from casually collecting other Ground Resources while already occupied by a WorkAction.
- Updated crafting, architecture, roadmap, vertical-slice, knapping, and handoff documentation for the v0.1.4 implementation state.

### Current Limitations

- The progress indicator is intentionally an action-bar prototype rather than the final custom HUD.
- Knapping currently reuses vanilla arm swings and stone sounds; dedicated custom work animations/audio remain future work.
- Ground reservations protect right-click interaction but do not yet provide a synchronized visual reservation marker or fully block another player from physically breaking the reserved Ground Resource. If it disappears, the action cancels safely.
- Skills, failures, variable yield, quality, tool wear, partial-progress persistence, and settlers are not implemented yet.

### Validation Note

The assistant environment can statically validate source/resources/docs and inspect Minecraft 26.2 APIs, but it does not currently provide JDK 25 for the project Gradle build. The project owner must run `runDatagen`, `build`, `runClient`, and dedicated-server tests locally before v0.1.4 is considered verified.

---
## 0.1.3 — Primitive Knapping

### Added

- Added the first reusable production-operation foundation: `OperationDefinition`, `OperationCatalog`, categories, input/tool matching, output roles, and fixed output definitions.
- Added `realisticciv:initial_flint_knapping` as the first registered operation.
- Added new item forms: `Flint Core`, `Flint Flake`, and `Flint Chips`, with original project textures and generated item models/translations.
- Added form/capability tags for knapping nodules, cores, stone flakes, knapping waste, and primitive cutting edges.
- Added `PrimitiveKnappingService` as a server-authoritative player adapter over the shared operation definition.
- Added two-hand knapping: hold a valid hammerstone and Flint Nodule in opposite hands and right-click/use either participant.
- Added Ground Resource knapping in either orientation: held Granite Stone can work a ground Flint Nodule, and held Flint Nodule can be worked against a ground Granite Stone.
- Added short stone sound/action-bar feedback on successful knapping.
- Added `docs/20_PRIMITIVE_KNAPPING.md`.

### Changed

- Flint material identity now maps multiple physical forms (`Flint Nodule`, `Flint Core`, `Flint Flake`, `Flint Chips`) to the same underlying `flint` material definition.
- `realisticciv:knappable_stones` now includes Flint Core as well as Flint Nodule; operation-specific form tags decide which form a given operation actually accepts.
- `Granite Stone`, `Flint Nodule`, and `Flint Core` use a lightweight knapping-participant item hook so the operation can be initiated from either hand without embedding recipe logic in the item classes.
- Project version advanced to `0.1.3`.

### Gameplay

The first primitive processing loop is now playable:

```text
Granite Stone + Flint Nodule
        ↓
Initial Flint Knapping
        ↓
1 Flint Core + 2 Flint Flakes + 2 Flint Chips
```

The Granite Stone is not consumed in this bootstrap version. The outcome is deterministic; timed `WorkAction`, skill, quality, failure, waste variation, tool wear, and visible staged knapping remain future milestones.

### Validation Note

The source/resources/docs/ZIP can be statically validated in the assistant environment, but the project owner's local Java 25 Fabric toolchain remains the final compilation/runtime test. Run `runDatagen`, `build`, `runClient`, and a dedicated-server knapping check before considering v0.1.3 verified.

---
## 0.1.2 Hotfix 4 — 32x32 Branch Texture

### Changed

- Replaced the placeholder 16x16 `Branch` inventory texture with a new original 32x32 pixel-art texture.
- Preserved the same overall branch identity while adding a slightly smoother silhouette, clearer tapering, and a small offshoot so the item reads better at the larger in-world Ground Resource scale.
- Because Ground Resources already reuse canonical inventory item rendering, the improved 32x32 texture automatically updates both the inventory icon and the natural in-world branch appearance without additional model changes.

### Testing Note

- Verify that the Branch now looks slightly more detailed in both the inventory and as a natural Ground Resource, while still fitting Minecraft's visual language and without changing pickup/interactions.

---
## 0.1.2 Hotfix 3 — Ground Resources Reuse Item Models

### Changed

- Replaced the hand-authored Ground Resource voxel models with a shared client renderer that draws each resource's canonical inventory item model directly in the world.
- Branch, Granite Stone, and Flint Nodule now use the same `textures/item/*.png` art for inventory and natural world placement; duplicate `textures/block/ground_*` assets were removed.
- Ground Resources are rendered stationary and laid flat on the terrain while retaining Minecraft's generated-item pixel extrusion, giving the icon silhouette visible physical thickness without dropped-item bobbing, spinning, physics, or despawning.
- Added stable per-position yaw variation so natural resources do not all face the same direction, without moving the visual away from the centered interaction shape.
- Increased Branch world render scale relative to the stones so it reads as a substantial gathered branch while keeping the inventory icon unchanged.
- Added a single tickless `GroundResourceBlockEntity` / renderer type as the visual anchor; pickup/resource identity remains derived from the owning `GroundResourceBlock` and no duplicate item state is persisted in NBT.

### Removed

- Removed the temporary dedicated Ground Resource block textures and hand-authored block model JSON files introduced in Hotfix 2.

### Testing Note

This hotfix changes rendering architecture but not spawning or pickup rules. Existing Ground Resources should use the new appearance after restarting the client. Verify that the visible item is centered inside its selection outline, has subtle thickness, stays stationary, and that the Branch is large enough to read clearly.

---
## 0.1.2 Hotfix 2 — Ground Resource Interaction + 3D Pass

### Fixed

- Removed the random X/Z block-model offset from Ground Resources. The renderer was shifting the visible resource away from its actual block position while the interaction/selection shape stayed centered, causing the visible item and outline to disagree and making right-click pickup unnecessarily awkward.
- Enlarged and centered the Ground Resource selection shapes so the small objects remain easy to target.

### Changed

- Reworked Branch, Granite Stone, and Flint Nodule world models from nearly-flat textured planes into small low-profile 3D voxel models with visible thickness.
- Increased the physical/world-model size of the Branch so it reads as a useful gathered branch rather than a tiny twig.
- Added dedicated opaque world textures for the new 3D resource models while retaining the existing inventory icons unchanged.
- Branch world generation now uses the data-driven `realisticciv:branch_rich` biome tag rather than only `minecraft:is_forest`. The default tag includes vanilla forest, taiga, jungle, swamp, and mangrove-swamp environments. Modpacks/data packs can extend the tag for additional wooded biomes without Java changes.

### Testing Note

Existing Ground Resource blocks should receive the model/alignment changes after a client restart/resource reload. Testing the expanded branch biome coverage requires newly generated chunks because world-generation features do not retroactively populate already-generated terrain.

---
## 0.1.2 Hotfix — Ground Resource Textures

### Fixed

- Fixed naturally generated Ground Resource blocks rendering with Minecraft's magenta/black missing-texture pattern.
- Ground Resource block models now reference dedicated textures under `assets/realisticciv/textures/block/` instead of inventory-item texture paths.
- The dedicated block textures currently mirror the corresponding original inventory textures, keeping the visual identity consistent while allowing later world-specific art without changing item icons.

### Validation Note

The inventory item models were already correct; this defect only affected the world-block representation. Existing generated Ground Resources should render correctly after restarting/reloading resources with this hotfix.

---

## 0.1.2 — Ground Resources + Bootstrap Locks

### Added

- Added a reusable `GroundResourceBlock` for small, non-colliding, world-visible pickup resources.
- Added world-only ground resource blocks for:
  - `realisticciv:ground_branch`
  - `realisticciv:ground_granite_stone`
  - `realisticciv:ground_flint_nodule`
- Added server-authoritative right-click pickup behavior that grants the canonical inventory item and removes the world resource.
- Added simple low-profile ground models/blockstates using the existing original RealisticCiv item textures.
- Added JSON worldgen configured/placed features for the first three loose resources.
- Added surface-support block tags to keep branches/stones on plausible bootstrap surfaces.
- Added block loot tables so ground resources also yield their canonical item when broken.
- Added `realisticciv:felling_tools`, intentionally empty until the first legitimate RealisticCiv felling tool exists.
- Added `VanillaBootstrapLocks` as a centralized first server-authoritative progression-lock service.
- Added `docs/19_GROUND_RESOURCES_AND_BOOTSTRAP_LOCKS.md`.

### Changed

- `Branch` now generates primarily in forest biomes.
- `Granite Stone` now generates as a sparse loose surface resource across suitable Overworld surfaces.
- `Flint Nodule` now generates more rarely on exposed gravel/sand-style surfaces.
- Survival players can no longer break vanilla log blocks unless the held tool is accepted by `realisticciv:felling_tools`. Creative-mode development remains unaffected.
- Disabled the first group of vanilla bootstrap recipes: log/wood-to-planks, sticks, crafting table, furnace, wooden tools, and stone tools.
- Updated roadmap, world-resource, vanilla-lock, handoff, and vertical-slice documentation for the v0.1.2 state.

### Gameplay

The player can now encounter and pick up the first RealisticCiv resources naturally in newly generated chunks, while the obvious vanilla tree/plank/stick/tool bootstrap is blocked. Knapping, plant fibre, cordage, timed `WorkAction` crafting, and the first legitimate felling tool are **not implemented yet**, so this version intentionally leaves survival progression at the gathered-resource stage.

### Validation Note

All JSON/resource files, documentation links, generated package contents, and ZIP integrity were statically validated in the assistant environment. The assistant environment does not provide the project's Java 25 toolchain, so the project owner must run `runDatagen`, `build`, `runClient`, and `runServer` locally before v0.1.2 is considered verified. Ground-resource world generation must be tested in a new world or newly generated chunks.

---

## 0.1.1 — Material Identity Foundation

### Added

- Registered the first RealisticCiv items:
  - `realisticciv:branch`
  - `realisticciv:granite_stone`
  - `realisticciv:flint_nodule`
- Added stable item `ResourceKey` definitions for use by registries and datagen.
- Added a dedicated RealisticCiv creative tab for development/testing.
- Added coarse item tags:
  - `realisticciv:natural_branches`
  - `realisticciv:loose_stones`
  - `realisticciv:hammerstones`
  - `realisticciv:knappable_stones`
- Added the bootstrap material model:
  - `MaterialDefinition`
  - `MaterialTrait`
  - `MaterialCatalog`
- Added initial material identities for granite and flint with validated normalized gameplay properties.
- Added Fabric datagen providers for English translations, item/client-item models, and item tags.
- Added generated JSON output for the first items/tags so the source snapshot already contains the current generated resources.
- Added original placeholder 16x16 textures for Branch, Granite Stone, and Flint Nodule.
- Added `docs/18_MATERIAL_IDENTITY_AND_PROPERTIES.md`.

### Changed

- Material identity now begins immediately instead of using a generic `Tool Stone` item.
- Granite Stone is the initial common hammerstone/general hard-stone material.
- Flint Nodule is the initial high-quality knappable stone material.
- Updated roadmap, vertical slice, early-game, architecture, quest-book, visual-crafting, and handoff documentation to use material/capability-based terminology.
- Explicitly documented that RealisticCiv will own its core Quest Book implementation rather than requiring an external quest framework.
- Updated project status/handoff instructions for the next developer/GPT session.
- Cleaned the source-package script header and removed the redundant root `stuff.txt` command note.

### Removed

- Remaining Fabric template example Mixin classes/config files.
- The planned generic `Tool Stone` inventory item concept.

### Gameplay

The three new items are registered and inspectable, but **world gathering is not implemented yet**. They do not naturally spawn, knapping is not implemented, and vanilla progression is not blocked yet.

### Validation Note

The implementation was statically checked against the current Fabric 26.2 item/creative-tab/datagen APIs and all JSON/assets/package contents were validated in the handoff environment. A full Gradle compile/run could not be executed in the assistant sandbox because it only provides Java 21 and cannot download the required Java 25/Gradle toolchain. Run the local verification commands in `docs/14_CURRENT_STATE_AND_HANDOFF.md` before beginning Phase 1.

---

## 0.1.0 — Project Foundation / Bootstrap

- Created and verified the Fabric 26.2 development project.
- Verified local development client and dedicated-server workflow.
- Added the complete project design/documentation set under `docs/`.
- Added initial package/data/resource folder skeleton for planned systems.
- Removed unused Fabric template example Mixins.
- Replaced template initialization text with the RealisticCiv initializer baseline.
- Added `tools/package_source.py` for clean development handoff ZIPs.
- Added Quest Book UI mockup references.
- Documented visual in-world crafting/worksite/animation direction.

### Gameplay

No gameplay systems were implemented in v0.1.0.
