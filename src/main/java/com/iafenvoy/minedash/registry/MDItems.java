package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MDItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MineDash.MOD_ID);

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
