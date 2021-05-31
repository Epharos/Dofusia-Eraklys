package fr.eraklys.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemStackUtil 
{
	public static ItemStack getPrototype(ItemStack is)
	{
		return ItemHandlerHelper.copyStackWithSize(is, 1);
	}
}
