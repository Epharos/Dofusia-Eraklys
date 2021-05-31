package fr.eraklys.player.inventory;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
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
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenInventory extends MenuableContainerScreen<ContainerInventory>
{
	public static final ResourceLocation texture = new ResourceLocation(Eraklys.MODID, "textures/gui/inventory.png");
	private int rotation = -1;
	
	private SimpleScrollBar scrollBar;
	
	public boolean hotbarMode = false;
	public Item selectedItem = null;
	
	public ScreenInventory(PlayerEntity player) 
	{
		super(new ContainerInventory(player), null, new TranslationTextComponent("player.inventory"));
		this.hotbarMode = false;
		this.selectedItem = null;
	}
	
	protected void init()
	{		
		super.init();
		this.xSize = 314;
		this.ySize = 119;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		
		this.addButton(new RotationArrow(this.guiLeft + 52, this.guiTop + 85, false, this));
		this.addButton(new RotationArrow(this.guiLeft + 94, this.guiTop + 84, true, this));
		
		this.addButton(scrollBar = new SimpleScrollBar(this.guiLeft + this.xSize - 10, this.guiTop + 7, 4 * 18, (int)(Math.ceil(this.getContainer().stacks.size() / 9) * 18)));
	}

	@SuppressWarnings("resource")
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderBackground();
		
		this.getMinecraft().getTextureManager().bindTexture(texture);
		
		this.blit(this.guiLeft, this.guiTop, 0, 0, 140, 119);
		this.blit(this.guiLeft + 140, this.guiTop, 0, 119, 174, 119);
		
		this.blit(this.guiLeft + 142, this.guiTop + 84, 0, 238, 158, 5);
		
		
		this.getMinecraft().player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
		{
			this.blit(this.guiLeft + 142, this.guiTop + 84, 0, 243, (int)(158.0f * Math.max(0, Math.min(100, cap.getWeight() / 4000.f))), 5);
		});
		
//		this.getMinecraft().player.getCapability(Eraklys.MONEY_CAPABILITY).ifPresent(cap -> 
//		{
//			this.font.drawString(FontRendererStringUtil.formatMoney(cap.getMoney()), this.guiLeft + 24, this.guiTop + 103, 0xd0d0d0);
//		});
		
		this.renderEntity(this.guiLeft + 79, this.guiTop + 87, 35, this.getMinecraft().player);
	}
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
	{
		this.renderHoveredToolTip(mouseX, mouseY);
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
				this.drawSlot2(slot);
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
		
		this.getContainer().scrollTo(scrollBar.getScrollValue());
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
	
	private void drawSlot2(Slot slotIn) 
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
			this.minecraft.player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
			{
				if(!itemstack.isEmpty())
				{
					Integer value = cap.getInventory().getMapping().get(new ComparableItemStack(itemstack)) == null ? 0 : cap.getInventory().getMapping().get(new ComparableItemStack(itemstack));
					FontRendererStringUtil.drawScaledString(font, value > 0 ? FontRendererStringUtil.formatInteger(value) : "0", i, j, 0.6f, 1666);
				}
			});
		}

		this.itemRenderer.zLevel = 0.0F;
		this.setBlitOffset(0);
	}
	
	private void renderEntity(int x, int y, int scale, LivingEntity entity) 
	{
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)x, (float)y, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float)scale, (float)scale, (float)scale);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(-5.0F);
		quaternion.multiply(quaternion1);
		matrixstack.rotate(quaternion);
		float f2 = entity.renderYawOffset;
		float f3 = entity.rotationYaw;
		float f4 = entity.rotationPitch;
		float f5 = entity.prevRotationYawHead;
		float f6 = entity.rotationYawHead;
		entity.renderYawOffset = 180.0F + rotation * 20.0F;
		entity.rotationYaw = 180.0F + rotation * 20.0F;
		entity.rotationPitch = 3.0F;
		entity.rotationYawHead = entity.rotationYaw;
		entity.prevRotationYawHead = entity.rotationYaw;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
		irendertypebuffer$impl.finish();
		entityrenderermanager.setRenderShadow(true);
		entity.renderYawOffset = f2;
    	entity.rotationYaw = f3;
     	entity.rotationPitch = f4;
     	entity.prevRotationYawHead = f5;
     	entity.rotationYawHead = f6;
		RenderSystem.popMatrix();
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
	
	public class RotationArrow extends Widget
	{
		public boolean direction = false;
		private ScreenInventory si;
		
		public RotationArrow(int xIn, int yIn, boolean f, ScreenInventory si) 
		{
			super(xIn, yIn, 13, 10, "");
			this.direction = f;
			this.si = si;
		}
		
		public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) 
		{
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getTextureManager().bindTexture(ScreenInventory.texture);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.blit(this.x, this.y, 226 + (this.isHovered() ? 15 : 0), 235 + (this.direction ? 0 : 10), 13, 10);
		}
		
		public void onClick(double p_onClick_1_, double p_onClick_3_) 
		{
			if(this.direction)
				si.rotation--;
			else
				si.rotation++;
		}
	}
}
