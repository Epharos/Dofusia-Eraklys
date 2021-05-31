package fr.eraklys.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import fr.eraklys.entities.npc.TalkingNpcEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SpawnNpcCommand 
{
	public static void register(CommandDispatcher<CommandSource> dispatcher)
	{
		dispatcher.register(
				Commands.literal("npc")
				.requires(src -> src.hasPermissionLevel(4))
					.then(Commands.argument("name", StringArgumentType.string())
						.then(Commands.argument("texture", StringArgumentType.string())
							.then(Commands.argument("speech", IntegerArgumentType.integer())
									.executes(ctx -> spawnNpc(ctx.getSource().asPlayer(), StringArgumentType.getString(ctx, "name"), StringArgumentType.getString(ctx, "texture"), IntegerArgumentType.getInteger(ctx, "speech"))))))
		);
	}

	private static int spawnNpc(ServerPlayerEntity player, String name, String texture, int speech) 
	{
		TalkingNpcEntity entity = new TalkingNpcEntity(null, player.getEntityWorld());
		entity.setName(name);
		entity.setTexture(texture);
		entity.setSpeech(speech);
		entity.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
		entity.setTheoricalPosition(new Vec3d(player.getPosX(), player.getPosY(), player.getPosZ()));
		player.getEntityWorld().addEntity(entity);
		return 0;
	}
}
