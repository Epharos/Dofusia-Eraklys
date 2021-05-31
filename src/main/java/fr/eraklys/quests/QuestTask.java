package fr.eraklys.quests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public abstract class QuestTask 
{
	protected int id;
	
	public abstract boolean isDone();
	public abstract boolean setDone();
	public abstract void writeAdditionnalNBT(CompoundNBT compound);
	public abstract void readNBT(CompoundNBT compound);
	public abstract void loadFromJson(JsonObject object);
	public abstract void writeAdditionnalPacket(PacketBuffer buffer);
	public abstract void readAdditionnalPacket(PacketBuffer buffer);
	public abstract void writeUpdatePacket(PacketBuffer buffer);
	public abstract void readUpdatePacket(PacketBuffer buffer);
	public abstract ITextComponent formatedText();
	public abstract QuestTask copy();
	public abstract void updateFromPacket(QuestTask task);
	
	public static Map<String, Class<? extends QuestTask>> tasksType = new HashMap<>();
	
	public static void writePacket(QuestTask task, PacketBuffer buffer)
	{
		buffer.writeString(QuestTask.fromClass(task.getClass()));
		buffer.writeInt(task.getID());
		task.writeAdditionnalPacket(buffer);
	}
	
	public static QuestTask readPacket(PacketBuffer buffer) 
	{
		QuestTask task = QuestTask.fromKey(buffer.readString());
		task.setID(buffer.readInt());
		task.readAdditionnalPacket(buffer);
		
		return task;
	}
	
	public static QuestTask fromKey(String key)
	{
		try
		{
			return tasksType.get(key).getDeclaredConstructor().newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String fromClass(Class<? extends QuestTask> clazz)
	{
		for(Entry<String, Class<? extends QuestTask>> entry : tasksType.entrySet())
		{
			if(entry.getValue() == clazz)
			{
				return entry.getKey();
			}
		}
		
		return "";
	}
	
	private static void registerTask(String key, Class<? extends QuestTask> value)
	{
		tasksType.put(key, value);
	}
	
	public void writeNBT(CompoundNBT compound)
	{
		compound.putInt("taskID", this.getID());
		this.writeAdditionnalNBT(compound);
	}
	
	public void setID(int i)
	{
		this.id = i;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	static
	{
		registerTask("killmonster", KillMonsterTask.class);
		registerTask("handin", HandInItemTask.class);
	}
}
