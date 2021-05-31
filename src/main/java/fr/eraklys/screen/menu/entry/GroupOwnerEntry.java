package fr.eraklys.screen.menu.entry;

import fr.eraklys.screen.menu.Menu;
import net.minecraft.client.resources.I18n;

public class GroupOwnerEntry extends MenuEntry 
{
	public GroupOwnerEntry(Menu menu) 
	{
		super(I18n.format("menu.group.setowner"), menu);
	}

	public void execute() 
	{
		
	}
}
