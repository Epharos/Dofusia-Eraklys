package fr.eraklys.economy.bank;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import net.minecraftforge.fml.network.NetworkHooks;

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
			
			NetworkHooks.openGui(context.getSender(), new INamedContainerProvider()
					{
						public Container createMenu(int windowID, PlayerInventory inventory,
								PlayerEntity player) 
						{
							return new ContainerBank(player, windowID);
						}

						public ITextComponent getDisplayName() 
						{
							return null;
						}
					});
		});
	}
}
