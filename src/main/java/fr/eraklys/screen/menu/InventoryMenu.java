package fr.eraklys.screen.menu;

import fr.eraklys.screen.menu.entry.HotbarEntry;
import net.minecraft.item.ItemStack;

public class InventoryMenu extends Menu 
{
	private ItemStack stack;
	
	public InventoryMenu(ItemStack item)
	{
		this.setItemStack(item.copy());
		
		this.addEntry(new HotbarEntry(this, stack.getItem()));
	}
	
	public void setItemStack(ItemStack s)
	{
		stack = s;
	}
	
	public String toString()
	{
		return this.getClass().getCanonicalName() + " [" + this.stack + "]";
	}
}
