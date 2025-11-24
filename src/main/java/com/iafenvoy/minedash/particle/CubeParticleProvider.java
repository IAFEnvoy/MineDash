package com.iafenvoy.minedash.particle;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public record CubeParticleProvider(SpriteSet sprite) implements ParticleProvider<CubeParticleOptions> {
    @Override
    public @NotNull Particle createParticle(CubeParticleOptions options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Vec3 here = new Vec3(x, y, z), far = here.add(new Vec3(MDMath.rotationToPosition(options.getRange(), level.random.nextDouble() * Math.PI / 2, level.random.nextDouble() * Math.PI * 2).toVector3f().rotate(options.getDirection().getRotation())));
        return switch (options.getMovementType()) {
            case ACCUMULATE -> new LinearCubeParticle(options, level, far, here, this.sprite);
            case RADIATE -> new LinearCubeParticle(options, level, here, far, this.sprite);
        };
    }
}
