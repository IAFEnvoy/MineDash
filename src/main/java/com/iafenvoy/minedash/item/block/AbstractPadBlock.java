package com.iafenvoy.minedash.item.block;

import com.google.common.collect.ImmutableMap;
import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.api.Interactable;
import com.iafenvoy.minedash.item.block.entity.PadBlockEntity;
import com.iafenvoy.minedash.particle.CubeParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AbstractPadBlock extends FacingBlock implements EntityBlock, Interactable {
    private static final Map<Direction, VoxelShape> HITBOXES = ImmutableMap.<Direction, VoxelShape>builder()
            .put(Direction.UP, box(0, 0, 0, 16, 2, 16))
            .put(Direction.DOWN, box(0, 14, 0, 16, 16, 16))
            .put(Direction.EAST, box(0, 0, 0, 2, 16, 16))
            .put(Direction.WEST, box(14, 0, 0, 16, 16, 16))
            .put(Direction.SOUTH, box(0, 0, 0, 16, 16, 2))
            .put(Direction.NORTH, box(0, 0, 14, 16, 16, 16))
            .build();
    protected final int color;

    public AbstractPadBlock(int color) {
        super(Properties.of());
        this.color = color;
    }

    @Override
    public @NotNull HitboxType getHitboxType() {
        return HitboxType.INTERACTABLE;
    }

    @Override
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        return HITBOXES.get(state.getValue(FACING));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PadBlockEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.animateTick(state, level, pos, random);
        Vec3 center = pos.getCenter();
        for (int i = 0; i < 3; i++)
            level.addParticle(new CubeParticleOptions(CubeParticleOptions.MovementType.DIRECTIONAL, this.color | 0x5F000000, level.random.nextFloat() * 0.1f + 0.05f, 2, state.getValue(FACING)), center.x, center.y, center.z, 0, 0, 0);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.getHitbox(state);
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    public int getColor() {
        return this.color;
    }
}
