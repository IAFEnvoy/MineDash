package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MDSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MineDash.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLODE = register("explode");

    public static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
        return REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, id)));
    }
}
