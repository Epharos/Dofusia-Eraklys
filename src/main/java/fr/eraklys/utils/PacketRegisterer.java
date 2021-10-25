package fr.eraklys.utils;

import fr.eraklys.economy.bank.PacketBankMoney;
import fr.eraklys.economy.bank.PacketOpenBank;
import fr.eraklys.economy.bank.PacketSendBankItems;
import fr.eraklys.economy.bank.PacketUpdateBankInventory;
import fr.eraklys.packets.PacketCloseScreen;
import fr.eraklys.player.inventory.PacketChangeInventory;
import fr.eraklys.player.inventory.PacketUpdateHotbar;
import fr.eraklys.player.inventory.PacketUpdateMoney;
import fr.eraklys.quests.PacketSendQuest;
import fr.eraklys.quests.PacketUpdateQuest;
import fr.eraklys.speech.PacketResponseFromClient;
import fr.eraklys.speech.PacketSendSpeechToClient;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketRegisterer 
{
	public static void registerPackets(SimpleChannel channel)
	{
		int i = 0;
		channel.messageBuilder(PacketChangeInventory.class, i++).encoder(PacketChangeInventory::write).decoder(PacketChangeInventory::read).consumer(PacketChangeInventory::handle).add();
		channel.messageBuilder(PacketUpdateHotbar.class, i++).encoder(PacketUpdateHotbar::write).decoder(PacketUpdateHotbar::read).consumer(PacketUpdateHotbar::handle).add();
		channel.messageBuilder(PacketSendSpeechToClient.class, i++).encoder(PacketSendSpeechToClient::write).decoder(PacketSendSpeechToClient::read).consumer(PacketSendSpeechToClient::handle).add();
		channel.messageBuilder(PacketResponseFromClient.class, i++).encoder(PacketResponseFromClient::write).decoder(PacketResponseFromClient::read).consumer(PacketResponseFromClient::handle).add();
		channel.messageBuilder(PacketCloseScreen.class, i++).encoder(PacketCloseScreen::write).decoder(PacketCloseScreen::read).consumer(PacketCloseScreen::handle).add();
		channel.messageBuilder(PacketSendQuest.class, i++).encoder(PacketSendQuest::write).decoder(PacketSendQuest::read).consumer(PacketSendQuest::handle).add();
		channel.messageBuilder(PacketUpdateQuest.class, i++).encoder(PacketUpdateQuest::write).decoder(PacketUpdateQuest::read).consumer(PacketUpdateQuest::handle).add();
		channel.messageBuilder(PacketOpenBank.class, i++).encoder(PacketOpenBank::write).decoder(PacketOpenBank::read).consumer(PacketOpenBank::handle).add();
		channel.messageBuilder(PacketUpdateMoney.class, i++).encoder(PacketUpdateMoney::write).decoder(PacketUpdateMoney::read).consumer(PacketUpdateMoney::handle).add();
		channel.messageBuilder(PacketBankMoney.class, i++).encoder(PacketBankMoney::write).decoder(PacketBankMoney::read).consumer(PacketBankMoney::handle).add();
		channel.messageBuilder(PacketSendBankItems.class, i++).encoder(PacketSendBankItems::write).decoder(PacketSendBankItems::read).consumer(PacketSendBankItems::handle).add();
		channel.messageBuilder(PacketUpdateBankInventory.class, i++).encoder(PacketUpdateBankInventory::write).decoder(PacketUpdateBankInventory::read).consumer(PacketUpdateBankInventory::handle).add();
	}
}
