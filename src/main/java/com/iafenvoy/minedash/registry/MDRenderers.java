package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.item.block.AbstractPadBlock;
import com.iafenvoy.minedash.item.block.AbstractRingBlock;
import com.iafenvoy.minedash.item.block.AbstractSpikeBlock;
import com.iafenvoy.minedash.particle.CubeParticleProvider;
import com.iafenvoy.minedash.render.block.DefaultBackgroundBlockEntityRenderer;
import com.iafenvoy.minedash.render.entity.GamePlayEntityRenderer;
import com.iafenvoy.minedash.render.extra.*;
import com.iafenvoy.minedash.render.item.DefaultBackgroundBlockItemRenderer;
import com.iafenvoy.minedash.render.item.DynamicItemRenderer;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

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
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MDParticles.CUBE.get(), CubeParticleProvider::new);
    }

    @SubscribeEvent
    public static void registerExtraRenderer(FMLClientSetupEvent event) {
        for (DeferredHolder<Block, ? extends Block> holder : MDBlocks.REGISTRY.getEntries()) {
            Block block = holder.get();
            if (block instanceof AbstractPadBlock) ExtraRenderManager.register(block, new PadBlockRenderer());
            if (block instanceof AbstractRingBlock) ExtraRenderManager.register(block, new RingBlockRenderer());
            if (block instanceof AbstractSpikeBlock) ExtraRenderManager.register(block, new SpikeBlockRenderer());
            if (block instanceof HitboxProvider) ExtraRenderManager.register(block, new BlockHitboxRenderer());
        }
        ExtraRenderManager.register(MDEntities.GAME_PLAY.get(), new EntityHitboxRenderer());
    }
}
