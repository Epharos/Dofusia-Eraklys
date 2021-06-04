package fr.eraklys.economy.bank.capability;

import fr.eraklys.inventory.InventoryStackHolder;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryBankHolder implements IBank
{
	InventoryStackHolder inventory = new InventoryStackHolder();
	
	public InventoryStackHolder getBankInventory() 
	{
		return this.inventory;
	}

	public PlayerEntity getPlayer() 
	{
		return null;
	}

}
