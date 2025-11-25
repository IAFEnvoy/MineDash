package com.iafenvoy.minedash.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class VertexHelper {
    public static void vertexBlack(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        vertex(consumer, pose, x, y, z, 0xFF000000);
    }

    public static void vertexWhite(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        vertex(consumer, pose, x, y, z, 0xFFFFFFFF);
    }

    public static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int color) {
        consumer.addVertex(pose, x, y, z).setColor(color).setNormal(pose, x / 16, y / 16, z / 16);
    }
}
