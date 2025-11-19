package com.iafenvoy.minedash.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum PlayMode {
    CUBE, SHIP, BALL, UFO, WAVE, ROBOT, SPIDER, SWING, JETPACK;
    public static final StreamCodec<ByteBuf, PlayMode> STREAM_CODEC = ByteBufCodecs.idMapper(i -> PlayMode.values()[i], Enum::ordinal);
}
