package com.iafenvoy.minedash;

import com.iafenvoy.minedash.registry.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(MineDash.MOD_ID)
public final class MineDash {
    public static final String MOD_ID = "minedash";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MineDash(IEventBus bus) {
        MDBlockEntities.REGISTRY.register(bus);
        MDBlocks.REGISTRY.register(bus);
        MDCreativeTabs.REGISTRY.register(bus);
        MDDataComponents.REGISTRY.register(bus);
        MDItems.REGISTRY.register(bus);
    }
}
