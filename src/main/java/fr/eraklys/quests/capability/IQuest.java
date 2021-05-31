package fr.eraklys.quests.capability;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.eraklys.Eraklys;
import fr.eraklys.quests.PacketSendQuest;
import fr.eraklys.quests.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;

public interface IQuest 
{
	public List<Quest> getQueue();
	public PlayerEntity getPlayer();
	public List<Quest> getQuests();
	
	public default void addQuest(Quest quest)
	{
		this.addQuest(quest, false);
	}
	
	public default void addQuest(Quest quest, boolean print)
	{
		this.getQuests().add(quest);
		
		if(print)
			this.getPlayer().sendMessage(new TranslationTextComponent("quest.added", Eraklys.TRANSLATION.getTranslation(this.getPlayer(), quest.getName())));
		
		DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> this.syncPlayer(quest));
	}
	
	public default boolean hasQuest(int id)
	{
		for(Quest quest : this.getQuests())
		{
			if(quest.getId() == id)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public default Quest getQuest(int id)
	{
		for(Quest quest : this.getQuests())
		{
			if(quest.getId() == id)
			{
				return quest;
			}
		}
		
		return null;
	}
	
	public default boolean hasQueue()
	{
		return this.getQueue().size() > 0;
	}
	
	public default int countActiveQuests()
	{
		int i = 0;
		
		for(Quest quest : this.getQuests())
		{
			if(!quest.isFinished())
			{
				i++;
			}
		}
		
		return i;
	}
	
	public default int countFinishedQuests()
	{
		return this.getQuests().size() - this.countActiveQuests();
	}
	
	public default List<Quest> getActiveQuests()
	{
		List<Quest> tmp = new ArrayList<>();
		
		this.getQuests().forEach(e -> {
			if(!e.isFinished())
				tmp.add(e);
		});
		
		return tmp;
	}
	
	public default List<Quest> getFinishedQuests()
	{
		List<Quest> tmp = new ArrayList<>();
		
		this.getQuests().forEach(e -> {
			if(e.isFinished())
				tmp.add(e);
		});
		
		return tmp;
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public default void syncPlayer(Quest quest)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)this.getPlayer();
		
		if(player != null)
		{
			if(player.connection != null)
			{
				this.syncQueue();
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayer()), new PacketSendQuest(quest).setPlayer(player));
			}
			else
			{
				this.getQueue().add(quest);
			}
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public default void syncQueue()
	{
		if(this.getQueue().size() > 0)
		{
			for(Iterator<Quest> it = this.getQueue().iterator() ; it.hasNext() ;)
			{
				Quest quest = it.next();
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayer()), new PacketSendQuest(quest).setPlayer((ServerPlayerEntity) this.getPlayer()));
				it.remove();
			}
		}
	}
}
