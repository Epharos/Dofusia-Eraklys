package fr.eraklys.screen.menu.entry;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.eraklys.screen.MenuableContainerScreen;
import fr.eraklys.screen.menu.Menu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;

public abstract class MenuEntry extends Widget
{
	public final List<MenuEntry> entries = new ArrayList<MenuEntry>();
	protected Menu menuIn;
	
	public MenuEntry(String msg, Menu menu) 
	{
		super(0, 0, 0, 0, msg);
		this.menuIn = menu;
	}		
	
	public void renderButton(int mouseX, int mouseY, float partialTick) 
	{
		this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
	    Minecraft minecraft = Minecraft.getInstance();
	    FontRenderer fontRenderer = minecraft.fontRenderer;
	    RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
	    RenderSystem.enableBlend();
	    RenderSystem.defaultBlendFunc();
	    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		fill(x, y, x + this.getWidth() + 6, y + 13, 0xff666666);
		fill(x + 1, y + 1, x + this.getWidth() + 5, y + 12, !this.isHovered() ? 0xffbbbbbb : 0xff9a9a9a);
			
		RenderSystem.pushMatrix();
		RenderSystem.translatef(0f, 0f, 800f);
		fontRenderer.drawStringWithShadow(I18n.format(this.getMessage()), x + (this.isHovered() ? 4 : 3), y + 3, 0xeeeeee);
		RenderSystem.popMatrix();
		
		this.active = this.menuIn.active;
	}
	
	@SuppressWarnings("resource")
	public void onClick(double p_onClick_1_, double p_onClick_3_) 
	{
		this.execute();
		
		if(Minecraft.getInstance().currentScreen != null)
		{
			if(Minecraft.getInstance().currentScreen instanceof MenuableContainerScreen)
				((MenuableContainerScreen<?>)Minecraft.getInstance().currentScreen).closeMenu();
		}
	}
	
	public abstract void execute();
}
