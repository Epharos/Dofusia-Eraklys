package fr.eraklys.quests;

import com.google.gson.JsonObject;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class KillMonsterTask extends QuestTask 
{
	public EntityType<?> monsterType;
	public int remaining;
	public int total;
	
	public KillMonsterTask()
	{
		
	}
	
	public KillMonsterTask(EntityType<?> type, int tokill)
	{
		this.monsterType = type;
		this.total = tokill;
		this.remaining = this.total;
	}
	
	public boolean isDone() 
	{
		return this.remaining <= 0;
	}
	
	public EntityType<?> getMonsterType() 
	{
		return monsterType;
	}

	public void setMonsterType(EntityType<?> monsterType) 
	{
		this.monsterType = monsterType;
	}

	public void writeAdditionnalNBT(CompoundNBT compound) 
	{
		compound.putInt("remaining", this.remaining);
	}

	public void readNBT(CompoundNBT compound) 
	{
		this.remaining = compound.getInt("remaining");
	}

	public boolean setDone() 
	{
		this.remaining = 0;
		return this.isDone();
	}

	public void loadFromJson(JsonObject object) 
	{
		total = object.get("total").getAsInt();
		this.remaining = total;
		monsterType = EntityType.byKey(object.get("monster").getAsString()).get();
	}

	public void writeAdditionnalPacket(PacketBuffer buffer) 
	{
		buffer.writeString(EntityType.getKey(this.monsterType).toString());
		buffer.writeInt(this.total);
		buffer.writeInt(this.remaining);
	}

	public void readAdditionnalPacket(PacketBuffer buffer) 
	{
		this.monsterType = EntityType.byKey(buffer.readString()).get();
		this.total = buffer.readInt();
		this.remaining = buffer.readInt();
	}

	public ITextComponent formatedText() 
	{
		ITextComponent tr = new TranslationTextComponent("quest.killmonster", this.total, this.monsterType.getName());
		
		if(!this.isDone())
		{
			tr.appendText(" ");
			tr.appendSibling(new TranslationTextComponent("quest.remaining", this.remaining));
		}
		
		return tr;
	}

	public void writeUpdatePacket(PacketBuffer buffer) 
	{
		buffer.writeInt(this.remaining);
	}

	public void readUpdatePacket(PacketBuffer buffer) 
	{
		this.remaining = buffer.readInt();
	}

	public QuestTask copy() 
	{
		KillMonsterTask task = new KillMonsterTask();
		task.setID(this.getID());
		task.total = this.total;
		task.remaining = this.remaining;
		
		return task;
	}

	public void updateFromPacket(QuestTask task) 
	{
		this.remaining = ((KillMonsterTask)task).remaining;
	}

	public void update(int quantity) 
	{
		this.remaining = Math.max(0, this.remaining - quantity);
	}
}
