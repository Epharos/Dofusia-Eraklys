package fr.eraklys.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fr.eraklys.Eraklys;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class GiveItemCommand 
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
				Commands.literal("giveitem")
				.requires(src -> src.hasPermissionLevel(4))
				.then(Commands.argument("player", EntityArgument.player())
						.then(Commands.argument("item", ItemArgument.item())
								.then(Commands.argument("quantity", IntegerArgumentType.integer())
										.executes(ctx -> givePlayer(ctx.getSource().asPlayer(), EntityArgument.getPlayer(ctx, "player"), ItemArgument.getItem(ctx, "item"), IntegerArgumentType.getInteger(ctx, "quantity"))))))
		);
	}

	private static int givePlayer(ServerPlayerEntity asPlayer, ServerPlayerEntity player, ItemInput item, int integer) 
	{		
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
		{
			try 
			{
				ItemStack is = item.createStack(1, false);
				cap.putStack(is, integer);
				
				asPlayer.sendMessage(new TranslationTextComponent("admin.item.give", is.getTextComponent(), integer, player.getName().getString()));
				player.sendMessage(new TranslationTextComponent("user.item.give", is.getTextComponent(), integer));
			} 
			catch (CommandSyntaxException e) 
			{
				e.printStackTrace();
			}
		});
		
		return 0;
	}
}
