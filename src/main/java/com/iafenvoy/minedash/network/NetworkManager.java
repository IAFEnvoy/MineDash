package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import com.iafenvoy.minedash.network.payload.ThemeColorChangeS2CPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber
public final class NetworkManager {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(ThemeColorChangeS2CPayload.TYPE, ThemeColorChangeS2CPayload.STREAM_CODEC, ClientNetworkHandler::onThemeColorChange)
                .playToServer(GamePlayControlC2SPayload.TYPE, GamePlayControlC2SPayload.STREAM_CODEC, ServerNetworkHandler::onGamePlayControl);
    }
}
