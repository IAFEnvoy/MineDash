package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.block.entity.DefaultBackgroundBlockEntity;
import com.iafenvoy.minedash.item.block.entity.SpikeBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("DataFlowIssue")
public final class MDBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MineDash.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DefaultBackgroundBlockEntity>> DEFAULT_BACKGROUND = register("default_background", () -> BlockEntityType.Builder.of(DefaultBackgroundBlockEntity::new, MDBlocks.DEFAULT_BACKGROUND.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SpikeBlockEntity>> SPIKE = register("spike", () -> BlockEntityType.Builder.of(SpikeBlockEntity::new, MDBlocks.SPIKE.get(), MDBlocks.SMALL_SPIKE.get()).build(null));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String id, Supplier<BlockEntityType<T>> obj) {
        return REGISTRY.register(id, obj);
    }
}
