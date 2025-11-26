package com.iafenvoy.minedash.render.extra;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface ExtraEntityRenderer {
    void render(Entity entity, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource);
}
