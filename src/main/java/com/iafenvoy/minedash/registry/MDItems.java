package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.DifficultyFaceItem;
import com.iafenvoy.minedash.item.GamePlaySpawnEggItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class MDItems {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MineDash.MOD_ID);

    public static final DeferredItem<GamePlaySpawnEggItem> GAME_PLAY_SPAWN_EGG = register("game_play_spawn_egg", GamePlaySpawnEggItem::new);
    public static final DeferredItem<Item> DELETE_STICK = register("delete_stick", () -> new Item(new Item.Properties()));

    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_NA = register("difficulty_na", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_NA));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_AUTO = register("difficulty_auto", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_AUTO));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_EASY = register("difficulty_easy", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_EASY));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_NORMAL = register("difficulty_normal", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_NORMAL));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_HARD = register("difficulty_hard", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_HARD));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_HARDER = register("difficulty_harder", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_HARDER));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_INSANE = register("difficulty_insane", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_INSANE));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_EASY_DEMON = register("difficulty_easy_demon", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_EASY_DEMON));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_MEDIUM_DEMON = register("difficulty_medium_demon", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_MEDIUM_DEMON));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_HARD_DEMON = register("difficulty_hard_demon", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_HARD_DEMON));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_INSANE_DEMON = register("difficulty_insane_demon", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_INSANE_DEMON));
    public static final DeferredItem<DifficultyFaceItem> DIFFICULTY_EXTREME_DEMON = register("difficulty_extreme_demon", () -> new DifficultyFaceItem(MDSounds.DIFFICULTY_EXTREME_DEMON));

    public static <T extends Item> DeferredItem<T> register(String id, Supplier<T> obj) {
        return REGISTRY.register(id, obj);
    }
}
