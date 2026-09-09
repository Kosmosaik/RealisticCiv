package com.realisticciv.content.item;

import com.realisticciv.crafting.knapping.PrimitiveKnappingService;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * Small bootstrap item hook that lets either hand initiate a valid two-item
 * knapping pair. PrimitiveKnappingService resolves the participants and the
 * server WorkActionManager owns timed execution/outcome resolution.
 */
public final class KnappingParticipantItem extends Item {
    public KnappingParticipantItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return PrimitiveKnappingService.tryKnapHeldPair(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }

        InteractionResult result = PrimitiveKnappingService.tryKnapHeldPair(
                context.getLevel(),
                player,
                context.getHand()
        );
        return result != InteractionResult.PASS ? result : super.useOn(context);
    }
}
