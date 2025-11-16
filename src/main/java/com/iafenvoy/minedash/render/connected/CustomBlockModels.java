package com.iafenvoy.minedash.render.connected;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class CustomBlockModels {
    private final Multimap<ResourceLocation, NonNullFunction<BakedModel, ? extends BakedModel>> modelFuncs = MultimapBuilder.hashKeys().arrayListValues().build();
    private final Set<Block> registered = new HashSet<>();
    private final Map<Block, NonNullFunction<BakedModel, ? extends BakedModel>> finalModelFuncs = new IdentityHashMap<>();
    private boolean funcsLoaded = false;

    public void register(Block block, NonNullFunction<BakedModel, ? extends BakedModel> func) {
        this.modelFuncs.put(BuiltInRegistries.BLOCK.getKey(block), func);
        this.registered.add(block);
    }

    public void forEach(NonNullBiConsumer<Block, NonNullFunction<BakedModel, ? extends BakedModel>> consumer) {
        this.loadEntriesIfMissing();
        this.finalModelFuncs.forEach(consumer);
    }

    private void loadEntriesIfMissing() {
        if (!this.funcsLoaded) {
            ModConnectives.register();
            this.loadEntries();
            this.funcsLoaded = true;
        }
    }

    private void loadEntries() {
        this.finalModelFuncs.clear();
        this.modelFuncs.asMap().forEach((location, funcList) -> {
            Block block = BuiltInRegistries.BLOCK.get(location);

            NonNullFunction<BakedModel, ? extends BakedModel> finalFunc = null;
            for (NonNullFunction<BakedModel, ? extends BakedModel> func : funcList) {
                if (finalFunc == null) {
                    finalFunc = func;
                } else {
                    finalFunc = finalFunc.andThen(func);
                }
            }

            this.finalModelFuncs.put(block, finalFunc);
        });
    }

    public boolean containsBlock(Block block) {
        return this.registered.contains(block);
    }
}
