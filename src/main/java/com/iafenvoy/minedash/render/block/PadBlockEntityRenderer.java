package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.item.block.AbstractPadBlock;
import com.iafenvoy.minedash.item.block.entity.PadBlockEntity;
import com.iafenvoy.minedash.render.VertexCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

import static com.iafenvoy.minedash.render.VertexHelper.vertex;
import static com.iafenvoy.minedash.render.VertexHelper.vertexWhite;

public class PadBlockEntityRenderer implements BlockEntityRenderer<PadBlockEntity> {
    private static final VertexCollector VERTEXES = new VertexCollector();

    @Override
    public void render(@NotNull PadBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int light, int overlay) {
        if (!(blockEntity.getBlockState().getBlock() instanceof AbstractPadBlock pad)) return;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(blockEntity.getBlockState().getOptionalValue(AbstractPadBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        PoseStack.Pose pose = poseStack.last();
        //Quads
        VertexConsumer consumer1 = source.getBuffer(RenderType.debugQuads());
        int color = pad.getColor() | 0xFF000000;
        VERTEXES.forEach((x, y, z) -> vertex(consumer1, pose, x, y, z, color));
        //Borders
        VertexConsumer consumer2 = source.getBuffer(RenderType.lineStrip());
        vertexWhite(consumer2, pose, 0, 0, 0);
        vertexWhite(consumer2, pose, 1, 0, 0);
        vertexWhite(consumer2, pose, 1, 0, 1);
        vertexWhite(consumer2, pose, 0, 0, 1);
        vertexWhite(consumer2, pose, 0, 0, 0);
        poseStack.popPose();
    }

    static {
        float height = 0.125f, margin = 0.25f;

        VERTEXES.add(margin, height, margin);
        VERTEXES.add(1 - margin, height, margin);
        VERTEXES.add(1 - margin, height, 1 - margin);
        VERTEXES.add(margin, height, 1 - margin);

        VERTEXES.add(0, 0, 0);
        VERTEXES.add(1, 0, 0);
        VERTEXES.add(1, 0, 1);
        VERTEXES.add(0, 0, 1);

        VERTEXES.add(margin, height, margin);
        VERTEXES.add(1 - margin, height, margin);
        VERTEXES.add(1, 0, 0);
        VERTEXES.add(0, 0, 0);

        VERTEXES.add(1 - margin, height, margin);
        VERTEXES.add(1 - margin, height, 1 - margin);
        VERTEXES.add(1, 0, 1);
        VERTEXES.add(1, 0, 0);

        VERTEXES.add(1 - margin, height, 1 - margin);
        VERTEXES.add(margin, height, 1 - margin);
        VERTEXES.add(0, 0, 1);
        VERTEXES.add(1, 0, 1);

        VERTEXES.add(margin, height, 1 - margin);
        VERTEXES.add(margin, height, margin);
        VERTEXES.add(0, 0, 0);
        VERTEXES.add(0, 0, 1);
    }
}
