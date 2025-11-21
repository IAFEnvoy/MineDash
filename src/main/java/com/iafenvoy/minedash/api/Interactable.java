package com.iafenvoy.minedash.api;

import com.iafenvoy.minedash.entity.GamePlayEntity;

public interface Interactable extends HitboxProvider {
    default void onCollision(GamePlayEntity entity) {
    }

    default void onClick(GamePlayEntity entity) {
    }
}
