package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.ThemeColorChangePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber
public final class NetworkManager {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(ThemeColorChangePayload.TYPE, ThemeColorChangePayload.STREAM_CODEC, ClientNetworkHandler::onThemeColorChange);
    }
}
