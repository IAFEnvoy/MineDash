package com.iafenvoy.minedash.render.connected;

import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import net.minecraft.resources.ResourceLocation;

public interface CTType {
    ResourceLocation getId();

    int getSheetSize();

    ConnectedTextureBehaviour.ContextRequirement getContextRequirement();

    int getTextureIndex(ConnectedTextureBehaviour.CTContext context);
}
