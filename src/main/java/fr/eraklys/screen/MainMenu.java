package fr.eraklys.screen;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.eraklys.Eraklys;
import fr.eraklys.screen.widget.ClickableText;
import fr.eraklys.utils.FontRendererStringUtil;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WinGameScreen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MainMenu extends Screen
{
	public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation(Eraklys.MODID, "textures/gui/menu/background"));
	@Nullable
	private static final ResourceLocation ERAKLYS_LOGO_TEXTURES = new ResourceLocation(Eraklys.MODID, "textures/gui/menu/logo.png");

	private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);
	private int widthCopyright;
	private int widthCopyrightRest;
	private final boolean showFadeInAnimation;
	private long firstRenderTime;
	int xLogoSize = 1024, yLogoSize = 1024;
	int guiLeft, guiTop;

	public MainMenu() 
	{
		this(false);
	}

	public MainMenu(boolean fadeIn) 
	{
		super(new TranslationTextComponent("narrator.screen.title"));
		this.showFadeInAnimation = fadeIn;
	}

	public static CompletableFuture<Void> loadAsync(TextureManager texMngr, Executor backgroundExecutor)
	{
		return CompletableFuture.allOf(PANORAMA_RESOURCES.loadAsync(texMngr, backgroundExecutor), texMngr.loadAsync(ERAKLYS_LOGO_TEXTURES, backgroundExecutor));
	}

	public boolean isPauseScreen() 
	{
		return false;
	}

	public boolean shouldCloseOnEsc()
	{
		return false;
	}

	protected void init() 
	{
		this.widthCopyright = this.font.getStringWidth("Copyright Mojang AB. Do not distribute!");
		this.widthCopyrightRest = this.width - this.widthCopyright - 2;
		int j = this.height / 4 + 48;
		this.addButton(new Button(this.width / 2 - 150, j + 70 + 12, 68, 20, I18n.format("menu.options"), (p_213096_1_) -> {
			this.minecraft.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
		}));
		this.addButton(new Button(this.width / 2 - 75, j + 70 + 12, 68, 20, I18n.format("menu.quit"), (p_213094_1_) -> {
			this.minecraft.shutdown();
		}));
		this.addSingleplayerMultiplayerButtons(j, 24);
	}

	private void addSingleplayerMultiplayerButtons(int yIn, int rowHeightIn) 
	{
		this.addButton(new Button(this.width / 2 - 150, yIn + 15 + 12, 144, 20, I18n.format("mainmenu.connect"), (p_213094_1_) -> {
			this.minecraft.displayGuiScreen(new ConnectingScreen(this, this.minecraft, new ServerData("Local", "localhost", false)));
		}));
		this.addButton(new Button(this.width / 2 - 150, yIn + 40 + 12, 144, 20, I18n.format("menu.singleplayer"), (p_213089_1_) -> {
			this.minecraft.displayGuiScreen(new WorldSelectionScreen(this));
		}));
	}

	public void render(int p_render_1_, int p_render_2_, float p_render_3_) 
	{
		if (this.firstRenderTime == 0L && this.showFadeInAnimation) 
		{
			this.firstRenderTime = Util.milliTime();
		}

		float f = this.showFadeInAnimation ? (float)(Util.milliTime() - this.firstRenderTime) / 1000.0F : 1.0F;
		fill(0, 0, this.width, this.height, -1);
		this.panorama.render(p_render_3_, MathHelper.clamp(f, 0.0F, 1.0F));
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.showFadeInAnimation ? (float)MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
		blit(0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float f1 = this.showFadeInAnimation ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
		this.fillGradient(182, 65, this.width - 340, this.height - 104, 0xee777777, 0xee333333);
		this.fillGradient(365, 65, this.width - 130, this.height - 104, 0xee777777, 0xee333333);
		int l = MathHelper.ceil(f1 * 255.0F) << 24;
		if ((l & -67108864) != 0) 
		{
			float scale = .1f, inverse = 1f/scale;
			RenderSystem.scalef(scale, scale, scale);
			this.minecraft.getTextureManager().bindTexture(ERAKLYS_LOGO_TEXTURES);
			Screen.blit((int)(((this.width - 265) / 2) * inverse), 620, this.getBlitOffset(), 1.0f, 1.0f, this.xLogoSize, this.yLogoSize, 1024, 1024);
			RenderSystem.scalef(inverse, inverse, inverse);

			this.drawString(this.font, "Eraklys", 2, this.height - ( 10 + 1 * (this.font.FONT_HEIGHT + 1)), 16777215 | l);
			this.drawString(this.font, "Version " + Eraklys.VERSION, 2, this.height - ( 10 + 0 * (this.font.FONT_HEIGHT + 0)), 16777215 | l);
			this.drawString(this.font, "Copyright Eraklys. Do not distribute!", this.widthCopyrightRest + 15, this.height - 20 + 0, 16777215 | l);

			this.drawString(this.font, "Copyright Mojang AB. Do not distribute!", this.widthCopyrightRest, this.height - 10, 16777215 | l);
			if (p_render_1_ > this.widthCopyrightRest && p_render_1_ < this.widthCopyrightRest + this.widthCopyright && p_render_2_ > this.height - 10 && p_render_2_ < this.height) 
			{
				fill(this.widthCopyrightRest, this.height - 1, this.widthCopyrightRest + this.widthCopyright, this.height, 16777215 | l);
				
			}
			this.drawString(this.font, "Patchs et nouveautés v" + Eraklys.VERSION, 370, this.height - ( 279 + 0 * (this.font.FONT_HEIGHT + 0)), 16777215 | l);
			this.drawString(this.font, "Page suivante", 480, this.height - ( 116 + 0 * (this.font.FONT_HEIGHT + 0)), 16777215 | l);

			for(Widget widget : this.buttons)
			{
				widget.setAlpha(f1);
			}

			super.render(p_render_1_, p_render_2_, p_render_3_);
		}
	}

	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) 
	{
		if (super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) 
		{
			return true;
		} 
		else 
		{
			if (p_mouseClicked_1_ > (double)this.widthCopyrightRest && p_mouseClicked_1_ < (double)(this.widthCopyrightRest + this.widthCopyright) && p_mouseClicked_3_ > (double)(this.height - 10) && p_mouseClicked_3_ < (double)this.height) 
			{
				this.minecraft.displayGuiScreen(new WinGameScreen(false, Runnables.doNothing()));
			}

			return false;
		}
	}
}
