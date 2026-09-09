package com.realisticciv.material;

import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.Set;

/**
 * Gameplay-relevant identity for a physical material.
 *
 * Values are normalized to 0..1 for the bootstrap. The model is deliberately
 * independent of any one recipe so later operations can ask for capabilities
 * such as "knappability >= 0.7" instead of requiring a fake Tool Stone item.
 */
public record MaterialDefinition(
        Identifier id,
        Set<MaterialTrait> traits,
        float knappability,
        float edgeQuality,
        float toughness,
        float hardness
) {
    public MaterialDefinition {
        Objects.requireNonNull(id, "id");
        traits = Set.copyOf(Objects.requireNonNull(traits, "traits"));
        validateNormalized("knappability", knappability);
        validateNormalized("edgeQuality", edgeQuality);
        validateNormalized("toughness", toughness);
        validateNormalized("hardness", hardness);

        if (traits.contains(MaterialTrait.KNAPPABLE) && knappability <= 0.0F) {
            throw new IllegalArgumentException(id + " is KNAPPABLE but has zero knappability");
        }
    }

    public boolean hasTrait(MaterialTrait trait) {
        return traits.contains(trait);
    }

    private static void validateNormalized(String name, float value) {
        if (Float.isNaN(value) || value < 0.0F || value > 1.0F) {
            throw new IllegalArgumentException(name + " must be between 0 and 1, got " + value);
        }
    }
}
