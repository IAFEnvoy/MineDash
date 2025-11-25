package com.iafenvoy.minedash.mixin;

import com.iafenvoy.minedash.sound.MusicManager;
import com.mojang.blaze3d.audio.Library;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Library.class)
public class LibraryMixin {
    @Inject(method = "init", at = @At("RETURN"))
    private void openPlayer(String deviceSpecifier, boolean directionalAudio, CallbackInfo ci) {
        MusicManager.createPlayer();
    }

    @Inject(method = "cleanup", at = @At("HEAD"))
    private void closePlayer(CallbackInfo ci) {
        MusicManager.destroyPlayer();
    }
}
