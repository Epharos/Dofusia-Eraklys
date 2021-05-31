package fr.eraklys.entities.npc;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NpcModel<T extends NpcEntity> extends BipedModel<T>
{
	public NpcModel(float modelSize) 
	{
		super(RenderType::getEntityTranslucent, modelSize, 0.0F, 64, 64);
	}
}
