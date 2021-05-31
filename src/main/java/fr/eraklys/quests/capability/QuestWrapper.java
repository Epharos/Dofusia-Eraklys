package fr.eraklys.quests.capability;

import fr.eraklys.Eraklys;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class QuestWrapper implements ICapabilitySerializable<INBT>
{
	private IQuest holder;
	private final LazyOptional<IQuest> lazyOptional;
	
	public QuestWrapper(IQuest hold)
	{
		this.holder = hold;
		this.lazyOptional = LazyOptional.of(() -> this.holder);
	}
	
	public QuestWrapper()
	{
		this(Eraklys.QUEST_CAPABILITY.getDefaultInstance());
	}
	
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		return Eraklys.QUEST_CAPABILITY.orEmpty(cap, this.lazyOptional);
	}

	public INBT serializeNBT() 
	{
		return Eraklys.QUEST_CAPABILITY.writeNBT(this.holder, null);
	}

	public void deserializeNBT(INBT nbt) 
	{
		Eraklys.QUEST_CAPABILITY.readNBT(this.holder, null, nbt);
	}
}
