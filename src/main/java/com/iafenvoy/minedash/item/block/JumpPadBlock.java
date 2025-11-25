package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalInt;

public class JumpPadBlock extends AbstractPadBlock {
    private final double power;

    public JumpPadBlock(int color, double power) {
        super(color);
        this.power = power;
    }

    @Override
    public OptionalInt onCollision(BlockState state, GamePlayEntity entity) {
        entity.setDeltaMovement(entity.getDeltaMovement().with(Direction.Axis.Y, this.power * entity.gravityFactor()));
        return OptionalInt.of(this.power > 1 ? BIG_PAD_TICK : TICK);
    }
}
