package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalInt;

public class GravityPadBlock extends AbstractPadBlock {
    public GravityPadBlock(int color) {
        super(color);
    }

    @Override
    public OptionalInt onCollision(BlockState state, GamePlayEntity entity) {
        entity.reverseGravity(true);
        entity.setDeltaMovement(entity.getDeltaMovement().with(Direction.Axis.Y, -0.33 * entity.gravityFactor()));
        return OptionalInt.of(TICK);
    }
}
