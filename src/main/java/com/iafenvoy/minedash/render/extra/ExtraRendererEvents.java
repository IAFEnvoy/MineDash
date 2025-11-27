package com.iafenvoy.minedash.render.extra;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class ExtraRendererEvents {
    @SubscribeEvent
    public static void renderExtra(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES)
            ExtraRenderManager.renderBlockExtra(event.getPoseStack(), event.getCamera(), event.getPartialTick());
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES)
            ExtraRenderManager.renderEntityExtra(event.getPoseStack(), event.getCamera(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void onChunkData(ChunkEvent.Load event) {
        LevelCacheStorage.onChunkLoad(event.getChunk());
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        LevelCacheStorage.onChunkUnload(event.getChunk());
    }
}
