package fr.eraklys.speech;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.ServerPlayerEntity;

public class Speech 
{
	public static Map<ServerPlayerEntity, SpeechNode> playerTchating = new HashMap<>();
	public SpeechNodesLoader speechNodesLoader;
	
	public Speech()
	{
		this.speechNodesLoader = new SpeechNodesLoader();
	}
}
