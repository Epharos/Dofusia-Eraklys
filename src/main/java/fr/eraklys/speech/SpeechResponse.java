package fr.eraklys.speech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;

public class SpeechResponse 
{
	private final int id;
	private final String response;
	private int next = -1;
	private boolean close = false;
	private List<IAction> actions = new ArrayList<>();
	private List<ITrigger> triggers = new ArrayList<>();
	
	public SpeechResponse(int id, String content)
	{
		this.id = id;
		this.response = content;
	}
	
	public SpeechResponse setClose()
	{
		this.close = true;
		return this;
	}
	
	public SpeechResponse setNext(int nextSpeechID)
	{
		this.next = nextSpeechID;
		return this;
	}
	
	public String getResponse() 
	{
		return response;
	}

	public int getNext() 
	{
		return next;
	}

	public int getId() 
	{
		return id;
	}

	public boolean isClose() 
	{
		return close;
	}
	
	public void execute(PlayerEntity player)
	{
		for(IAction action : this.actions)
		{
			action.execute(player);
		}
	}
	
	public boolean trigger(PlayerEntity player)
	{
		for(ITrigger trigger : this.triggers )
		{
			if(!trigger.trigger(player))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void addAction(IAction action)
	{
		this.actions.add(action);
	}
	
	public void addTrigger(ITrigger trigger)
	{
		this.triggers.add(trigger);
	}

	public static interface IAction
	{
		public void execute(PlayerEntity player);
	}
	
	public static interface ITrigger
	{
		public boolean trigger(PlayerEntity player);
	}
}
