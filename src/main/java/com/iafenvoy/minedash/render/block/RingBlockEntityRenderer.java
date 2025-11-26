package com.iafenvoy.minedash.render.block;

import com.iafenvoy.minedash.item.block.entity.RingBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class RingBlockEntityRenderer implements BlockEntityRenderer<RingBlockEntity> {
    private final ItemRenderer itemRenderer;

    public RingBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(@NotNull RingBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource source, int light, int overlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        Quaternionf quaternionf = new Quaternionf();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        SingleQuadParticle.FacingCameraMode.LOOKAT_XYZ.setRotation(quaternionf, camera, partialTicks);
        poseStack.mulPose(quaternionf);
        this.itemRenderer.renderStatic(new ItemStack(blockEntity.getBlockState().getBlock()), ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, overlay, poseStack, source, null, 0);
        poseStack.popPose();
    }
}
