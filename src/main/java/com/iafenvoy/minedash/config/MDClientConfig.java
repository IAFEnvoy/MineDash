package com.iafenvoy.minedash.config;

import com.iafenvoy.jupiter.config.container.AutoInitConfigContainer;
import com.iafenvoy.jupiter.config.entry.BooleanEntry;
import com.iafenvoy.jupiter.config.entry.IntegerEntry;
import com.iafenvoy.jupiter.interfaces.IConfigEntry;
import com.iafenvoy.minedash.MineDash;
import net.minecraft.resources.ResourceLocation;

public class MDClientConfig extends AutoInitConfigContainer {
    public static final MDClientConfig INSTANCE = new MDClientConfig();
    public final General general = new General();

    public MDClientConfig() {
        super(ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "client"), "screen.%s.client.title".formatted(MineDash.MOD_ID), "./config/%s-client.json".formatted(MineDash.MOD_ID));
    }

    private static String format(String category, String jsonKey) {
        return "config.%s.%s.%s".formatted(MineDash.MOD_ID, category, jsonKey);
    }

    public static class General extends AutoInitConfigCategoryBase {
        public final IConfigEntry<Boolean> showHitboxes = new BooleanEntry(format("general", "showHitboxes"), false).json("showHitboxes");
        public final IConfigEntry<Integer> hitboxDisplayRange = new IntegerEntry(format("general", "hitboxDisplayRange"), 5, 1, 32).json("hitboxDisplayRange");

        public General() {
            super("general", "category.%s.general".formatted(MineDash.MOD_ID));
        }
    }
}
