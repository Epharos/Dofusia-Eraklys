package fr.eraklys.inventory.slots;

import javax.annotation.Nonnull;

import fr.eraklys.player.inventory.ScreenInventory;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class HotbarSlot extends Slot implements IRenderSlot
{
	private static Inventory emptyInventory = new Inventory(0);
	private ItemStack stack;
	
	public HotbarSlot(int index, int xPosition, int yPosition, @Nonnull ItemStack stack) 
	{
		super(emptyInventory, index, xPosition, yPosition);
		this.stack = ItemStackUtil.getPrototype(stack);
	}
	
	public HotbarSlot(int index, int xPosition, int yPosition) 
	{
		this(index, xPosition, yPosition, ItemStack.EMPTY);
	}
	
	@Override
    @Nonnull
    public ItemStack getStack()
    {
        return this.stack != null ? this.stack : ItemStack.EMPTY;
    }
	
	public void setStack(ItemStack stack)
	{
		this.stack = ItemStackUtil.getPrototype(stack);
	}
	
	public void renderSlot(Screen screen) 
	{
		if(screen instanceof ScreenInventory)
		{
			ScreenInventory inventory = (ScreenInventory)screen;
			
			if(inventory.hotbarMode)
			{
				Screen.fill(this.xPos, this.yPos, this.xPos + 16, this.yPos + 16, 0x33ffff00);
			}
		}
		
	}
}
