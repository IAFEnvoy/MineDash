package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.block.entity.DefaultBackgroundBlockEntity;
import com.iafenvoy.minedash.render.DynamicItemRenderer;
import com.iafenvoy.minedash.render.shader.BackgroundRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class DefaultBackgroundBlockEntityRenderer implements BlockEntityRenderer<DefaultBackgroundBlockEntity>, DynamicItemRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/background/game_bg_01_001.png");

    @Override
    public void render(@NotNull DefaultBackgroundBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();
        this.renderCube(poseStack.last().pose(), multiBufferSource.getBuffer(this.renderType()));
        poseStack.popPose();
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        this.renderCube(poseStack.last().pose(), bufferSource.getBuffer(this.renderType()));
        poseStack.popPose();
    }

    private void renderCube(Matrix4f matrix4f, VertexConsumer consumer) {
        this.renderFace(matrix4f, consumer, 0, 1, 0, 1, 1, 1, 1, 1, Direction.SOUTH);
        this.renderFace(matrix4f, consumer, 0, 1, 1, 0, 0, 0, 0, 0, Direction.NORTH);
        this.renderFace(matrix4f, consumer, 1, 1, 1, 0, 0, 1, 1, 0, Direction.EAST);
        this.renderFace(matrix4f, consumer, 0, 0, 0, 1, 0, 1, 1, 0, Direction.WEST);
        this.renderFace(matrix4f, consumer, 0, 1, 0, 0, 0, 0, 1, 1, Direction.DOWN);
        this.renderFace(matrix4f, consumer, 0, 1, 1, 1, 1, 1, 0, 0, Direction.UP);
    }

    private void renderFace(Matrix4f matrix4f, VertexConsumer consumer, float x1, float x2, float y1, float y2, float z1, float z2, float z3, float z4, Direction direction) {
        float r = 0;
        float g = 0;
        float b = 1;
        consumer.addVertex(matrix4f, x1, y1, z1).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x2, y1, z2).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x2, y2, z3).setColor(r, g, b, 1.0F);
        consumer.addVertex(matrix4f, x1, y2, z4).setColor(r, g, b, 1.0F);
    }

    protected RenderType renderType() {
        return BackgroundRenderTypes.background(TEXTURE);
    }
}
