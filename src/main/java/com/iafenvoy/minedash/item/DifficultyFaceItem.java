package com.iafenvoy.minedash.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class DifficultyFaceItem extends Item {
    private final Supplier<SoundEvent> soundEvent;

    public DifficultyFaceItem(Supplier<SoundEvent> soundEvent) {
        super(new Properties());
        this.soundEvent = soundEvent;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide())
            level.playSound(null, player.blockPosition(), this.soundEvent.get(), SoundSource.NEUTRAL, 1, 1);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }
}
