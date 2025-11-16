package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.event.RegisterCTBlockEvent;
import com.iafenvoy.minedash.render.connected.AllSpriteShifts;
import com.iafenvoy.minedash.render.connected.behaviour.SimpleCTBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(Dist.CLIENT)
public final class MDConnectives {
    //FIXME::Cannot register???
    @SubscribeEvent
    public static void registerCTBlocks(RegisterCTBlockEvent event) {
        event.register(MDBlocks.SQUARE.get(), () -> new SimpleCTBehaviour(AllSpriteShifts.SQUARE));
    }
}
