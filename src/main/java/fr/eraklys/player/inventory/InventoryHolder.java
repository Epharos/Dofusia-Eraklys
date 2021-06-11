package fr.eraklys.player.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import fr.eraklys.Eraklys;
import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.InventoryStackHolder;
import fr.eraklys.utils.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;

public class InventoryHolder extends InventoryStackHolder 
{
	public ItemStack head = ItemStack.EMPTY,
			 body = ItemStack.EMPTY,
			 ring1 = ItemStack.EMPTY,
			 ring2 = ItemStack.EMPTY,
			 neck = ItemStack.EMPTY,
			 belt = ItemStack.EMPTY,
			 weapon = ItemStack.EMPTY,
			 shield = ItemStack.EMPTY,
			 boots = ItemStack.EMPTY,
			 pet = ItemStack.EMPTY;

	public ItemStack[] hotbar = new ItemStack[9];
	public ItemStack[] relics = new ItemStack[5];
	
	protected List<QueueHotbar> enqueuedHotbar = new ArrayList<QueueHotbar>();
	
	public InventoryHolder()
	{
		super();
		for(int i = 0 ; i < hotbar.length ; i++)
			hotbar[i] = ItemStack.EMPTY;
		for(int i = 0 ; i < relics.length ; i++)
			relics[i] = ItemStack.EMPTY;
	}
	
	public void addStack(@Nonnull ItemStack proto, int amount, PlayerEntity player)
	{		
		super.addStack(proto, amount, player);
		
		DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> syncPlayer((ServerPlayerEntity) player, proto, amount, true));
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
					this.getMapping().remove(cis);
				
				DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> syncPlayer((ServerPlayerEntity) player, proto, amount, false));
			}
			
			return amount == realAmount;
		}
		
		return false;
	}
	
	public void setHotbarItem(Item item, int pos, PlayerEntity player, boolean sendPacket)
	{
		this.hotbar[pos] = new ItemStack(item, 1);
		
		if(player != null && sendPacket)
			DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> this.syncHotbar((ServerPlayerEntity) player, pos));
	}
	
	public void setHotbarItem(Item item, int pos, PlayerEntity player)
	{
		this.setHotbarItem(item, pos, player, false);
	}
	
	public void setHotbarItem(Item item, int pos)
	{
		this.setHotbarItem(item, pos, null, false);
	}
	
	public void clearInventory(PlayerEntity player)
	{
		while(this.getMapping().size() > 0)
		{
			final ItemStack proto = ItemStackUtil.getPrototype(this.getMapping().listKeys().get(0).getStack());
			final int amount = this.getMapping().listValues().get(0);
			this.getMapping().remove(this.getMapping().listKeys().get(0));
			DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> syncPlayer((ServerPlayerEntity) player, proto, amount, false));
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void syncHotbar(ServerPlayerEntity player, int pos)
	{
		if(player != null)
		{
			if(player.connection != null)
			{
				this.syncHotbarQueue(player);
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdateHotbar(hotbar[pos].getItem(), pos));
			}
			else
			{
				this.enqueuedHotbar.add(new QueueHotbar(hotbar[pos].getItem(), pos));
			}
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void syncHotbarQueue(ServerPlayerEntity player)
	{
		if(this.enqueuedHotbar.size() > 0)
		{
			for(Iterator<QueueHotbar> it = this.enqueuedHotbar.iterator() ; it.hasNext() ;)
			{
				QueueHotbar qh = it.next();
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdateHotbar(qh.item, qh.pos));
				it.remove();
			}
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void syncPlayer(ServerPlayerEntity player, ItemStack s, int amount, boolean add)
	{
		if(player != null)
		{
			if(player.connection != null)
			{
				this.syncQueue(player);
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketChangeInventory(s, amount, add));
			}
			else
			{
				this.enqueuedItems.add(new QueuedItem(s, amount, add));
			}
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void syncQueue(ServerPlayerEntity player)
	{
		if(this.enqueuedItems.size() > 0)
		{
			for(Iterator<QueuedItem> it = this.enqueuedItems.iterator() ; it.hasNext() ;)
			{
				QueuedItem qi = it.next();
				Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketChangeInventory(qi.stack, qi.quantity, qi.add));
				it.remove();
			}
		}
	}
	
	public CompoundNBT serializeNBT(PlayerEntity e) 
	{		
		CompoundNBT nbt = super.serializeNBT(e);
		ListNBT listnbt = new ListNBT();
		
		for(int i = 0 ; i < this.hotbar.length ; i++)
		{
			CompoundNBT cnbt = new CompoundNBT();
			this.hotbar[i].write(cnbt);
			listnbt.add(cnbt);
		}
		
		nbt.put("Hotbar", listnbt);
		
		return nbt;
	}

	public void deserializeNBT(CompoundNBT nbt, PlayerEntity e) 
	{
		super.deserializeNBT(nbt, e);
		ListNBT listnbt = nbt.getList("Hotbar", Constants.NBT.TAG_COMPOUND);
		
		for(int i = 0 ; i < Math.min(listnbt.size(), hotbar.length) ; i++)
		{
			CompoundNBT cnbt = listnbt.getCompound(i);
			this.setHotbarItem(ItemStack.read(cnbt).getItem(), i, e, true);
		}
	}
	
	public boolean hasHotbarQueue()
	{
		return this.enqueuedHotbar.size() > 0;
	}
	
	private class QueueHotbar
	{
		public Item item;
		public int pos;
		
		public QueueHotbar(Item i, int p)
		{
			this.item = i;
			this.pos = p;
		}
	}
}
