package fr.eraklys.speech;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import fr.eraklys.entities.npc.screen.TalkingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketSendSpeechToClient 
{
	public ITextComponent content;
	public List<PSpeechResponse> responses = new ArrayList<PSpeechResponse>();
	
	public PacketSendSpeechToClient()
	{
		
	}
	
	public PacketSendSpeechToClient(ITextComponent c, List<PSpeechResponse> rl)
	{
		this.content = c;
		this.responses.addAll(rl);
	}
	
	public static void write(PacketSendSpeechToClient packet, PacketBuffer buffer)
	{
		buffer.writeTextComponent(packet.content);
		buffer.writeInt(packet.responses.size());
		
		for(PSpeechResponse response : packet.responses)
		{
			buffer.writeInt(response.id);
			buffer.writeString(response.content);
		}
	}
	
	public static PacketSendSpeechToClient read(PacketBuffer buffer)
	{
		ITextComponent content = buffer.readTextComponent();
		int size = buffer.readInt();
		List<PSpeechResponse> rl = new ArrayList<PSpeechResponse>();
		
		for(int i = 0 ; i < size ; i++)
		{
			rl.add(new PSpeechResponse(buffer.readInt(), buffer.readString()));
		}
		
		return new PacketSendSpeechToClient(content, rl);
	}
	
	public static void handle(PacketSendSpeechToClient packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketSendSpeechToClient packet, Context context) 
	{
		if(Minecraft.getInstance().currentScreen instanceof TalkingScreen)
		{
			TalkingScreen talkingScreen = (TalkingScreen) Minecraft.getInstance().currentScreen;
			talkingScreen.clearResponses();
			talkingScreen.setSpeechContent(packet.content);
			talkingScreen.setResponses(packet.responses);
			talkingScreen.resize();
		}
	}
	
	public static class PSpeechResponse
	{
		public int id;
		public String content;
		
		public PSpeechResponse(int i, String s)
		{
			this.id = i;
			this.content = s;
		}
	}
}
