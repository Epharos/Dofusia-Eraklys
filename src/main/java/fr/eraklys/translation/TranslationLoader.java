package fr.eraklys.translation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.eraklys.Eraklys;
import fr.eraklys.utils.ObfuscationHelper;
import net.minecraft.entity.player.PlayerEntity;

public class TranslationLoader
{
	private JsonObject translations;
	
	public TranslationLoader()
	{
		this.load();
	}
	
	private void load()
	{
		Path speechTranslationPath = Paths.get("translations.json");
		String translationContent = "";
		
		File file = speechTranslationPath.toFile();
		
		try(BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String current = "";
			for( ; (current = br.readLine()) != null ; )
				translationContent += current;
		}
		catch(Exception e)
		{
			Eraklys.LOGGER.error("Can't read the translations file");
			e.printStackTrace();
		}
		
		translations = new Gson().fromJson(translationContent, JsonObject.class);
	}
	
	public void save()
	{
		Path speechTranslationPath = Paths.get("translations.json");
		File file = speechTranslationPath.toFile();
		
		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write(this.translations.toString());
			fw.close();
		}
		catch(Exception e)
		{
			Eraklys.LOGGER.error("Can't write the translations file");
			e.printStackTrace();
		}
	}
	
	public JsonObject getLanguageObject(String lang)
	{
		JsonElement langElm = this.translations.get(lang);
		
		if(langElm != null)
			return langElm.getAsJsonObject();
		
		return null;
	}
	
	public String getTranslation(String lang, String key)
	{
		JsonObject langObj = this.getLanguageObject(lang);
		
		if(langObj != null)
		{
			JsonElement keyElm = langObj.get(key);
			
			if(keyElm != null)
			{
				return keyElm.getAsString();
			}
			
			return langObj.get("key.unknown").getAsString();
		}
		
		return this.getLanguageObject("en_us").get("key.unknown").getAsString();
	}
	
	public String getTranslation(PlayerEntity player, String key)
	{
		String lang = "en_us";
		
		try 
		{
			Field language = player.getClass().getDeclaredField(ObfuscationHelper.getVariable("language"));
			language.setAccessible(true);
			lang = (String) language.get(player);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		Eraklys.LOGGER.debug("lang : " + lang);
		
		return this.getTranslation(lang.toLowerCase(), key);
	}
	
	public void writeKey(String lang, String key, String value)
	{
		JsonObject langObj = this.getLanguageObject(lang);
		
		if(langObj == null)
		{
			Eraklys.LOGGER.error("Couldn't write the key in the translation JSON because the lang doesn't exist");
			return;
		}
		
		langObj.addProperty(key, value);
	}
}
