package com.iafenvoy.minedash.render.entity;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.registry.MDLayerDefinitions;
import com.iafenvoy.minedash.render.HitBoxRenderer;
import com.iafenvoy.minedash.render.model.GamePlayModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
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
    public void render(@NotNull GamePlayEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        HitBoxRenderer.queueEntityRender(entity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GamePlayEntity entity) {
        return TEXTURE;
    }
}
