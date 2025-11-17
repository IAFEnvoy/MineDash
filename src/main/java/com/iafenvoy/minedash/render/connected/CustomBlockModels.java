package com.iafenvoy.minedash.render.connected;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CustomBlockModels {
    private final Multimap<ResourceLocation, Function<BakedModel, ? extends BakedModel>> modelFunc = MultimapBuilder.hashKeys().arrayListValues().build();
    private final Set<Block> registered = new HashSet<>();
    private final Map<Block, Function<BakedModel, ? extends BakedModel>> finalModelFunc = new IdentityHashMap<>();

    public void register(Block block, Function<BakedModel, ? extends BakedModel> func) {
        this.modelFunc.put(BuiltInRegistries.BLOCK.getKey(block), func);
        this.registered.add(block);
    }

    public void forEach(BiConsumer<Block, Function<BakedModel, ? extends BakedModel>> consumer) {
        this.reloadEntries();
        this.finalModelFunc.forEach(consumer);
    }

    private void reloadEntries() {
        this.finalModelFunc.clear();
        this.modelFunc.asMap().forEach((location, funcList) -> funcList.stream().filter(Objects::nonNull).reduce(Function::andThen).ifPresent(f -> this.finalModelFunc.put(BuiltInRegistries.BLOCK.get(location), f)));
    }

    public boolean containsBlock(Block block) {
        return this.registered.contains(block);
    }
}
