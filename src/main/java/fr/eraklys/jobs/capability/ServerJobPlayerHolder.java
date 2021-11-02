package fr.eraklys.jobs.capability;

import net.minecraft.entity.player.PlayerEntity;

public class ServerJobPlayerHolder extends PlayerJobsHolder 
{
private PlayerEntity player;
	
	public ServerJobPlayerHolder(PlayerEntity e)
	{
		this.player = e;
	}
	
	public PlayerEntity getPlayer() 
	{
		return this.player;
	}
}
