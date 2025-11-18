package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import net.minecraft.server.level.ServerLevel;
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
            case JUMP -> entity.setJump(payload.pressed());
            case LEFT -> entity.setLeft(payload.pressed());
            case RIGHT -> entity.setRight(payload.pressed());
        }
        entity.hurtMarked = true;
    }
}
