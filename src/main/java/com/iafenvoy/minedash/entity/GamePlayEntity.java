package com.iafenvoy.minedash.entity;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.api.HitboxType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class GamePlayEntity extends Mob implements HitboxProvider {
    public GamePlayEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.GRAVITY, 1);
    }

    @Override
    public @NotNull HitboxType getHitboxType() {
        return HitboxType.PLAYER;
    }

    @Override
    public @NotNull VoxelShape getHitbox(BlockState state) {
        return Shapes.block();
    }
}
