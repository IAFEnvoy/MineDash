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
        if (this.reverseVelocity) {
            Vec3 vec3 = entity.getDeltaMovement();
            vec3 = new Vec3(vec3.x, -vec3.y, vec3.z);
            entity.setDeltaMovement(vec3);
        } else entity.addDeltaMovement(new Vec3(0, 0.33 * entity.gravityFactor(), 0));
    }
}
