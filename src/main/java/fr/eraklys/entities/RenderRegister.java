package fr.eraklys.entities;

import fr.eraklys.Eraklys;
import fr.eraklys.entities.npc.NpcRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class RenderRegister 
{
	public static void registryEntityRenders()
	{
		Eraklys.LOGGER.debug("REGISTER RENDERER ERAKLYS");
		RenderingRegistry.registerEntityRenderingHandler(EntitiesRegister.TALKING_NPC_ENTITY, NpcRenderer::new);
	}
}
