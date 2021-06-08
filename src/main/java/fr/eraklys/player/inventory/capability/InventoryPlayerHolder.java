package fr.eraklys.player.inventory.capability;

import fr.eraklys.player.inventory.InventoryHolder;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryPlayerHolder implements IInventoryPlayer 
{
	InventoryHolder inventory = new InventoryHolder();
	int money = 0;
	
	public InventoryHolder getInventory() 
	{
		return this.inventory;
	}

	public PlayerEntity getPlayer() 
	{
		return null;
	}

	public int getMoney() 
	{
		return money;
	}

	@Override
	public int setMoney(int value) 
	{
		money = value;
		return money;
	}
}
