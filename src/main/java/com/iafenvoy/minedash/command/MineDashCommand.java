package com.iafenvoy.minedash.command;

import com.iafenvoy.minedash.MineDash;
import com.iafenvoy.minedash.network.payload.ThemeColorChangeS2CPayload;
import com.iafenvoy.minedash.render.GravityIndicatorRenderer;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

@EventBusSubscriber
public final class MineDashCommand {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal(MineDash.MOD_ID)
                .then(literal("theme")
                        .then(argument("r", FloatArgumentType.floatArg(0, 1))
                                .then(argument("g", FloatArgumentType.floatArg(0, 1))
                                        .then(argument("b", FloatArgumentType.floatArg(0, 1))
                                                .executes(MineDashCommand::changeThemeColor)))))
                .then(literal("debug")
                        .then(literal("gravity")
                                .then(argument("reverse", BoolArgumentType.bool())
                                        .executes(ctx -> {
                                            boolean reverse = BoolArgumentType.getBool(ctx, "reverse");
                                            GravityIndicatorRenderer.addIndicator(reverse);
                                            return 1;
                                        }))))
        );
    }

    public static int changeThemeColor(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        float r = FloatArgumentType.getFloat(ctx, "r"), g = FloatArgumentType.getFloat(ctx, "g"), b = FloatArgumentType.getFloat(ctx, "b");
        PacketDistributor.sendToPlayer(player, new ThemeColorChangeS2CPayload(r, g, b));
        return 1;
    }
}
