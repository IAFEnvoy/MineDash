package com.iafenvoy.minedash.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public interface HitboxProvider {
    @NotNull
    HitboxType getHitboxType();

    @NotNull
    VoxelShape getHitbox(BlockState state);
}
