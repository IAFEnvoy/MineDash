package com.iafenvoy.minedash.sound;

import com.iafenvoy.minedash.MineDash;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class SoundPlayer {
    private final Object2IntMap<MusicData> bufferIds = new Object2IntLinkedOpenHashMap<>();
    private final Source music, sfx;

    public SoundPlayer() {
        RenderSystem.assertOnRenderThread();
        MineDash.LOGGER.info("Creating MineDash's OpenAL source.");
        this.music = new Source(true);
        this.sfx = new Source(false);
    }

    public void play(MusicData data) {
        this.play(data, 0);
    }

    public void play(MusicData data, int second) {
        if (!this.bufferIds.containsKey(data))
            try {
                this.bufferIds.put(data, data.loadFromDisk());
            } catch (Exception e) {
                throw new RuntimeException("Failed to play sound.", e);
            }
        (data.type().isSfx() ? this.sfx : this.music).play(this.bufferIds.getInt(data), second);
    }

    public void stop() {
        RenderSystem.assertOnRenderThread();
        this.music.stop();
        this.sfx.stop();
        this.bufferIds.values().forEach(AL10::alDeleteBuffers);
    }

    @ApiStatus.Internal
    public void close() {
        RenderSystem.assertOnRenderThread();
        MineDash.LOGGER.info("Shutdown MineDash's OpenAL source.");
        this.music.close();
        this.sfx.close();
    }

    private record Source(int source, boolean singleton) {
        public Source(boolean singleton) {
            this(createSource(), singleton);
        }

        public static int createSource() {
            RenderSystem.assertOnRenderThread();
            int source = AL10.alGenSources();
            AL10.alSourcef(source, AL10.AL_MIN_GAIN, 0.0f);
            AL10.alSourcef(source, AL10.AL_MAX_GAIN, 1.0f);
            return source;
        }

        public void setVolume(float volume) {
            RenderSystem.assertOnRenderThread();
            AL10.alSourcef(this.source, AL10.AL_GAIN, volume);
        }

        public void play(int buffer, int second) {
            this.play(buffer);
            AL11.alSourcei(this.source, AL11.AL_SEC_OFFSET, second);
        }

        public void play(int buffer) {
            RenderSystem.assertOnRenderThread();
            if (this.singleton) this.stop();
            try {
                AL10.alSourcei(this.source, AL10.AL_BUFFER, buffer);
                AL10.alSourcePlay(this.source);
            } catch (Exception e) {
                MineDash.LOGGER.error("Failed to play buffer {}", buffer, e);
            }
        }

        public void stop() {
            RenderSystem.assertOnRenderThread();
            AL10.alSourceStop(this.source);
        }

        public void close() {
            RenderSystem.assertOnRenderThread();
            this.stop();
            AL10.alDeleteSources(this.source);
        }
    }
}
