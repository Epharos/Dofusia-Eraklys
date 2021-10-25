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

	public static enum Jobs
	{
		LUMBERJACK(0, "lumberjack"),
		ALCHEMIST(1, "alchemist"),
		FARMER(2, "farmer"),
		MINER(3, "miner"),
		FISHMAN(4, "fisherman"),
		NONE(-1, "none");

		public int index;
		public String name;

		Jobs(int index, String name) 
		{
			this.index = index;
			this.name = name;
		}

		public static Jobs byIndex(int index)
		{
			switch(index)
			{
			case 0:
				return LUMBERJACK;
			case 1:
				return ALCHEMIST;
			case 2:
				return FARMER;
			case 3:
				return MINER;
			default:
				return NONE;
			}
		}
		
		public static String[] names()
		{
			return new String[] {LUMBERJACK.name(), ALCHEMIST.name(), FARMER.name(), MINER.name()};
		}
		
		public static Jobs fromString(String str)
		{
			try
			{
				return Jobs.valueOf(str.toUpperCase());
			}
			catch (IllegalArgumentException e)
			{
				return Jobs.NONE;
			}
		}
		
	}
	
	public static enum XPCategories
	{
		CRAFTING(false, 0),
		SMELTING(false, 1),
		BREAKING(false, 2),
		HARVESTING(false, 3),

		XP(true, 0),
		UNLOCK(true, 1);


		public final boolean isCategory;
		public final int index;
		XPCategories(boolean isCategory, int in)
		{
			this.isCategory = isCategory;
			this.index = in;
		}
		
		public static XPCategories[] getXPValues()
		{
			return new XPCategories[] {CRAFTING, SMELTING, BREAKING, HARVESTING};
		}

	}
}