package com.iafenvoy.minedash.item.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SmallSpikeBlock extends AbstractSpikeBlock {
    private static final MapCodec<SmallSpikeBlock> CODEC = simpleCodec(SmallSpikeBlock::new);
    private static final Map<Direction, VoxelShape> HITBOXES = ImmutableMap.<Direction, VoxelShape>builder()
            .put(Direction.UP, box(6.4, 2, 6.4, 9.6, 5.5, 9.6))
            .put(Direction.DOWN, box(6.4, 10.5, 6.4, 9.6, 14, 9.6))
            .put(Direction.EAST, box(2, 6.4, 6.4, 5.5, 9.6, 9.6))
            .put(Direction.WEST, box(10.5, 6.4, 6.4, 14, 9.6, 9.6))
            .put(Direction.SOUTH, box(6.4, 6.4, 2, 9.6, 9.6, 5.5))
            .put(Direction.NORTH, box(6.4, 6.4, 10.5, 9.6, 9.6, 14))
            .build();

    public SmallSpikeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        return HITBOXES.get(state.getValue(FACING));
    }
}
