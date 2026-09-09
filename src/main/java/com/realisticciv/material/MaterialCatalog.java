package com.realisticciv.material;

import com.realisticciv.RealisticCiv;
import com.realisticciv.registry.ModItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Bootstrap material catalog.
 *
 * This establishes the material-identity API before the full data-driven
 * material loader exists. Multiple item forms may map to the same underlying
 * physical material; e.g. Flint Nodule, Flint Core, Flint Flake, and Flint
 * Chips are all forms of the same Flint material identity while form-specific
 * operation eligibility is handled by item tags.
 */
public final class MaterialCatalog {
    public static final Identifier GRANITE_ID = RealisticCiv.id("granite");
    public static final Identifier FLINT_ID = RealisticCiv.id("flint");

    private static final Map<Identifier, MaterialDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static final Map<Item, Identifier> ITEM_MATERIALS = new IdentityHashMap<>();
    private static boolean initialized;

    private MaterialCatalog() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        register(
                new MaterialDefinition(
                        GRANITE_ID,
                        Set.of(MaterialTrait.STONE, MaterialTrait.HAMMERSTONE),
                        0.15F,
                        0.15F,
                        0.90F,
                        0.85F
                ),
                ModItems.GRANITE_STONE
        );

        register(
                new MaterialDefinition(
                        FLINT_ID,
                        Set.of(MaterialTrait.STONE, MaterialTrait.KNAPPABLE),
                        0.95F,
                        0.95F,
                        0.55F,
                        0.75F
                ),
                ModItems.FLINT_NODULE,
                ModItems.FLINT_CORE,
                ModItems.FLINT_FLAKE,
                ModItems.FLINT_CHIPS
        );

        validate();
        initialized = true;
        RealisticCiv.LOGGER.info(
                "Loaded {} bootstrap material definitions mapped to {} item forms",
                DEFINITIONS.size(),
                ITEM_MATERIALS.size()
        );
    }

    public static Optional<MaterialDefinition> get(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }

    public static Optional<MaterialDefinition> get(Item item) {
        Identifier id = ITEM_MATERIALS.get(item);
        return id == null ? Optional.empty() : get(id);
    }

    public static Map<Identifier, MaterialDefinition> definitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    private static void register(MaterialDefinition definition, Item... items) {
        MaterialDefinition previous = DEFINITIONS.putIfAbsent(definition.id(), definition);
        if (previous != null) {
            throw new IllegalStateException("Duplicate material definition: " + definition.id());
        }

        for (Item item : items) {
            Identifier previousItemMaterial = ITEM_MATERIALS.putIfAbsent(item, definition.id());
            if (previousItemMaterial != null) {
                throw new IllegalStateException("Item already has a material identity: " + item);
            }
        }
    }

    private static void validate() {
        requireTrait(ModItems.GRANITE_STONE, MaterialTrait.HAMMERSTONE);
        requireTrait(ModItems.FLINT_NODULE, MaterialTrait.KNAPPABLE);
        requireSameMaterial(ModItems.FLINT_NODULE, ModItems.FLINT_CORE);
        requireSameMaterial(ModItems.FLINT_NODULE, ModItems.FLINT_FLAKE);
        requireSameMaterial(ModItems.FLINT_NODULE, ModItems.FLINT_CHIPS);
    }

    private static void requireTrait(Item item, MaterialTrait trait) {
        MaterialDefinition definition = get(item)
                .orElseThrow(() -> new IllegalStateException("Missing material definition for item " + item));
        if (!definition.hasTrait(trait)) {
            throw new IllegalStateException(definition.id() + " must have trait " + trait);
        }
    }

    private static void requireSameMaterial(Item first, Item second) {
        Identifier firstId = ITEM_MATERIALS.get(first);
        Identifier secondId = ITEM_MATERIALS.get(second);
        if (firstId == null || secondId == null || !firstId.equals(secondId)) {
            throw new IllegalStateException(first + " and " + second + " must share a material identity");
        }
    }
}
