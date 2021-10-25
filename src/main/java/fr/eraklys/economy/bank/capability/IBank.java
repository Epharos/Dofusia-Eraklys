package fr.eraklys.economy.bank.capability;

import javax.annotation.Nonnull;

import fr.eraklys.Eraklys;
import fr.eraklys.economy.bank.PacketBankMoney;
import fr.eraklys.economy.bank.PacketOpenBank;
import fr.eraklys.economy.bank.PacketSendBankItems;
import fr.eraklys.economy.bank.ScreenBank;
import fr.eraklys.inventory.InventoryStackHolder;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public interface IBank 
{
	public InventoryStackHolder getBankInventory();
	public PlayerEntity getPlayer();
	
	default void putStack(@Nonnull ItemStack stack, int amount)
	{
		this.getBankInventory().addStack(ItemStackUtil.getPrototype(stack), amount, this.getPlayer());
	}
	
	default void removeStack(@Nonnull ItemStack prototype, int amount, boolean flag)
	{
		this.getBankInventory().removeStack(ItemStackUtil.getPrototype(prototype), amount, this.getPlayer(), flag);
	}
	
	default int getBankPrice()
	{
		return (int)(Math.floor(this.getBankInventory().getSize() * 1.2d));
	}
	
	public int getMoney();
	public int setMoney(int value);
	
	default void addMoney(int value)
	{
		if(value < 0)
			return;
		
		this.setMoney(this.getMoney() + value);
	}
	
	default boolean removeMoney(int value)
	{
		if(value < 0)
			return false;
		
		if(this.getMoney() < value)
			return false;
		
		this.setMoney(this.getMoney() - value);
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	default void openBank()
	{
		while(this.getBankInventory().getMapping().size() > 0) //delete the client-side-stocked inventory 
		{
			this.getBankInventory().getMapping().remove(this.getBankInventory().getMapping().listKeys().get(0));
		}
		
		Eraklys.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PacketOpenBank());
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	default void synchronizeClient()
	{
		Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayer()), new PacketBankMoney(this.getMoney()));
		
		for(int i = 0 ; i < this.getBankInventory().getSize() ; i++)
		{
			Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayer()), new PacketSendBankItems(this.getBankInventory().getMapping().listKeys().get(i).getStack(), this.getBankInventory().getMapping().listValues().get(i), (i == this.getBankInventory().getSize() - 1)));
		}
	}
}
