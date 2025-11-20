package com.iafenvoy.minedash.item.block.entity;

import com.iafenvoy.minedash.registry.MDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RingBlockEntity extends BlockEntity {
    public RingBlockEntity(BlockPos pos, BlockState blockState) {
        super(MDBlockEntities.RING.get(), pos, blockState);
    }
}
