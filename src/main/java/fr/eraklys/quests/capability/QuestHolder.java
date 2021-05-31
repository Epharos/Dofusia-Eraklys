package fr.eraklys.quests.capability;

import java.util.ArrayList;
import java.util.List;

import fr.eraklys.quests.Quest;
import net.minecraft.entity.player.PlayerEntity;

public class QuestHolder implements IQuest 
{
	private List<Quest> questList = new ArrayList<>();
	private List<Quest> queue = new ArrayList<>();
	
	public PlayerEntity getPlayer() 
	{
		return null;
	}

	public List<Quest> getQuests() 
	{
		return this.questList;
	}

	public List<Quest> getQueue() 
	{
		return this.queue;
	}

}
