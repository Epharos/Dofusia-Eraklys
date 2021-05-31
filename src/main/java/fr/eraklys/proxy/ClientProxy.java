package fr.eraklys.proxy;

import org.lwjgl.glfw.GLFW;

import fr.eraklys.Eraklys;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends Proxy 
{
	public static final KeyBinding showQuests = new KeyBinding("key.quests", GLFW.GLFW_KEY_W, "key.category." + Eraklys.MODID);
	
	public ClientProxy()
	{
		
	}
	
	static
	{
		ClientRegistry.registerKeyBinding(showQuests);
	}
}
