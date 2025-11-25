package com.iafenvoy.minedash.particle;

import com.iafenvoy.minedash.registry.MDParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class CubeParticleOptions extends ParticleType<CubeParticleOptions> implements ParticleOptions {
    public static final MapCodec<CubeParticleOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            MovementType.CODEC.optionalFieldOf("movementType", MovementType.ACCUMULATE).forGetter(CubeParticleOptions::getMovementType),
            Codec.INT.optionalFieldOf("color", -1).forGetter(CubeParticleOptions::getColor),
            Codec.FLOAT.optionalFieldOf("scale", 1f).forGetter(CubeParticleOptions::getScale),
            Codec.DOUBLE.optionalFieldOf("range", 1d).forGetter(CubeParticleOptions::getRange),
            Direction.CODEC.optionalFieldOf("direction", Direction.UP).forGetter(CubeParticleOptions::getDirection)
    ).apply(i, CubeParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CubeParticleOptions> STREAM_CODEC = StreamCodec.composite(
            MovementType.STREAM_CODEC, CubeParticleOptions::getMovementType,
            ByteBufCodecs.INT, CubeParticleOptions::getColor,
            ByteBufCodecs.FLOAT, CubeParticleOptions::getScale,
            ByteBufCodecs.DOUBLE, CubeParticleOptions::getRange,
            Direction.STREAM_CODEC, CubeParticleOptions::getDirection,
            CubeParticleOptions::new
    );
    private final MovementType movementType;
    private final int color;
    private final float scale;
    private final double range;
    private final Direction direction;

    public CubeParticleOptions(MovementType movementType, int color, float scale, double range, Direction direction) {
        super(true);
        this.movementType = movementType;
        this.color = color;
        this.scale = scale;
        this.range = range;
        this.direction = direction;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return MDParticles.CUBE.get();
    }

    @Override
    public @NotNull MapCodec<CubeParticleOptions> codec() {
        return CODEC;
    }

    @Override
    public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, CubeParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }

    public MovementType getMovementType() {
        return this.movementType;
    }

    public int getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }

    public double getRange() {
        return this.range;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public enum MovementType implements StringRepresentable {
        ACCUMULATE, RADIATE, DIRECTIONAL;
        public static final Codec<MovementType> CODEC = StringRepresentable.fromValues(MovementType::values);
        public static final StreamCodec<ByteBuf, MovementType> STREAM_CODEC = ByteBufCodecs.idMapper(i -> MovementType.values()[i], Enum::ordinal);

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
