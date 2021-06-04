package fr.eraklys.economy.bank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.eraklys.Eraklys;
import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.InventoryStackHolder;
import fr.eraklys.inventory.slots.InventorySlot;
import fr.eraklys.player.inventory.InventoryHolder;
import fr.eraklys.utils.ListedMap.Entry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerBank extends Container 
{
	@ObjectHolder(Eraklys.MODID + ":container_bank")
	public static ContainerType<ContainerBank> _TYPE;
	public InventoryStackHolder stackholder;
	public InventoryHolder playerInventory;
	public List<ItemStack> stacks = new ArrayList<ItemStack>(), playerStacks = new ArrayList<>();
	public float inventoryScrollBar = .0f, bankScrollBar = .0f;
	
	public ContainerBank(int windowID, PlayerInventory inventoryPlayer, PacketBuffer data)
	{
		this(inventoryPlayer.player);
	}
	
	protected ContainerBank(PlayerEntity player)
	{
		super(ContainerBank._TYPE, 0);
		
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> playerInventory = cap.getInventory());
		player.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> stackholder = cap.getBankInventory());
		
		if(this.playerInventory != null)
		{
			for(int i = 0 ; i < 8 ; i++)
			{
				for(int j = 0 ; j < 9 ; j++)
				{
					this.addSlot(new InventorySlot(this.inventorySlots.size(), j * 18 + 189, i * 18 + 20));
				}
			}
		}
		
		if(this.stackholder != null)
		{
			for(int i = 0 ; i < 8 ; i++)
			{
				for(int j = 0 ; j < 9 ; j++)
				{
					this.addSlot(new InventorySlot(this.inventorySlots.size(), j * 18 + 8, i * 18 + 20));
				}
			}
		}
		
		Iterator<Entry<ComparableItemStack, Integer>> it = this.stackholder.getMapping().entryList().iterator();
		while(it.hasNext())
		{
			ItemStack stack = it.next().getKey().getStack();
			stacks.add(stack);
		}
		
		it = this.playerInventory.getMapping().entryList().iterator();
		while(it.hasNext())
		{
			ItemStack stack = it.next().getKey().getStack();
			playerStacks.add(stack);
		}
		
		this.scrollBankTo(.0f);
		this.scrollInventoryTo(.0f);
	}
	
	public void scrollBankTo(float f)
	{
		this.bankScrollBar = MathHelper.clamp(f, 0.0f, 1.0f);
		
		int a = (this.stacks.size() + 9) / 9 - 8;
		int b = (int)((double)(a * f) + 0.5d);
		
		if(b < 0)
			b = 0;
		
		for(int i = 0 ; i < 8 ; i++)
		{
			for(int j = 0 ; j < 9 ; j++)
			{
				int k = j + (i + b) * 9;
				if(k >= 0 && k < this.stacks.size())
				{
					if(this.getSlot(i) instanceof InventorySlot)
					{
						((InventorySlot)this.getSlot(i * 9 + j)).setStack(this.stacks.get(k) != null ? stacks.get(k) : ItemStack.EMPTY);
					}
				}
				else
				{
					if(this.getSlot(i) instanceof InventorySlot)
					{
						((InventorySlot)this.getSlot(i * 9 + j)).setStack(ItemStack.EMPTY);
					}
				}
			}
		}
	}
	
	public void scrollInventoryTo(float f)
	{
		this.inventoryScrollBar = MathHelper.clamp(f, 0.0f, 1.0f);
		
		int a = (this.playerStacks.size() + 9) / 9 - 8;
		int b = (int)((double)(a * f) + 0.5d);
		
		if(b < 0)
			b = 0;
		
		for(int i = 0 ; i < 8 ; i++)
		{
			for(int j = 0 ; j < 9 ; j++)
			{
				int k = j + (i + b) * 9;
				if(k >= 0 && k < this.playerStacks.size())
				{
					if(this.getSlot(i) instanceof InventorySlot)
					{
						((InventorySlot)this.getSlot(i * 9 + j)).setStack(this.playerStacks.get(k) != null ? playerStacks.get(k) : ItemStack.EMPTY);
					}
				}
				else
				{
					if(this.getSlot(i) instanceof InventorySlot)
					{
						((InventorySlot)this.getSlot(i * 9 + j)).setStack(ItemStack.EMPTY);
					}
				}
			}
		}
	}

	public boolean canInteractWith(PlayerEntity playerIn) 
	{
		return true;
	}
	
	@Override
	public ItemStack slotClick(int slotId, int click, ClickType clickTypeIn, PlayerEntity player)
	{		
		if((this.getSlot(slotId) instanceof InventorySlot))
		{
			InventorySlot slot = (InventorySlot) this.getSlot(slotId);
			if(slot.getHasStack())
			{
				if(clickTypeIn == ClickType.PICKUP_ALL) //double left click
				{
					//TODO passer un item d'un côté à l'autre
				}
				else if(clickTypeIn == ClickType.PICKUP && click == 1) //right click once
				{
//					final ItemStack stack = slot.getStack();
//					DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
//						Screen current = null;
//						if((current = Minecraft.getInstance().currentScreen) instanceof ScreenInventory)
//						{
//							((ScreenInventory)current).setMenu(new InventoryMenu(stack).setPos(((ScreenInventory)current).getGuiLeft() + slot.xPos + 9, ((ScreenInventory)current).getGuiTop() + slot.yPos + 9));
//						}
//					});
				}
				else if(clickTypeIn == ClickType.QUICK_MOVE) //shift + right click
				{
					//TODO passer tous les items d'un côté à l'autre
				}
			}
	     
			return ItemStack.EMPTY;
		}
	    
		return ItemStack.EMPTY;
	}
}
