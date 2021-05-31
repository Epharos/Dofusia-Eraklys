package fr.eraklys.speech;

import java.util.ArrayList;
import java.util.List;

public class SpeechNode 
{
	private final int id;
	private final String content;
	private final List<SpeechResponse> responses = new ArrayList<SpeechResponse>();
	
	public SpeechNode(int i, String c, List<SpeechResponse> sr)
	{
		this.id = i;
		this.content = c;
		
		responses.addAll(sr);
	}

	public String getContent() 
	{
		return content;
	}

	public int getId() 
	{
		return id;
	}
	
	public List<SpeechResponse> getResponses()
	{
		return this.responses;
	}
}
