package com.iafenvoy.minedash.network.payload;

import com.iafenvoy.minedash.MineDash;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ThemeColorChangePayload(float r, float g, float b) implements CustomPacketPayload {
    public static final Type<ThemeColorChangePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "theme_color_change"));
    public static final StreamCodec<ByteBuf, ThemeColorChangePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ThemeColorChangePayload::r,
            ByteBufCodecs.FLOAT, ThemeColorChangePayload::g,
            ByteBufCodecs.FLOAT, ThemeColorChangePayload::b,
            ThemeColorChangePayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
