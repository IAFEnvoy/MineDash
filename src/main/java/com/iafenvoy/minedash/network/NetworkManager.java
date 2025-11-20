package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import com.iafenvoy.minedash.network.payload.GravityIndicatorS2CPayload;
import com.iafenvoy.minedash.network.payload.ThemeColorChangeS2CPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;

@EventBusSubscriber
public final class NetworkManager {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToServer(GamePlayControlC2SPayload.TYPE, GamePlayControlC2SPayload.STREAM_CODEC, new MainThreadPayloadHandler<>(ServerNetworkHandler::onGamePlayControl))
                .playToClient(GravityIndicatorS2CPayload.TYPE, GravityIndicatorS2CPayload.STREAM_CODEC, new MainThreadPayloadHandler<>(ClientNetworkHandler::onGravityIndicator))
                .playToClient(ThemeColorChangeS2CPayload.TYPE, ThemeColorChangeS2CPayload.STREAM_CODEC, new MainThreadPayloadHandler<>(ClientNetworkHandler::onThemeColorChange));
    }
}
