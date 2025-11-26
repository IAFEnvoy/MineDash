package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.api.Spike;
import com.iafenvoy.minedash.item.block.AbstractSpikeBlock;
import com.iafenvoy.minedash.item.block.entity.SpikeBlockEntity;
import com.iafenvoy.minedash.render.VertexCollector;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static com.iafenvoy.minedash.render.VertexHelper.vertexBlack;
import static com.iafenvoy.minedash.render.VertexHelper.vertexWhite;

@OnlyIn(Dist.CLIENT)
public class SpikeBlockEntityRenderer implements BlockEntityRenderer<SpikeBlockEntity> {
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
    public void render(@NotNull SpikeBlockEntity blockEntity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, int i1) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(blockEntity.getBlockState().getOptionalValue(AbstractSpikeBlock.FACING).orElse(Direction.UP).getRotation());
        poseStack.translate(-0.5, -0.5, -0.5);
        PoseStack.Pose pose = poseStack.last();
        VertexCollector collector = VERTEXES.apply(blockEntity.getBlockState().getBlock() instanceof Spike spike ? spike.getHeight() : 1f);
        //Quads
        VertexConsumer consumer1 = multiBufferSource.getBuffer(RenderType.debugFilledBox());
        collector.forEach((x, y, z) -> vertexBlack(consumer1, pose, x, y, z));
        //Borders
        VertexConsumer consumer2 = multiBufferSource.getBuffer(RenderType.lineStrip());
        collector.forEach((x, y, z) -> vertexWhite(consumer2, pose, x, y, z));
        poseStack.popPose();
    }
}
