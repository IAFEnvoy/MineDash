package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.data.PlayMode;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class MDEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> REGISTRY = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, MineDash.MOD_ID);

    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<PlayMode>> PLAY_MODE = register("play_mode", () -> EntityDataSerializer.forValueType(PlayMode.STREAM_CODEC));

    public static <T> DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<T>> register(String id, Supplier<EntityDataSerializer<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
