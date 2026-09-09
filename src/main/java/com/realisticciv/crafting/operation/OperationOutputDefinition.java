package com.realisticciv.crafting.operation;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/** Immutable fixed output for the bootstrap operation system. */
public record OperationOutputDefinition(
        Item item,
        int count,
        OperationOutputRole role
) {
    public OperationOutputDefinition {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(role, "role");
        if (count <= 0) {
            throw new IllegalArgumentException("Operation output count must be positive");
        }
    }

    public ItemStack createStack() {
        return new ItemStack(item, count);
    }
}
