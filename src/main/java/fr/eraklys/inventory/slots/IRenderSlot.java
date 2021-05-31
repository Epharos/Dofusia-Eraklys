package fr.eraklys.inventory.slots;

import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderSlot 
{
	@OnlyIn(Dist.CLIENT)
	void renderSlot(Screen screen);
}

