package fr.eraklys.economy.bank.capability;

import fr.eraklys.inventory.InventoryStackHolder;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryBankHolder implements IBank
{
	InventoryStackHolder inventory = new InventoryStackHolder();
	int money = 0;
	
	public InventoryStackHolder getBankInventory() 
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

	public int setMoney(int value) {
		money = value;
		return money;
	}

}
