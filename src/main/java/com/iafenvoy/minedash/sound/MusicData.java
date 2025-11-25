package com.iafenvoy.minedash.sound;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.util.FileDownloader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public record MusicData(MusicType type, String name) {
    public static final String INDEX_URL = "https://geometrydashfiles.b-cdn.net/%s%s.ogg";
    public static final Codec<MusicData> CODEC = RecordCodecBuilder.create(i -> i.group(
            MusicType.CODEC.fieldOf("type").forGetter(MusicData::type),
            Codec.STRING.fieldOf("name").forGetter(MusicData::name)
    ).apply(i, MusicData::new));

    public String getFilePath() {
        return "./%s/%s/%s.ogg".formatted(MineDash.MOD_ID, this.type.getFolder(), this.name);
    }

    public void downloadFromIndex() throws IOException, IllegalArgumentException, IllegalStateException {
        if (this.type.isCustom()) throw new IllegalStateException("Cannot download custom music from index");
        FileDownloader.downloadFile(INDEX_URL.formatted(this.type.isSfx() ? "sfx/s" : "music/", this.name), this.getFilePath());
    }

    public int loadFromDisk() throws FileNotFoundException, RuntimeException {
        String path = this.getFilePath();
        if (Files.notExists(Path.of(path))) throw new FileNotFoundException("Cannot find file: " + path);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer error = stack.mallocInt(1);
            long decoder = STBVorbis.stb_vorbis_open_filename(path, error, null);
            if (decoder == MemoryUtil.NULL)
                throw new RuntimeException("Failed to open OGG file (Error: %d)".formatted(error.get()));

            STBVorbisInfo info = STBVorbisInfo.malloc(stack);
            STBVorbis.stb_vorbis_get_info(decoder, info);

            int channels = info.channels();
            int sampleRate = info.sample_rate();
            int samples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);
            ShortBuffer pcm = MemoryUtil.memAllocShort(samples * channels);
            STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);

            int format = channels == 1 ? AL10.AL_FORMAT_MONO16 : AL10.AL_FORMAT_STEREO16;
            int buffer = AL10.alGenBuffers();
            AL10.alBufferData(buffer, format, pcm, sampleRate);
            STBVorbis.stb_vorbis_close(decoder);
            return buffer;
        }
    }

    public enum MusicType implements StringRepresentable {
        INDEX_MUSIC("index-music", false, false),
        INDEX_SFX("index-sfx", false, true),
        CUSTOM_MUSIC("custom-music", true, false),
        CUSTOM_SFX("custom-sfx", true, true);
        public static final Codec<MusicType> CODEC = StringRepresentable.fromValues(MusicType::values);
        private final String folder;
        private final boolean custom, sfx;

        MusicType(String folder, boolean custom, boolean sfx) {
            this.folder = folder;
            this.custom = custom;
            this.sfx = sfx;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.folder;
        }

        public String getFolder() {
            return this.folder;
        }

        public boolean isCustom() {
            return this.custom;
        }

        public boolean isSfx() {
            return this.sfx;
        }
    }
}
