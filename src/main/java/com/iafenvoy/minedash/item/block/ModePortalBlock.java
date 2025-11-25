package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.data.PlayMode;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.particle.CubeParticleOptions;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalInt;

public class ModePortalBlock extends AbstractPortalBlock {
    private final PlayMode classic, platformer;

    public ModePortalBlock(PlayMode classic, PlayMode platformer, int particleColor) {
        super(particleColor);
        this.classic = classic;
        this.platformer = platformer;
    }

    @Override
    public OptionalInt onCollision(BlockState state, GamePlayEntity entity) {
        //FIXME::Game mode check
        entity.setPlayMode(this.platformer);
        return OptionalInt.empty();
    }

    @Override
    protected ParticleOptions createParticle(RandomSource random, Direction direction) {
        return new CubeParticleOptions(CubeParticleOptions.MovementType.ACCUMULATE, this.particleColor | 0x5F000000, random.nextFloat() * 0.15f + 0.15f, 1.5, direction);
    }
}
