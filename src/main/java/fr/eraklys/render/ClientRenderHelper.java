package fr.eraklys.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;

public class ClientRenderHelper 
{
	public static void renderTextureInWorld(MatrixStack matrixStack, ResourceLocation texture, IRenderTypeBuffer bufferIn, float x, float y, float z, float u, float v, float w, float h, int packedLightIn, boolean centered)
	{
		IVertexBuilder vertex = bufferIn.getBuffer(RenderTypeTexture.getTextureType(texture));
		vertex.pos(matrixStack.getLast().getMatrix(), x - (centered ? w / 2.0f : 0.0f), y - (centered ? h / 2.0f : 0.0f), z).tex(0, 0).color(255, 255, 255, 255).normal(matrixStack.getLast().getNormal(), 0.0F, 1.0F, 0.0F).lightmap(packedLightIn).endVertex();
		vertex.pos(matrixStack.getLast().getMatrix(), x - (centered ? w / 2.0f : 0.0f), y + h - (centered ? h / 2.0f : 0.0f), z).tex(0, v).color(255, 255, 255, 255).normal(matrixStack.getLast().getNormal(), 0.0F, 1.0F, 0.0F).lightmap(packedLightIn).endVertex();
		vertex.pos(matrixStack.getLast().getMatrix(), x + w - (centered ? w / 2.0f : 0.0f), y + h - (centered ? h / 2.0f : 0.0f), z).tex(u, v).color(255, 255, 255, 255).normal(matrixStack.getLast().getNormal(), 0.0F, 1.0F, 0.0F).lightmap(packedLightIn).endVertex();
		vertex.pos(matrixStack.getLast().getMatrix(), x + w - (centered ? w / 2.0f : 0.0f), y - (centered ? h / 2.0f : 0.0f), z).tex(u, 0).color(255, 255, 255, 255).normal(matrixStack.getLast().getNormal(), 0.0F, 1.0F, 0.0F).lightmap(packedLightIn).endVertex();		
	}
	
	public static void renderTextureInWorld(MatrixStack matrixStack, ResourceLocation texture, IRenderTypeBuffer bufferIn, float x, float y, float z, float u, float v, float w, float h, int packedLightIn)
	{
		ClientRenderHelper.renderTextureInWorld(matrixStack, texture, bufferIn, x, y, z, u, v, w, h, packedLightIn, false);
	}
}
