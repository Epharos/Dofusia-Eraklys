package fr.eraklys.economy.bank;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import fr.eraklys.inventory.ComparableItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketUpdateBankInventory 
{
	private ItemStack stack;
	private int count;
	private boolean add = false;
	
	public PacketUpdateBankInventory()
	{
		
	}
	
	public PacketUpdateBankInventory(ItemStack s, int c, boolean a)
	{
		this.stack = s;
		this.count = c;
		this.add = a;
	}
	
	public static void write(PacketUpdateBankInventory packet, PacketBuffer buffer)
	{
		buffer.writeItemStack(packet.stack);
		buffer.writeInt(packet.count);
		buffer.writeBoolean(packet.add);
	}
	
	public static PacketUpdateBankInventory read(PacketBuffer buffer)
	{
		return new PacketUpdateBankInventory(buffer.readItemStack(), buffer.readInt(), buffer.readBoolean());
	}
	
	public static void handle(PacketUpdateBankInventory packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleServer(PacketUpdateBankInventory packet, Context context) 
	{
		if(packet.add) //if we add items from the player inventory to the bank
		{
			context.getSender().getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
				if(cap.getInventory().getMapping().get(new ComparableItemStack(packet.stack)) < packet.count)
					return;
				
				cap.removeStack(packet.stack, packet.count, false);
			});
			
			context.getSender().getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
				if(packet.add)
				{
					cap.putStack(packet.stack, packet.count);
				}
			});
		}
		else
		{
			context.getSender().getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
				if(cap.getBankInventory().getMapping().get(new ComparableItemStack(packet.stack)) < packet.count)
					return;
				
				cap.removeStack(packet.stack, packet.count, false);
			});
			
			context.getSender().getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
				cap.putStack(packet.stack, packet.count);
			});
		}
	}
}
