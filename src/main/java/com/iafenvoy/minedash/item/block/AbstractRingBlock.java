package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.api.Interactable;
import com.iafenvoy.minedash.item.block.entity.RingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AbstractRingBlock extends Block implements Interactable, EntityBlock {
    public AbstractRingBlock() {
        super(Properties.ofFullCopy(Blocks.GLASS).lightLevel(state -> 15));
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull HitboxType getHitboxType() {
        return HitboxType.INTERACTABLE;
    }

    @Override
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        return box(-2, -2, -2, 18, 18, 18);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RingBlockEntity(pos, state);
    }
}
