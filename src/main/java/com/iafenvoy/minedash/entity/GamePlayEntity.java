package com.iafenvoy.minedash.entity;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.api.HitboxType;
import com.iafenvoy.minedash.api.Interactable;
import com.iafenvoy.minedash.data.ControlType;
import com.iafenvoy.minedash.data.PlayMode;
import com.iafenvoy.minedash.data.TrailData;
import com.iafenvoy.minedash.network.GamePlayPacketDistributor;
import com.iafenvoy.minedash.network.payload.GravityIndicatorS2CPayload;
import com.iafenvoy.minedash.registry.MDEntityDataSerializers;
import com.iafenvoy.minedash.registry.MDItems;
import com.iafenvoy.minedash.util.FakeExplosionDamageCalculator;
import com.iafenvoy.minedash.util.Timeout;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GamePlayEntity extends LivingEntity implements OwnableEntity, HitboxProvider {
    //Play meta
    public static final EntityDataAccessor<PlayMode> PLAY_MODE = SynchedEntityData.defineId(GamePlayEntity.class, MDEntityDataSerializers.PLAY_MODE.get());
    public static final EntityDataAccessor<Boolean> REVERSE_GRAVITY = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> REVERSE_CONTROL = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> SMALL = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.BOOLEAN);
    //Rendering
    public static final EntityDataAccessor<Integer> PRIMARY_COLOR = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> SECONDARY_COLOR = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.INT);
    //Below should not save
    public static final EntityDataAccessor<Boolean> TRAIL = SynchedEntityData.defineId(GamePlayEntity.class, EntityDataSerializers.BOOLEAN);
    //Fields
    @Nullable
    private UUID owner;
    //Below should not save
    private boolean jump, left, right, dead;
    @Nullable
    private BlockPos collidingPos = null;
    private boolean collidingInteracted = false;
    private Direction direction = Direction.SOUTH;
    private int trailTick = 0;
    //Client only cache
    private final TrailData trailData = new TrailData(0.5f, 40);

    public GamePlayEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return createLivingAttributes();
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PLAY_MODE, PlayMode.CUBE);
        builder.define(REVERSE_GRAVITY, false);
        builder.define(REVERSE_CONTROL, false);
        builder.define(SMALL, false);
        builder.define(PRIMARY_COLOR, 0xFFFF00);
        builder.define(SECONDARY_COLOR, 0x00FFFF);
        builder.define(TRAIL, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPlayMode(tag.contains("PlayMode", Tag.TAG_STRING) ? PlayMode.valueOf(tag.getString("PlayMode")) : PlayMode.CUBE);
        this.setReverseGravity(tag.getBoolean("ReverseGravity"), false);
        this.setReverseControl(tag.getBoolean("ReverseControl"));
        this.setSmall(tag.getBoolean("Small"));
        this.setPrimaryColor(tag.getInt("PrimaryColor"));
        this.setSecondaryColor(tag.getInt("SecondaryColor"));
        this.owner = tag.contains("Owner", Tag.TAG_INT_ARRAY) ? tag.getUUID("Owner") : null;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("PlayMode", this.getPlayMode().name());
        tag.putBoolean("ReverseGravity", this.isReverseGravity());
        tag.putBoolean("ReverseControl", this.isReverseControl());
        tag.putBoolean("Small", this.isSmall());
        tag.putInt("PrimaryColor", this.getPrimaryColor());
        tag.putInt("SecondaryColor", this.getSecondaryColor());
        if (this.owner != null) tag.putUUID("Owner", this.owner);
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
    public @NotNull InteractionResult interact(Player player, @NotNull InteractionHand hand) {
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
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    public boolean isInLevel(double y) {
        return this.level().getMinBuildHeight() - 10 <= y && y <= this.level().getMaxBuildHeight() + 10;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInLevel(this.getY())) this.gameOver();
        this.checkCollisions();
        if (!this.level().isClientSide) this.tickTrail();
        this.updateDirection(Direction.fromYRot(this.getYRot()));
        this.setDeltaMovement(this.calculateHorizontalMovement(this.getDeltaMovement().add(0, this.calculateVerticalMovement(), 0)));
    }

    private void checkCollisions() {
        VoxelShape entityShape = Shapes.create(this.getBoundingBox());
        if (this.collidingPos != null) {//Check if it's still colliding
            BlockState state = this.level().getBlockState(this.collidingPos);
            if (!(state.getBlock() instanceof HitboxProvider provider) || !Shapes.joinIsNotEmpty(provider.getHitbox(state, this.collidingPos), entityShape, BooleanOp.AND)) {
                this.collidingPos = null;
                this.collidingInteracted = false;
            }
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
                                if (this.collidingPos == null) {
                                    this.collidingPos = pos;
                                    this.collidingInteracted = false;
                                    if (provider instanceof Interactable interactable)
                                        interactable.onCollision(state, this).ifPresent(this::setTrailTick);
                                }
                            }
                        }
                }
    }

    public void updateDirection(Direction direction) {
        this.direction = direction;
        this.setYRot(this.yHeadRot = this.yBodyRot = direction.toYRot());
    }

    private double calculateVerticalMovement() {
        return this.jump && this.onGround() ? 0.67 * this.gravityFactor() : 0;
    }

    private Vec3 calculateHorizontalMovement(Vec3 original) {
        //TODO::Free move when no owner
        Vec3 movement = Vec3.ZERO;
        if (this.left ^ this.right) {
            double factor = this.right ^ this.isReverseControl() ? -0.33 : 0.33;
            movement = Vec3.atLowerCornerOf(this.direction.getClockWise().getNormal()).multiply(factor, factor, factor);
        }
        return original.with(Direction.Axis.X, movement.x).with(Direction.Axis.Z, movement.z);
    }

    public void gameOver() {
        if (this.dead) return;
        this.dead = true;
        Timeout.create(4, () -> {
            this.discard();
            this.level().explode(this, this.damageSources().generic(), new FakeExplosionDamageCalculator(), this.position(), 2, false, Level.ExplosionInteraction.NONE);
        });
    }

    public void tickTrail() {
        if (this.trailTick <= 0) this.setTrail(false);
        else {
            this.setTrail(true);
            this.trailTick--;
        }
    }

    public void setTrailTick(int tick) {
        this.trailTick = tick;
    }

    public void handleControl(ControlType controlType, boolean pressed) {
        switch (controlType) {
            case JUMP -> {
                this.jump = pressed;
                if (pressed && this.collidingPos != null && !this.collidingInteracted) {
                    BlockState state = this.level().getBlockState(this.collidingPos);
                    if (state.getBlock() instanceof Interactable interactable) {
                        this.collidingInteracted = true;
                        interactable.onClick(state, this).ifPresent(this::setTrailTick);
                    }
                }
            }
            case LEFT -> this.left = pressed;
            case RIGHT -> this.right = pressed;
        }
    }

    public PlayMode getPlayMode() {
        return this.entityData.get(PLAY_MODE);
    }

    public void setPlayMode(PlayMode playMode) {
        this.entityData.set(PLAY_MODE, playMode);
    }

    public boolean isReverseGravity() {
        return this.entityData.get(REVERSE_GRAVITY);
    }

    public void setReverseGravity(boolean reverseGravity, boolean indicate) {
        boolean changed = this.isReverseGravity() ^ reverseGravity;
        this.entityData.set(REVERSE_GRAVITY, reverseGravity);
        AttributeInstance attribute = this.getAttribute(Attributes.GRAVITY);
        if (attribute != null) attribute.setBaseValue(0.08 * this.gravityFactor());
        if (changed && indicate && !this.level().isClientSide && this.getOwner() instanceof ServerPlayer player)
            PacketDistributor.sendToPlayer(player, new GravityIndicatorS2CPayload(reverseGravity));
    }

    public void reverseGravity(boolean indicate) {
        this.setReverseGravity(!this.isReverseGravity(), indicate);
    }

    public int gravityFactor() {
        return this.isReverseGravity() ? -1 : 1;
    }

    public boolean isReverseControl() {
        return this.entityData.get(REVERSE_CONTROL);
    }

    public void setReverseControl(boolean reverseControl) {
        this.entityData.set(REVERSE_CONTROL, reverseControl);
    }

    public boolean isSmall() {
        return this.entityData.get(SMALL);
    }

    public void setSmall(boolean small) {
        this.entityData.set(SMALL, small);
    }

    public int getPrimaryColor() {
        return this.entityData.get(PRIMARY_COLOR);
    }

    public void setPrimaryColor(int color) {
        this.entityData.set(PRIMARY_COLOR, color);
    }

    public int getSecondaryColor() {
        return this.entityData.get(SECONDARY_COLOR);
    }

    public void setSecondaryColor(int color) {
        this.entityData.set(SECONDARY_COLOR, color);
    }

    public boolean hasTrail() {
        return this.entityData.get(TRAIL);
    }

    public void setTrail(boolean trail) {
        this.entityData.set(TRAIL, trail);
    }

    public TrailData getTrailData() {
        return this.trailData;
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        return super.makeBoundingBox().move(0, this.isReverseGravity() ? 0.1 : 0, 0);
    }

    //Gravity patch
    @Override
    protected void checkSupportingBlock(boolean onGround, @javax.annotation.Nullable Vec3 movement) {
        if (onGround) {
            AABB aabb = this.getBoundingBox();
            AABB aabb1 = this.isReverseGravity() ? new AABB(aabb.minX, aabb.maxY, aabb.minZ, aabb.maxX, aabb.maxY + 1.0E-6, aabb.maxZ) : new AABB(aabb.minX, aabb.minY - 1.0E-6, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
            Optional<BlockPos> optional = this.level().findSupportingBlock(this, aabb1);
            if (optional.isEmpty() && !this.onGroundNoBlocks) {
                if (movement != null) {
                    AABB aabb2 = aabb1.move(-movement.x, 0, -movement.z);
                    optional = this.level().findSupportingBlock(this, aabb2);
                    this.mainSupportingBlockPos = optional;
                }
            } else this.mainSupportingBlockPos = optional;
            this.onGroundNoBlocks = optional.isEmpty();
        } else {
            this.onGroundNoBlocks = false;
            if (this.mainSupportingBlockPos.isPresent()) this.mainSupportingBlockPos = Optional.empty();
        }
    }
}
