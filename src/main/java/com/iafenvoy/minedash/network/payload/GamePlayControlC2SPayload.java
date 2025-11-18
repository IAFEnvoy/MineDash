package com.iafenvoy.minedash.network.payload;

import com.iafenvoy.minedash.MineDash;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record GamePlayControlC2SPayload(ControlType controlType, boolean pressed) implements CustomPacketPayload {
    public static final Type<GamePlayControlC2SPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "game_play_control_c2s"));
    public static final StreamCodec<ByteBuf, GamePlayControlC2SPayload> STREAM_CODEC = StreamCodec.composite(
            ControlType.CODEC, GamePlayControlC2SPayload::controlType,
            ByteBufCodecs.BOOL, GamePlayControlC2SPayload::pressed,
            GamePlayControlC2SPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum ControlType {
        JUMP, LEFT, RIGHT;
        public static final StreamCodec<ByteBuf, ControlType> CODEC = ByteBufCodecs.idMapper(i -> ControlType.values()[i], Enum::ordinal);
    }
}
