package com.iafenvoy.minedash.network.payload;

import com.iafenvoy.minedash.MineDash;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ThemeColorChangeS2CPayload(float r, float g, float b) implements CustomPacketPayload {
    public static final Type<ThemeColorChangeS2CPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "theme_color_change_s2c"));
    public static final StreamCodec<ByteBuf, ThemeColorChangeS2CPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ThemeColorChangeS2CPayload::r,
            ByteBufCodecs.FLOAT, ThemeColorChangeS2CPayload::g,
            ByteBufCodecs.FLOAT, ThemeColorChangeS2CPayload::b,
            ThemeColorChangeS2CPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
