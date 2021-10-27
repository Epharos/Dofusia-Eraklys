package fr.eraklys.jobs;

import net.minecraft.item.ItemStack;

public class Job 
{
    final int id;
    final String name;
    ItemStack icon = ItemStack.EMPTY;

    public Job (int var1, String var2)
    {
        id = var1;
        name = var2;
    }

    public int getId()
    {
        return this.id;
    }

    public Job setIcon(ItemStack stack)
    {
      icon = stack;
      return this;
    }

    public String getName() 
    {
        return this.name;
    }

    public ItemStack getIcon()
    {
      return this.icon;
    }
}