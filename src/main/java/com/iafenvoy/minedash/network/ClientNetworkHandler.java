package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.network.payload.GravityIndicatorS2CPayload;
import com.iafenvoy.minedash.network.payload.ThemeColorChangeS2CPayload;
import com.iafenvoy.minedash.render.GravityIndicatorRenderer;
import com.iafenvoy.minedash.render.ThemeColorManager;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientNetworkHandler {
    public static void onGravityIndicator(GravityIndicatorS2CPayload payload, IPayloadContext ctx) {
        GravityIndicatorRenderer.addIndicator(payload.reverse());
    }

    public static void onThemeColorChange(ThemeColorChangeS2CPayload payload, IPayloadContext ctx) {
        ThemeColorManager.INSTANCE.setColor(payload.r(), payload.g(), payload.b(), 10);
    }
}
