package com.iafenvoy.minedash.render.extra;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.iafenvoy.minedash.config.MDClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
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

    static Set<Block> getRequiredBlock() {
        return BLOCK_RENDERERS.keySet();
    }

    @ApiStatus.Internal
    @SubscribeEvent
    public static void renderHitBoxes(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;
        //Prepare base fields
        LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        MultiBufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        //Prepare Camera
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        Vec3 cameraPos = event.getCamera().getPosition();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        //Block
        float partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(true);
        for (BlockPos pos : LevelCacheStorage.collectForRender(player.chunkPosition(), MDClientConfig.INSTANCE.general.hitboxDisplayRange.getValue())) {
            BlockState state = player.level().getBlockState(pos);
            Collection<ExtraBlockRenderer> renderers = BLOCK_RENDERERS.get(state.getBlock());
            if (renderers.isEmpty()) continue;
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            for (ExtraBlockRenderer renderer : renderers) renderer.render(state, partialTicks, poseStack, bufferSource);
            poseStack.popPose();
        }
        //Entity
        for (Entity entity : player.level().getEntities(player, new AABB(player.blockPosition()).inflate(RANGE, RANGE, RANGE))) {
            float partialTick = wrapPartialTick(event.getPartialTick(), entity);
            for (ExtraEntityRenderer renderer : ENTITY_RENDERERS.get(entity.getType()))
                renderer.render(entity, partialTick, poseStack, bufferSource);
        }
        //End
        poseStack.popPose();
    }

    private static float wrapPartialTick(DeltaTracker tracker, Entity entity) {
        TickRateManager tickratemanager = entity.level().tickRateManager();
        return tracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
    }
}
