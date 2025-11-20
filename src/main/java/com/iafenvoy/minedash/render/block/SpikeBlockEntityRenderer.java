package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.api.Spike;
import com.iafenvoy.minedash.item.block.AbstractSpikeBlock;
import com.iafenvoy.minedash.item.block.entity.SpikeBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import static com.iafenvoy.minedash.render.VertexHelper.vertexBlack;
import static com.iafenvoy.minedash.render.VertexHelper.vertexWhite;

public class SpikeBlockEntityRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
    @Override
    public void render(@NotNull SpikeBlockEntity blockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(blockEntity.getBlockState().getOptionalValue(AbstractSpikeBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        float height = blockEntity.getBlockState().getBlock() instanceof Spike spike ? spike.getHeight() : 1;
        vertexQuads(poseStack, multiBufferSource, height);
        vertexBorders(poseStack, multiBufferSource, height);
        poseStack.popPose();
    }

    private static void vertexQuads(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, float height) {
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.debugFilledBox());
        PoseStack.Pose pose = poseStack.last();
        vertexBlack(consumer, pose, 0, 0, 0);
        vertexBlack(consumer, pose, 0.5f, height, 0.5f);
        vertexBlack(consumer, pose, 1, 0, 0);
        vertexBlack(consumer, pose, 0.5f, height, 0.5f);
        vertexBlack(consumer, pose, 1, 0, 1);
        vertexBlack(consumer, pose, 0.5f, height, 0.5f);
        vertexBlack(consumer, pose, 0, 0, 1);
        vertexBlack(consumer, pose, 0.5f, height, 0.5f);
        vertexBlack(consumer, pose, 0, 0, 0);
        vertexBlack(consumer, pose, 1, 0, 0);
        vertexBlack(consumer, pose, 1, 0, 1);
        vertexBlack(consumer, pose, 0, 0, 1);
        vertexBlack(consumer, pose, 0, 0, 0);
    }

    private static void vertexBorders(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, float height) {
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose = poseStack.last();
        vertexWhite(consumer, pose, 0, 0, 0);
        vertexWhite(consumer, pose, 0.5f, height, 0.5f);
        vertexWhite(consumer, pose, 1, 0, 0);
        vertexWhite(consumer, pose, 0.5f, height, 0.5f);
        vertexWhite(consumer, pose, 1, 0, 1);
        vertexWhite(consumer, pose, 0.5f, height, 0.5f);
        vertexWhite(consumer, pose, 0, 0, 1);
        vertexWhite(consumer, pose, 0.5f, height, 0.5f);
        vertexWhite(consumer, pose, 0, 0, 0);
        vertexWhite(consumer, pose, 1, 0, 0);
        vertexWhite(consumer, pose, 1, 0, 1);
        vertexWhite(consumer, pose, 0, 0, 1);
        vertexWhite(consumer, pose, 0, 0, 0);
    }
}
