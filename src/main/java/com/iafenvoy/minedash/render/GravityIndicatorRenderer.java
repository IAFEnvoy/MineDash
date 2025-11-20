package com.iafenvoy.minedash.render;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.MineDash;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public enum GravityIndicatorRenderer implements LayeredDraw.Layer {
    INSTANCE;
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "gravity_indicator");
    private static final List<RenderInstance> INSTANCES = new CopyOnWriteArrayList<>();

    public static void addIndicator(boolean reverse) {
        INSTANCES.add(new RenderInstance(reverse, Minecraft.getInstance().getWindow().getGuiScaledWidth()));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker deltaTracker) {
        INSTANCES.forEach(x -> x.render(graphics));
        INSTANCES.removeIf(RenderInstance::ended);
    }

    @SubscribeEvent
    public static void registerIndicatorLayer(RegisterGuiLayersEvent event) {
        event.registerBelowAll(ID, INSTANCE);
    }

    private static class RenderInstance {
        private static final int MOVE_RANGE = 8, MOVE_DELTA = 10, COUNT = 30;
        private static final Random RANDOM = new Random();
        private final boolean reverse;
        private final List<SingleLine> points;
        private int progress = MOVE_RANGE;

        public RenderInstance(boolean reverse, int width) {
            this.reverse = reverse;
            ImmutableList.Builder<SingleLine> builder = ImmutableList.builder();
            double step = (double) width / (COUNT - 1);
            for (int i = 0; i < COUNT; i++)
                builder.add(new SingleLine(
                        (int) Mth.clamp(i * step + (RANDOM.nextDouble() - 0.5) * step * 0.6, 0, width),
                        (int) RANDOM.nextDouble(-20, 20),
                        (int) RANDOM.nextDouble(1, 3),
                        (int) RANDOM.nextDouble(70, 80),
                        (int) RANDOM.nextDouble(0x50, 0x7F)
                ));
            this.points = builder.build();
        }

        public void render(GuiGraphics graphics) {
            int baseHeight = graphics.guiHeight() / 2 + this.progress * MOVE_DELTA * (this.reverse ? 1 : -1), baseColor = this.reverse ? 0xFFFF00 : 0x00FFFF;
            for (SingleLine line : this.points) {
                int x = line.x, y = baseHeight + line.y, width = line.halfWidth, height = line.halfHeight, color = line.mixColor(baseColor, this.progress);
                graphics.fillGradient(x - width, y - height, x + width, y, 0, color);
                graphics.fillGradient(x - width, y, x + width, y + height, color, 0);
            }
            this.progress--;
        }

        public boolean ended() {
            return this.progress < -MOVE_RANGE;
        }

        private record SingleLine(int x, int y, int halfWidth, int halfHeight, int maxAlpha) {
            public int mixColor(int color, int progress) {
                return color | (int) (this.maxAlpha * Math.clamp(1 - 1.0 * Math.abs(progress) / MOVE_RANGE, 0, 1)) << 24;
            }
        }
    }
}
