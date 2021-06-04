package fr.eraklys.player.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.eraklys.Eraklys;
import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.StuffType;
import fr.eraklys.inventory.slots.HotbarSlot;
import fr.eraklys.inventory.slots.InventorySlot;
import fr.eraklys.inventory.slots.StuffSlot;
import fr.eraklys.screen.menu.InventoryMenu;
import fr.eraklys.utils.ListedMap.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerInventory extends Container 
{
	@ObjectHolder(Eraklys.MODID + ":container_inventory")
	public static ContainerType<ContainerInventory> _TYPE;
	public InventoryHolder stackholder;
	public List<ItemStack> stacks = new ArrayList<ItemStack>();
	public float scrollValue = 0.0f;
	
	public ContainerInventory(int windowID, PlayerInventory inventoryPlayer, PacketBuffer data)
	{
		this(inventoryPlayer.player);
	}
	
	protected ContainerInventory(PlayerEntity player) 
	{
		super(ContainerInventory._TYPE, 0);
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> stackholder = cap.getInventory());
		
		if(stackholder != null)
		{		
			for(int i = 0 ; i < 4 ; i++)
			{
				for(int j = 0 ; j < 9 ; j++)
				{
					this.addSlot(new InventorySlot(this.inventorySlots.size(), j * 18 + 141, i * 18 + 8));
				}
			}
			
			for(int i = 0 ; i < 9 ; i++)
			{
				HotbarSlot slot = new HotbarSlot(this.inventorySlots.size(), i * 18 + 141, 95);
				slot.setStack(this.stackholder.hotbar[i]);
				this.addSlot(slot);
			}
			
			for(int i = 0 ; i < 5 ; i++)
			{
				this.addSlot(new StuffSlot(this.inventorySlots.size(), 8, i * 18 + 8, stackholder.relics[i], StuffType.RELIC));
			}
			
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 30, 8 + 0 * 18, stackholder.neck, StuffType.NECK));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 30, 8 + 1 * 18, stackholder.shield, StuffType.SHIELD));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 30, 8 + 2 * 18, stackholder.ring1, StuffType.RING));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 30, 8 + 3 * 18, stackholder.belt, StuffType.BELT));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 30, 8 + 4 * 18, stackholder.boots, StuffType.BOOTS));
			
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 112, 8 + 0 * 18, stackholder.head, StuffType.HEAD));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 112, 8 + 1 * 18, stackholder.body, StuffType.BODY));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 112, 8 + 2 * 18, stackholder.weapon, StuffType.WEAPON));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 112, 8 + 3 * 18, stackholder.ring2, StuffType.RING));
			this.addSlot(new StuffSlot(this.inventorySlots.size(), 112, 8 + 4 * 18, stackholder.pet, StuffType.PET));
			
			
			Iterator<Entry<ComparableItemStack, Integer>> it = this.stackholder.getMapping().entryList().iterator();
			while(it.hasNext())
			{
				ItemStack stack = it.next().getKey().getStack();
				stacks.add(stack);
			}
			
//			for(int i = 0 ; i < 9 * 4 ; i++)
//			{
//				if(this.getSlot(i) instanceof InventorySlot)
//				{
//					if(i < this.stacks.size())
//					{
//						((InventorySlot)this.getSlot(i)).setStack(stacks.get(i) != null ? stacks.get(i) : ItemStack.EMPTY);
//					}
//				}
//					
//			}
			
			this.scrollTo(.0f);
		}
	}
	
	public void scrollTo(float f)
	{
		this.scrollValue = MathHelper.clamp(f, 0.0f, 1.0f);
		
		int a = (this.stacks.size() + 9) / 9 - 4;
		int b = (int)((double)(a * f) + 0.5d);
		
		if(b < 0)
			b = 0;
		
		for(int i = 0 ; i < 4 ; i++)
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

	public boolean canInteractWith(PlayerEntity playerIn) 
	{
		return true;
	}
	
	@SuppressWarnings("resource")
	@Override
	public ItemStack slotClick(int slotId, int click, ClickType clickTypeIn, PlayerEntity player)
	{
		if((this.getSlot(slotId) instanceof HotbarSlot))
		{
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
				HotbarSlot slot = (HotbarSlot) this.getSlot(slotId);
				Screen current = null;
				
				if((current = Minecraft.getInstance().currentScreen) instanceof ScreenInventory)
				{
					if(((ScreenInventory)current).hotbarMode)
					{
						slot.setStack(new ItemStack(((ScreenInventory)current).selectedItem, 1));
						player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> {
							cap.getInventory().setHotbarItem(((ScreenInventory)Minecraft.getInstance().currentScreen).selectedItem, slotId - 36);
							Eraklys.CHANNEL.sendToServer(new PacketUpdateHotbar(((ScreenInventory)Minecraft.getInstance().currentScreen).selectedItem, slotId - 36));
						});
						
						((ScreenInventory)current).selectedItem = null;
						((ScreenInventory)current).hotbarMode = false;
					}
				}
			});
			
			return ItemStack.EMPTY;
		}
		
		if((this.getSlot(slotId) instanceof InventorySlot))
		{
			InventorySlot slot = (InventorySlot) this.getSlot(slotId);
			if(slot.getHasStack())
			{
				if(clickTypeIn == ClickType.PICKUP_ALL) //double left click
				{
					//TODO equip/unequip
				}
				else if(clickTypeIn == ClickType.PICKUP && click == 1) //right click once
				{
					final ItemStack stack = slot.getStack();
					DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
						Screen current = null;
						if((current = Minecraft.getInstance().currentScreen) instanceof ScreenInventory)
						{
							((ScreenInventory)current).setMenu(new InventoryMenu(stack).setPos(((ScreenInventory)current).getGuiLeft() + slot.xPos + 9, ((ScreenInventory)current).getGuiTop() + slot.yPos + 9));
						}
					});
				}
				else if(clickTypeIn == ClickType.QUICK_MOVE && click == 1) //shift + right click
				{
					//TODO drop the object on the ground
					//todo later (later)
				}
			}
	     
			return ItemStack.EMPTY;
		}
		
		if((this.getSlot(slotId) instanceof StuffSlot))
		{
			return ItemStack.EMPTY;
		}
	    
		return ItemStack.EMPTY;
	}
}
