package fr.eraklys.quests.capability;

import fr.eraklys.Eraklys;
import fr.eraklys.quests.Quest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class DefaultQuestStorage implements Capability.IStorage<IQuest>
{
	public INBT writeNBT(Capability<IQuest> capability, IQuest instance, Direction side) 
	{
		ListNBT list = new ListNBT();
		
		for(Quest quest : instance.getQuests())
		{
			quest.writeNBT(list);
		}
		
		return list;
	}

	public void readNBT(Capability<IQuest> capability, IQuest instance, Direction side, INBT nbt) 
	{
		if(nbt instanceof ListNBT)
		{
			ListNBT list = (ListNBT)nbt;
			
			for(int i = 0 ; i < list.size() ; i++)
			{
				CompoundNBT compound = list.getCompound(i);
				int questID = compound.getInt("questID");
				Quest loadedQuest = Eraklys.QUEST.getQuest(questID);
				loadedQuest.readNBT(compound);
				instance.addQuest(loadedQuest);
			}
		}
	}
}
