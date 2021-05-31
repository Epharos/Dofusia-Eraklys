package fr.eraklys.quests;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateQuest 
{
	public int quest;
	public int part;
	public QuestTask task;
	
	//variables int
	//constructeur avec les objets
	//constructeur avec les ids
	//write qui écrit les id
	//read qui utilise le constructeur id
	//handle qui modifie en fonction des id
	
	public PacketUpdateQuest()
	{
		
	}
	
	public PacketUpdateQuest(int q, int p, QuestTask t)
	{
		this.quest = q;
		this.part = p;
		this.task = t;
	}
	
	public static void write(PacketUpdateQuest packet, PacketBuffer buffer)
	{
		buffer.writeInt(packet.quest);
		buffer.writeInt(packet.part);
		buffer.writeInt(packet.task.getID());
		
		packet.task.writeUpdatePacket(buffer);
	}
	
	@SuppressWarnings("resource")
	public static PacketUpdateQuest read(PacketBuffer buffer)
	{
		PacketUpdateQuest tr = new PacketUpdateQuest();
		
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			tr.quest = buffer.readInt();
			tr.part = buffer.readInt();
			Minecraft.getInstance().player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> {
				QuestTask task = cap.getQuest(tr.quest).getPartByID(tr.part).getTaskByID(buffer.readInt()).copy();
				task.readUpdatePacket(buffer);
				tr.task = task;
			});
		});
		
		return tr;
	}
	
	public static void handle(PacketUpdateQuest packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketUpdateQuest packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> {
			QuestTask task = cap.getQuest(packet.quest).getPartByID(packet.part).getTaskByID(packet.task.getID());
			task.updateFromPacket(packet.task);
			cap.getQuest(packet.quest).checkIfFinished();
		});
	}
}
