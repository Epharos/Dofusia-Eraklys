package fr.eraklys.quests;

import java.util.ArrayList;
import java.util.List;

import fr.eraklys.utils.ListedMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class QuestPart 
{
	final int id;
	int xpReward = 0, moneyReward = 0;
	ListedMap<ItemStack, Integer> itemsReward = new ListedMap<>();
	List<QuestTask> tasks = new ArrayList<>();
	
	public QuestPart(int i)
	{
		this.id = i;
	}

	public int getXpReward() 
	{
		return xpReward;
	}

	public QuestPart setXpReward(int xpReward) 
	{
		this.xpReward = xpReward;
		return this;
	}

	public int getMoneyReward() 
	{
		return moneyReward;
	}

	public QuestPart setMoneyReward(int moneyReward) 
	{
		this.moneyReward = moneyReward;
		return this;
	}

	public ListedMap<ItemStack, Integer> getItemsReward() 
	{
		return itemsReward;
	}

	public QuestPart addItemReward(ItemStack stack, int count)
	{
		this.itemsReward.put(stack, count);
		return this;
	}

	public List<QuestTask> getTasks() 
	{
		return tasks;
	}

	public QuestPart addTask(QuestTask task) 
	{
		this.tasks.add(task);
		return this;
	}

	public int getId() 
	{
		return id;
	}
	
	public boolean isDone()
	{
		for(QuestTask task : this.getTasks())
		{
			if(!task.isDone())
			{
				return false;
			}
		}
		
		return true;
	}
	
	public void setDone()
	{
		for(QuestTask task : this.getTasks())
		{
			task.setDone();
		}
	}
	
	public QuestTask getTaskByID(int id)
	{
		for(QuestTask task : this.getTasks())
		{
			if(task.getID() == id)
			{
				return task;
			}
		}
		
		return null;
	}
	
	public void writeNBT(CompoundNBT compound)
	{
		compound.putInt("partID", this.getId());
		ListNBT tasks = new ListNBT();
		
		for(QuestTask task : this.getTasks())
		{
			CompoundNBT cnbt = new CompoundNBT();
			task.writeNBT(cnbt);
			tasks.add(cnbt);
		}
		
		compound.put("tasks", tasks);
	}
	
	public void readNBT(CompoundNBT compound)
	{
		ListNBT tasks = compound.getList("tasks", Constants.NBT.TAG_COMPOUND);
		
		for(int i = 0 ; i < tasks.size() ; i++)
		{
			CompoundNBT taskCompound = tasks.getCompound(i);
			int taskID = taskCompound.getInt("taskID");
			QuestTask task = this.getTaskByID(taskID);
			task.readNBT(taskCompound);
		}
	}
	
	public static void writePacket(QuestPart part, PacketBuffer buffer)
	{
		buffer.writeInt(part.getId());
		buffer.writeInt(part.getXpReward());
		buffer.writeInt(part.getMoneyReward());
		//TODO ItemStack Rewards
		buffer.writeInt(part.getTasks().size());
		
		for(QuestTask task : part.getTasks())
		{
			QuestTask.writePacket(task, buffer);
		}
	}

	public static QuestPart readPacket(PacketBuffer buffer) 
	{
		QuestPart part = new QuestPart(buffer.readInt());
		part.setXpReward(buffer.readInt());
		part.setMoneyReward(buffer.readInt());
		//TODO ItemStack Rewards
		
		int n = buffer.readInt();
		
		for(int i = 0 ; i < n ; i++)
		{
			part.addTask(QuestTask.readPacket(buffer));
		}
		
		return part;
	}
}
