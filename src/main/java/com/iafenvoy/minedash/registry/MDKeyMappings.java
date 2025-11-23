package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.data.ControlType;
import com.iafenvoy.minedash.network.GamePlayPacketDistributor;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
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
        new KeyHolder(JUMP).callback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, ControlType.JUMP, b));
        new KeyHolder(LEFT).callback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, ControlType.LEFT, b));
        new KeyHolder(RIGHT).callback(b -> GamePlayPacketDistributor.runAction(Minecraft.getInstance().player, ControlType.RIGHT, b));
    }

    private static class KeyHolder {
        public final KeyMapping keyBinding;
        private final List<BooleanConsumer> callback = new ArrayList<>();
        private boolean pressed;

        public KeyHolder(KeyMapping keyBinding) {
            this.keyBinding = keyBinding;
            NeoForge.EVENT_BUS.addListener(this::tick);
        }

        public void callback(BooleanConsumer consumer) {
            this.callback.add(consumer);
        }

        public void tick(RenderLevelStageEvent event) {//client tick=20, render tick=fps
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
            boolean curr = this.keyBinding.isDown();
            if (!this.pressed && curr) this.callback.forEach(x -> x.accept(true));
            if (this.pressed && !curr) this.callback.forEach(x -> x.accept(false));
            this.pressed = curr;
        }
    }
}
