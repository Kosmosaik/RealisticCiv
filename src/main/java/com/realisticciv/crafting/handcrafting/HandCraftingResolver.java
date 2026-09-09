package com.realisticciv.crafting.handcrafting;

import com.realisticciv.crafting.operation.OperationCatalog;
import com.realisticciv.crafting.operation.OperationCategory;
import com.realisticciv.crafting.operation.OperationDefinition;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Resolves the vanilla inventory 2x2 grid as RealisticCiv Hand Crafting.
 *
 * <p>v0.1.5 deliberately supports the first simple schema: exactly one
 * consumed material stack and one persistent tool/capability stack. The
 * operation itself is still defined independently from the UI, allowing the
 * same operation model to be reused by future workstations and settlers.</p>
 */
public final class HandCraftingResolver {
    private HandCraftingResolver() {
    }

    public static Optional<HandCraftingMatch> find(List<ItemStack> grid) {
        int first = -1;
        int second = -1;

        for (int slot = 0; slot < grid.size(); slot++) {
            if (grid.get(slot).isEmpty()) {
                continue;
            }
            if (first < 0) {
                first = slot;
            } else if (second < 0) {
                second = slot;
            } else {
                // More than two occupied slots are not supported by the
                // bootstrap Hand Crafting schema yet.
                return Optional.empty();
            }
        }

        if (first < 0 || second < 0) {
            return Optional.empty();
        }

        ItemStack firstStack = grid.get(first);
        ItemStack secondStack = grid.get(second);

        for (OperationDefinition definition : OperationCatalog.definitions().values()) {
            if (definition.category() != OperationCategory.HAND_CRAFTING) {
                continue;
            }

            if (definition.matches(firstStack, secondStack)) {
                return Optional.of(new HandCraftingMatch(definition, first, second));
            }
            if (definition.matches(secondStack, firstStack)) {
                return Optional.of(new HandCraftingMatch(definition, second, first));
            }
        }

        return Optional.empty();
    }
}
