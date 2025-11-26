package com.iafenvoy.minedash.render.item;

import com.iafenvoy.minedash.registry.MDDataComponents;
import com.iafenvoy.minedash.render.model.LayerModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultBackgroundBlockItemRenderer implements DynamicItemRenderer {
    private final LayerModel model = new LayerModel(LayerModel.createBodyLayer().bakeRoot());

    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        ResourceLocation texture = stack.get(MDDataComponents.TEXTURE);
        if (texture == null) return;
        poseStack.pushPose();
        poseStack.translate(1, 0, 1);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutout(texture)), light, overlay);
        poseStack.popPose();
    }
}
