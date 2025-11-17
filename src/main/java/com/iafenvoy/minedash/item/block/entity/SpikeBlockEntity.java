package com.iafenvoy.minedash.item.block.entity;

import com.iafenvoy.minedash.registry.MDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SpikeBlockEntity extends BlockEntity {
    public SpikeBlockEntity(BlockPos pos, BlockState blockState) {
        super(MDBlockEntities.SPIKE.get(), pos, blockState);
    }
}
