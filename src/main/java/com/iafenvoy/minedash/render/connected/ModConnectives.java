package com.iafenvoy.minedash.render.connected;

import com.iafenvoy.minedash.registry.MDBlocks;
import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import com.iafenvoy.minedash.render.connected.behaviour.SimpleCTBehaviour;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.function.Supplier;

@EventBusSubscriber(Dist.CLIENT)
public final class ModConnectives {
    public static final ModelSwapper MODEL_SWAPPER = new ModelSwapper();

    public static void register() {
        register(MDBlocks.SQUARE.get(), () -> new SimpleCTBehaviour(AllSpriteShifts.SQUARE));
    }

    private static void register(Block entry, Supplier<ConnectedTextureBehaviour> behaviorSupplier) {
        MODEL_SWAPPER.getCustomBlockModels().register(entry, model -> new CTModel(model, behaviorSupplier.get()));
    }

    @SubscribeEvent
    public static void swapModel(ModelEvent.ModifyBakingResult event) {
        MODEL_SWAPPER.onModelBake(event.getModels());
    }
}
