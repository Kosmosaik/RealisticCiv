package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * Capability/form-oriented item tags used by operations instead of checking
 * for one hardcoded material item.
 */
public final class ModItemTags {
    public static final TagKey<Item> NATURAL_BRANCHES = create("natural_branches");
    public static final TagKey<Item> LOOSE_STONES = create("loose_stones");

    // Primitive stone-working capabilities/forms.
    public static final TagKey<Item> HAMMERSTONES = create("hammerstones");
    public static final TagKey<Item> KNAPPABLE_STONES = create("knappable_stones");
    public static final TagKey<Item> KNAPPING_NODULES = create("knapping_nodules");
    public static final TagKey<Item> KNAPPING_CORES = create("knapping_cores");
    public static final TagKey<Item> STONE_FLAKES = create("stone_flakes");
    public static final TagKey<Item> KNAPPING_WASTE = create("knapping_waste");
    public static final TagKey<Item> CUTTING_EDGES = create("cutting_edges");
    public static final TagKey<Item> TOOL_HAFTS = create("tool_hafts");

    /**
     * Future legitimate tree-felling tools. Intentionally empty until the
     * primitive axe milestone. Vanilla axes are not accepted because they
     * would bypass RealisticCiv's primitive-tool progression.
     */
    public static final TagKey<Item> FELLING_TOOLS = create("felling_tools");

    private ModItemTags() {
    }

    private static TagKey<Item> create(String path) {
        return TagKey.create(net.minecraft.core.registries.Registries.ITEM, RealisticCiv.id(path));
    }
}
