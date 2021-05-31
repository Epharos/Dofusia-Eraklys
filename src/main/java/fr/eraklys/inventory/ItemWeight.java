package fr.eraklys.inventory;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public final class ItemWeight 
{
	public static HashMap<Item, Integer> weights = new HashMap<Item, Integer>();
	
	private static void weight(Item i, int w)
	{
		ItemWeight.weights.put(i, w);
	}
	
	public static int getWeight(Item i)
	{
		return ItemWeight.weights.get(i) != null ? ItemWeight.weights.get(i) : 1;
	}
	
	static
	{
		weight(Items.COBBLESTONE, 10);
	}
}
