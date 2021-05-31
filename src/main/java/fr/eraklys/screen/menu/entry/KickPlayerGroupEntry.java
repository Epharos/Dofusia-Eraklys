package fr.eraklys.screen.menu.entry;

import fr.eraklys.screen.menu.Menu;
import net.minecraft.client.resources.I18n;

public class KickPlayerGroupEntry extends MenuEntry
{
	public KickPlayerGroupEntry(Menu menu) 
	{
		super(I18n.format("menu.group.kick.player"), menu);
	}

	public void execute() 
	{
		
	}
}
