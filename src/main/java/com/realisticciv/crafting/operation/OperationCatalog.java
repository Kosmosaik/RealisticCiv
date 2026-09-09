package com.realisticciv.crafting.operation;

import com.realisticciv.RealisticCiv;
import com.realisticciv.registry.ModItemTags;
import com.realisticciv.registry.ModItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Bootstrap registry for RealisticCiv production operations.
 *
 * <p>The first definition is intentionally Java-backed while the schema is
 * still being proven. v0.1.5 executes operation base durations through the generic
 * WorkAction manager and adds the first inventory Hand Crafting operation. The long-term design remains data-driven; callers use
 * operation IDs/definitions rather than embedding Flint-specific conversion
 * logic into interaction code.</p>
 */
public final class OperationCatalog {
    public static final Identifier INITIAL_FLINT_KNAPPING_ID = RealisticCiv.id("initial_flint_knapping");
    public static final Identifier SHAPE_WOODEN_HAFT_ID = RealisticCiv.id("shape_wooden_haft");

    private static final Map<Identifier, OperationDefinition> DEFINITIONS = new LinkedHashMap<>();
    private static boolean initialized;

    private OperationCatalog() {
    }

    public static void initialize() {
        if (initialized) {
            return;
        }

        register(new OperationDefinition(
                INITIAL_FLINT_KNAPPING_ID,
                OperationCategory.KNAPPING,
                ModItemTags.KNAPPING_NODULES,
                ModItemTags.HAMMERSTONES,
                1,
                80,
                java.util.List.of(
                        new OperationOutputDefinition(ModItems.FLINT_CORE, 1, OperationOutputRole.PRIMARY),
                        new OperationOutputDefinition(ModItems.FLINT_FLAKE, 2, OperationOutputRole.USEFUL_BYPRODUCT),
                        new OperationOutputDefinition(ModItems.FLINT_CHIPS, 2, OperationOutputRole.WASTE)
                )
        ));

        register(new OperationDefinition(
                SHAPE_WOODEN_HAFT_ID,
                OperationCategory.HAND_CRAFTING,
                ModItemTags.NATURAL_BRANCHES,
                ModItemTags.CUTTING_EDGES,
                1,
                100,
                java.util.List.of(
                        new OperationOutputDefinition(ModItems.WOODEN_HAFT, 1, OperationOutputRole.PRIMARY)
                )
        ));

        initialized = true;
        RealisticCiv.LOGGER.info("Loaded {} bootstrap operation definitions", DEFINITIONS.size());
    }

    public static Optional<OperationDefinition> get(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }

    public static Map<Identifier, OperationDefinition> definitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    public static Optional<OperationMatch> findTwoItemOperation(
            OperationCategory category,
            ItemStack first,
            ItemStack second
    ) {
        for (OperationDefinition definition : DEFINITIONS.values()) {
            if (definition.category() != category) {
                continue;
            }

            if (definition.matches(first, second)) {
                return Optional.of(new OperationMatch(definition, first, second));
            }
            if (definition.matches(second, first)) {
                return Optional.of(new OperationMatch(definition, second, first));
            }
        }
        return Optional.empty();
    }

    private static void register(OperationDefinition definition) {
        OperationDefinition previous = DEFINITIONS.putIfAbsent(definition.id(), definition);
        if (previous != null) {
            throw new IllegalStateException("Duplicate operation definition: " + definition.id());
        }
    }
}
