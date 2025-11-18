package com.iafenvoy.minedash.network;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.network.payload.GamePlayControlC2SPayload;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class GamePlayPacketDistributor {
    //DO NOT SAVE DIRECTLY
    private static final Object2IntMap<UUID> CONTROLLING = new Object2IntOpenHashMap<>();

    public static void bindEntity(Player player, GamePlayEntity entity) {
        CONTROLLING.put(player.getUUID(), entity.getId());
    }

    public static void runAction(@Nullable Player player, GamePlayControlC2SPayload.ControlType controlType, boolean pressed) {
        if (player == null) return;
        UUID uuid = player.getUUID();
        if (!CONTROLLING.containsKey(uuid)) return;
        if (!(player.level().getEntity(CONTROLLING.getInt(uuid)) instanceof GamePlayEntity entity)) return;
        switch (controlType) {
            case JUMP -> entity.setJump(pressed);
            case LEFT -> entity.setLeft(pressed);
            case RIGHT -> entity.setRight(pressed);
        }
        entity.hurtMarked = true;
        if (player.level().isClientSide)
            PacketDistributor.sendToServer(new GamePlayControlC2SPayload(controlType, pressed));
    }
}
