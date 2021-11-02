package fr.eraklys.jobs.capability;

public class PlayerJobsHolder implements IPlayerJobs
{
	protected int farmerExperience;
	protected int minerExperience;
	protected int lumberjackExperience;
	protected int alchemistExperience;

	public PlayerJobsHolder() 
	{
		this.farmerExperience = 0;
		this.minerExperience = 0;
		this.lumberjackExperience = 0;
		this.alchemistExperience = 0;
	}
    
    @Override
    public int getFarmerExperience() 
    {
        return this.farmerExperience;
    }

    @Override
    public void setFarmerExperience(int level) 
    {
        this.farmerExperience = level;
    }

    @Override
    public int getMinerExperience() 
    {
        return this.minerExperience;
    }

    @Override
    public void setMinerExperience(int level) 
    {
        this.minerExperience = level;
    }

    @Override
    public int getLumberjackExperience() 
    {
        return this.lumberjackExperience;
    }

    @Override
    public void setLumberjackExperience(int level) 
    {
        this.lumberjackExperience = level;
    }

    @Override
    public int getAlchemistExperience() 
    {
        return this.alchemistExperience;
    }

    @Override
    public void setAlchemistExperience(int level) 
    {
        this.alchemistExperience = level;
    }
    
    @Override
    public IPlayerJobs get() 
    {
        return this;
    }
    
    @Override
    public void set(IPlayerJobs jobs) 
    {
        setFarmerExperience(jobs.getFarmerExperience());
        setMinerExperience(jobs.getMinerExperience());
        setLumberjackExperience(jobs.getLumberjackExperience());
        setAlchemistExperience(jobs.getAlchemistExperience());
    }
}
