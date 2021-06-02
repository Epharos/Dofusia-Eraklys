package fr.eraklys.proxy;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import fr.eraklys.Eraklys;
import fr.eraklys.screen.menu.MainMenu;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.fixes.ChunkPaletteFormat.Direction.Offset;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends Proxy 
{
	public static final KeyBinding showQuests = new KeyBinding("key.quests", GLFW.GLFW_KEY_W, "key.category." + Eraklys.MODID);

	public static boolean drawTitle = true;
	public static boolean drawSplash = true;
	public static boolean drawJavaEd = true;
	public static boolean drawForgeInfo = true;
	public static boolean drawPanorama = false;
	public static Offset title, javaEd, forgeWarn, splash;
	public static boolean folderPack = false;
	public static float splashRotation = -20.0F;
	public static int splashColor = 16776960 | (255 << 24);
	public static List<ResourceLocation> slideshowTextures;
	public static int slideshowDuration = 200;
	public static int slideshowTransition = 20;
	public static boolean slideshow = false;
	
	public ClientProxy()
	{
		MinecraftForge.EVENT_BUS.addListener(this::mainMenu);
	}

	static
	{
		ClientRegistry.registerKeyBinding(showQuests);
	}

	@SubscribeEvent
	public void mainMenu(GuiOpenEvent e) 
	{
		if (e.getGui() != null && e.getGui().getClass() == MainMenuScreen.class) 
		{
			e.setGui(new MainMenu());
		}
	}
}
