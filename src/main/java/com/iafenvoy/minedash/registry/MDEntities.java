package com.iafenvoy.minedash.registry;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.util.EntityBuildHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber
public final class MDEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, MineDash.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<GamePlayEntity>> GAME_PLAY = register("game_play", GamePlayEntity::new, MobCategory.CREATURE, 64, 3, false, 1, 1);

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String id, EntityType.EntityFactory<T> constructor, MobCategory category, int trackingRange, int updateInterval, boolean fireImmune, float sizeX, float sizeY) {
        return REGISTRY.register(id, EntityBuildHelper.build(id, constructor, category, trackingRange, updateInterval, fireImmune, sizeX, sizeY));
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(GAME_PLAY.get(), GamePlayEntity.createAttributes().build());
    }
}
