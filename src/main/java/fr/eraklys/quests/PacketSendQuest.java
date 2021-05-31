package fr.eraklys.quests;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSendQuest 
{
	public Quest quest;
	public ServerPlayerEntity player;
	
	public PacketSendQuest()
	{
		
	}
	
	public PacketSendQuest(Quest q)
	{
		this.quest = q;
	}
	
	public PacketSendQuest setPlayer(ServerPlayerEntity player)
	{
		this.player = player;
		return this;
	}
	
	public static void write(PacketSendQuest packet, PacketBuffer buffer)
	{
		Quest.writePacket(packet.quest, buffer, packet.player);
	}
	
	public static PacketSendQuest read(PacketBuffer buffer)
	{
		return new PacketSendQuest(Quest.readPacket(buffer));
	}
	
	public static void handle(PacketSendQuest packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() -> {
			if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
			{
				handleClient(packet, context.get());
			}
		});
		context.get().setPacketHandled(true);
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	private static void handleClient(PacketSendQuest packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> {
			cap.addQuest(packet.quest);
		});
	}
}
