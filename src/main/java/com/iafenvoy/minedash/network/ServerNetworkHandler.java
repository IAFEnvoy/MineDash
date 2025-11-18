package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ServerNetworkHandler {
    public static void onGamePlayControl(GamePlayControlC2SPayload payload, IPayloadContext ctx) {
        GamePlayPacketDistributor.runAction(ctx.player(), payload.controlType(), payload.pressed());
    }
}
