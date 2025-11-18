package com.iafenvoy.minedash.util;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class EntityBuildHelper {
    public static <T extends Entity> Supplier<EntityType<T>> build(String name, EntityType.EntityFactory<T> constructor, MobCategory category, int trackingRange, int updateInterval, boolean fireImmune, float sizeX, float sizeY) {
        return () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(constructor, category).clientTrackingRange(trackingRange).updateInterval(updateInterval).sized(sizeX, sizeY);
            if (fireImmune) builder.fireImmune();
            return builder.build(name);
        };
    }
}
