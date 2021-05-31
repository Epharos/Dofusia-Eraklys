package fr.eraklys.entities;

import fr.eraklys.Eraklys;
import fr.eraklys.entities.npc.TalkingNpcEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;

public class EntitiesRegister 
{
	@SuppressWarnings("unchecked")
	public static EntityType<TalkingNpcEntity> TALKING_NPC_ENTITY = (EntityType<TalkingNpcEntity>) EntityType.Builder.create(TalkingNpcEntity::new, EntityClassification.CREATURE)
			.build(Eraklys.MODID + ":talking_npc")
			.setRegistryName(Eraklys.MODID, "talking_npc");
}
