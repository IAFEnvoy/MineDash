package com.iafenvoy.minedash.render.entity;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.registry.MDLayerDefinitions;
import com.iafenvoy.minedash.render.model.GamePlayModel;
import com.iafenvoy.minedash.trail.TrailHolder;
import com.iafenvoy.minedash.trail.TrailRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class GamePlayEntityRenderer extends LivingEntityRenderer<GamePlayEntity, GamePlayModel<GamePlayEntity>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/entity/default_game_play.png");

    public GamePlayEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new GamePlayModel<>(context.getModelSet().bakeLayer(MDLayerDefinitions.GAME_PLAY)), 0.5f);
    }

    @Override
    public void render(@NotNull GamePlayEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.pushPose();
        Vec3 pos = entity.getPosition(partialTicks);
        poseStack.translate(-pos.x, -pos.y, -pos.z);
        TrailHolder holder = entity.getTrail();
        TrailRenderer.render(holder, entity, buffer, poseStack);
        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GamePlayEntity entity) {
        return TEXTURE;
    }

    @Override
    protected boolean shouldShowName(@NotNull GamePlayEntity entity) {
        return false;
    }
}
