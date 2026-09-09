package com.realisticciv.progression;

import com.realisticciv.registry.ModItemTags;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;

/**
 * First server-authoritative pass at closing vanilla progression shortcuts.
 *
 * <p>This is intentionally centralized. Later unlocks should modify capability
 * tags / RealisticCiv rules instead of scattering ad-hoc checks around the mod.</p>
 */
public final class VanillaBootstrapLocks {
    private VanillaBootstrapLocks() {
    }

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (player.isCreative()) {
                return true;
            }

            if (state.is(BlockTags.LOGS) && !player.getMainHandItem().is(ModItemTags.FELLING_TOOLS)) {
                player.sendOverlayMessage(Component.translatable("message.realisticciv.requires_felling_tool"));
                return false;
            }

            return true;
        });
    }
}
