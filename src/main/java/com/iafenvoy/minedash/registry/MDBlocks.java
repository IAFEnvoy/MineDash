package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class MDBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(MineDash.MOD_ID);

    public static final DeferredBlock<DefaultBackgroundBlock> DEFAULT_BACKGROUND = register("default_background", () -> new DefaultBackgroundBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE = register("square", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE_1 = register("square_1", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE_F = register("square_f", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<AbstractSpikeBlock> SPIKE = register("spike", () -> new SpikeBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<AbstractSpikeBlock> SMALL_SPIKE = register("small_spike", () -> new SmallSpikeBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<JumpRingBlock> JUMP_RING = register("jump_ring", () -> new JumpRingBlock(0.67));
    public static final DeferredBlock<JumpRingBlock> BIG_JUMP_RING = register("big_jump_ring", () -> new JumpRingBlock(0.85));
    public static final DeferredBlock<JumpRingBlock> SMALL_JUMP_RING = register("small_jump_ring", () -> new JumpRingBlock(0.45));
    public static final DeferredBlock<GravityRingBlock> GRAVITY_RING = register("gravity_ring", () -> new GravityRingBlock(true));
    public static final DeferredBlock<GravityRingBlock> GRAVITY_JUMP_RING = register("gravity_jump_ring", () -> new GravityRingBlock(false));

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj) {
        DeferredBlock<T> r = REGISTRY.register(id, obj);
        MDItems.register(id, () -> new BlockItem(r.get(), new Item.Properties()));
        return r;
    }
}
