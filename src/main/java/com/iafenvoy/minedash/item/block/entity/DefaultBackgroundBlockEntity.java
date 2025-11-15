package com.iafenvoy.minedash.item.block.entity;

import com.iafenvoy.minedash.registry.MDBlockEntities;
import com.iafenvoy.minedash.registry.MDDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DefaultBackgroundBlockEntity extends BlockEntity {
    private static final String TEXTURE_KEY = "Texture";
    @Nullable
    private ResourceLocation texture;

    public DefaultBackgroundBlockEntity(BlockPos pos, BlockState blockState) {
        super(MDBlockEntities.DEFAULT_BACKGROUND.get(), pos, blockState);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(TEXTURE_KEY, Tag.TAG_STRING))
            this.texture = ResourceLocation.parse(tag.getString(TEXTURE_KEY));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.texture != null) tag.putString(TEXTURE_KEY, this.texture.toString());
    }

    @Override
    public void saveToItem(@NotNull ItemStack stack, @NotNull HolderLookup.Provider registries) {
        super.saveToItem(stack, registries);
        stack.set(MDDataComponents.TEXTURE, this.getTexture());
    }

    public @Nullable ResourceLocation getTexture() {
        return this.texture;
    }

    public void setTexture(@Nullable ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }
}
