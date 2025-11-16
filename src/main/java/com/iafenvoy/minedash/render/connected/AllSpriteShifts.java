package com.iafenvoy.minedash.render.connected;

import com.iafenvoy.minedash.MineDash;
import net.minecraft.resources.ResourceLocation;

public class AllSpriteShifts {
    public static final CTSpriteShiftEntry SQUARE = omni("square");

    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }

    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, originalLocation), ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, targetLocation));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "block/" + blockTextureName), ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }
}
