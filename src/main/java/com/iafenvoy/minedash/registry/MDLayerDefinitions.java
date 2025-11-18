package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.render.model.GamePlayModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class MDLayerDefinitions {
    public static final ModelLayerLocation GAME_PLAY = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "game_play"), "main");

    @SubscribeEvent
    public static void registerModelContext(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GAME_PLAY, GamePlayModel::createBodyLayer);
    }
}
