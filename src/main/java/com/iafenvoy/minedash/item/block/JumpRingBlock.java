package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.core.Direction;

import java.util.OptionalInt;

public class JumpRingBlock extends RingBlock {
    private final double power;

    public JumpRingBlock(double power) {
        this.power = power;
    }

    @Override
    public OptionalInt onClick(GamePlayEntity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().with(Direction.Axis.Y, this.power * entity.gravityFactor()));
        return OptionalInt.of(RING_TICK);
    }
}
