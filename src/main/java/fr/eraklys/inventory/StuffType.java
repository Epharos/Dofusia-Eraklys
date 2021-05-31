package fr.eraklys.inventory;

public enum StuffType 
{
	HEAD("item.stuff.head"),
	BODY("item.stuff.body"),
	RING("item.stuff.ring"),
	NECK("item.stuff.neck"),
	WEAPON("item.stuff.weapon"),
	SHIELD("item.stuff.shield"),
	PET("item.stuff.pet"),
	BELT("item.stuff.belt"),
	BOOTS("item.stuff.boots"),
	RELIC("item.stuff.relic");
	
	private String translation;
	
	StuffType(String translation)
	{
		this.translation = translation;
	}
	
	public String toString()
	{
		return this.translation;
	}
}
