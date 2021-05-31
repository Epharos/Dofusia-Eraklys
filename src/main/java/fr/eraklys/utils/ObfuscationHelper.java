package fr.eraklys.utils;

import java.util.HashMap;
import java.util.Map;

public class ObfuscationHelper 
{
	public static Map<String, String> variables = new HashMap<>();
	public static final boolean COMPILATED = false;
	
	private static void addObfuscatedVariable(String clear, String obfuscated)
	{
		ObfuscationHelper.variables.put(clear, obfuscated);
	}
	
	public static String getVariable(String clear)
	{
		if(ObfuscationHelper.COMPILATED)
		{
			return ObfuscationHelper.variables.get(clear);
		}
		
		return clear;
	}
	
	static
	{
		addObfuscatedVariable("language", "field_74363_ab"); //field_71148_cg
	}
}
