package fr.eraklys.utils;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;

public class FontRendererStringUtil 
{
	@SuppressWarnings("resource")
	public static List<String> splitStringMultiline(int width, String text)
	{
		if(Minecraft.getInstance().fontRenderer.getStringWidth(text) <= width)
		{
			List<String> l = new ArrayList<String>();
			l.add(text);
			return l;
		}
		
		return FontRendererStringUtil.splitStringMultiline(width, text.split(" "));
	}
	
	@SuppressWarnings("resource")
	public static List<String> splitStringMultiline(int width, String[] splitedText)
	{		
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		String temp = "";
		List<String> tr = new ArrayList<String>();
		int i = 0;
		
		for(String word : splitedText)
		{
			if(font.getStringWidth(word) > width)
			{
				throw new RuntimeException("Preventing Stackoverflow : the set width is too small for the text you printed");
			}
			
			if(font.getStringWidth(temp.concat(word)) < width)
			{
				temp = temp.concat(word.concat(" "));
				i++;
			}
			else
			{
				break;
			}
			
			if(i == splitedText.length)
			{
				tr.add(temp);
				return tr;
			}
		}
		
		String[] next = new String[splitedText.length - i];
		
		for(int a = i ; a < splitedText.length ; a++)
		{
			next[a - i] = splitedText[a];
		}
		
		tr.add(temp);
		tr.addAll(FontRendererStringUtil.splitStringMultiline(width, next));
		return tr;
	}
	
	public static void drawScaledString(FontRenderer font, String s, int x, int y, float scale, float zLevel)
	{
		float invertScale = 1.0f / scale;
		boolean flag = font.getBidiFlag();
		font.setBidiFlag(false);
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.pushMatrix();
		RenderSystem.scalef(scale, scale, scale);
		float posX = (float) Math.floor((((float)x + 16.0f - (float)font.getStringWidth(s) * scale) * invertScale));
		float posY = (float) Math.floor((((float)y + 16.0f - 7.0f * scale) * invertScale));
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, (double)(-zLevel));
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        font.renderString(s, posX, posY, 16777215, true, matrixstack.getLast().getMatrix(), irendertypebuffer$impl, true, 0, 15728880);
        irendertypebuffer$impl.finish();
		RenderSystem.popMatrix();
		font.setBidiFlag(flag);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
	}
	
	public static void drawScaledString(FontRenderer font, String s, int x, int y, float scale, float zLevel, int color, boolean shadow)
	{
		float invertScale = 1.0f / scale;
		boolean flag = font.getBidiFlag();
		font.setBidiFlag(false);
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.pushMatrix();
		RenderSystem.scalef(scale, scale, scale);
		float posX = (float) Math.floor((((float)x + 16.0f - (float)font.getStringWidth(s) * scale) * invertScale));
		float posY = (float) Math.floor((((float)y + 16.0f - 7.0f * scale) * invertScale));
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, (double)(-zLevel));
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        font.renderString(s, posX, posY, color, shadow, matrixstack.getLast().getMatrix(), irendertypebuffer$impl, true, 0, 15728880);
        irendertypebuffer$impl.finish();
		RenderSystem.popMatrix();
		font.setBidiFlag(flag);
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
	}
	
	public static String formatMoney(int money)
	{
		String ts = String.valueOf(money);
		
		String tr = "";
		int sep = ts.length() % 3;
		
		for(int i = 0 ; i < ts.length() ; i++)
		{
			tr += ts.charAt(i);
			
			if(sep != 0)
			{
				if((i + 1 + (3 - sep)) % 3 == 0)
				{
					tr += " ";
				}
			}
			else
			{
				if((i + 1) % 3 == 0 && i != 0)
				{
					tr += " ";
				}
			}
		}
		
		return tr;
	}
	
	public static String formatInteger(int i)
	{
		if(i > 1000000000)
		{
			return ((int)Math.floor(i / 1000000000)) + "b";
		}
		
		if(i > 1000000)
		{
			return ((int)Math.floor(i / 1000000)) + "M";
		}
		
		if(i > 1000)
		{
			return ((int)Math.floor(i / 1000)) + "k";
		}
		
		return String.valueOf(i);
	}
}
