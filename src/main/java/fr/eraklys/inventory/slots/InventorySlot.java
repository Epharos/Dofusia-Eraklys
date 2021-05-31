package fr.eraklys.inventory.slots;

import javax.annotation.Nonnull;

import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class InventorySlot extends Slot 
{
	private static Inventory emptyInventory = new Inventory(0);
	private ItemStack stack;
	
	public InventorySlot(int index, int xPosition, int yPosition, @Nonnull ItemStack stack) 
	{
		super(emptyInventory, index, xPosition, yPosition);
		this.stack = ItemStackUtil.getPrototype(stack);
	}
	
	public InventorySlot(int index, int xPosition, int yPosition) 
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
}
