package fr.eraklys.inventory.slots;

import javax.annotation.Nonnull;

import fr.eraklys.inventory.StuffType;
import fr.eraklys.player.inventory.ScreenInventory;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StuffSlot extends Slot implements IRenderSlot
{
	private static Inventory emptyInventory = new Inventory(0);
	private ItemStack stack;
	private StuffType type;
	
	public StuffSlot(int index, int xPosition, int yPosition, @Nonnull ItemStack stack, StuffType st) 
	{
		super(emptyInventory, index, xPosition, yPosition);
		this.stack = ItemStackUtil.getPrototype(stack);
		this.type = st;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderSlot(Screen screen) 
	{
		screen.getMinecraft().getTextureManager().bindTexture(ScreenInventory.texture);
		
		int i = 172, j = 0;
		
		switch(type)
		{
			case HEAD :
				i = 140;
				j = 80;
				break;
			case BODY :
				i = 140;
				j = 63;
				break;
			case NECK :
				i = 140;
				j = 32;
				break;
			case PET :
				//TODO
				break;
			case RING :
				i = 140;
				j = 48;
				break;
			case SHIELD :
				i = 140;
				j = 95;
				break;
			case WEAPON :
				i = 156;
				j = 0;
				break;
			case BELT :
				i = 140;
				j = 16;
				break;
			case BOOTS :
				i = 140;
				j = 0;
				break;
			case RELIC :
				i = 155;
				j = 16;
				break;
			default :
				break;
		}
		
		if(!this.getHasStack()) screen.blit(this.xPos, this.yPos, i, j, 16, 16);
	}
	
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