package com.iafenvoy.minedash.render.extra.block;

import com.iafenvoy.minedash.api.Spike;
import com.iafenvoy.minedash.item.block.AbstractSpikeBlock;
import com.iafenvoy.minedash.render.extra.ExtraBlockRenderer;
import com.iafenvoy.minedash.render.util.VertexCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.iafenvoy.minedash.render.util.VertexHelper.vertex;

@OnlyIn(Dist.CLIENT)
public class SpikeBlockRenderer implements ExtraBlockRenderer {
    private static final Function<Float, VertexCollector> VERTEXES = Util.memoize(height -> {
        VertexCollector collector = new VertexCollector();
        collector.add(0, 0, 0);
        collector.add(0.5f, height, 0.5f);
        collector.add(1, 0, 0);
        collector.add(0.5f, height, 0.5f);
        collector.add(1, 0, 1);
        collector.add(0.5f, height, 0.5f);
        collector.add(0, 0, 1);
        collector.add(0.5f, height, 0.5f);
        collector.add(0, 0, 0);
        collector.add(1, 0, 0);
        collector.add(1, 0, 1);
        collector.add(0, 0, 1);
        collector.add(0, 0, 0);
        return collector;
    });

    @Override
    public void render(BlockState state, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(state.getOptionalValue(AbstractSpikeBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        PoseStack.Pose pose = poseStack.last();
        VertexCollector collector = VERTEXES.apply(state.getBlock() instanceof Spike spike ? spike.getHeight() : 1f);
        //Quads
        VertexConsumer consumer1 = bufferSource.getBuffer(RenderType.debugFilledBox());
        collector.forEach((x, y, z) -> vertex(consumer1, pose, x, y, z, 0xFF000000));
        //Borders
        VertexConsumer consumer2 = bufferSource.getBuffer(RenderType.lineStrip());
        collector.forEach((x, y, z) -> vertex(consumer2, pose, x, y, z, 0xFFFFFFFF));
        poseStack.popPose();
    }
}
