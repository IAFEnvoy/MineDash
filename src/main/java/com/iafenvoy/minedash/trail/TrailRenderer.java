package com.iafenvoy.minedash.trail;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.registry.MDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class TrailRenderer {
    private static final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/entity/concentrated_trail.png");

    public static void render(TrailHolder effect, GamePlayEntity entity, MultiBufferSource provider, PoseStack matrices) {
        Vec3 pos = entity.position();
        int trailColor = entity.getSecondaryColor() | 0xFF000000;
        effect.tick(pos.add(0, entity.getBbHeight() / 2, 0), pos.subtract(new Vec3(entity.xo, entity.yo, entity.zo)));
        renderTrail(effect.getVerticalPoints(), provider, matrices, trailColor);
        renderTrail(effect.getHorizontalPoints(), provider, matrices, trailColor);
    }

    private static void renderTrail(List<TrailHolder.TrailPoint> points, MultiBufferSource provider, PoseStack matrices, int color) {
        VertexConsumer consumer = provider.getBuffer(MDRenderTypes.translucentNoDepth(TRAIL_TEXTURE));
        if (points.size() >= 2)
            for (int i = 0; i < points.size() - 1; i++) {
                TrailHolder.TrailPoint from = points.get(i), to = points.get(i + 1);
                PoseStack.Pose pose = matrices.last();
                int endColor = i == 0 ? color & 0x00FFFFFF : color;
                vertex(consumer, pose, from.upper(), endColor, 0, 0);
                vertex(consumer, pose, to.upper(), color, 1, 0);
                vertex(consumer, pose, to.lower(), color, 1, 1);
                vertex(consumer, pose, from.lower(), endColor, 0, 1);
            }
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Vec3 pos, int color, int u, int v) {
        consumer.addVertex(pose, (float) pos.x, (float) pos.y, (float) pos.z).setColor(color).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(LightTexture.FULL_BRIGHT).setNormal(pose, 0, 1, 0);
    }
}
