package fr.eraklys.economy.bank.capability;

import fr.eraklys.Eraklys;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class InventoryBankWrapper implements ICapabilitySerializable<INBT>  
{
	private IBank holder;
	private final LazyOptional<IBank> lazyOptional;
	
	public InventoryBankWrapper(IBank hold)
	{
		this.holder = hold;
		this.lazyOptional = LazyOptional.of(() -> this.holder);
	}
	
	public InventoryBankWrapper()
	{
		this(Eraklys.BANK_CAPABILITY.getDefaultInstance());
	}
	
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) 
	{
		return Eraklys.BANK_CAPABILITY.orEmpty(cap, this.lazyOptional);
	}

	public INBT serializeNBT() 
	{
		return Eraklys.BANK_CAPABILITY.writeNBT(this.holder, null);
	}

	public void deserializeNBT(INBT nbt) 
	{
		Eraklys.BANK_CAPABILITY.readNBT(this.holder, null, nbt);
	}
}
