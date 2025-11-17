package com.iafenvoy.minedash.render.connected;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(Dist.CLIENT)
public class StitchedSprite {
    private static final Map<ResourceLocation, List<StitchedSprite>> ALL = new HashMap<>();
    protected final ResourceLocation location;
    protected TextureAtlasSprite sprite;

    public StitchedSprite(ResourceLocation atlas, ResourceLocation location) {
        this.location = location;
        ALL.computeIfAbsent(atlas, rl -> new ArrayList<>()).add(this);
    }

    public StitchedSprite(ResourceLocation location) {
        this(InventoryMenu.BLOCK_ATLAS, location);
    }

    @SubscribeEvent
    public static void onTextureStitchPost(TextureAtlasStitchedEvent event) {
        TextureAtlas atlas = event.getAtlas();
        List<StitchedSprite> sprites = ALL.get(atlas.location());
        if (sprites != null)
            for (StitchedSprite sprite : sprites)
                sprite.loadSprite(atlas);
    }

    protected void loadSprite(TextureAtlas atlas) {
        this.sprite = atlas.getSprite(this.location);
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public TextureAtlasSprite get() {
        return this.sprite;
    }
}
