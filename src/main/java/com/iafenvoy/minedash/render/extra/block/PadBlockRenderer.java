package com.iafenvoy.minedash.render.extra.block;

import com.iafenvoy.minedash.item.block.AbstractPadBlock;
import com.iafenvoy.minedash.render.extra.ExtraBlockRenderer;
import com.iafenvoy.minedash.render.util.VertexCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.iafenvoy.minedash.render.util.VertexHelper.vertex;

@OnlyIn(Dist.CLIENT)
public class PadBlockRenderer implements ExtraBlockRenderer {
    private static final VertexCollector VERTEXES = new VertexCollector();

    @Override
    public void render(BlockState state, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource) {
        if (!(state.getBlock() instanceof AbstractPadBlock pad)) return;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(state.getOptionalValue(AbstractPadBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        PoseStack.Pose pose = poseStack.last();
        //Quads
        VertexConsumer consumer1 = bufferSource.getBuffer(RenderType.debugQuads());
        int color = pad.getColor() | 0xFF000000;
        VERTEXES.forEach((x, y, z) -> vertex(consumer1, pose, x, y, z, color));
        //Borders
        VertexConsumer consumer2 = bufferSource.getBuffer(RenderType.lineStrip());
        vertex(consumer2, pose, (float) 0, (float) 0, (float) 0, 0xFFFFFFFF);
        vertex(consumer2, pose, (float) 1, (float) 0, (float) 0, 0xFFFFFFFF);
        vertex(consumer2, pose, (float) 1, (float) 0, (float) 1, 0xFFFFFFFF);
        vertex(consumer2, pose, (float) 0, (float) 0, (float) 1, 0xFFFFFFFF);
        vertex(consumer2, pose, (float) 0, (float) 0, (float) 0, 0xFFFFFFFF);
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
