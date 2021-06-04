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
}
