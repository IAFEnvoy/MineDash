package com.iafenvoy.minedash.render.entity;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.registry.MDLayerDefinitions;
import com.iafenvoy.minedash.render.model.GamePlayModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GamePlayEntityRenderer extends MobRenderer<GamePlayEntity, GamePlayModel<GamePlayEntity>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/entity/default_game_play.png");

    public GamePlayEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GamePlayModel<>(context.getModelSet().bakeLayer(MDLayerDefinitions.GAME_PLAY)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GamePlayEntity entity) {
        return TEXTURE;
    }
}
