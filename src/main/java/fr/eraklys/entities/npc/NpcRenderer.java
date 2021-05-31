package fr.eraklys.entities.npc;

import com.mojang.blaze3d.matrix.MatrixStack;

import fr.eraklys.Eraklys;
import fr.eraklys.render.ClientRenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NpcRenderer extends LivingRenderer<NpcEntity, NpcModel<NpcEntity>> 
{
	ResourceLocation bedrock = new ResourceLocation(Eraklys.MODID, "textures/gui/quest_giver.png");
	
	public NpcRenderer(EntityRendererManager rendererManager) 
	{
		super(rendererManager, new NpcModel<NpcEntity>(.5f), .5f);
	}

	public ResourceLocation getEntityTexture(NpcEntity entity) 
	{
		return NpcSkinRegistry.skins.get(entity.getTexture());
	}
	
	protected void renderName(NpcEntity entityIn, String displayNameIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) 
	{
		matrixStackIn.push();
		matrixStackIn.translate(0, .12f, 0);
		super.renderName(entityIn, displayNameIn, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.translate(0, .14f, 0);
		matrixStackIn.translate(.0f, (double)entityIn.getHeight() + .8F, 0.0D);
		matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.scale(-0.04F, -0.04F, 0.04F);
		ClientRenderHelper.renderTextureInWorld(matrixStackIn, bedrock, bufferIn, 0, 0, 0, 1.f, 1.f, 16, 16, packedLightIn, true);
        matrixStackIn.pop();
	}
}
