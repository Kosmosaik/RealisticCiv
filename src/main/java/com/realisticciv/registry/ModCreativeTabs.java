package com.realisticciv.registry;

import com.realisticciv.RealisticCiv;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/** Development-friendly creative tab for inspecting RealisticCiv content. */
public final class ModCreativeTabs {
    public static final ResourceKey<CreativeModeTab> REALISTIC_CIV_KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            RealisticCiv.id("realistic_civ")
    );

    public static final CreativeModeTab REALISTIC_CIV = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.FLINT_NODULE))
            .title(Component.translatable("creativeTab.realisticciv"))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.BRANCH);
                output.accept(ModItems.GRANITE_STONE);
                output.accept(ModItems.FLINT_NODULE);
                output.accept(ModItems.FLINT_CORE);
                output.accept(ModItems.FLINT_FLAKE);
                output.accept(ModItems.FLINT_CHIPS);
                output.accept(ModItems.WOODEN_HAFT);
            })
            .build();

    private ModCreativeTabs() {
    }

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, REALISTIC_CIV_KEY, REALISTIC_CIV);
    }
}
