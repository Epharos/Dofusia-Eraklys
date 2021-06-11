package fr.eraklys.economy.bank;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PacketBankMoney 
{
	private int money = 0;
	
	public PacketBankMoney()
	{
		
	}
	
	public PacketBankMoney(int i)
	{
		this.money = i;
	}
	
	public static void write(PacketBankMoney packet, PacketBuffer buffer)
	{
		buffer.writeInt(packet.money);
	}
	
	public static PacketBankMoney read(PacketBuffer buffer)
	{
		return new PacketBankMoney(buffer.readInt());
	}
	
	public static void handle(PacketBankMoney packet, Supplier<NetworkEvent.Context> context)
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
	private static void handleClient(PacketBankMoney packet, Context context) 
	{
		Minecraft.getInstance().player.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
			cap.setMoney(packet.money);
		});
	}
}
