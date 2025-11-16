package com.iafenvoy.minedash.render.connected;

import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CTModel extends BakedModelWrapperWithData {
    protected static final ModelProperty<CTData> CT_PROPERTY = new ModelProperty<>();
    protected final ConnectedTextureBehaviour behaviour;

    public CTModel(BakedModel originalModel, ConnectedTextureBehaviour behaviour) {
        super(originalModel);
        this.behaviour = behaviour;
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        return builder.with(CT_PROPERTY, this.createCTData(world, pos, state));
    }

    protected CTData createCTData(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        CTData data = new CTData();
        MutableBlockPos mutablePos = new MutableBlockPos();
        for (Direction face : Direction.values()) {
            if (!this.behaviour.buildContextForOccludedDirections() && !Block.shouldRenderFace(state, world, pos, face, mutablePos.setWithOffset(pos, face)))
                continue;
            CTType dataType = this.behaviour.getDataType(world, pos, state, face);
            if (dataType == null) continue;
            ConnectedTextureBehaviour.CTContext context = this.behaviour.buildContext(world, pos, state, face, dataType.getContextRequirement());
            data.put(face, dataType.getTextureIndex(context));
        }
        return data;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        CTData data = extraData.get(CT_PROPERTY);
        if (data == null) return quads;
        quads = new ArrayList<>(quads);

        for (int i = 0; i < quads.size(); i++) {
            BakedQuad quad = quads.get(i);

            int index = data.get(quad.getDirection());
            if (index == -1)
                continue;

            CTSpriteShiftEntry spriteShift = this.behaviour.getShift(state, quad.getDirection(), quad.getSprite());
            if (spriteShift == null)
                continue;
            if (quad.getSprite() != spriteShift.getOriginal())
                continue;

            BakedQuad newQuad = BakedQuadHelper.clone(quad);
            int[] vertexData = newQuad.getVertices();

            for (int vertex = 0; vertex < 4; vertex++) {
                float u = BakedQuadHelper.getU(vertexData, vertex);
                float v = BakedQuadHelper.getV(vertexData, vertex);
                BakedQuadHelper.setU(vertexData, vertex, spriteShift.getTargetU(u, index));
                BakedQuadHelper.setV(vertexData, vertex, spriteShift.getTargetV(v, index));
            }

            quads.set(i, newQuad);
        }

        return quads;
    }

    protected static class CTData {
        private final int[] indices;

        public CTData() {
            this.indices = new int[6];
            Arrays.fill(this.indices, -1);
        }

        public void put(Direction face, int texture) {
            this.indices[face.get3DDataValue()] = texture;
        }

        public int get(Direction face) {
            return this.indices[face.get3DDataValue()];
        }
    }

}
