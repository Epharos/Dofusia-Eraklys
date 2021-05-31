package fr.eraklys.inventory;

import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.item.ItemStack;

public class ComparableItemStack 
{
	private ItemStack stack;
	
	public ComparableItemStack(ItemStack is)
	{
		this.stack = ItemStackUtil.getPrototype(is);
	}
	
	public boolean equals(Object o)
	{
		if(!(o instanceof ComparableItemStack))
			return false;
		
		return ItemStack.areItemStacksEqual(stack, ((ComparableItemStack)o).getStack());
	}
	
	public ItemStack getStack()
	{
		return stack;
	}
	
	public String toString()
	{
		return "CIS : " + getStack().toString();
	}
}
