package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class ServerNetworkHandler {
    //FIXME::Bad implements
    public static final Map<UUID, UUID> CONTROLLING = new LinkedHashMap<>();

    public static void onGamePlayControl(GamePlayControlC2SPayload payload, IPayloadContext ctx) {
        UUID target = CONTROLLING.get(ctx.player().getUUID());
        if (target == null) return;
        if (!(ctx.player().level() instanceof ServerLevel level && level.getEntity(target) instanceof GamePlayEntity entity))
            return;

        switch (payload.controlType()) {
            case JUMP -> {
                if (entity.onGround()) entity.addDeltaMovement(new Vec3(0, 0.67, 0));
            }
            case LEFT -> entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y, 0.33);
            case RIGHT -> entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y, -0.33);
        }
        entity.hurtMarked = true;
    }
}
