package fr.eraklys.screen.menu.entry;

import fr.eraklys.player.inventory.ScreenInventory;
import fr.eraklys.screen.menu.Menu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;

public class HotbarEntry extends MenuEntry 
{
	Item item;
	
	public HotbarEntry(Menu menu, Item item) 
	{
		super(I18n.format("menu.inventory.addtohotbar"), menu);
		this.item = item;
	}

	@SuppressWarnings("resource")
	public void execute() 
	{
		ScreenInventory inventory = (ScreenInventory)Minecraft.getInstance().currentScreen;
		inventory.hotbarMode = true;
		inventory.selectedItem = item;
	}
}
