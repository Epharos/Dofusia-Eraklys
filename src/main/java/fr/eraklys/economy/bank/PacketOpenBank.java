package fr.eraklys.economy.bank;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketOpenBank 
{
	public PacketOpenBank()
	{
		
	}
	
	public static void write(PacketOpenBank packet, PacketBuffer buffer)
	{
		
	}
	
	public static PacketOpenBank read(PacketBuffer buffer)
	{
		return new PacketOpenBank();
	}
	
	public static void handle(PacketOpenBank packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleServer(PacketOpenBank packet, Context context) 
	{
		context.getSender().getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
			cap.synchronizeClient();
		});
	}
}
