package com.iafenvoy.minedash.mixin;

import com.iafenvoy.minedash.render.HitBoxRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Used to refresh hitboxes render data
@OnlyIn(Dist.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Shadow
    private ClientLevel level;

    @Inject(method = "handleLevelChunkWithLight", at = @At(value = "TAIL"))
    private void onChunkData(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
        HitBoxRenderer.onChunkData(this.level, packet.getX(), packet.getZ());
    }

    @Inject(method = "handleForgetLevelChunk", at = @At("TAIL"))
    private void onUnloadChunk(ClientboundForgetLevelChunkPacket packet, CallbackInfo ci) {
        HitBoxRenderer.onChunkUnload(packet.pos());
    }

    @Inject(method = "handleBlockUpdate", at = @At("TAIL"))
    private void onBlockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
        HitBoxRenderer.onBlockUpdate(packet.getPos(), packet.getBlockState());
    }
}
