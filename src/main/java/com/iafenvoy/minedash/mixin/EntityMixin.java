package com.iafenvoy.minedash.mixin;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    public boolean verticalCollision;

    @Shadow
    public boolean verticalCollisionBelow;

    @Unique
    private Entity mineDash$self() {
        return (Entity) (Object) this;
    }

    //FIXME::BAD MIXIN
    @Inject(method = "move", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;verticalCollisionBelow:Z", ordinal = 0, shift = At.Shift.AFTER))
    private void handleReversedGravity(MoverType type, Vec3 pos, CallbackInfo ci) {
        if (this.mineDash$self() instanceof GamePlayEntity entity && entity.isReverseGravity())
            this.verticalCollisionBelow = this.verticalCollision && pos.y > 0;
    }
}
