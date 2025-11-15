package com.iafenvoy.minedash.item.block;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.item.block.entity.DefaultBackgroundBlockEntity;
import com.iafenvoy.minedash.registry.MDBlocks;
import com.iafenvoy.minedash.registry.MDDataComponents;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class DefaultBackgroundBlock extends BaseEntityBlock {
    public static final Supplier<List<ItemStack>> BUILTIN_STACKS = Suppliers.memoize(() -> Util.make(() -> {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        for (int i = 1; i <= 7; i++) {
            ItemStack stack = MDBlocks.DEFAULT_BACKGROUND.toStack();
            stack.set(MDDataComponents.TEXTURE, ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, "textures/item/default_background_%s.png".formatted(i)));
            builder.add(stack);
        }
        return builder.build();
    }));
    private static final MapCodec<DefaultBackgroundBlock> CODEC = simpleCodec(DefaultBackgroundBlock::new);

    public DefaultBackgroundBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DefaultBackgroundBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        ResourceLocation texture = stack.get(MDDataComponents.TEXTURE);
        if (level.getBlockEntity(pos) instanceof DefaultBackgroundBlockEntity blockEntity)
            blockEntity.setTexture(texture);
    }

    @Override
    protected @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.Builder params) {
        ItemStack stack = MDBlocks.DEFAULT_BACKGROUND.toStack();
        if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof DefaultBackgroundBlockEntity b)
            stack.set(MDDataComponents.TEXTURE, b.getTexture());
        return List.of(stack);
    }
}
