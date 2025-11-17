package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.item.block.SpikeBlock;
import com.iafenvoy.minedash.item.block.entity.SpikeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class SpikeBlockEntityRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    @Override
    public void render(@NotNull SpikeBlockEntity blockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(blockEntity.getBlockState().getOptionalValue(SpikeBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        vertexQuads(poseStack, multiBufferSource);
        vertexBorders(poseStack, multiBufferSource);
        poseStack.popPose();
    }

    private static void vertexQuads(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource) {
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.debugFilledBox());
        PoseStack.Pose pose = poseStack.last();
        vertexBlack(consumer, pose, 0, 0, 0);
        vertexBlack(consumer, pose, 0.5f, 1, 0.5f);
        vertexBlack(consumer, pose, 1, 0, 0);
        vertexBlack(consumer, pose, 0.5f, 1, 0.5f);
        vertexBlack(consumer, pose, 1, 0, 1);
        vertexBlack(consumer, pose, 0.5f, 1, 0.5f);
        vertexBlack(consumer, pose, 0, 0, 1);
        vertexBlack(consumer, pose, 0.5f, 1, 0.5f);
        vertexBlack(consumer, pose, 0, 0, 0);
        vertexBlack(consumer, pose, 1, 0, 0);
        vertexBlack(consumer, pose, 1, 0, 1);
        vertexBlack(consumer, pose, 0, 0, 1);
        vertexBlack(consumer, pose, 0, 0, 0);
    }

    private static void vertexBorders(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource) {
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();
        vertexWhite(consumer, pose, 0, 0, 0);
        vertexWhite(consumer, pose, 0.5f, 1, 0.5f);
        vertexWhite(consumer, pose, 1, 0, 0);
        vertexWhite(consumer, pose, 0.5f, 1, 0.5f);
        vertexWhite(consumer, pose, 1, 0, 1);
        vertexWhite(consumer, pose, 0.5f, 1, 0.5f);
        vertexWhite(consumer, pose, 0, 0, 1);
        vertexWhite(consumer, pose, 0.5f, 1, 0.5f);
        vertexWhite(consumer, pose, 0, 0, 0);
        vertexWhite(consumer, pose, 1, 0, 0);
        vertexWhite(consumer, pose, 1, 0, 1);
        vertexWhite(consumer, pose, 0, 0, 1);
        vertexWhite(consumer, pose, 0, 0, 0);
    }

    private static void vertexBlack(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        consumer.addVertex(pose, x, y, z).setColor(0xFF000000);
    }

    private static void vertexWhite(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        consumer.addVertex(pose, x, y, z).setColor(0xFFFFFFFF).setNormal(pose, x / 16, y / 16, z / 16);
    }
}
