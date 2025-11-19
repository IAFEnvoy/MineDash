package com.iafenvoy.minedash.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public interface HitboxProvider {
    @NotNull
    HitboxType getHitboxType();

    @NotNull
    VoxelShape getHitbox(@NotNull BlockState state);

    @NotNull
    default VoxelShape getHitbox(@NotNull BlockState state, @NotNull BlockPos pos) {
        return this.getHitbox(state).move(pos.getX(), pos.getY(), pos.getZ());
    }
}
