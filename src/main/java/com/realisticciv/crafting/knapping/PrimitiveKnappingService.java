package com.realisticciv.crafting.knapping;

import com.realisticciv.crafting.operation.OperationCatalog;
import com.realisticciv.crafting.operation.OperationCategory;
import com.realisticciv.crafting.operation.OperationMatch;
import com.realisticciv.crafting.workaction.WorkActionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Player-facing adapter for primitive knapping.
 *
 * <p>The physical recipe/capability definition lives in {@link OperationCatalog}
 * and timed execution lives in {@link WorkActionManager}. This class only
 * resolves the player's chosen held/ground participants and asks the generic
 * WorkAction layer to begin the matching operation.</p>
 */
public final class PrimitiveKnappingService {
    private PrimitiveKnappingService() {
    }

    /**
     * Starts knapping when the two player hands contain a valid input/tool pair,
     * in either orientation. Example: Granite Stone + Flint Nodule.
     */
    public static InteractionResult tryKnapHeldPair(Level level, Player player, InteractionHand usedHand) {
        InteractionHand otherHand = usedHand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;

        ItemStack usedStack = player.getItemInHand(usedHand);
        ItemStack otherStack = player.getItemInHand(otherHand);

        Optional<OperationMatch> match = OperationCatalog.findTwoItemOperation(
                OperationCategory.KNAPPING,
                usedStack,
                otherStack
        );

        if (match.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            OperationMatch resolved = match.get();
            InteractionHand inputHand = resolved.input() == usedStack ? usedHand : otherHand;
            InteractionHand toolHand = resolved.tool() == usedStack ? usedHand : otherHand;
            WorkActionManager.tryStartHeldPair(serverPlayer, resolved, inputHand, toolHand);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Starts knapping when one participant is a persistent Ground Resource and
     * the other is the item currently held by the player. Either orientation is
     * accepted: held Granite Stone + ground Flint Nodule, or held Flint Nodule +
     * ground Granite Stone.
     */
    public static InteractionResult tryKnapGroundPair(
            Level level,
            BlockPos groundPos,
            Player player,
            InteractionHand heldHand,
            ItemStack heldStack,
            Item groundItem
    ) {
        ItemStack groundStack = new ItemStack(groundItem);
        Optional<OperationMatch> match = OperationCatalog.findTwoItemOperation(
                OperationCategory.KNAPPING,
                heldStack,
                groundStack
        );

        if (match.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            OperationMatch resolved = match.get();
            boolean inputIsGround = resolved.input() == groundStack;
            WorkActionManager.tryStartGroundPair(
                    serverPlayer,
                    resolved,
                    heldHand,
                    groundPos,
                    groundItem,
                    inputIsGround
            );
        }

        return InteractionResult.SUCCESS;
    }
}
