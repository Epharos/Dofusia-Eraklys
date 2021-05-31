package fr.eraklys.speech;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import fr.eraklys.packets.PacketCloseScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.PacketDistributor;

public class PacketResponseFromClient 
{
	public int responseID;
	
	public PacketResponseFromClient()
	{
		
	}
	
	public PacketResponseFromClient(int rid)
	{
		this.responseID = rid;
	}
	
	public static void write(PacketResponseFromClient packet, PacketBuffer buffer)
	{
		buffer.writeInt(packet.responseID);
	}
	
	public static PacketResponseFromClient read(PacketBuffer buffer)
	{
		return new PacketResponseFromClient(buffer.readInt());
	}
	
	public static void handle(PacketResponseFromClient packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() -> {
			if(context.get().getDirection().getReceptionSide() == LogicalSide.SERVER)
			{
				handleServer(packet, context.get());
			}
		});
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.DEDICATED_SERVER)
	private static void handleServer(PacketResponseFromClient packet, Context context) 
	{
		SpeechNode node = null;
		
		for(SpeechResponse sr : Speech.playerTchating.get(context.getSender()).getResponses())
		{
			if(sr.getId() == packet.responseID)
			{
				sr.execute(context.getSender());
				
				if(sr.isClose())
				{
					Speech.playerTchating.remove(context.getSender());
					Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> context.getSender()), new PacketCloseScreen());
					return;
				}
				
				node = Eraklys.SPEECH.speechNodesLoader.getSpeechNode(sr.getNext());
				break;
			}
		}
		
		if(node != null)
		{
			String content = Eraklys.TRANSLATION.getTranslation(context.getSender(), node.getContent());
			List<PacketSendSpeechToClient.PSpeechResponse> srl = new ArrayList<>();
			
			for(SpeechResponse sr : node.getResponses())
			{
				if(sr.trigger(context.getSender()))
				{
					srl.add(new PacketSendSpeechToClient.PSpeechResponse(sr.getId(), Eraklys.TRANSLATION.getTranslation(context.getSender(), sr.getResponse())));
				}
			}
			
			Speech.playerTchating.put(context.getSender(), node);
			Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> context.getSender()), new PacketSendSpeechToClient(new StringTextComponent(content), srl));
		}
	}
}
