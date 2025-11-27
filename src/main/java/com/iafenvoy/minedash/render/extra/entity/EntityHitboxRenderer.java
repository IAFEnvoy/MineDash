package com.iafenvoy.minedash.render.extra.entity;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.iafenvoy.minedash.render.extra.ExtraEntityRenderer;
import com.iafenvoy.minedash.render.util.VertexHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EntityHitboxRenderer implements ExtraEntityRenderer {
    @Override
    public void render(Entity entity, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource) {
        if (MDClientConfig.INSTANCE.general.showHitboxes.getValue() && entity instanceof HitboxProvider provider) {
            poseStack.pushPose();
            Vec3 pos = entity.getPosition(partialTicks);
            poseStack.translate(pos.x - 0.5, pos.y, pos.z - 0.5);
            VertexHelper.renderBox(poseStack, bufferSource, provider.getHitbox(Blocks.AIR.defaultBlockState()), provider.getHitboxType().getColor());
            poseStack.popPose();
        }
    }
}
