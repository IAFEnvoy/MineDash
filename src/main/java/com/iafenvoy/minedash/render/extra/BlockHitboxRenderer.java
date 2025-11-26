package com.iafenvoy.minedash.render.extra;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.iafenvoy.minedash.render.util.VertexHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockHitboxRenderer implements ExtraBlockRenderer {
    @Override
    public void render(BlockState state, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource) {
        if (MDClientConfig.INSTANCE.general.showHitboxes.getValue() && state.getBlock() instanceof HitboxProvider provider) {
            poseStack.pushPose();
            VertexHelper.renderBox(poseStack.last(), provider.getHitbox(state), provider.getHitboxType().getColor());
            poseStack.popPose();
        }
    }
}
