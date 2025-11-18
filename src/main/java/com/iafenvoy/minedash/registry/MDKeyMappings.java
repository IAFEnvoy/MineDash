package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
//TODO::Should use key-down and key-up instead of key-press?
public final class MDKeyMappings {
    public static final String CATEGORY = "category.%s.main".formatted(MineDash.MOD_ID);

    public static final KeyMapping JUMP = new KeyMapping("key.%s.jump".formatted(MineDash.MOD_ID), InputConstants.KEY_UP, CATEGORY);
    public static final KeyMapping LEFT = new KeyMapping("key.%s.left".formatted(MineDash.MOD_ID), InputConstants.KEY_LEFT, CATEGORY);
    public static final KeyMapping RIGHT = new KeyMapping("key.%s.right".formatted(MineDash.MOD_ID), InputConstants.KEY_RIGHT, CATEGORY);

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(JUMP);
    }

    @SubscribeEvent
    public static void checkKeyMappings(ClientTickEvent.Post event) {
        if (JUMP.isDown())
            PacketDistributor.sendToServer(new GamePlayControlC2SPayload(GamePlayControlC2SPayload.ControlType.JUMP));
        if (LEFT.isDown())
            PacketDistributor.sendToServer(new GamePlayControlC2SPayload(GamePlayControlC2SPayload.ControlType.LEFT));
        if (RIGHT.isDown())
            PacketDistributor.sendToServer(new GamePlayControlC2SPayload(GamePlayControlC2SPayload.ControlType.RIGHT));
    }
}
