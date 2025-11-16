package com.iafenvoy.minedash.render.connected;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class SpriteShiftEntry {
    protected StitchedSprite original;
    protected StitchedSprite target;

    public void set(ResourceLocation originalTextureLocation, ResourceLocation targetTextureLocation) {
        this.original = new StitchedSprite(originalTextureLocation);
        this.target = new StitchedSprite(targetTextureLocation);
    }

    public TextureAtlasSprite getOriginal() {
        return this.original.get();
    }

    public TextureAtlasSprite getTarget() {
        return this.target.get();
    }

    /**
     * &#064;removed  <code>* 16.0F</code>
     *
     * @see TextureAtlasSprite#getU(float)
     */
    public static float getUnInterpolatedU(TextureAtlasSprite sprite, float u) {
        float f = sprite.getU1() - sprite.getU0();
        return (u - sprite.getU0()) / f;
    }

    /**
     * &#064;removed  <code>* 16.0F</code>
     *
     * @see TextureAtlasSprite#getV(float)
     */
    public static float getUnInterpolatedV(TextureAtlasSprite sprite, float v) {
        float f = sprite.getV1() - sprite.getV0();
        return (v - sprite.getV0()) / f;
    }
}
