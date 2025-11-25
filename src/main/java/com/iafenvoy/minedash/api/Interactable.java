package com.iafenvoy.minedash.api;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalInt;

public interface Interactable extends HitboxProvider {
    int TICK = 20, SPIDER_TICK = 10, BIG_PAD_TICK = 30;

    //Return value: Trail ticks
    default OptionalInt onCollision(BlockState state, GamePlayEntity entity) {
        return OptionalInt.empty();
    }

    default OptionalInt onClick(BlockState state, GamePlayEntity entity) {
        return OptionalInt.empty();
    }
}
