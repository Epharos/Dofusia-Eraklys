package fr.eraklys.jobs.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerJobsWrapper implements ICapabilitySerializable<INBT>
{
	protected IPlayerJobs playerJobs;
	private final LazyOptional<IPlayerJobs> lazyOptional;

	public PlayerJobsWrapper(IPlayerJobs playerJob)
	{
		this.playerJobs = playerJob;
		this.lazyOptional = LazyOptional.of(() -> this.playerJobs);
	}
	
	public PlayerJobsWrapper()
	{
		this(PlayerJobsStorage.PLAYER_JOBS_CAPABILITY.getDefaultInstance());
	}
	
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		return PlayerJobsStorage.PLAYER_JOBS_CAPABILITY.orEmpty(cap, this.lazyOptional);
	}

	public INBT serializeNBT() 
	{
		return PlayerJobsStorage.PLAYER_JOBS_CAPABILITY.writeNBT(this.playerJobs, null);
	}

	public void deserializeNBT(INBT nbt) 
	{
		PlayerJobsStorage.PLAYER_JOBS_CAPABILITY.readNBT(this.playerJobs, null, nbt);
	}
}
