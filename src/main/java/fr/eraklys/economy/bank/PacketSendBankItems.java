package fr.eraklys.economy.bank;

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

public class PacketSendBankItems 
{
	private ItemStack proto = null;
	private int amount = 0;
	private boolean last = false;
	
	public PacketSendBankItems()
	{
		
	}
	
	public PacketSendBankItems(ItemStack s, int a, boolean l)
	{
		this.proto = s;
		this.amount = a;
		this.last = l;
	}
	
	public static void write(PacketSendBankItems packet, PacketBuffer buffer)
	{
		buffer.writeItemStack(packet.proto, false);
		buffer.writeInt(packet.amount);
		buffer.writeBoolean(packet.last);
	}
	
	public static PacketSendBankItems read(PacketBuffer buffer)
	{
		return new PacketSendBankItems(buffer.readItemStack(), buffer.readInt(), buffer.readBoolean());
	}
	
	public static void handle(PacketSendBankItems packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketSendBankItems packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
			cap.getBankInventory().addStack(packet.proto, packet.amount, null);
			
			if(packet.last)
			{
				Minecraft m = Minecraft.getInstance();
				
				if(m.currentScreen.getClass() == ScreenBank.class)
				{
					((ScreenBank)m.currentScreen).getContainer().resetBankContent();
				}
			}
		});
	}
}
