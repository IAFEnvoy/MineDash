package com.iafenvoy.minedash.api;

import com.iafenvoy.minedash.entity.GamePlayEntity;

import java.util.OptionalInt;

public interface Interactable extends HitboxProvider {
    int RING_TICK = 20, SPIDER_RING_TICK = 10;

    //Return value: Trail ticks
    default OptionalInt onCollision(GamePlayEntity entity) {
        return OptionalInt.empty();
    }

    default OptionalInt onClick(GamePlayEntity entity) {
        return OptionalInt.empty();
    }
}
