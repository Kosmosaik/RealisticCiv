package com.realisticciv.crafting.handcrafting;

import com.realisticciv.crafting.workaction.WorkActionManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/** Server-side entry point for the inventory 2x2 Hand Crafting button. */
public final class HandCraftingService {
    private HandCraftingService() {
    }

    public static void tryStartFromInventoryGrid(ServerPlayer player) {
        if (player.containerMenu != player.inventoryMenu || !(player.containerMenu instanceof InventoryMenu menu)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.hand_crafting_inventory_only"));
            return;
        }

        Optional<HandCraftingMatch> resolved = HandCraftingResolver.find(menu.getCraftSlots().getItems());
        if (resolved.isEmpty()) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.hand_crafting_no_operation"));
            return;
        }

        HandCraftingMatch match = resolved.get();
        ItemStack input = menu.getCraftSlots().getItem(match.inputSlot());
        ItemStack tool = menu.getCraftSlots().getItem(match.toolSlot());

        // Revalidate everything on the logical server. The client sends no
        // trusted recipe ID, slots, counts, or result data.
        if (!match.definition().matches(input, tool)) {
            player.sendOverlayMessage(Component.translatable("message.realisticciv.hand_crafting_changed"));
            return;
        }

        ItemStack reserved = input.copyWithCount(match.definition().inputCount());
        if (!player.isCreative()) {
            input.shrink(match.definition().inputCount());
            menu.getCraftSlots().setChanged();
        } else {
            reserved = ItemStack.EMPTY;
        }

        if (!WorkActionManager.tryStartHandCrafting(player, match.definition(), reserved)) {
            if (!reserved.isEmpty()) {
                input.grow(reserved.getCount());
                menu.getCraftSlots().setChanged();
            }
            return;
        }

        // The inventory grid is only the setup/selection surface. Once the
        // server has validated and reserved the input, close it and perform the
        // timed action in-world. InventoryMenu.removed returns the persistent
        // cutting tool to the player's inventory.
        player.closeContainer();
    }
}
