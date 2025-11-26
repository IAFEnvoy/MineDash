package com.iafenvoy.minedash.render.util;

import com.iafenvoy.minedash.registry.MDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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

    public static void renderBox(PoseStack.Pose pose, VoxelShape box, int color) {
        float minX = (float) box.min(Direction.Axis.X), maxX = (float) box.max(Direction.Axis.X);
        float minY = (float) box.min(Direction.Axis.Y), maxY = (float) box.max(Direction.Axis.Y);
        float minZ = (float) box.min(Direction.Axis.Z), maxZ = (float) box.max(Direction.Axis.Z);
        MultiBufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer consumer1 = source.getBuffer(MDRenderTypes.hitboxOutlineStrip());
        vertex(consumer1, pose, minX, minY, minZ, color);
        vertex(consumer1, pose, minX, maxY, minZ, color);
        vertex(consumer1, pose, maxX, maxY, minZ, color);
        vertex(consumer1, pose, maxX, minY, minZ, color);
        vertex(consumer1, pose, minX, minY, minZ, color);

        vertex(consumer1, pose, minX, minY, maxZ, color);
        vertex(consumer1, pose, minX, maxY, maxZ, color);
        vertex(consumer1, pose, maxX, maxY, maxZ, color);
        vertex(consumer1, pose, maxX, minY, maxZ, color);
        vertex(consumer1, pose, minX, minY, maxZ, color);

        VertexConsumer consumer2 = source.getBuffer(MDRenderTypes.hitboxOutline());
        vertex(consumer2, pose, minX, maxY, minZ, color);
        vertex(consumer2, pose, minX, maxY, maxZ, color);

        vertex(consumer2, pose, maxX, maxY, minZ, color);
        vertex(consumer2, pose, maxX, maxY, maxZ, color);

        vertex(consumer2, pose, maxX, minY, minZ, color);
        vertex(consumer2, pose, maxX, minY, maxZ, color);
    }
}
