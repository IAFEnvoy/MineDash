package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.block.DefaultBackgroundBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MDCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MineDash.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.%s.main".formatted(MineDash.MOD_ID)))
            .icon(MDItems.GAME_PLAY_SPAWN_EGG::toStack)
            .displayItems((params, output) -> {
                output.acceptAll(DefaultBackgroundBlock.BUILTIN_STACKS.get());
                MDItems.REGISTRY.getEntries().stream().map(DeferredHolder::get).forEach(output::accept);
            }).build());

    public static <T extends CreativeModeTab> DeferredHolder<CreativeModeTab, T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
