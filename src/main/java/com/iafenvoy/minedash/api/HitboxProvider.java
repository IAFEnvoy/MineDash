package com.iafenvoy.minedash.api;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface HitboxProvider {
    HitboxType getHitboxType();

    VoxelShape getHitbox(BlockState state);
}
