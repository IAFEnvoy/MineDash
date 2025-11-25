package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.data.PlayMode;
import com.iafenvoy.minedash.item.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class MDBlocks {
    public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(MineDash.MOD_ID);

    public static final DeferredBlock<DefaultBackgroundBlock> DEFAULT_BACKGROUND = register("default_background", () -> new DefaultBackgroundBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE = register("square", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE_1 = register("square_1", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<ConnectedBlock> SQUARE_F = register("square_f", () -> new ConnectedBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<AbstractSpikeBlock> SPIKE = register("spike", () -> new SpikeBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<AbstractSpikeBlock> SMALL_SPIKE = register("small_spike", () -> new SmallSpikeBlock(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<JumpRingBlock> JUMP_RING = register("jump_ring", () -> new JumpRingBlock(0.67));//2
    public static final DeferredBlock<JumpRingBlock> BIG_JUMP_RING = register("big_jump_ring", () -> new JumpRingBlock(0.85));//4
    public static final DeferredBlock<JumpRingBlock> SMALL_JUMP_RING = register("small_jump_ring", () -> new JumpRingBlock(0.45));//1
    public static final DeferredBlock<JumpRingBlock> DROP_RING = register("drop_ring", () -> new JumpRingBlock(-0.67));
    public static final DeferredBlock<GravityRingBlock> GRAVITY_RING = register("gravity_ring", () -> new GravityRingBlock(true));
    public static final DeferredBlock<GravityRingBlock> GRAVITY_JUMP_RING = register("gravity_jump_ring", () -> new GravityRingBlock(false));
    public static final DeferredBlock<SpiderRingBlock> SPIDER_RING_UP = register("spider_ring_up", () -> new SpiderRingBlock(true));
    public static final DeferredBlock<SpiderRingBlock> SPIDER_RING_DOWN = register("spider_ring_down", () -> new SpiderRingBlock(false));

    public static final DeferredBlock<JumpPadBlock> JUMP_PAD = register("jump_pad", () -> new JumpPadBlock(0xFFFF00, 0.88));//4.5
    public static final DeferredBlock<JumpPadBlock> BIG_JUMP_PAD = register("big_jump_pad", () -> new JumpPadBlock(0xFF0000, 1.1));//7
    public static final DeferredBlock<JumpPadBlock> SMALL_JUMP_PAD = register("small_jump_pad", () -> new JumpPadBlock(0xFF00FF, 0.67));//2
    public static final DeferredBlock<GravityPadBlock> GRAVITY_PAD = register("gravity_pad", () -> new GravityPadBlock(0x00FFFF));
    public static final DeferredBlock<SpiderPadBlock> SPIDER_PAD = register("spider_pad", () -> new SpiderPadBlock(0x8A2BE2));

    public static final DeferredBlock<ModePortalBlock> CUBE_PORTAL = register("cube_portal", () -> new ModePortalBlock(PlayMode.CUBE, PlayMode.CUBE, 0x00FF00));
    public static final DeferredBlock<ModePortalBlock> SHIP_PORTAL = register("ship_portal", () -> new ModePortalBlock(PlayMode.SHIP, PlayMode.JETPACK, 0xFF00FF));
    public static final DeferredBlock<ModePortalBlock> BALL_PORTAL = register("ball_portal", () -> new ModePortalBlock(PlayMode.BALL, PlayMode.BALL, 0xFF0000));
    public static final DeferredBlock<ModePortalBlock> UFO_PORTAL = register("ufo_portal", () -> new ModePortalBlock(PlayMode.UFO, PlayMode.UFO, 0xFFA500));
    public static final DeferredBlock<ModePortalBlock> WAVE_PORTAL = register("wave_portal", () -> new ModePortalBlock(PlayMode.WAVE, PlayMode.JETPACK, 0x00FFFF));
    public static final DeferredBlock<ModePortalBlock> ROBOT_PORTAL = register("robot_portal", () -> new ModePortalBlock(PlayMode.ROBOT, PlayMode.ROBOT, 0xFFFFFF));
    public static final DeferredBlock<ModePortalBlock> SPIDER_PORTAL = register("spider_portal", () -> new ModePortalBlock(PlayMode.SPIDER, PlayMode.SPIDER, 0x8A2BE2));
    public static final DeferredBlock<ModePortalBlock> SWING_PORTAL = register("swing_portal", () -> new ModePortalBlock(PlayMode.SWING, PlayMode.JETPACK, 0xFFFF00));

    public static <T extends Block> DeferredBlock<T> register(String id, Supplier<T> obj) {
        DeferredBlock<T> r = REGISTRY.register(id, obj);
        MDItems.register(id, () -> new BlockItem(r.get(), new Item.Properties()));
        return r;
    }
}
