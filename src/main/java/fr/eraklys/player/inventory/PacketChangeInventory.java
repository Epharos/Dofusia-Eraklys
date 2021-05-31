package fr.eraklys.player.inventory;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketChangeInventory 
{
	private ItemStack proto = null;
	private int amount = 0;
	private boolean add = false;
	
	public PacketChangeInventory()
	{
		
	}
	
	public PacketChangeInventory(ItemStack s, int a, boolean b)
	{
		this.proto = s;
		this.amount = a;
		this.add = b;
	}
	
	public static void write(PacketChangeInventory packet, PacketBuffer buffer)
	{
		buffer.writeItemStack(packet.proto, false);
		buffer.writeInt(packet.amount);
		buffer.writeBoolean(packet.add);
	}
	
	public static PacketChangeInventory read(PacketBuffer buffer)
	{
		return new PacketChangeInventory(buffer.readItemStack(), buffer.readInt(), buffer.readBoolean());
	}
	
	public static void handle(PacketChangeInventory packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketChangeInventory packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
			if(packet.add)
				cap.getInventory().addStack(packet.proto, packet.amount, null);
			else
				cap.getInventory().removeStack(packet.proto, packet.amount, null, false);
		});
	}
}
