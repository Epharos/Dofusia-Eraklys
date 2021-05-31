package fr.eraklys.entities.npc;

import java.util.HashMap;
import java.util.Map;

import fr.eraklys.Eraklys;
import net.minecraft.util.ResourceLocation;

public class NpcSkinRegistry 
{
	public static Map<String, ResourceLocation> skins = new HashMap<String, ResourceLocation>();
	
	public static void registerSkin(String i, String rl)
	{
		skins.put(i, new ResourceLocation(Eraklys.MODID, "textures/entities/npcs/" + rl + ".png"));
	}
	
	static
	{
		registerSkin("default", "default");
		registerSkin("blacksmith", "blacksmith");
		registerSkin("citizen1", "citizen1");
		registerSkin("citizen2", "citizen2");
		registerSkin("citizen3", "citizen3");
		registerSkin("citizen4", "citizen4");
		registerSkin("knight1", "knight1");
		registerSkin("knight2", "knight2");
		registerSkin("knight3", "knight3");
		registerSkin("ladycitizen1", "ladycitizen1");
		registerSkin("ladycitizen2", "ladycitizen2");
		registerSkin("ladycitizen3", "ladycitizen3");
		registerSkin("ladycitizen4", "ladycitizen4");
		registerSkin("oldcitizen", "oldcitizen");
		registerSkin("thief1", "thief1");
		registerSkin("thief2", "thief2");
		registerSkin("farmer", "farmer");
	}
}
