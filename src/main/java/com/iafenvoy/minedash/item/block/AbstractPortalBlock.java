package com.iafenvoy.minedash.item.block;

import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.api.Interactable;
import com.iafenvoy.minedash.util.MDMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPortalBlock extends FacingBlock implements Interactable {
    protected final int particleColor;

    public AbstractPortalBlock(int particleColor) {
        super(Properties.of());
        this.particleColor = particleColor;
    }

    @Override
    public @NotNull HitboxType getHitboxType() {
        return HitboxType.INTERACTABLE;
    }

    @Override
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        Vec3 normal = MDMath.unitNormal(state.getValue(FACING).getAxis());
        return box(-normal.x, -normal.y, -normal.z, normal.x + 16, normal.y + 16, normal.z + 16);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        for (int i = 0; i < 3; i++)
            level.addParticle(this.createParticle(level.getRandom(), state.getValue(FACING)), center.x, center.y, center.z, 0, 0, 0);
    }

    protected abstract ParticleOptions createParticle(RandomSource random, Direction direction);

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }
}
