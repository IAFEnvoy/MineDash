package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.item.block.entity.DefaultBackgroundBlockEntity;
import com.iafenvoy.minedash.render.ThemeColorManager;
import com.iafenvoy.minedash.render.shader.BackgroundRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class DefaultBackgroundBlockEntityRenderer implements BlockEntityRenderer<DefaultBackgroundBlockEntity> {
    @Override
    public void render(@NotNull DefaultBackgroundBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int light, int overlay) {
        ResourceLocation texture = blockEntity.getTexture();
        if (texture == null) return;
        poseStack.pushPose();
        this.renderCube(poseStack.last().pose(), multiBufferSource.getBuffer(BackgroundRenderTypes.background(texture)));
        poseStack.popPose();
    }

    private void renderCube(Matrix4f matrix4f, VertexConsumer consumer) {
        this.renderFace(matrix4f, consumer, 0, 1, 0, 1, 1, 1, 1, 1);
        this.renderFace(matrix4f, consumer, 0, 1, 1, 0, 0, 0, 0, 0);
        this.renderFace(matrix4f, consumer, 1, 1, 1, 0, 0, 1, 1, 0);
        this.renderFace(matrix4f, consumer, 0, 0, 0, 1, 0, 1, 1, 0);
        this.renderFace(matrix4f, consumer, 0, 1, 0, 0, 0, 0, 1, 1);
        this.renderFace(matrix4f, consumer, 0, 1, 1, 1, 1, 1, 0, 0);
    }

    private void renderFace(Matrix4f matrix4f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4) {
        ThemeColorManager color = ThemeColorManager.INSTANCE;
        float r = color.getR();
        float g = color.getG();
        float b = color.getB();
        consumer.addVertex(matrix4f, x1, y1, z1).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x2, y1, z2).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x2, y2, z3).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x1, y2, z4).setColor(r, g, b, 1.0F);
    }
}
