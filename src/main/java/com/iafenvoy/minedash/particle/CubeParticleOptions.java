package com.iafenvoy.minedash.particle;

import com.iafenvoy.minedash.registry.MDParticles;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class CubeParticleOptions extends ParticleType<CubeParticleOptions> implements ParticleOptions {
    public static final MapCodec<CubeParticleOptions> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.INT.fieldOf("color").forGetter(CubeParticleOptions::getColor),
            Codec.FLOAT.fieldOf("scale").forGetter(CubeParticleOptions::getScale)
    ).apply(i, CubeParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CubeParticleOptions> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, CubeParticleOptions::getColor,
            ByteBufCodecs.FLOAT, CubeParticleOptions::getScale,
            CubeParticleOptions::new
    );
    private final int color;
    private final float scale;

    public CubeParticleOptions(int color, float scale) {
        super(true);
        this.color = color;
        this.scale = scale;
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

    public int getColor() {
        return this.color;
    }

    public float getScale() {
        return this.scale;
    }
}
