package com.iafenvoy.minedash.event;

import com.iafenvoy.minedash.render.connected.ModelSwapper;
import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.Event;

import java.util.function.Supplier;

public class RegisterCTBlockEvent extends Event {
    public void register(Block entry, Supplier<ConnectedTextureBehaviour> behaviorSupplier) {
        ModelSwapper.register(entry, behaviorSupplier);
    }
}
