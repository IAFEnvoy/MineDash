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
        Direction direction = state.getValue(FACING);
        boolean reverse;
        if (direction == Direction.UP) reverse = true;
        else if (direction == Direction.DOWN) reverse = false;
        else return OptionalInt.empty();
        if (reverse == entity.isReverseGravity()) return OptionalInt.empty();
        entity.setReverseGravity(reverse, true);
        entity.setDeltaMovement(entity.getDeltaMovement().with(Direction.Axis.Y, -0.33 * entity.gravityFactor()));
        return OptionalInt.of(TICK);
    }
}
