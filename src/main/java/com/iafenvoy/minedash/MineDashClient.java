package com.iafenvoy.minedash;

import com.iafenvoy.jupiter.ConfigManager;
import com.iafenvoy.jupiter.render.screen.ClientConfigScreen;
import com.iafenvoy.minedash.config.MDClientConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@OnlyIn(Dist.CLIENT)
@Mod(value = MineDash.MOD_ID, dist = Dist.CLIENT)
public final class MineDashClient {
    public MineDashClient(ModContainer container) {
        ConfigManager.getInstance().registerConfigHandler(MDClientConfig.INSTANCE);
        container.registerExtensionPoint(IConfigScreenFactory.class, ($, parent) -> new ClientConfigScreen(parent, MDClientConfig.INSTANCE));
    }
}
