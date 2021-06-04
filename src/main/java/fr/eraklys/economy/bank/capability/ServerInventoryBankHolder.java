package fr.eraklys.economy.bank.capability;

import net.minecraft.entity.player.PlayerEntity;

public class ServerInventoryBankHolder extends InventoryBankHolder
{
	private PlayerEntity player;
	
	public ServerInventoryBankHolder(PlayerEntity e)
	{
		this.player = e;
	}
	
	public PlayerEntity getPlayer()
	{
		return this.player;
	}
}
