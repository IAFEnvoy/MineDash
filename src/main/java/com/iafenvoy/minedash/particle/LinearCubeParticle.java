package com.iafenvoy.minedash.particle;

import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LinearCubeParticle extends TextureSheetParticle {
    private final float baseScale;
    private final Vec3 start, end;

    public LinearCubeParticle(CubeParticleOptions options, ClientLevel level, Vec3 start, Vec3 end, SpriteSet sprite) {
        super(level, start.x, start.y, start.z);
        this.setColor(options.getColor());
        this.baseScale = this.quadSize = options.getScale();
        this.start = start;
        this.end = end;
        this.setPos(this.start.x, this.start.y, this.start.z);
        this.pickSprite(sprite);
        this.setLifetime(20);
        this.hasPhysics = false;
    }

    public void setColor(int color) {
        this.setColor(FastColor.ARGB32.red(color) / 255.0f, FastColor.ARGB32.green(color) / 255.0f, FastColor.ARGB32.blue(color) / 255.0f);
        this.setAlpha(FastColor.ARGB32.alpha(color) / 255.0f);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = 1.0f * this.age / this.lifetime;
        Vec3 pos = MDMath.lerpVec(progress, this.start, this.end);
        this.setPos(pos.x, pos.y, pos.z);
        this.quadSize = this.baseScale * (1 - progress);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
