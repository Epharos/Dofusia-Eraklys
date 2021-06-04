package fr.eraklys.utils;

import fr.eraklys.economy.bank.capability.DefaultBankStorage;
import fr.eraklys.economy.bank.capability.IBank;
import fr.eraklys.economy.bank.capability.InventoryBankHolder;
import fr.eraklys.player.inventory.capability.DefaultInventoryPlayerStorage;
import fr.eraklys.player.inventory.capability.IInventoryPlayer;
import fr.eraklys.player.inventory.capability.InventoryPlayerHolder;
import fr.eraklys.quests.capability.DefaultQuestStorage;
import fr.eraklys.quests.capability.IQuest;
import fr.eraklys.quests.capability.QuestHolder;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitiesRegisterer 
{
	public static void registerCapabilities()
	{
		CapabilityManager.INSTANCE.register(IInventoryPlayer.class, new DefaultInventoryPlayerStorage(), InventoryPlayerHolder::new);
		CapabilityManager.INSTANCE.register(IQuest.class, new DefaultQuestStorage(), QuestHolder::new);
		CapabilityManager.INSTANCE.register(IBank.class, new DefaultBankStorage(), InventoryBankHolder::new);
	}
}
