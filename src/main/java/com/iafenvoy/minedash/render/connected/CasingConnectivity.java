package com.iafenvoy.minedash.render.connected;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public class CasingConnectivity {
    private final Map<Block, Entry> entries;

    public CasingConnectivity() {
        this.entries = new IdentityHashMap<>();
    }

    public Entry get(BlockState blockState) {
        return this.entries.get(blockState.getBlock());
    }

    public void makeCasing(Block block, CTSpriteShiftEntry casing) {
        new Entry(block, casing, (s, f) -> true).register();
    }

    public void make(Block block, CTSpriteShiftEntry casing) {
        new Entry(block, casing, (s, f) -> true).register();
    }

    public void make(Block block, CTSpriteShiftEntry casing, BiPredicate<BlockState, Direction> predicate) {
        new Entry(block, casing, predicate).register();
    }

    public class Entry {
        private final Block block;
        private final CTSpriteShiftEntry casing;
        private final BiPredicate<BlockState, Direction> predicate;

        private Entry(Block block, CTSpriteShiftEntry casing, BiPredicate<BlockState, Direction> predicate) {
            this.block = block;
            this.casing = casing;
            this.predicate = predicate;
        }

        public CTSpriteShiftEntry getCasing() {
            return this.casing;
        }

        public boolean isSideValid(BlockState state, Direction face) {
            return this.predicate.test(state, face);
        }

        public void register() {
            CasingConnectivity.this.entries.put(this.block, this);
        }

    }
}
