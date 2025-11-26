package com.iafenvoy.minedash.render;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.data.TrailData;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class TrailRenderer {
    private static final ResourceLocation TRAIL_TEXTURE = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/entity/concentrated_trail.png");

    public static void render(TrailData effect, GamePlayEntity entity, MultiBufferSource provider, PoseStack matrices) {
        int trailColor = entity.getSecondaryColor() | 0x7F000000;
        effect.tick(entity.getBoundingBox().getCenter(), entity.hasTrail());
        for (List<TrailData.TrailPoint> l : effect.getXTrails()) renderTrail(l, provider, matrices, trailColor);
        for (List<TrailData.TrailPoint> l : effect.getYTrails()) renderTrail(l, provider, matrices, trailColor);
        for (List<TrailData.TrailPoint> l : effect.getZTrails()) renderTrail(l, provider, matrices, trailColor);
    }

    private static void renderTrail(List<TrailData.TrailPoint> points, MultiBufferSource provider, PoseStack matrices, int color) {
        VertexConsumer consumer = provider.getBuffer(RenderType.entityTranslucent(TRAIL_TEXTURE));
        for (int i = 0; i < points.size() - 1; i++) {
            TrailData.TrailPoint from = points.get(i), to = points.get(i + 1);
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
