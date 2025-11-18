package com.iafenvoy.minedash.entity;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.network.ServerNetworkHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GamePlayEntity extends Mob implements OwnableEntity, HitboxProvider {
    @Nullable
    private UUID owner;
    private boolean jump, left, right;

    public GamePlayEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }

    @Override
    public @NotNull HitboxType getHitboxType() {
        return HitboxType.PLAYER;
    }

    @Override
    public @NotNull VoxelShape getHitbox(BlockState state) {
        return Shapes.block();
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        this.owner = player.getUUID();
        ServerNetworkHandler.CONTROLLING.put(player.getUUID(), this.getUUID());
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    @Override
    public @Nullable UUID getOwnerUUID() {
        return this.owner;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.owner = tag.contains("Owner", Tag.TAG_INT_ARRAY) ? tag.getUUID("Owner") : null;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.owner != null) tag.putUUID("Owner", this.owner);
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 deltaMovement = this.getDeltaMovement();
        if (this.jump && this.onGround()) deltaMovement = deltaMovement.add(0, 0.67, 0);
        if (this.left) deltaMovement = deltaMovement.with(Direction.Axis.Z, 0.33);
        if (this.right) deltaMovement = deltaMovement.with(Direction.Axis.Z, -0.33);
        this.setDeltaMovement(deltaMovement);
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
