package com.iafenvoy.minedash.item.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SmallSpikeBlock extends AbstractSpikeBlock {
    private static final MapCodec<SmallSpikeBlock> CODEC = simpleCodec(SmallSpikeBlock::new);

    public SmallSpikeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getHitbox(state);
    }

    @Override
    public VoxelShape getHitbox(BlockState state) {
        //FIXME::Rotate
        return box(6.4, 2.133, 6.4, 9.6, 5.333, 9.6);
    }
}
