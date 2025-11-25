package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalInt;

public class SpiderPadBlock extends AbstractPadBlock {
    private static final double CHECK_UNIT = 0.1;

    public SpiderPadBlock(int color) {
        super(color);
    }

    @Override
    public OptionalInt onCollision(BlockState state, GamePlayEntity entity) {
        Direction direction = state.getValue(FACING);
        boolean reverse;
        if (direction == Direction.UP) reverse = true;
        else if (direction == Direction.DOWN) reverse = false;
        else return OptionalInt.empty();

        entity.setReverseGravity(reverse, false);
        int gravityFactor = reverse ? 1 : -1;
        double y = entity.getY(), unit = CHECK_UNIT * gravityFactor, result = unit;
        while (entity.isInLevel(y + result) && entity.isFree(0, result, 0)) result += unit;
        result -= unit;
        entity.setPos(entity.getX(), y + result - 0.35 * gravityFactor, entity.getZ());
        Vec3 velocity = entity.getDeltaMovement();
        entity.setDeltaMovement(velocity.x, 0, velocity.z);
        return OptionalInt.of(SPIDER_TICK);
    }
}
