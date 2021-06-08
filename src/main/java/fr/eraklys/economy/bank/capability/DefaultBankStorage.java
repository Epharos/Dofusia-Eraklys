package fr.eraklys.economy.bank.capability;

import fr.eraklys.Eraklys;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class DefaultBankStorage implements Capability.IStorage<IBank> 
{
	public INBT writeNBT(Capability<IBank> capability, IBank instance, Direction side) 
	{
		CompoundNBT compound = instance.getBankInventory().serializeNBT(instance.getPlayer());
		compound.putInt("money", instance.getMoney());
		return compound;
	}

	public void readNBT(Capability<IBank> capability, IBank instance, Direction side, INBT nbt) {
		if(nbt instanceof CompoundNBT)
		{
			instance.getBankInventory().deserializeNBT((CompoundNBT) nbt, instance.getPlayer());
			instance.setMoney(((CompoundNBT) nbt).getInt("money"));
		}
		else
			Eraklys.LOGGER.error("Erreur en chargeant la banque d'un joueur ! (" + instance.getPlayer().getUniqueID() + ")");
	}
}
