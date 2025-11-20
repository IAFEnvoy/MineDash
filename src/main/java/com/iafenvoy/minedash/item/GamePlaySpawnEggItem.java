package com.iafenvoy.minedash.item;

import com.iafenvoy.minedash.entity.GamePlayEntity;
import com.iafenvoy.minedash.registry.MDEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GamePlaySpawnEggItem extends Item {
    public GamePlaySpawnEggItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            ItemStack stack = context.getItemInHand();
            BlockPos clickedPos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState state = level.getBlockState(clickedPos);
            BlockPos pos = state.getCollisionShape(level, clickedPos).isEmpty() ? clickedPos : clickedPos.relative(direction);
            GamePlayEntity entity = MDEntities.GAME_PLAY.get().spawn(serverLevel, stack, context.getPlayer(), pos, MobSpawnType.SPAWN_EGG, true, !Objects.equals(clickedPos, pos) && direction == Direction.UP);
            if (entity != null) {
                entity.updateDirection(context.getHorizontalDirection().getOpposite());
                stack.shrink(1);
                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, clickedPos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
