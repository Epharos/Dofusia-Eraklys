package fr.eraklys.player.inventory.capability;

import fr.eraklys.Eraklys;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class DefaultInventoryPlayerStorage implements Capability.IStorage<IInventoryPlayer> 
{
	public INBT writeNBT(Capability<IInventoryPlayer> capability, IInventoryPlayer instance, Direction side) 
	{
		return instance.getInventory().serializeNBT(instance.getPlayer());
	}

	public void readNBT(Capability<IInventoryPlayer> capability, IInventoryPlayer instance, Direction side, INBT nbt) 
	{
		if(nbt instanceof CompoundNBT)
			instance.getInventory().deserializeNBT((CompoundNBT) nbt, instance.getPlayer());
		else
			Eraklys.LOGGER.error("Erreur en chargeant l'inventaire d'un joueur ! (" + instance.getPlayer().getUniqueID() + ")");
	}
}
