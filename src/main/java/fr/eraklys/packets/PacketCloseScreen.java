package fr.eraklys.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketCloseScreen 
{
	public PacketCloseScreen()
	{
		
	}
	
	public static void write(PacketCloseScreen packet, PacketBuffer buffer)
	{
		
	}
	
	public static PacketCloseScreen read(PacketBuffer buffer)
	{
		return new PacketCloseScreen();
	}
	
	public static void handle(PacketCloseScreen packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketCloseScreen packet, Context context) 
	{
		if(Minecraft.getInstance().currentScreen != null)
		{
			Minecraft.getInstance().currentScreen.onClose();
		}
	}
}
