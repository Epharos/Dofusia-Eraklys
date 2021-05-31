package fr.eraklys.player.inventory.capability;

import fr.eraklys.Eraklys;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class InventoryPlayerWrapper implements ICapabilitySerializable<INBT>  
{
	private IInventoryPlayer holder;
	private final LazyOptional<IInventoryPlayer> lazyOptional;
	
	public InventoryPlayerWrapper(IInventoryPlayer hold)
	{
		this.holder = hold;
		this.lazyOptional = LazyOptional.of(() -> this.holder);
	}
	
	public InventoryPlayerWrapper()
	{
		this(Eraklys.INVENTORY_CAPABILITY.getDefaultInstance());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		return Eraklys.INVENTORY_CAPABILITY.orEmpty(cap, this.lazyOptional);
	}

	@Override
	public INBT serializeNBT() 
	{
		return Eraklys.INVENTORY_CAPABILITY.writeNBT(this.holder, null);
	}

	@Override
	public void deserializeNBT(INBT nbt) 
	{
		Eraklys.INVENTORY_CAPABILITY.readNBT(this.holder, null, nbt);
	}
}
