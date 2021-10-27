package fr.eraklys.jobs.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class PlayerJobsStorage implements Capability.IStorage<IPlayerJobs>
{
	@CapabilityInject(IPlayerJobs.class)
    public static final Capability<IPlayerJobs> PLAYER_JOBS_CAPABILITY = null;
	
	@Override
	public INBT writeNBT(Capability<IPlayerJobs> capability, IPlayerJobs instance, Direction side) 
	{
		CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("farmerExperience", instance.getFarmerExperience());
        nbt.putInt("minerExperience", instance.getMinerExperience());
        nbt.putInt("lumberjackExperience", instance.getLumberjackExperience());
        nbt.putInt("alchemistExperience", instance.getAlchemistExperience());
        return nbt;
	}

	@Override
	public void readNBT(Capability<IPlayerJobs> capability, IPlayerJobs instance, Direction side, INBT nbt)
	{
		if (nbt instanceof CompoundNBT) 
        {
        	CompoundNBT nbt1 = (CompoundNBT) nbt;
            instance.setFarmerExperience(nbt1.getInt("farmerExperience"));
            instance.setMinerExperience(nbt1.getInt("minerExperience"));
            instance.setLumberjackExperience(nbt1.getInt("lumberjackExperience"));
            instance.setAlchemistExperience(nbt1.getInt("achemistExperience"));
        }
	}
}
