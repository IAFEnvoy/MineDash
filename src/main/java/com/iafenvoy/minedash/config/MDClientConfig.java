package com.iafenvoy.minedash.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.minedash.MineDash;
import net.minecraft.resources.ResourceLocation;

public class MDClientConfig extends AutoInitConfigContainer {
    private static final String PATH = "./config/%s-client.json".formatted(MineDash.MOD_ID);
    public static final MDClientConfig INSTANCE = new MDClientConfig();
    public final General general = new General();

    public MDClientConfig() {
        super(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "client"), "screen.%s.client.title".formatted(MineDash.MOD_ID), PATH);
    }

    private static String format(String category, String jsonKey) {
        return "config.%s.%s.%s".formatted(MineDash.MOD_ID, category, jsonKey);
    }

    public static class General extends AutoInitConfigCategoryBase {
        public final BooleanEntry showHitboxes = BooleanEntry.builder(format("general", "showHitboxes"), false).json("showHitboxes").build();
        public final IntegerEntry hitboxDisplayRange = IntegerEntry.builder(format("general", "hitboxDisplayRange"), 4).min(1).max(64).json("hitboxDisplayRange").build();

        public General() {
            super("general", "category.%s.general".formatted(MineDash.MOD_ID));
        }
    }
}
