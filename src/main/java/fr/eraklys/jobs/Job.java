package fr.eraklys.jobs;

public class Job 
{
	public static int levelToExp(int level) 
	{
		if (level <= 30) 
		{
			return (level * 30 + 250) * level;
		}
		if (level <= 60)
		{
			return (level * 50 + 1000) * level;
		}
		if (level <= 90)
		{
			return (level * 80 + 2500) * level;
		}
		if (level <= 120)
		{
			return (level * 110 + 3000) * level;
		}
		if (level <= 160)
		{
			return (level * 150 + 3500) * level;
		}
		if (level <= 180)
		{
			return (level * 170 + 4000) * level;
		}
		return (level * 175 + 4500) * level;
	}
	
	public static int MAX_XP = levelToExp(180);
}