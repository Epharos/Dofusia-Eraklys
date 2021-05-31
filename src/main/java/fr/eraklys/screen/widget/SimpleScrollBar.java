package fr.eraklys.screen.widget;

import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;

public class SimpleScrollBar extends Widget
{
	public int scrollingHeight;
	private float scrollValue;
	
	private boolean scrolling = false;
	private int containerHeight;
	
	public SimpleScrollBar(int xIn, int yIn, int scroll, int ch) 
	{
		super(xIn, yIn, 5, 13, "");
		this.scrollingHeight = scroll;
		this.setScrollValue(.0f);
		this.containerHeight = ch - this.getHeight();
	}
	
	public boolean isScrollNeeded()
	{
		return this.containerHeight > this.scrollingHeight;
	}
	
	public void renderButton(int mouseX, int mouseY, float partialTick) 
	{	
		fill(x, y, x + this.getWidth(), y + this.scrollingHeight, 0xff333333);
		if(this.isScrollNeeded()) fill(x, (int) (y + (this.scrollingHeight - this.getHeight()) * this.getScrollValue()), x + this.getWidth(), (int) (y + this.getHeight() + (this.scrollingHeight - this.getHeight()) * this.getScrollValue()), 0xff999999);
	}
	
	public float getScrollValue() 
	{
		return scrollValue;
	}

	public void setScrollValue(float scrollValue) 
	{
		this.scrollValue = MathHelper.clamp(scrollValue, 0.f, 1.f);
	}
	
	public boolean isScrollingHovered(int mouseX, int mouseY)
	{
		return mouseX >= this.x && mouseX <= this.x + this.getWidth() && mouseY >= (y + (this.scrollingHeight - this.getHeight()) * this.getScrollValue()) && mouseY <= (y + this.getHeight() + (this.scrollingHeight - this.getHeight()) * this.getScrollValue());
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int p_mouseClicked_5_) 
	{
		if(this.isScrollingHovered((int)mouseX, (int)mouseY) && this.active)
		{
			this.scrolling = true;
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, p_mouseClicked_5_);
	}
	
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) 
	{
		this.scrolling = false;
		return true;
	}
	
	public boolean mouseDragged(double mouseX, double mouseY, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) 
	{
		if(this.scrolling)
		{
			float f = (float) ((mouseY - (float)this.y) / (float)this.scrollingHeight);
			this.setScrollValue(f);
			return true;
		}
		
		return false;
	}
	
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double scrollAmount) 
	{
		this.setScrollValue((float) (this.getScrollValue() + scrollAmount / ((double)this.containerHeight / 100.0d)));
		return true;
	}
}
