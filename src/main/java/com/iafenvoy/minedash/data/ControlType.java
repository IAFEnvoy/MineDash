package com.iafenvoy.minedash.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum ControlType {
    JUMP, LEFT, RIGHT;
    public static final StreamCodec<ByteBuf, ControlType> STREAM_CODEC = ByteBufCodecs.idMapper(i -> ControlType.values()[i], Enum::ordinal);
}
