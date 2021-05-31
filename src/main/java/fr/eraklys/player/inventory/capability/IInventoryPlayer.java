package fr.eraklys.player.inventory.capability;

import javax.annotation.Nonnull;

import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.ItemWeight;
import fr.eraklys.player.inventory.InventoryHolder;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IInventoryPlayer 
{
	public InventoryHolder getInventory();
	public PlayerEntity getPlayer();
	
	default void putStack(@Nonnull ItemStack stack, int amount)
	{
		this.getInventory().addStack(ItemStackUtil.getPrototype(stack), amount, this.getPlayer());
	}
	
	default void removeStack(@Nonnull ItemStack prototype, int amount, boolean flag)
	{
		this.getInventory().removeStack(ItemStackUtil.getPrototype(prototype), amount, this.getPlayer(), flag);
	}
	
	default void clearInventory()
	{
		this.getInventory().clearInventory(this.getPlayer());
	}
	
	default int getWeight()
	{
		int weight = 0;
		
		for(ComparableItemStack cis : this.getInventory().getMapping().listKeys())
		{
			weight += ItemWeight.getWeight(cis.getStack().getItem()) * this.getInventory().getMapping().get(cis);
		}
		
		return weight;
	}
}
