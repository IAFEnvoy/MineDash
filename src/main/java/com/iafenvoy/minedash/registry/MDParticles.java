package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.particle.CubeParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MDParticles {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(Registries.PARTICLE_TYPE, MineDash.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, CubeParticleOptions> CUBE = register("cube", () -> new CubeParticleOptions(0xFFFFFFFF, 1));

    public static <O extends ParticleOptions, T extends ParticleType<O>> DeferredHolder<ParticleType<?>, T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
