package fr.eraklys.quests;

import java.util.ArrayList;
import java.util.List;

import fr.eraklys.Eraklys;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Constants;

public class Quest 
{
	private final int id;
	private List<QuestPart> parts = new ArrayList<>();
	private final String name, description;
	private boolean finished = false;
	
	public Quest(int i, String name, String description)
	{
		this.id = i;
		this.name = name;
		this.description = description;
	}

	public List<QuestPart> getParts() 
	{
		return parts;
	}

	public Quest addPart(QuestPart part) 
	{
		this.parts.add(part);
		return this;
	}
	
	public int currentPart()
	{
		int i = 1;
		
		for(QuestPart part : this.getParts())
		{
			if(part.isDone())
			{
				i++;
			}
			else
			{
				return i;
			}
		}
		
		return i - 1;
	}

	public int getId() 
	{
		return id;
	}

	public String getName() 
	{
		return name;
	}

	public String getDescription() 
	{
		return description;
	}

	public boolean isFinished() 
	{
		return finished;
	}

	public void setFinished(boolean finished) 
	{
		this.finished = finished;
	}
	
	public void checkIfFinished()
	{
		boolean flag = true;
		
		for(QuestPart part : this.getParts())
		{
			if(!part.isDone())
			{
				flag = false;
			}
		}
		
		this.finished = flag;
	}
	
	public QuestPart getPartByID(int id)
	{
		for(QuestPart part : this.getParts())
		{
			if(part.getId() == id)
			{
				return part;
			}
		}
		
		return null;
	}
	
	public void writeNBT(ListNBT list)
	{
		CompoundNBT compound = new CompoundNBT();
		
		compound.putInt("questID", this.getId());
		this.checkIfFinished();
		
		boolean flag = this.isFinished();
		compound.putBoolean("finished", flag);
		
		if(!flag)
		{
			ListNBT partList = new ListNBT();
			
			for(QuestPart part : this.getParts())
			{
				CompoundNBT partCompound = new CompoundNBT();
				part.writeNBT(partCompound);
				partList.add(partCompound);
			}
			
			compound.put("partList", partList);
		}
		
		list.add(compound);
	}
	
	public void readNBT(CompoundNBT compound)
	{
		boolean flag = compound.getBoolean("finished");
		
		if(flag)
		{
			this.setFinished(true);
			
			for(QuestPart part : this.getParts())
			{
				part.setDone();
			}
		}
		else
		{
			this.setFinished(false);
			
			ListNBT partList = compound.getList("partList", Constants.NBT.TAG_COMPOUND);
			
			for(int i = 0 ; i < partList.size() ; i++)
			{
				CompoundNBT partCompound = partList.getCompound(i);
				int partID = partCompound.getInt("partID");
				QuestPart part = this.getPartByID(partID);
				part.readNBT(partCompound);
			}
		}
	}
	
	public static void writePacket(Quest quest, PacketBuffer buffer, ServerPlayerEntity player)
	{
		buffer.writeInt(quest.id);
		buffer.writeString(Eraklys.TRANSLATION.getTranslation(player, quest.name));
		buffer.writeString(Eraklys.TRANSLATION.getTranslation(player, quest.description));
		buffer.writeInt(quest.getParts().size());
		
		for(QuestPart part : quest.getParts())
		{
			QuestPart.writePacket(part, buffer);
		}
	}
	
	public static Quest readPacket(PacketBuffer buffer)
	{
		Quest quest = new Quest(buffer.readInt(), buffer.readString(), buffer.readString());
		
		int n = buffer.readInt();
		
		for(int i = 0 ; i < n ; i++)
		{
			quest.addPart(QuestPart.readPacket(buffer));
		}
		
		return quest;
	}
}
