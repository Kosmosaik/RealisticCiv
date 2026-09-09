package com.realisticciv.crafting.operation;

import net.minecraft.world.item.ItemStack;

/** Resolved input/tool orientation for a two-item operation. */
public record OperationMatch(
        OperationDefinition definition,
        ItemStack input,
        ItemStack tool
) {
}
