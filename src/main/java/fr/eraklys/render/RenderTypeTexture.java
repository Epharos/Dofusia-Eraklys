package fr.eraklys.render;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;

public class RenderTypeTexture extends RenderState
{
	public RenderTypeTexture(String nameIn, Runnable setupTaskIn, Runnable clearTaskIn) 
	{
		super(nameIn, setupTaskIn, clearTaskIn);
	}

	public static final VertexFormat POSITION_TEX_COLOR_NORMAL_LIGHTMAP = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(POSITION_3F).add(TEX_2F).add(COLOR_4UB).add(NORMAL_3B).add(TEX_2SB).add(PADDING_1B).build());

	public static RenderType getTextureType(ResourceLocation location)
	{
		RenderType.State renderTypeState = RenderType.State.getBuilder().texture(new TextureState(location, false, false)).alpha(DEFAULT_ALPHA).transparency(TRANSLUCENT_TRANSPARENCY).diffuseLighting(RenderState.DIFFUSE_LIGHTING_DISABLED).lightmap(LIGHTMAP_ENABLED).build(true);
		return RenderType.makeType("texture", POSITION_TEX_COLOR_NORMAL_LIGHTMAP, 7, 256, true, true, renderTypeState);
	}
}
