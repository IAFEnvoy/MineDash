package com.iafenvoy.minedash.render.extra;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public interface ExtraBlockRenderer {
    void render(BlockState state, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource);
}
