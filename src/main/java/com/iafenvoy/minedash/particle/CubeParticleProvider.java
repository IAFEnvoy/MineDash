package com.iafenvoy.minedash.particle;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public record CubeParticleProvider(SpriteSet sprite) implements ParticleProvider<CubeParticleOptions> {
    @Override
    public @Nullable Particle createParticle(@NotNull CubeParticleOptions options, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        CubeParticleOptions.MovementType type = options.getMovementType();
        Direction direction = options.getDirection();
        RandomSource random = level.getRandom();
        double range = options.getRange();
        if (type == CubeParticleOptions.MovementType.DIRECTIONAL) {
            Vec3 normal = Vec3.atLowerCornerOf(direction.getNormal());
            Vec3 offset = MDMath.unitNormal(direction.getAxis()).multiply(random.nextDouble() - 0.5, random.nextDouble() - 0.5, random.nextDouble() - 0.5);
            Vec3 start = offset.add(x, y, z).add(normal.multiply(-0.5, -0.5, -0.5)), end = normal.multiply(2, 2, 2).add(start);
            return new LinearCubeParticle(options, level, start, end, this.sprite);
        } else {
            Vec3 here = new Vec3(x, y, z), far = here.add(new Vec3(MDMath.rotationToPosition(range, random.nextDouble() * Math.PI / 2, random.nextDouble() * Math.PI * 2).toVector3f().rotate(direction.getRotation())));
            if (type == CubeParticleOptions.MovementType.ACCUMULATE)
                return new LinearCubeParticle(options, level, far, here, this.sprite);
            if (type == CubeParticleOptions.MovementType.RADIATE)
                return new LinearCubeParticle(options, level, here, far, this.sprite);
        }
        return null;
    }
}
