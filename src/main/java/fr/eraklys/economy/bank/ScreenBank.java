package fr.eraklys.economy.bank;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import fr.eraklys.Eraklys;
import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.slots.IRenderSlot;
import fr.eraklys.screen.MenuableContainerScreen;
import fr.eraklys.screen.widget.SimpleScrollBar;
import fr.eraklys.utils.FontRendererStringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenBank extends MenuableContainerScreen<ContainerBank>
{
	public static final ResourceLocation texture = new ResourceLocation(Eraklys.MODID, "textures/gui/bank.png");
	
	private SimpleScrollBar inventoryBar, bankBar;
	
	public Item selectedItem = null;
	public boolean from = false; //false = de l'inventaire vers la banque, true = de la banque vers l'inventaire
	
	public ScreenBank(PlayerEntity player) 
	{
		super(new ContainerBank(player), null, new TranslationTextComponent("economy.bank"));
	}
	
	protected void init()
	{
		super.init();
		
		this.xSize = 362;
		this.ySize = 185;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		this.addButton(inventoryBar = new SimpleScrollBar(this.guiLeft + this.xSize - 10, this.guiTop + 19, 8 * 18, (int)(Math.ceil(this.getContainer().playerStacks.size() / 9) * 18)));
		this.addButton(bankBar = new SimpleScrollBar(this.guiLeft + this.xSize / 2 - 10, this.guiTop + 19, 8 * 18, (int)(Math.ceil(this.getContainer().stacks.size() / 9) * 18)));
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderBackground();
		
		this.getMinecraft().getTextureManager().bindTexture(texture);
		
		this.blit(this.guiLeft, this.guiTop, 0, 0, xSize / 2, ySize);
		this.blit(this.guiLeft + xSize / 2, this.guiTop, 0, 0, xSize / 2, ySize);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		this.renderHoveredToolTip(mouseX, mouseY);
		
		this.font.drawString(new TranslationTextComponent("player.inventory").getFormattedText(), this.guiLeft + this.xSize / 2 + 8, this.guiTop + 7, 0x333333);
		this.font.drawString(new TranslationTextComponent("player.bank").getFormattedText(), this.guiLeft + 8, this.guiTop + 7, 0x333333);
	}
	
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) 
	{
		int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, p_render_1_, p_render_2_));
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableDepthTest();
      
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)i, (float)j, 0.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableRescaleNormal();
		this.hoveredSlot = null;
		RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		for(int i1 = 0; i1 < this.container.inventorySlots.size(); ++i1)
		{
			Slot slot = this.container.inventorySlots.get(i1);
			
			if (slot.isEnabled()) 
			{
				if(slot.slotNumber < 9 * 8 - 1)
					this.drawSlot2(slot, true);
				else
					this.drawSlot2(slot, false);
			}

			if (this.isSlotSelected2(slot, (double)p_render_1_, (double)p_render_2_) && slot.isEnabled()) 
			{
				this.hoveredSlot = slot;
				RenderSystem.disableDepthTest();
				int j1 = slot.xPos;
				int k1 = slot.yPos;
				RenderSystem.colorMask(true, true, true, false);
				int slotColor = this.getSlotColor(i1);
				this.fillGradient(j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
				RenderSystem.colorMask(true, true, true, true);
				RenderSystem.enableDepthTest();
			}
		}
		
		RenderSystem.translatef((float)-i, (float)-j, 0.0f);

		this.drawGuiContainerForegroundLayer(p_render_1_, p_render_2_);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, p_render_1_, p_render_2_));

		RenderSystem.popMatrix();
		RenderSystem.enableDepthTest();
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableDepthTest();
      
		for(int a = 0; a < this.buttons.size(); ++a) 
		{
			this.buttons.get(a).render(p_render_1_, p_render_2_, p_render_3_);
		}
		
		this.getContainer().scrollBankTo(this.bankBar.getScrollValue());
		this.getContainer().scrollInventoryTo(this.inventoryBar.getScrollValue());
	}
	
	protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) 
	{
		if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) 
		{
			this.renderTooltip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
		}
	}
	
	private boolean isSlotSelected2(Slot p_195362_1_, double p_195362_2_, double p_195362_4_) 
	{
		return this.isPointInRegion(p_195362_1_.xPos, p_195362_1_.yPos, 16, 16, p_195362_2_, p_195362_4_);
	}
	
	private void drawSlot2(Slot slotIn, boolean inventory) 
	{
		int i = slotIn.xPos;
		int j = slotIn.yPos;
		ItemStack itemstack = slotIn.getStack();
		boolean flag = false;

		this.setBlitOffset(200);
		this.itemRenderer.zLevel = 100.0F;
		
		if(slotIn instanceof IRenderSlot)
			((IRenderSlot)slotIn).renderSlot(this);
		
		if (itemstack.isEmpty() && slotIn.isEnabled()) 
		{
			Pair<ResourceLocation, ResourceLocation> pair = slotIn.func_225517_c_();
			if (pair != null) 
			{
				TextureAtlasSprite textureatlassprite = this.minecraft.getAtlasSpriteGetter(pair.getFirst()).apply(pair.getSecond());
				this.minecraft.getTextureManager().bindTexture(textureatlassprite.getAtlasTexture().getTextureLocation());
				blit(i, j, this.getBlitOffset(), 16, 16, textureatlassprite);
				flag = true;
			}
		}

		if (!flag) 
		{
			RenderSystem.enableDepthTest();
			this.itemRenderer.renderItemAndEffectIntoGUI(this.minecraft.player, itemstack, i, j);
			
			if(inventory)
			{
				this.minecraft.player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
				{
					if(!itemstack.isEmpty())
					{
						Integer value = cap.getInventory().getMapping().get(new ComparableItemStack(itemstack)) == null ? 0 : cap.getInventory().getMapping().get(new ComparableItemStack(itemstack));
						FontRendererStringUtil.drawScaledString(font, value > 0 ? FontRendererStringUtil.formatInteger(value) : "0", i, j, 0.6f, 1666);
					}
				});
			}
			else
			{
				this.minecraft.player.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> 
				{
					if(!itemstack.isEmpty())
					{
						Integer value = cap.getBankInventory().getMapping().get(new ComparableItemStack(itemstack)) == null ? 0 : cap.getBankInventory().getMapping().get(new ComparableItemStack(itemstack));
						FontRendererStringUtil.drawScaledString(font, value > 0 ? FontRendererStringUtil.formatInteger(value) : "0", i, j, 0.6f, 1666);
					}
				});
			}
		}

		this.itemRenderer.zLevel = 0.0F;
		this.setBlitOffset(0);
	}
	
	public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) 
	{
		return this.getFocused() != null && this.isDragging() && p_mouseDragged_5_ == 0 ? this.getFocused().mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_) : false;
	}

	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) 
	{
		this.setDragging(false);
		return this.getEventListenerForPos(p_mouseReleased_1_, p_mouseReleased_3_).filter((p_212931_5_) -> {
			return p_212931_5_.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
		}).isPresent();
	}
	
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double scrollAmount) 
	{
		boolean flag = false;
		
		for(Widget w : this.buttons)
			if(w.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, scrollAmount))
				flag = true;
		
		return flag;
	}
	
	@SuppressWarnings("resource")
	protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) 
	{
		if (slotIn != null) 
		{
			this.getContainer().slotClick(slotIn.getSlotIndex(), mouseButton, type, Minecraft.getInstance().player);
		}
	}
}
