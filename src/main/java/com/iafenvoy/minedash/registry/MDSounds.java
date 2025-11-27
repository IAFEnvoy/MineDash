package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MDSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MineDash.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_NA = register("difficulty/na");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_AUTO = register("difficulty/auto");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_EASY = register("difficulty/easy");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_NORMAL = register("difficulty/normal");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_HARD = register("difficulty/hard");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_HARDER = register("difficulty/harder");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_INSANE = register("difficulty/insane");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_EASY_DEMON = register("difficulty/easy_demon");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_MEDIUM_DEMON = register("difficulty/medium_demon");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_HARD_DEMON = register("difficulty/hard_demon");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_INSANE_DEMON = register("difficulty/insane_demon");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIFFICULTY_EXTREME_DEMON = register("difficulty/extreme_demon");

    public static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
        return REGISTRY.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, id)));
    }
}
