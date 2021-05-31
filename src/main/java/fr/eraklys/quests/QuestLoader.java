package fr.eraklys.quests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.eraklys.Eraklys;

public class QuestLoader 
{
	private JsonObject quests;
	
	public QuestLoader()
	{
		this.load();
	}
	
	private void load()
	{
		Path speechTranslationPath = Paths.get("quests.json");
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
		
		quests = new Gson().fromJson(translationContent, JsonObject.class);
	}
	
	public void save()
	{
		Path speechTranslationPath = Paths.get("quests.json");
		File file = speechTranslationPath.toFile();
		
		try
		{
			FileWriter fw = new FileWriter(file);
			fw.write(this.quests.toString());
			fw.close();
		}
		catch(Exception e)
		{
			Eraklys.LOGGER.error("Can't write the translations file");
			e.printStackTrace();
		}
	}
	
	public Quest getQuest(int id)
	{
		JsonObject questObj = this.quests.get(String.valueOf(id)).getAsJsonObject();
		Quest quest = new Quest(id, questObj.get("name").getAsString(), questObj.get("desc").getAsString());
		
		JsonObject parts = questObj.get("parts").getAsJsonObject();
		
		for(Entry<String, JsonElement> part : parts.entrySet())
		{
			quest.addPart(this.loadQuestPart(Integer.valueOf(part.getKey()), part.getValue().getAsJsonObject()));
		}
		
		return quest;
	}
	
	public QuestPart loadQuestPart(int id, JsonObject questObjPart)
	{
		QuestPart questPart = new QuestPart(id);
		JsonElement partXP = questObjPart.get("xp");
		JsonElement partMoney = questObjPart.get("money");
		
		if(partXP != null)
			questPart.setXpReward(partXP.getAsInt());
		
		if(partMoney != null)
			questPart.setMoneyReward(partMoney.getAsInt());
		
		JsonObject tasks = questObjPart.get("tasks").getAsJsonObject();
		
		for(Entry<String, JsonElement> task : tasks.entrySet())
		{
			questPart.addTask(this.loadQuestTask(Integer.valueOf(task.getKey()), task.getValue().getAsJsonObject()));
		}
		
		return questPart;
	}
	
	public QuestTask loadQuestTask(int id, JsonObject questObjTask)
	{
		QuestTask questTask = QuestTask.fromKey(questObjTask.get("type").getAsString());
		questTask.setID(id);
		questTask.loadFromJson(questObjTask);
		return questTask;
	}
	
	public QuestTask loadQuestTask(int qid, int pid, int tid)
	{
		JsonObject questObj = this.quests.get(String.valueOf(qid)).getAsJsonObject();
		JsonObject parts = questObj.get("parts").getAsJsonObject();
		JsonObject part = parts.get(String.valueOf(pid)).getAsJsonObject();
		JsonObject tasks = part.get("tasks").getAsJsonObject();
		JsonObject task = tasks.get(String.valueOf(tid)).getAsJsonObject();
		QuestTask questTask = QuestTask.fromKey(task.get("type").getAsString());
		questTask.setID(tid);
		questTask.loadFromJson(task);
		return questTask;
	}
}
