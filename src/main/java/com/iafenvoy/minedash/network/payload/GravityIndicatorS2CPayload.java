package com.iafenvoy.minedash.network.payload;

import com.iafenvoy.minedash.MineDash;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GravityIndicatorS2CPayload(boolean reverse) implements CustomPacketPayload {
    public static final Type<GravityIndicatorS2CPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "gravity_indicator_s2c"));
    public static final StreamCodec<ByteBuf, GravityIndicatorS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, GravityIndicatorS2CPayload::reverse,
            GravityIndicatorS2CPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
