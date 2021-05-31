package fr.eraklys.entities.npc;

import fr.eraklys.entities.EntitiesRegister;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class TalkingNpcEntity extends NpcEntity 
{
	public TalkingNpcEntity(EntityType<? extends NpcEntity> type, World worldIn) 
	{
		super(EntitiesRegister.TALKING_NPC_ENTITY, worldIn);
	}
}
