package com.realisticciv.crafting.handcrafting;

import com.realisticciv.crafting.operation.OperationDefinition;

/**
 * A validated match inside the player's 2x2 Hand Crafting grid.
 *
 * @param definition operation that can be performed
 * @param inputSlot consumed input slot index inside the 2x2 crafting container
 * @param toolSlot persistent tool/capability slot index inside the 2x2 crafting container
 */
public record HandCraftingMatch(OperationDefinition definition, int inputSlot, int toolSlot) {
}
