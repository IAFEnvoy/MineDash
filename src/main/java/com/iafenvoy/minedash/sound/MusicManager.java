package com.iafenvoy.minedash.sound;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class MusicManager {
    /**
     * Loading ordinal:
     * 1. Save/Server
     * 2. Config Folder
     * 3. GD Music Index
     * All files put in ./minedash folder
     */


    @Nullable
    private static SoundPlayer PLAYER;

    public static void play(MusicData data) {
        if (PLAYER != null) PLAYER.play(data);
    }

    public static @Nullable SoundPlayer getPlayer() {
        return PLAYER;
    }

    public static void createPlayer() {
        PLAYER = new SoundPlayer();
    }

    public static void destroyPlayer() {
        if (PLAYER != null) PLAYER.close();
        PLAYER = null;
    }
}
