package com.iafenvoy.minedash.render.connected;

import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(Dist.CLIENT)
public class ModelSwapper {
    private static final ModelSwapper MODEL_SWAPPER = new ModelSwapper();
    protected CustomBlockModels customBlockModels = new CustomBlockModels();

    public CustomBlockModels getCustomBlockModels() {
        return this.customBlockModels;
    }

    public void onModelBake(Map<ModelResourceLocation, BakedModel> modelRegistry) {
        this.customBlockModels.forEach((block, modelFunc) -> swapModels(modelRegistry, getAllBlockStateModelLocations(BuiltInRegistries.BLOCK.getKey(block), block), modelFunc));
    }

    public static <T extends BakedModel> void swapModels(Map<ModelResourceLocation, BakedModel> modelRegistry, List<ModelResourceLocation> locations, Function<BakedModel, T> factory) {
        locations.forEach(location -> swapModels(modelRegistry, location, factory));
    }

    public static <T extends BakedModel> void swapModels(Map<ModelResourceLocation, BakedModel> modelRegistry, ModelResourceLocation location, Function<BakedModel, T> factory) {
        modelRegistry.put(location, factory.apply(modelRegistry.get(location)));
    }

    public static List<ModelResourceLocation> getAllBlockStateModelLocations(ResourceLocation id, Block block) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();
        int arraySize = possibleStates.size();
        List<ModelResourceLocation> models = new ArrayList<>(5 + arraySize + (arraySize / 10));
        possibleStates.forEach(state -> models.add(BlockModelShaper.stateToModelLocation(id, state)));
        return models;
    }

    public static void register(Block entry, Supplier<ConnectedTextureBehaviour> behaviorSupplier) {
        MODEL_SWAPPER.getCustomBlockModels().register(entry, model -> new CTModel(model, behaviorSupplier.get()));
    }

    @ApiStatus.Internal
    @SubscribeEvent
    public static void swapModel(ModelEvent.ModifyBakingResult event) {
        MODEL_SWAPPER.onModelBake(event.getModels());
    }
}
