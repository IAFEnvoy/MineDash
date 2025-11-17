package com.iafenvoy.minedash.render.connected;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.render.connected.behaviour.ConnectedTextureBehaviour;
import net.minecraft.resources.ResourceLocation;

public abstract class CTTypes implements CTType {
    public static final CTTypes OMNIDIRECTIONAL = new CTTypes("omnidirectional", 8, ConnectedTextureBehaviour.ContextRequirement.builder().all().build()) {
        @Override
        public int getTextureIndex(ConnectedTextureBehaviour.CTContext context) {
            int tileX = 0, tileY = 0;
            int borders = (!context.up ? 1 : 0) + (!context.down ? 1 : 0) + (!context.left ? 1 : 0) + (!context.right ? 1 : 0);

            if (context.up) tileX++;
            if (context.down) tileX += 2;
            if (context.left) tileY++;
            if (context.right) tileY += 2;

            if (borders == 0) {
                if (context.topRight) tileX++;
                if (context.topLeft) tileX += 2;
                if (context.bottomRight) tileY += 2;
                if (context.bottomLeft) tileY++;
            }

            if (borders == 1) {
                if (!context.right)
                    if (context.topLeft || context.bottomLeft) {
                        tileY = 4;
                        tileX = -1 + (context.bottomLeft ? 1 : 0) + (context.topLeft ? 1 : 0) * 2;
                    }
                if (!context.left)
                    if (context.topRight || context.bottomRight) {
                        tileY = 5;
                        tileX = -1 + (context.bottomRight ? 1 : 0) + (context.topRight ? 1 : 0) * 2;
                    }
                if (!context.down)
                    if (context.topLeft || context.topRight) {
                        tileY = 6;
                        tileX = -1 + (context.topLeft ? 1 : 0) + (context.topRight ? 1 : 0) * 2;
                    }
                if (!context.up)
                    if (context.bottomLeft || context.bottomRight) {
                        tileY = 7;
                        tileX = -1 + (context.bottomLeft ? 1 : 0) + (context.bottomRight ? 1 : 0) * 2;
                    }
            }
            if (borders == 2)
                if ((context.up && context.left && context.topLeft) || (context.down && context.left && context.bottomLeft)
                        || (context.up && context.right && context.topRight) || (context.down && context.right && context.bottomRight))
                    tileX += 3;
            return tileX + 8 * tileY;
        }
    };

    private final ResourceLocation id;
    private final int sheetSize;
    private final ConnectedTextureBehaviour.ContextRequirement contextRequirement;

    CTTypes(String name, int sheetSize, ConnectedTextureBehaviour.ContextRequirement contextRequirement) {
        this.id = ResourceLocation.fromNamespaceAndPath(MineDash.MOD_ID, name);
        this.sheetSize = sheetSize;
        this.contextRequirement = contextRequirement;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public int getSheetSize() {
        return this.sheetSize;
    }

    @Override
    public ConnectedTextureBehaviour.ContextRequirement getContextRequirement() {
        return this.contextRequirement;
    }
}
