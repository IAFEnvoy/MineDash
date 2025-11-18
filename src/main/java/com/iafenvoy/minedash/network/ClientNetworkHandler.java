package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.ThemeColorChangeS2CPayload;
import com.iafenvoy.minedash.render.ThemeColorManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientNetworkHandler {
    public static void onThemeColorChange(ThemeColorChangeS2CPayload payload, IPayloadContext ctx) {
        ThemeColorManager.INSTANCE.setColor(payload.r(), payload.g(), payload.b(), 10);
    }
}
