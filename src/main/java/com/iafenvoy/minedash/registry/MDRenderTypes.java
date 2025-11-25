package com.iafenvoy.minedash.registry;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.OptionalDouble;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class MDRenderTypes {
    private static final Function<ResourceLocation, RenderType> DEFAULT_BACKGROUND = Util.memoize(texture -> RenderType.create("default_background",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderType.ShaderStateShard(() -> MDShaderInstances.DEFAULT_BACKGROUND))
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(texture, false, false)
                            .build())
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false)));
    private static final RenderType HITBOX_OUTLINE = RenderType.create("hitbox_outline",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.LINES,
            RenderType.TRANSIENT_BUFFER_SIZE,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
                    .createCompositeState(false));
    private static final RenderType HITBOX_OUTLINE_STRIP = RenderType.create("hitbox_outline_strip",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.LINE_STRIP,
            RenderType.TRANSIENT_BUFFER_SIZE,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setCullState(RenderStateShard.NO_CULL)
                    .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
                    .createCompositeState(false));

    public static RenderType background(ResourceLocation texture) {
        return DEFAULT_BACKGROUND.apply(texture);
    }

    public static RenderType hitboxOutline() {
        return HITBOX_OUTLINE;
    }

    public static RenderType hitboxOutlineStrip() {
        return HITBOX_OUTLINE_STRIP;
    }
}
