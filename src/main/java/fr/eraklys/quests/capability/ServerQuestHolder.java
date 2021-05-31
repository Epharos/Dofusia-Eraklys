package fr.eraklys.quests.capability;

import net.minecraft.entity.player.PlayerEntity;

public class ServerQuestHolder extends QuestHolder 
{
	private PlayerEntity player;
	
	public ServerQuestHolder(PlayerEntity player)
	{
		this.player = player;
	}
	
	public PlayerEntity getPlayer() 
	{
		return this.player;
	}
}
