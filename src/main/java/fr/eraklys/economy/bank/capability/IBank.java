package fr.eraklys.economy.bank.capability;

import javax.annotation.Nonnull;

import fr.eraklys.inventory.InventoryStackHolder;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

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
}
