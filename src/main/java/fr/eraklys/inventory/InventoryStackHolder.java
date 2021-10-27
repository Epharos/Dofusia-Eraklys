package fr.eraklys.inventory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import fr.eraklys.Eraklys;
import fr.eraklys.utils.ItemStackUtil;
import fr.eraklys.utils.ListedMap;
import fr.eraklys.utils.ListedMap.Entry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class InventoryStackHolder implements IItemHandler, IItemHandlerModifiable
{
	protected ListedMap<ComparableItemStack, Integer> stackMapping = new ListedMap<ComparableItemStack, Integer>();
	protected List<QueuedItem> enqueuedItems = new ArrayList<QueuedItem>();
	
	public int getSize()
	{
		return this.getMapping().size();
	}
	
	public void addStack(@Nonnull ItemStack proto, int amount, PlayerEntity player)
	{		
		if(amount <= 0 || proto == ItemStack.EMPTY || proto.getItem() == Items.AIR)
			return;
		
		ItemStack stack = ItemStackUtil.getPrototype(proto);
		Integer i = this.getMapping().get(new ComparableItemStack(stack));
		
		if(i != null)
		{
			this.getMapping().put(new ComparableItemStack(stack), i + amount);
		}
		else
		{
			this.getMapping().put(new ComparableItemStack(stack), amount);
		}
	}
	
	public boolean removeStack(@Nonnull ItemStack proto, int amount, PlayerEntity player, boolean simulate)
	{
		ItemStack stack = ItemStackUtil.getPrototype(proto);
		Integer i = this.getMapping().get(new ComparableItemStack(stack));
		
		if(i != null)
		{
			int realAmount = Math.min(i, amount);
			
			if(!simulate)
			{
				ComparableItemStack cis = new ComparableItemStack(stack);
				this.getMapping().put(cis, i - realAmount);
				
				if(this.getMapping().get(cis) <= 0)
				{
					this.getMapping().remove(cis);
				}
			}
			
			return amount == realAmount;
		}
		
		return false;
	}
	
	public int getCount(ItemStack stack)
	{
		return this.getMapping().get(new ComparableItemStack(stack));
	}
	
	public ListedMap<ComparableItemStack, Integer> getMapping()
	{
		return this.stackMapping;
	}
	
	public boolean canAddStack(@Nonnull ItemStack stack)
	{
		return true;
	}
	
	public boolean hasQueue()
	{
		return this.enqueuedItems.size() > 0;
	}
	
	public CompoundNBT serializeNBT(PlayerEntity e) 
	{
		ListNBT listnbt = new ListNBT();
		
		for(Entry<ComparableItemStack, Integer> entry : this.getMapping().entryList())
		{
			CompoundNBT cnbt = new CompoundNBT();
			entry.getKey().getStack().write(cnbt);
			cnbt.putInt("Quantity", entry.getValue());
			listnbt.add(cnbt);
		}
		
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("ItemList", listnbt);
		
		return nbt;
	}

	public void deserializeNBT(CompoundNBT nbt, PlayerEntity e) 
	{
		ListNBT listnbt = nbt.getList("ItemList", Constants.NBT.TAG_COMPOUND);
		
		for(int i = 0 ; i < listnbt.size() ; i++)
		{
			CompoundNBT cnbt = listnbt.getCompound(i);
			ItemStack stack = ItemStack.read(cnbt);
			int k = cnbt.getInt("Quantity");
			this.addStack(stack, k, e);
		}
	}
	
	public final class QueuedItem
	{
		public ItemStack stack;
		public int quantity;
		public boolean add;
		
		public QueuedItem(ItemStack is, int q, boolean f)
		{
			this.stack = is;
			this.quantity = q;
			this.add = f;
		}
	}
	
	//------- DEPRECATED -------
	
	@Deprecated
	public void setStackInSlot(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Deprecated
	public int getSlots() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Deprecated
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public int getSlotLimit(int slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Deprecated
	public boolean isItemValid(int slot, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}
}
