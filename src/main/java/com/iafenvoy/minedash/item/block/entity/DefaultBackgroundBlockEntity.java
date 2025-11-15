package com.iafenvoy.minedash.item.block.entity;

import com.iafenvoy.minedash.registry.MDBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DefaultBackgroundBlockEntity extends BlockEntity {
    public DefaultBackgroundBlockEntity(BlockPos pos, BlockState blockState) {
        super(MDBlockEntities.DEFAULT_BACKGROUND.get(), pos, blockState);
    }
}
