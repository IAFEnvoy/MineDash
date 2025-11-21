package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.world.phys.Vec3;

public class SpiderRingBlock extends RingBlock {
    private static final double CHECK_UNIT = 0.1;
    private final boolean reverse;

    public SpiderRingBlock(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public void onClick(GamePlayEntity entity) {
        entity.setReverseGravity(this.reverse, false);
        int gravityFactor = this.reverse ? 1 : -1;
        double y = entity.getY(), unit = CHECK_UNIT * gravityFactor, result = unit;
        while (entity.isInLevel(y + result) && entity.isFree(0, result, 0)) result += unit;
        result -= unit;
        entity.setPos(entity.getX(), y + result - 0.35 * gravityFactor, entity.getZ());
        Vec3 velocity = entity.getDeltaMovement();
        entity.setDeltaMovement(velocity.x, 0, velocity.z);
    }
}
