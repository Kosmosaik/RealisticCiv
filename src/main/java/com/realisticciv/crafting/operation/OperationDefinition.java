package com.realisticciv.crafting.operation;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

/**
 * Bootstrap production definition shared by player interactions now and the
 * future WorkAction/settler execution layer later.
 *
 * <p>v0.1.3 intentionally supports a small fixed schema: one consumed input,
 * one persistent tool capability, a server-executed base duration, and fixed
 * outputs. The shape is deliberately operation-centric rather than tied to a
 * specific screen, recipe grid, or Player implementation.</p>
 */
public record OperationDefinition(
        Identifier id,
        OperationCategory category,
        TagKey<Item> inputTag,
        TagKey<Item> toolTag,
        int inputCount,
        int baseDurationTicks,
        List<OperationOutputDefinition> outputs
) {
    public OperationDefinition {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(category, "category");
        Objects.requireNonNull(inputTag, "inputTag");
        Objects.requireNonNull(toolTag, "toolTag");
        outputs = List.copyOf(Objects.requireNonNull(outputs, "outputs"));

        if (inputCount <= 0) {
            throw new IllegalArgumentException(id + " inputCount must be positive");
        }
        if (baseDurationTicks < 0) {
            throw new IllegalArgumentException(id + " baseDurationTicks cannot be negative");
        }
        if (outputs.isEmpty()) {
            throw new IllegalArgumentException(id + " must produce at least one output");
        }
    }

    public boolean matches(ItemStack input, ItemStack tool) {
        return input.getCount() >= inputCount && input.is(inputTag) && tool.is(toolTag);
    }
}
