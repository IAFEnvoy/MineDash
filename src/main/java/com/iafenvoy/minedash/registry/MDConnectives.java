package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.render.connected.CTSpriteShiftEntry;
import com.iafenvoy.minedash.render.connected.CTSpriteShifter;
import com.iafenvoy.minedash.render.connected.CTTypes;
import com.iafenvoy.minedash.render.connected.ConnectedTextureManager;
import com.iafenvoy.minedash.render.connected.behaviour.SimpleCTBehaviour;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class MDConnectives {
    public static final CTSpriteShiftEntry SQUARE = create("square");
    public static final CTSpriteShiftEntry SQUARE_F = create("square_f");

    private static CTSpriteShiftEntry create(String name) {
        return CTSpriteShifter.getCT(CTTypes.OMNIDIRECTIONAL, ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "block/" + name), ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "block/" + name + "_connected"));
    }

    @SubscribeEvent
    public static void registerCTBlocks(FMLClientSetupEvent event) {
        ConnectedTextureManager.register(MDBlocks.SQUARE.get(), () -> new SimpleCTBehaviour(SQUARE));
        ConnectedTextureManager.register(MDBlocks.SQUARE_F.get(), () -> new SimpleCTBehaviour(SQUARE_F));
    }
}
