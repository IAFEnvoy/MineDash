package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.data.PlayMode;
import com.iafenvoy.minedash.entity.GamePlayEntity;

public class ModePortalBlock extends AbstractPortalBlock {
    private final PlayMode classic, platformer;

    public ModePortalBlock(PlayMode classic, PlayMode platformer, int particleColor) {
        super(particleColor);
        this.classic = classic;
        this.platformer = platformer;
    }

    @Override
    public void onCollision(GamePlayEntity entity) {
        //FIXME::Game mode check
        entity.setPlayMode(this.platformer);
    }
}
