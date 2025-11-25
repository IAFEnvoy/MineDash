package com.iafenvoy.minedash.item.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SpikeBlock extends AbstractSpikeBlock {
    private static final Map<Direction, VoxelShape> HITBOXES = ImmutableMap.<Direction, VoxelShape>builder()
            .put(Direction.UP, box(6, 6, 6, 10, 12, 10))
            .put(Direction.DOWN, box(6, 4, 6, 10, 10, 10))
            .put(Direction.EAST, box(6, 6, 6, 12, 10, 10))
            .put(Direction.WEST, box(4, 6, 6, 10, 10, 10))
            .put(Direction.SOUTH, box(6, 6, 6, 10, 10, 12))
            .put(Direction.NORTH, box(6, 6, 4, 10, 10, 10))
            .build();

    public SpikeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public float getHeight() {
        return 1;
    }

    @Override
    public @NotNull VoxelShape getHitbox(@NotNull BlockState state) {
        return HITBOXES.get(state.getValue(FACING));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, components, tooltipFlag);
        components.add(Component.translatable("block.minedash.spike.tooltip"));
    }
}
