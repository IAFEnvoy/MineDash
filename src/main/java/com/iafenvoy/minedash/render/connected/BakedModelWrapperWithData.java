package com.iafenvoy.minedash.render.connected;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public abstract class BakedModelWrapperWithData extends BakedModelWrapper<BakedModel> {
    public BakedModelWrapperWithData(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public final @NotNull ModelData getModelData(@NotNull BlockAndTintGetter world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData blockEntityData) {
        ModelData.Builder builder = super.getModelData(world, pos, state, blockEntityData).derive();
        if (this.originalModel instanceof BakedModelWrapperWithData)
            ((BakedModelWrapperWithData) this.originalModel).gatherModelData(builder, world, pos, state, blockEntityData);
        this.gatherModelData(builder, world, pos, state, blockEntityData);
        return builder.build();
    }

    protected abstract ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData);
}
