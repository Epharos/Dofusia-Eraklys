package fr.eraklys.player.inventory;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateHotbar 
{
	private int itemId;
	private int hotbarPos;
	
	public PacketUpdateHotbar()
	{
		
	}
	
	public PacketUpdateHotbar(Item item, int hp)
	{
		this.itemId = Item.getIdFromItem(item);
		this.hotbarPos = hp;
	}
	
	public static void write(PacketUpdateHotbar packet, PacketBuffer buffer)
	{
		buffer.writeInt(packet.itemId);
		buffer.writeInt(packet.hotbarPos);
	}
	
	public static PacketUpdateHotbar read(PacketBuffer buffer)
	{
		return new PacketUpdateHotbar(Item.getItemById(buffer.readInt()), buffer.readInt());
	}
	
	public static void handle(PacketUpdateHotbar packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() -> {
			if(context.get().getDirection().getReceptionSide() == LogicalSide.SERVER)
			{
				handleServer(packet, context.get());
			}
			else
			{
				handleClient(packet, context.get());
			}
		});
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.DEDICATED_SERVER)
	private static void handleServer(PacketUpdateHotbar packet, Context context) 
	{
		context.getSender().getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
			cap.getInventory().setHotbarItem(Item.getItemById(packet.itemId), packet.hotbarPos, context.getSender());
		});
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	private static void handleClient(PacketUpdateHotbar packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
			cap.getInventory().setHotbarItem(Item.getItemById(packet.itemId), packet.hotbarPos);
		});
	}
}
