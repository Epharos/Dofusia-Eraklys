package fr.eraklys.speech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.eraklys.Eraklys;

public class SpeechNodesLoader 
{
	private JsonObject speechNodes;
	
	public SpeechNodesLoader()
	{
		this.load();
	}
	
	private void load()
	{
		Path speechTranslationPath = Paths.get("speechnodes.json");
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
			Eraklys.LOGGER.error("Can't read the speechnodes file");
			e.printStackTrace();
		}
		
		this.speechNodes = new Gson().fromJson(translationContent, JsonObject.class);
	}
	
	public void save()
	{
		Path speechTranslationPath = Paths.get("speechnodes.json");
		File file = speechTranslationPath.toFile();
		
		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write(this.speechNodes.toString());
			fw.close();
		}
		catch(Exception e)
		{
			Eraklys.LOGGER.error("Can't write the speechnodes file");
			e.printStackTrace();
		}
	}
	
	@Nullable
	public SpeechNode getSpeechNode(int id)
	{
		JsonObject speechNode = this.speechNodes.get(String.valueOf(id)).getAsJsonObject();
		
		if(speechNode == null)
		{
			return null;
		}
		
		String content = speechNode.get("speech").getAsString();
		
		return new SpeechNode(id, content, this.getSpeechNodeResponses(speechNode, id));
	}
	
	public List<SpeechResponse> getSpeechNodeResponses(JsonObject speechNode, int id)
	{
		List<SpeechResponse> responses = new ArrayList<SpeechResponse>();
		JsonObject answers = speechNode.get("answers").getAsJsonObject();
		
		for(Entry<String, JsonElement> entry : answers.entrySet())
		{
			JsonObject answer = entry.getValue().getAsJsonObject();
			
			String content = answer.get("content").getAsString();
			JsonElement close = answer.get("close");
			JsonElement next = answer.get("next");
			JsonElement action = answer.get("action");
			JsonElement trigger = answer.get("trigger");
			
			SpeechResponse spr = new SpeechResponse(Integer.valueOf(entry.getKey()), content);
			
			if(close != null)
			{
				boolean flag = close.getAsBoolean();
				
				if(flag) 
					spr.setClose();
			}
			
			if(next != null)
			{
				int nextID = next.getAsInt();
				
				if(nextID != id)
				{
					spr.setNext(nextID);
				}
			}
			
			if(action != null)
			{
				this.addActions(spr, action.getAsJsonObject());
			}
			
			if(trigger != null)
			{
				this.addTriggers(spr, trigger.getAsJsonObject());
			}
			
			responses.add(spr);
		}
		
		return responses;
	}
	
	public void addActions(SpeechResponse response, JsonObject actions)
	{
		JsonElement giveQuest = actions.get("questgiving");
		
		if(giveQuest != null)
		{
			response.addAction(player -> 
			{
				player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> 
				{
					cap.addQuest(Eraklys.QUEST.getQuest(giveQuest.getAsInt()), true);
				});
			});
		}
	}
	
	public void addTriggers(SpeechResponse response, JsonObject trigger)
	{
		JsonElement doesntHaveQuest = trigger.get("doesnthavequest");
		
		if(doesntHaveQuest != null)
		{
			JsonArray array = doesntHaveQuest.getAsJsonArray();
			
			for(AtomicInteger i = new AtomicInteger(0) ; i.get() < array.size() ; i.set(i.get() + 1))
			{
				response.addTrigger(player -> 
				{
					AtomicBoolean flag = new AtomicBoolean(true);
					
					player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> 
					{
						flag.set(!cap.hasQuest(array.get(i.get() - 1).getAsInt()));
					});
					
					return flag.get();
				});
			}
		}
	}
}
