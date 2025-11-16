package com.iafenvoy.minedash.render.connected;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.iafenvoy.minedash.event.RegisterCTBlockEvent;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CustomBlockModels {
    private final Multimap<ResourceLocation, Function<BakedModel, ? extends BakedModel>> modelFuncs = MultimapBuilder.hashKeys().arrayListValues().build();
    private final Set<Block> registered = new HashSet<>();
    private final Map<Block, Function<BakedModel, ? extends BakedModel>> finalModelFuncs = new IdentityHashMap<>();
    private boolean funcsLoaded = false;

    public void register(Block block, Function<BakedModel, ? extends BakedModel> func) {
        this.modelFuncs.put(BuiltInRegistries.BLOCK.getKey(block), func);
        this.registered.add(block);
    }

    public void forEach(BiConsumer<Block, Function<BakedModel, ? extends BakedModel>> consumer) {
        this.loadEntriesIfMissing();
        this.finalModelFuncs.forEach(consumer);
    }

    private void loadEntriesIfMissing() {
        if (!this.funcsLoaded) {
            NeoForge.EVENT_BUS.post(new RegisterCTBlockEvent());
            this.loadEntries();
            this.funcsLoaded = true;
        }
    }

    private void loadEntries() {
        this.finalModelFuncs.clear();
        this.modelFuncs.asMap().forEach((location, funcList) -> {
            Block block = BuiltInRegistries.BLOCK.get(location);
            Function<BakedModel, ? extends BakedModel> finalFunc = null;
            for (Function<BakedModel, ? extends BakedModel> func : funcList)
                finalFunc = finalFunc == null ? func : finalFunc.andThen(func);
            this.finalModelFuncs.put(block, finalFunc);
        });
    }

    public boolean containsBlock(Block block) {
        return this.registered.contains(block);
    }
}
