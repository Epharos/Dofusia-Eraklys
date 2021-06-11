package fr.eraklys.player.inventory;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateMoney 
{
	private int money = 0;
	
	public PacketUpdateMoney()
	{
		
	}
	
	public PacketUpdateMoney(int i)
	{
		this.money = i;
	}
	
	public static void write(PacketUpdateMoney packet, PacketBuffer buffer)
	{
		buffer.writeInt(packet.money);
	}
	
	public static PacketUpdateMoney read(PacketBuffer buffer)
	{
		return new PacketUpdateMoney(buffer.readInt());
	}
	
	public static void handle(PacketUpdateMoney packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketUpdateMoney packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
			cap.setMoney(packet.money);
		});
	}
}
