package com.iafenvoy.minedash.render.connected.behaviour;

import com.iafenvoy.minedash.render.connected.CTSpriteShiftEntry;
import com.iafenvoy.minedash.render.connected.CTType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class ConnectedTextureBehaviour {
    @Nullable
    public abstract CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite);

    @Nullable
    public abstract CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction);

    public boolean buildContextForOccludedDirections() {
        return false;
    }

    protected boolean isBeingBlocked(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        BlockPos blockingPos = otherPos.relative(face);
        BlockState blockState = reader.getBlockState(pos);
        BlockState blockingState = reader.getBlockState(blockingPos);

        if (!Block.isFaceFull(blockingState.getShape(reader, blockingPos), face.getOpposite())) return false;
        if (face.getAxis().choose(pos.getX(), pos.getY(), pos.getZ()) != face.getAxis().choose(otherPos.getX(), otherPos.getY(), otherPos.getZ()))
            return false;

        return this.connectsTo(state, this.getCTBlockState(reader, blockState, face.getOpposite(), pos.relative(face), blockingPos), reader, pos, blockingPos, face);
    }

    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return !this.isBeingBlocked(state, reader, pos, otherPos, face) && state.getBlock() == other.getBlock();
    }

    private boolean testConnection(BlockAndTintGetter reader, BlockPos currentPos, BlockState connectiveCurrentState, Direction textureSide, final Direction horizontal, final Direction vertical, int sh, int sv) {
        BlockState trueCurrentState = reader.getBlockState(currentPos);
        BlockPos targetPos = currentPos.relative(horizontal, sh).relative(vertical, sv);
        BlockState connectiveTargetState = this.getCTBlockState(reader, trueCurrentState, textureSide, currentPos, targetPos);
        return this.connectsTo(connectiveCurrentState, connectiveTargetState, reader, currentPos, targetPos, textureSide);
    }

    public BlockState getCTBlockState(BlockAndTintGetter reader, BlockState reference, Direction face, BlockPos fromPos, BlockPos toPos) {
        BlockState blockState = reader.getBlockState(toPos);
        return blockState.getAppearance(reader, toPos, face, reference, fromPos);
    }

    protected boolean reverseUVs(BlockState state, Direction face) {
        return false;
    }

    protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
        return this.reverseUVs(state, face);
    }

    protected boolean reverseUVsVertically(BlockState state, Direction face) {
        return this.reverseUVs(state, face);
    }

    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis().isHorizontal() ? Direction.UP : Direction.NORTH;
    }

    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == Axis.X ? Direction.SOUTH : Direction.WEST;
    }

    public CTContext buildContext(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face, ContextRequirement requirement) {
        boolean positive = face.getAxisDirection() == AxisDirection.POSITIVE;
        Direction h = this.getRightDirection(reader, pos, state, face);
        Direction v = this.getUpDirection(reader, pos, state, face);
        h = positive ? h.getOpposite() : h;
        if (face == Direction.DOWN) {
            v = v.getOpposite();
            h = h.getOpposite();
        }

        final Direction horizontal = h;
        final Direction vertical = v;

        boolean flipH = this.reverseUVsHorizontally(state, face);
        boolean flipV = this.reverseUVsVertically(state, face);
        int sh = flipH ? -1 : 1;
        int sv = flipV ? -1 : 1;

        CTContext context = new CTContext();

        if (requirement.up) {
            context.up = this.testConnection(reader, pos, state, face, horizontal, vertical, 0, sv);
        }
        if (requirement.down) {
            context.down = this.testConnection(reader, pos, state, face, horizontal, vertical, 0, -sv);
        }
        if (requirement.left) {
            context.left = this.testConnection(reader, pos, state, face, horizontal, vertical, -sh, 0);
        }
        if (requirement.right) {
            context.right = this.testConnection(reader, pos, state, face, horizontal, vertical, sh, 0);
        }

        if (requirement.topLeft) {
            context.topLeft =
                    context.up && context.left && this.testConnection(reader, pos, state, face, horizontal, vertical, -sh, sv);
        }
        if (requirement.topRight) {
            context.topRight =
                    context.up && context.right && this.testConnection(reader, pos, state, face, horizontal, vertical, sh, sv);
        }
        if (requirement.bottomLeft) {
            context.bottomLeft = context.down && context.left
                    && this.testConnection(reader, pos, state, face, horizontal, vertical, -sh, -sv);
        }
        if (requirement.bottomRight) {
            context.bottomRight = context.down && context.right
                    && this.testConnection(reader, pos, state, face, horizontal, vertical, sh, -sv);
        }

        return context;
    }

    public static class CTContext {
        public static final CTContext EMPTY = new CTContext();

        public boolean up, down, left, right;
        public boolean topLeft, topRight, bottomLeft, bottomRight;
    }

    public record ContextRequirement(boolean up, boolean down, boolean left, boolean right, boolean topLeft,
                                     boolean topRight, boolean bottomLeft, boolean bottomRight) {
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private boolean up, down, left, right;
            private boolean topLeft, topRight, bottomLeft, bottomRight;

            public Builder up() {
                this.up = true;
                return this;
            }

            public Builder down() {
                this.down = true;
                return this;
            }

            public Builder left() {
                this.left = true;
                return this;
            }

            public Builder right() {
                this.right = true;
                return this;
            }

            public Builder topLeft() {
                this.topLeft = true;
                return this;
            }

            public Builder topRight() {
                this.topRight = true;
                return this;
            }

            public Builder bottomLeft() {
                this.bottomLeft = true;
                return this;
            }

            public Builder bottomRight() {
                this.bottomRight = true;
                return this;
            }

            public Builder horizontal() {
                this.left();
                this.right();
                return this;
            }

            public Builder vertical() {
                this.up();
                this.down();
                return this;
            }

            public Builder axisAligned() {
                this.horizontal();
                this.vertical();
                return this;
            }

            public Builder corners() {
                this.topLeft();
                this.topRight();
                this.bottomLeft();
                this.bottomRight();
                return this;
            }

            public Builder all() {
                this.axisAligned();
                this.corners();
                return this;
            }

            public ContextRequirement build() {
                return new ContextRequirement(this.up, this.down, this.left, this.right, this.topLeft, this.topRight, this.bottomLeft, this.bottomRight);
            }
        }
    }

    public static abstract class Base extends ConnectedTextureBehaviour {
        @Override
        @Nullable
        public abstract CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite);

        @Override
        @Nullable
        public CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
            CTSpriteShiftEntry shift = this.getShift(state, direction, null);
            if (shift == null) return null;
            return shift.getType();
        }
    }
}
