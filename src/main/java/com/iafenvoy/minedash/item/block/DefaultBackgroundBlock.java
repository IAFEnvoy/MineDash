package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.item.block.entity.DefaultBackgroundBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultBackgroundBlock extends BaseEntityBlock {
    private static final MapCodec<DefaultBackgroundBlock> CODEC = simpleCodec(DefaultBackgroundBlock::new);

    public DefaultBackgroundBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DefaultBackgroundBlockEntity(pos, state);
    }
}
