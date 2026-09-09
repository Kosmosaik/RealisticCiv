package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Stable registry keys for RealisticCiv items.
 *
 * Keeping keys separate from item construction gives datagen, tags, recipes and
 * future definition validation a single place to reference item identities.
 */
public final class ModItemKeys {
    public static final ResourceKey<Item> BRANCH = create("branch");
    public static final ResourceKey<Item> GRANITE_STONE = create("granite_stone");
    public static final ResourceKey<Item> FLINT_NODULE = create("flint_nodule");
    public static final ResourceKey<Item> FLINT_CORE = create("flint_core");
    public static final ResourceKey<Item> FLINT_FLAKE = create("flint_flake");
    public static final ResourceKey<Item> FLINT_CHIPS = create("flint_chips");
    public static final ResourceKey<Item> WOODEN_HAFT = create("wooden_haft");

    private ModItemKeys() {
    }

    private static ResourceKey<Item> create(String path) {
        return ResourceKey.create(Registries.ITEM, RealisticCiv.id(path));
    }
}
