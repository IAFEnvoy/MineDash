package com.iafenvoy.minedash.render;

import net.minecraft.client.DeltaTracker;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;

public final class RenderHelper {
    public static float wrapPartialTick(DeltaTracker tracker, Entity entity) {
        TickRateManager tickratemanager = entity.level().tickRateManager();
        return tracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
    }
}
