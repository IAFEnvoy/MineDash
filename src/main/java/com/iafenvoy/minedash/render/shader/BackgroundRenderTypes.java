package com.iafenvoy.minedash.render.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class BackgroundRenderTypes {
    public static final Function<ResourceLocation, RenderType> DEFAULT_BACKGROUND = texture -> RenderType.create("default_background",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderType.ShaderStateShard(() -> ShaderInstances.DEFAULT_BACKGROUND))
                    .setTextureState(RenderStateShard.MultiTextureStateShard.builder()
                            .add(texture, false, false)
                            .build())
                    .createCompositeState(false));

    public static RenderType background(ResourceLocation texture) {
        return DEFAULT_BACKGROUND.apply(texture);
    }
}
