package com.iafenvoy.minedash.entity;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.api.Interactable;
import com.iafenvoy.minedash.network.GamePlayPacketDistributor;
import com.iafenvoy.minedash.registry.MDItems;
import com.iafenvoy.minedash.registry.MDSounds;
import com.iafenvoy.minedash.util.MathUtil;
import com.iafenvoy.minedash.util.Timeout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GamePlayEntity extends Mob implements OwnableEntity, HitboxProvider {
    @Nullable
    private UUID owner;
    private boolean jump, left, right, dead;
    @Nullable
    private BlockPos colliding = null;

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
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        return Shapes.block();
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(MDItems.DELETE_STICK)) this.discard();
        else {
            this.owner = player.getUUID();
            GamePlayPacketDistributor.bindEntity(player, this);
        }
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
        return !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public void tick() {
        super.tick();
        this.checkCollisions();
        //Rotation
        this.setYRot((float) MathUtil.findClosestMultipleOf90(this.getYRot()));
        this.yHeadRot = this.yBodyRot = this.getYRot();
        //Movement
        Vec3 deltaMovement = this.getDeltaMovement();
        if (this.jump && this.onGround()) deltaMovement = deltaMovement.add(0, 0.67, 0);
        if (this.left ^ this.right) deltaMovement = deltaMovement.with(Direction.Axis.Z, this.left ? 0.33 : -0.33);
        else deltaMovement = deltaMovement.with(Direction.Axis.Z, 0);
        this.setDeltaMovement(deltaMovement);
    }

    public void checkCollisions() {
        VoxelShape entityShape = Shapes.create(this.getBoundingBox());
        if (this.colliding != null) {//Check if it's still colliding
            BlockState state = this.level().getBlockState(this.colliding);
            if (!(state.getBlock() instanceof HitboxProvider provider) || !Shapes.joinIsNotEmpty(provider.getHitbox(state, this.colliding), entityShape, BooleanOp.AND))
                this.colliding = null;
        }
        final int checkRange = 3;
        for (int x = -checkRange; x <= checkRange; x++)
            for (int y = -checkRange; y <= checkRange; y++)
                for (int z = -checkRange; z <= checkRange; z++) {
                    BlockPos pos = this.blockPosition().offset(x, y, z);
                    BlockState state = this.level().getBlockState(pos);
                    if (state.getBlock() instanceof HitboxProvider provider && Shapes.joinIsNotEmpty(provider.getHitbox(state, pos), entityShape, BooleanOp.AND))
                        switch (provider.getHitboxType()) {
                            case CRITICAL -> this.gameOver();
                            case INTERACTABLE -> {
                                if (provider instanceof Interactable interactable) interactable.onCollision();
                                if (this.colliding == null) this.colliding = pos;
                            }
                        }
                }
    }

    public void gameOver() {
        if (this.dead) return;
        this.dead = true;
        if (!this.level().isClientSide)
            this.level().playSound(null, this.blockPosition(), MDSounds.EXPLODE.get(), SoundSource.NEUTRAL, 3, 1);
        Timeout.create(5, this::discard);
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
