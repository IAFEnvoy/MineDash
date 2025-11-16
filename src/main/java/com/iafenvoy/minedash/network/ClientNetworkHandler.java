package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.ThemeColorChangePayload;
import com.iafenvoy.minedash.render.ThemeColorManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientNetworkHandler {
    public static void onThemeColorChange(ThemeColorChangePayload payload, IPayloadContext ctx) {
        ThemeColorManager.INSTANCE.setColor(payload.r(), payload.g(), payload.b(), 10);
    }
}
