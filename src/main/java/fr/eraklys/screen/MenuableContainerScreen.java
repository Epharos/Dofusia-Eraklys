package fr.eraklys.screen;

import java.util.Iterator;

import fr.eraklys.screen.menu.Menu;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

public class MenuableContainerScreen<C extends Container> extends ContainerScreen<C> 
{
	public MenuableContainerScreen(C screenContainer, PlayerInventory inv, ITextComponent titleIn) 
	{
		super(screenContainer, inv, titleIn);
	}

	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		
	}
	
	public boolean isMenuActive()
	{
		for(Iterator<Widget> it = this.buttons.iterator() ; it.hasNext() ; )
		{
			Widget w = it.next();
			
			if(w instanceof Menu)
			{
				return ((Menu)w).active;
			}
		}
		
		return false;
	}
	
	public void closeMenu()
	{
		for(Iterator<Widget> it = this.buttons.iterator() ; it.hasNext() ; )
		{
			Widget w = it.next();
			
			if(w instanceof Menu)
			{
				it.remove();
			}
		}
		
		for(Iterator<IGuiEventListener> it = this.children.iterator() ; it.hasNext() ; )
		{
			IGuiEventListener w = it.next();
			
			if(w instanceof Menu)
			{
				it.remove();
			}
		}
	}
	
	public void setMenu(Menu menu)
	{
		this.closeMenu();		
		this.addButton(menu);
	}
}
