package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.network.GamePlayPacketDistributor;
import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
//TODO::Should use key-down and key-up instead of key-pressed?
public final class MDKeyMappings {
    public static final String CATEGORY = "category.%s.main".formatted(MineDash.MOD_ID);

    public static final KeyMapping JUMP = new KeyMapping("key.%s.jump".formatted(MineDash.MOD_ID), InputConstants.KEY_UP, CATEGORY);
    public static final KeyMapping LEFT = new KeyMapping("key.%s.left".formatted(MineDash.MOD_ID), InputConstants.KEY_LEFT, CATEGORY);
    public static final KeyMapping RIGHT = new KeyMapping("key.%s.right".formatted(MineDash.MOD_ID), InputConstants.KEY_RIGHT, CATEGORY);

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(JUMP);
        event.register(LEFT);
        event.register(RIGHT);
    }

    static {
        new KeyBindingHolder(JUMP).registerPressCallback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, GamePlayControlC2SPayload.ControlType.JUMP, b));
        new KeyBindingHolder(LEFT).registerPressCallback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, GamePlayControlC2SPayload.ControlType.LEFT, b));
        new KeyBindingHolder(RIGHT).registerPressCallback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, GamePlayControlC2SPayload.ControlType.RIGHT, b));
    }

    //FIXME::Complex?
    public static class KeyBindingHolder {
        public final Supplier<KeyMapping> keyBinding;
        private final List<BooleanConsumer> callback = new ArrayList<>();
        private boolean pressed;

        public KeyBindingHolder(KeyMapping keyBinding) {
            this.keyBinding = () -> keyBinding;
            NeoForge.EVENT_BUS.addListener(this::tick);
        }

        public void registerPressCallback(BooleanConsumer consumer) {
            this.callback.add(consumer);
        }

        public void tick(ClientTickEvent.Post event) {
            KeyMapping k = this.keyBinding.get();
            if (k == null) return;
            boolean curr = k.isDown();
            if (!this.pressed && curr) this.callback.forEach(x -> x.accept(true));
            if (this.pressed && !curr) this.callback.forEach(x -> x.accept(false));
            this.pressed = curr;
        }

        public boolean isPressed() {
            return this.pressed;
        }
    }
}
