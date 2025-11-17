package com.iafenvoy.minedash.render;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.registry.MDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class HitBoxRenderManager {
    private static final Map<ChunkPos, List<BlockPos>> CHUNK_DATA = new LinkedHashMap<>();

    public static void onChunkData(Level level, int chunkX, int chunkZ) {
        LevelChunk chunk = level.getChunk(chunkX, chunkZ);
        List<BlockPos> posList = new LinkedList<>();
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = chunk.getMinBuildHeight(); y <= chunk.getMaxBuildHeight(); y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(pos).getBlock() instanceof HitboxProvider) posList.add(pos);
                }
        CHUNK_DATA.put(new ChunkPos(chunkX, chunkZ), posList);
    }

    public static void onChunkUnload(ChunkPos pos) {
        CHUNK_DATA.remove(pos);
    }

    public static void onBlockUpdate(BlockPos pos, BlockState newState) {
        List<BlockPos> posList = CHUNK_DATA.get(new ChunkPos(pos));
        if (posList == null) return;
        posList.remove(pos);
        if (newState.getBlock() instanceof HitboxProvider) posList.add(pos);
    }

    public static List<BlockPos> collectForRender(ChunkPos pos, int chunkRange) {
        ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
        for (Map.Entry<ChunkPos, List<BlockPos>> entry : CHUNK_DATA.entrySet())
            if (entry.getKey().getChessboardDistance(pos) <= chunkRange)
                builder.addAll(entry.getValue());
        return builder.build();
    }

    @SubscribeEvent
    public static void renderHitBoxes(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        PoseStack poseStack = event.getPoseStack();
        //TODO::Config
        poseStack.pushPose();
        Vec3 cameraPos = event.getCamera().getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        for (BlockPos pos : collectForRender(player.chunkPosition(), 5)) {
            BlockState state = player.level().getBlockState(pos);
            if (state.getBlock() instanceof HitboxProvider provider)
                renderCube(poseStack.last(), pos, provider.getHitbox(state), provider.getHitboxType().getColor());
        }
        poseStack.popPose();
    }

    private static void renderCube(PoseStack.Pose pose, BlockPos pos, VoxelShape hitbox, int color) {
        double minX = pos.getX() + hitbox.min(Direction.Axis.X), maxX = pos.getX() + hitbox.max(Direction.Axis.X);
        double minY = pos.getY() + hitbox.min(Direction.Axis.Y), maxY = pos.getY() + hitbox.max(Direction.Axis.Y);
        double minZ = pos.getZ() + hitbox.min(Direction.Axis.Z), maxZ = pos.getZ() + hitbox.max(Direction.Axis.Z);
        MultiBufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer consumer = source.getBuffer(MDRenderTypes.hitboxOutlineStrip());
        vertex(pose, consumer, minX, minY, minZ, color);
        vertex(pose, consumer, minX, maxY, minZ, color);
        vertex(pose, consumer, maxX, maxY, minZ, color);
        vertex(pose, consumer, maxX, minY, minZ, color);
        vertex(pose, consumer, minX, minY, minZ, color);

        vertex(pose, consumer, minX, minY, maxZ, color);
        vertex(pose, consumer, minX, maxY, maxZ, color);
        vertex(pose, consumer, maxX, maxY, maxZ, color);
        vertex(pose, consumer, maxX, minY, maxZ, color);
        vertex(pose, consumer, minX, minY, maxZ, color);

        consumer = source.getBuffer(MDRenderTypes.hitboxOutline());
        vertex(pose, consumer, minX, maxY, minZ, color);
        vertex(pose, consumer, minX, maxY, maxZ, color);

        vertex(pose, consumer, maxX, maxY, minZ, color);
        vertex(pose, consumer, maxX, maxY, maxZ, color);

        vertex(pose, consumer, maxX, minY, minZ, color);
        vertex(pose, consumer, maxX, minY, maxZ, color);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, int color) {
        consumer.addVertex(pose, (float) x, (float) y, (float) z).setColor(color);
    }
}
