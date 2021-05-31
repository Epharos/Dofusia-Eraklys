package fr.eraklys.player.inventory.capability;

import fr.eraklys.player.inventory.InventoryHolder;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryPlayerHolder implements IInventoryPlayer 
{
	InventoryHolder inventory = new InventoryHolder();
	
	public InventoryHolder getInventory() 
	{
		return this.inventory;
	}

	public PlayerEntity getPlayer() 
	{
		return null;
	}
}
