package com.iafenvoy.minedash.render.connected.behaviour;

import com.iafenvoy.minedash.render.connected.CTSpriteShiftEntry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SimpleCTBehaviour extends ConnectedTextureBehaviour.Base {
    protected final CTSpriteShiftEntry shift;

    public SimpleCTBehaviour(CTSpriteShiftEntry shift) {
        this.shift = shift;
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @NotNull TextureAtlasSprite sprite) {
        return this.shift;
    }
}
