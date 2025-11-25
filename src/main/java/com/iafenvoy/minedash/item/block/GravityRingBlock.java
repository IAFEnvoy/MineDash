package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalInt;

public class GravityRingBlock extends AbstractRingBlock {
    private final boolean reverseVelocity;

    public GravityRingBlock(boolean reverseVelocity) {
        this.reverseVelocity = reverseVelocity;
    }

    @Override
    public OptionalInt onClick(BlockState state, GamePlayEntity entity) {
        entity.reverseGravity(true);
        Vec3 velocity = entity.getDeltaMovement();
        velocity = velocity.add(0, 0.33 * entity.gravityFactor(), 0);
        if (this.reverseVelocity) velocity = velocity.multiply(1, -1, 1);
        entity.setDeltaMovement(velocity);
        return OptionalInt.of(TICK);
    }
}
