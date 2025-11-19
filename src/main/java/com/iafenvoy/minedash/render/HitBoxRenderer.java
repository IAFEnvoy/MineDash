package com.iafenvoy.minedash.render;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.api.HitboxProvider;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.iafenvoy.minedash.registry.MDRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
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
public final class HitBoxRenderer {
    private static final Map<ChunkPos, List<BlockPos>> CHUNK_DATA = new LinkedHashMap<>();

    public static void onChunkData(Level level, int chunkX, int chunkZ) {
        LevelChunk chunk = level.getChunk(chunkX, chunkZ);
        List<BlockPos> posList = new LinkedList<>();
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = chunk.getMinBuildHeight(); y <= chunk.getMaxBuildHeight(); y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (chunk.getBlockState(pos).getBlock() instanceof HitboxProvider)
                        posList.add(pos.offset(chunkX * 16, 0, chunkZ * 16));
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
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES || !MDClientConfig.INSTANCE.general.showHitboxes.getValue())
            return;
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        PoseStack poseStack = event.getPoseStack();
        //TODO::Config
        int chunkRange = MDClientConfig.INSTANCE.general.hitboxDisplayRange.getValue();
        poseStack.pushPose();
        Vec3 cameraPos = event.getCamera().getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        //Block
        for (BlockPos pos : collectForRender(player.chunkPosition(), chunkRange)) {
            BlockState state = player.level().getBlockState(pos);
            if (state.getBlock() instanceof HitboxProvider provider) {
                poseStack.pushPose();
                poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
                renderHitBox(poseStack.last(), provider.getHitbox(state), provider.getHitboxType().getColor());
                poseStack.popPose();
            }
        }
        //Entity
        for (Entity entity : player.level().getEntities(player, new AABB(player.blockPosition()).inflate(chunkRange * 16, chunkRange * 16, chunkRange * 16)))
            if (entity instanceof HitboxProvider provider) {
                poseStack.pushPose();
                poseStack.translate(entity.getX() - 0.5, entity.getY(), entity.getZ() - 0.5);
                renderHitBox(poseStack.last(), provider.getHitbox(Blocks.AIR.defaultBlockState()), provider.getHitboxType().getColor());
                poseStack.popPose();
            }
        poseStack.popPose();
    }

    public static void renderHitBox(PoseStack.Pose pose, VoxelShape hitbox, int color) {
        double minX = hitbox.min(Direction.Axis.X), maxX = hitbox.max(Direction.Axis.X);
        double minY = hitbox.min(Direction.Axis.Y), maxY = hitbox.max(Direction.Axis.Y);
        double minZ = hitbox.min(Direction.Axis.Z), maxZ = hitbox.max(Direction.Axis.Z);
        MultiBufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();

        VertexConsumer consumer1 = source.getBuffer(MDRenderTypes.hitboxOutlineStrip());
        vertex(pose, consumer1, minX, minY, minZ, color);
        vertex(pose, consumer1, minX, maxY, minZ, color);
        vertex(pose, consumer1, maxX, maxY, minZ, color);
        vertex(pose, consumer1, maxX, minY, minZ, color);
        vertex(pose, consumer1, minX, minY, minZ, color);

        vertex(pose, consumer1, minX, minY, maxZ, color);
        vertex(pose, consumer1, minX, maxY, maxZ, color);
        vertex(pose, consumer1, maxX, maxY, maxZ, color);
        vertex(pose, consumer1, maxX, minY, maxZ, color);
        vertex(pose, consumer1, minX, minY, maxZ, color);

        VertexConsumer consumer2 = source.getBuffer(MDRenderTypes.hitboxOutline());
        vertex(pose, consumer2, minX, maxY, minZ, color);
        vertex(pose, consumer2, minX, maxY, maxZ, color);

        vertex(pose, consumer2, maxX, maxY, minZ, color);
        vertex(pose, consumer2, maxX, maxY, maxZ, color);

        vertex(pose, consumer2, maxX, minY, minZ, color);
        vertex(pose, consumer2, maxX, minY, maxZ, color);
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer consumer, double x, double y, double z, int color) {
        consumer.addVertex(pose, (float) x, (float) y, (float) z).setColor(color);
    }
}
