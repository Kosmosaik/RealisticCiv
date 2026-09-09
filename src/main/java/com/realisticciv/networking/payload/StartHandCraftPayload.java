package com.realisticciv.networking.payload;

import com.realisticciv.RealisticCiv;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Empty request payload: the server resolves and validates the current 2x2 grid. */
public record StartHandCraftPayload() implements CustomPacketPayload {
    public static final StartHandCraftPayload INSTANCE = new StartHandCraftPayload();
    public static final Type<StartHandCraftPayload> TYPE = new Type<>(RealisticCiv.id("start_hand_craft"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StartHandCraftPayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
