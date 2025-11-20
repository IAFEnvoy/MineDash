package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.render.DynamicItemRenderer;
import com.iafenvoy.minedash.render.block.DefaultBackgroundBlockEntityRenderer;
import com.iafenvoy.minedash.render.block.RingBlockEntityRenderer;
import com.iafenvoy.minedash.render.block.SpikeBlockEntityRenderer;
import com.iafenvoy.minedash.render.entity.GamePlayEntityRenderer;
import com.iafenvoy.minedash.render.item.DefaultBackgroundBlockItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class MDRenderers {
    @SubscribeEvent
    public static void registerDynamicItemRenderers(FMLClientSetupEvent event) {
        DynamicItemRenderer.RENDERERS.put(MDBlocks.DEFAULT_BACKGROUND.asItem(), new DefaultBackgroundBlockItemRenderer());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MDEntities.GAME_PLAY.get(), GamePlayEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(MDBlockEntities.DEFAULT_BACKGROUND.get(), ctx -> new DefaultBackgroundBlockEntityRenderer());
        event.registerBlockEntityRenderer(MDBlockEntities.SPIKE.get(), ctx -> new SpikeBlockEntityRenderer());
        event.registerBlockEntityRenderer(MDBlockEntities.RING.get(), RingBlockEntityRenderer::new);
    }
}
