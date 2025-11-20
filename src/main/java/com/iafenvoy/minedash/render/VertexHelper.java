package com.iafenvoy.minedash.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public final class VertexHelper {
    public static void vertexBlack(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        consumer.addVertex(pose, x, y, z).setColor(0xFF000000).setNormal(pose, x / 16, y / 16, z / 16);
    }

    public static void vertexWhite(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z) {
        consumer.addVertex(pose, x, y, z).setColor(0xFFFFFFFF).setNormal(pose, x / 16, y / 16, z / 16);
    }
}
