package fr.eraklys.player.inventory.capability;

import net.minecraft.entity.player.PlayerEntity;

public class ServerInventoryPlayerHolder extends InventoryPlayerHolder
{
	private PlayerEntity player;
	
	public ServerInventoryPlayerHolder(PlayerEntity e)
	{
		this.player = e;
	}
	
	public PlayerEntity getPlayer() 
	{
		return this.player;
	}
}
