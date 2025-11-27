package com.iafenvoy.minedash.render.extra;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.iafenvoy.minedash.util.CopyOnWriteHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class LevelCacheStorage {
    private static final Map<ChunkPos, List<BlockPos>> CHUNK_DATA = new CopyOnWriteHashMap<>();
    private static final Supplier<Set<Block>> NEEDED_BLOCK = Suppliers.memoize(ExtraRenderManager::getRequiredBlock);

    public static void onChunkLoad(ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        Set<Block> needed = NEEDED_BLOCK.get();
        List<BlockPos> posList = new LinkedList<>();
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
                for (int y = chunk.getMinBuildHeight(); y <= chunk.getMaxBuildHeight(); y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (needed.contains(chunk.getBlockState(pos).getBlock()))
                        posList.add(pos.offset(chunkPos.x * 16, 0, chunkPos.z * 16));
                }
        CHUNK_DATA.put(chunkPos, posList);
    }

    public static void onChunkUnload(ChunkAccess chunk) {
        CHUNK_DATA.remove(chunk.getPos());
    }

    public static void onBlockUpdate(BlockPos pos, BlockState newState) {
        List<BlockPos> posList = CHUNK_DATA.get(new ChunkPos(pos));
        if (posList == null) return;
        posList.remove(pos);
        if (NEEDED_BLOCK.get().contains(newState.getBlock())) posList.add(pos);
    }

    public static List<BlockPos> collectForRender(ChunkPos pos, int chunkRange) {
        ImmutableList.Builder<BlockPos> builder = ImmutableList.builder();
        for (Map.Entry<ChunkPos, List<BlockPos>> entry : CHUNK_DATA.entrySet())
            if (entry.getKey().getChessboardDistance(pos) <= chunkRange)
                builder.addAll(entry.getValue());
        return builder.build();
    }
}
