package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MDItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MineDash.MOD_ID);

    public static final DeferredItem<DeferredSpawnEggItem> GAME_PLAY_SPAWN_EGG = register("game_play_spawn_egg", () -> new DeferredSpawnEggItem(MDEntities.GAME_PLAY, -1, -1, new Item.Properties()));
    public static final DeferredItem<Item> DELETE_STICK = register("delete_stick", () -> new Item(new Item.Properties()));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
