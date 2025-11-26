package com.iafenvoy.minedash.render;

import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

@OnlyIn(Dist.CLIENT)
public final class ThemeColorManager {
    public static final ThemeColorManager INSTANCE = new ThemeColorManager(1, 1, 1);

    private float currentR, currentG, currentB;
    private float targetR, targetG, targetB;
    private int remainingTicks;
    private float stepR, stepG, stepB;

    public ThemeColorManager(float initialR, float initialG, float initialB) {
        this.targetR = this.currentR = Mth.clamp(initialR, 0, 1);
        this.targetG = this.currentG = Mth.clamp(initialG, 0, 1);
        this.targetB = this.currentB = Mth.clamp(initialB, 0, 1);
        this.remainingTicks = 0;
        this.stepR = this.stepG = this.stepB = 0;
        NeoForge.EVENT_BUS.register(this);
    }

    public float getR() {
        return this.currentR;
    }

    public float getG() {
        return this.currentG;
    }

    public float getB() {
        return this.currentB;
    }

    public void setColor(float targetR, float targetG, float targetB, int ticks) {
        this.targetR = Mth.clamp(targetR, 0, 1);
        this.targetG = Mth.clamp(targetG, 0, 1);
        this.targetB = Mth.clamp(targetB, 0, 1);

        if (ticks <= 0) {
            this.currentR = this.targetR;
            this.currentG = this.targetG;
            this.currentB = this.targetB;
            this.remainingTicks = 0;
            this.stepR = 0;
            this.stepG = 0;
            this.stepB = 0;
            return;
        }

        this.remainingTicks = ticks;
        this.stepR = (this.targetR - this.currentR) / ticks;
        this.stepG = (this.targetG - this.currentG) / ticks;
        this.stepB = (this.targetB - this.currentB) / ticks;
    }

    @SubscribeEvent
    public void tick(ClientTickEvent.Post event) {
        if (this.remainingTicks <= 0) return;
        // 推进一步渐变
        this.currentR += this.stepR;
        this.currentG += this.stepG;
        this.currentB += this.stepB;
        this.remainingTicks--;
        // 当渐变完成时，确保颜色准确设置到目标值（避免浮点误差）
        if (this.remainingTicks == 0) {
            this.currentR = this.targetR;
            this.currentG = this.targetG;
            this.currentB = this.targetB;
        }
    }
}
