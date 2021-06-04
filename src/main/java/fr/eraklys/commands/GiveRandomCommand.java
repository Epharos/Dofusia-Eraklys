package fr.eraklys.commands;

import java.util.Random;

import com.mojang.brigadier.CommandDispatcher;

import fr.eraklys.Eraklys;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class GiveRandomCommand 
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
				Commands.literal("giverand")
				.requires(src -> src.hasPermissionLevel(4))
					.executes(ctx -> givePlayer(ctx.getSource().asPlayer()))
		);
	}

	private static int givePlayer(ServerPlayerEntity player) 
	{		
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
		{
			for(int i = 0 ; i < 9 ; i++)
			{
				cap.getInventory().addStack(ItemStackUtil.getPrototype(new ItemStack(Registry.ITEM.getRandom(new Random()))), (int) (Math.random() * 1500), player);
			}
		});
		
		return 0;
	}
}
