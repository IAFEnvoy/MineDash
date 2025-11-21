package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.world.phys.Vec3;

public class GravityRingBlock extends RingBlock {
    private final boolean reverseVelocity;

    public GravityRingBlock(boolean reverseVelocity) {
        this.reverseVelocity = reverseVelocity;
    }

    @Override
    public void onClick(GamePlayEntity entity) {
        entity.reverseGravity(true);
        Vec3 velocity = entity.getDeltaMovement();
        velocity = velocity.add(0, 0.33 * entity.gravityFactor(), 0);
        if (this.reverseVelocity) velocity = velocity.multiply(1, -1, 1);
        entity.setDeltaMovement(velocity);
    }
}
