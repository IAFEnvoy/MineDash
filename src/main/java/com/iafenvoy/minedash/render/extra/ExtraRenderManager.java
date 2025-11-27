package com.iafenvoy.minedash.render.extra;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public final class ExtraRenderManager {
    private static final int RANGE = 64;
    private static final Multimap<Block, ExtraBlockRenderer> BLOCK_RENDERERS = HashMultimap.create();
    private static final Multimap<EntityType<?>, ExtraEntityRenderer> ENTITY_RENDERERS = HashMultimap.create();

    public static void register(Block block, ExtraBlockRenderer renderer) {
        BLOCK_RENDERERS.put(block, renderer);
    }

    public static void register(EntityType<?> entityType, ExtraEntityRenderer renderer) {
        ENTITY_RENDERERS.put(entityType, renderer);
    }

    public static void renderSingleBlock(BlockState state, PoseStack poseStack) {
        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        for (ExtraBlockRenderer renderer : BLOCK_RENDERERS.get(state.getBlock()))
            renderer.render(state, partialTicks, poseStack, bufferSource);
    }

    static Set<Block> getRequiredBlock() {
        return BLOCK_RENDERERS.keySet();
    }

    public static void renderBlockExtra(PoseStack poseStack, Camera camera, DeltaTracker deltaTracker) {
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        poseStack.pushPose();
        prepareCamera(poseStack, camera);
        float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(true);
        for (BlockPos pos : LevelCacheStorage.collectForRender(player.chunkPosition(), MDClientConfig.INSTANCE.general.hitboxDisplayRange.getValue())) {
            BlockState state = player.level().getBlockState(pos);
            Collection<ExtraBlockRenderer> renderers = BLOCK_RENDERERS.get(state.getBlock());
            if (renderers.isEmpty()) continue;
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            for (ExtraBlockRenderer renderer : renderers) renderer.render(state, partialTicks, poseStack, bufferSource);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    public static void renderEntityExtra(PoseStack poseStack, Camera camera, DeltaTracker deltaTracker) {
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        poseStack.pushPose();
        prepareCamera(poseStack, camera);
        for (Entity entity : player.level().getEntities(player, new AABB(player.blockPosition()).inflate(RANGE, RANGE, RANGE))) {
            float partialTick = wrapPartialTick(deltaTracker, entity);
            for (ExtraEntityRenderer renderer : ENTITY_RENDERERS.get(entity.getType()))
                renderer.render(entity, partialTick, poseStack, bufferSource);
        }
        poseStack.popPose();
    }

    private static void prepareCamera(PoseStack poseStack, Camera camera) {
        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    }

    private static float wrapPartialTick(DeltaTracker tracker, Entity entity) {
        TickRateManager tickratemanager = entity.level().tickRateManager();
        return tracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
    }
}
