package com.iafenvoy.minedash.render.extra.block;

import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.iafenvoy.minedash.render.extra.ExtraBlockRenderer;
import com.iafenvoy.minedash.render.util.VertexHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BlockHitboxRenderer implements ExtraBlockRenderer {
    @Override
    public void render(BlockState state, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource bufferSource) {
        if (MDClientConfig.INSTANCE.general.showHitboxes.getValue() && state.getBlock() instanceof HitboxProvider provider) {
            poseStack.pushPose();
            VertexHelper.renderBox(poseStack, bufferSource, provider.getHitbox(state), provider.getHitboxType().getColor());
            poseStack.popPose();
        }
    }
}
