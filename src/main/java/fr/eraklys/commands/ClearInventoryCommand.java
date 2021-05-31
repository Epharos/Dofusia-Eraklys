package fr.eraklys.commands;

import com.mojang.brigadier.CommandDispatcher;

import fr.eraklys.Eraklys;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class ClearInventoryCommand 
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
				Commands.literal("clearinv")
				.requires(src -> src.hasPermissionLevel(4))
				.then(Commands.argument("player", EntityArgument.player())
					.executes(ctx -> clearInventory(ctx.getSource().asPlayer(), EntityArgument.getPlayer(ctx, "player"))))
		);
	}

	private static int clearInventory(ServerPlayerEntity asPlayer, ServerPlayerEntity player) 
	{
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
		{
			cap.clearInventory();
		});
		
		return 0;
	}
}
