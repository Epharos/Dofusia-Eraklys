package fr.eraklys.jobs.capability;

public interface IPlayerJobs 
{
    int getFarmerExperience();

    void setFarmerExperience(int level);
    
    int getMinerExperience();

    void setMinerExperience(int level);
    
    int getLumberjackExperience();

    void setLumberjackExperience(int level);
    
    int getAlchemistExperience();

    void setAlchemistExperience(int level);
    
    IPlayerJobs get();
    void set(IPlayerJobs skills);
}
