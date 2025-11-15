package com.iafenvoy.minedash;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod(value = MineDash.MOD_ID, dist = Dist.CLIENT)
public final class MineDashClient {
    public MineDashClient(ModContainer container) {
    }
}
