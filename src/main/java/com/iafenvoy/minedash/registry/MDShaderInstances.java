package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class MDShaderInstances {
    @Nullable
    public static ShaderInstance DEFAULT_BACKGROUND;

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "default_background"), DefaultVertexFormat.POSITION_COLOR), program -> DEFAULT_BACKGROUND = program);
        } catch (Exception e) {
            MineDash.LOGGER.error("Failed to load MineDash shader", e);
        }
    }
}
