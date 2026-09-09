package com.realisticciv.registry;

import com.realisticciv.content.item.KnappingParticipantItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * RealisticCiv item registrations.
 *
 * These first items intentionally use real material identities rather than a
 * generic "tool stone" item. Material capabilities are described separately
 * by {@code MaterialCatalog} and form/capability tags.
 */
public final class ModItems {
    public static final Item BRANCH = register(ModItemKeys.BRANCH, Item::new, new Item.Properties());

    // Granite Stone and Flint Nodule/Core can participate directly in the first
    // two-hand knapping interaction. The service still validates capabilities
    // through operation tags; the item class itself does not hardcode recipes.
    public static final Item GRANITE_STONE = register(
            ModItemKeys.GRANITE_STONE,
            KnappingParticipantItem::new,
            new Item.Properties()
    );
    public static final Item FLINT_NODULE = register(
            ModItemKeys.FLINT_NODULE,
            KnappingParticipantItem::new,
            new Item.Properties()
    );
    public static final Item FLINT_CORE = register(
            ModItemKeys.FLINT_CORE,
            KnappingParticipantItem::new,
            new Item.Properties()
    );

    public static final Item FLINT_FLAKE = register(ModItemKeys.FLINT_FLAKE, Item::new, new Item.Properties());
    public static final Item FLINT_CHIPS = register(ModItemKeys.FLINT_CHIPS, Item::new, new Item.Properties());
    public static final Item WOODEN_HAFT = register(ModItemKeys.WOODEN_HAFT, Item::new, new Item.Properties());

    private ModItems() {
    }

    public static void initialize() {
        // Class loading performs registration. This method provides an explicit,
        // readable bootstrap call from the mod initializer.
    }

    private static Item register(
            ResourceKey<Item> key,
            Function<Item.Properties, Item> factory,
            Item.Properties properties
    ) {
        Item item = factory.apply(properties.setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
