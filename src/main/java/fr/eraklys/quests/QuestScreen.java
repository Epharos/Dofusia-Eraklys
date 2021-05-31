package fr.eraklys.quests;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import fr.eraklys.Eraklys;
import fr.eraklys.utils.FontRendererStringUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.TranslationTextComponent;

public class QuestScreen extends Screen 
{
	int xSize = 401, ySize = 283;
	int guiLeft, guiTop;
	
	boolean activeTab = false;
	Quest questIn = null;
	int page = 0;
	
	float scrollValue = .0f;
	
	ChangePageButton prev, next;
	
	public static final ResourceLocation QUEST_TEXTURE = new ResourceLocation(Eraklys.MODID, "textures/gui/quest.png");
	
	public QuestScreen() 
	{
		super(new TranslationTextComponent("quest.screen"));
	}
	
	@SuppressWarnings("resource")
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		RenderSystem.color4f(1.f, 1.f, 1.f, 1.f);
		Minecraft.getInstance().getTextureManager().bindTexture(QuestScreen.QUEST_TEXTURE);
		
		if(page <= 0)
		{
			this.prev.visible = false;
		}
		else
		{
			this.prev.visible = true;
		}
		
		if(questIn != null)
		{
			if(page == questIn.getParts().size())
			{
				this.next.visible = false;
			}
			else
			{
				this.next.visible = true;
			}
		}
		else
		{
			this.next.visible = false;
			this.prev.visible = false;
		}
		
		Screen.blit(this.guiLeft, this.guiTop, this.getBlitOffset(), .0f, .0f, this.xSize, this.ySize, 512, 512);
		FontRendererStringUtil.drawScaledString(this.font, this.getTitle().getFormattedText(), this.guiLeft + 92, this.guiTop + 12, 1.5f, 0, 0x40362C, false);
		
		Minecraft.getInstance().player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> 
		{
			this.font.drawString(new TranslationTextComponent("quest.active", cap.countActiveQuests()).getFormattedText(), this.guiLeft + 25, this.guiTop + 34, 0xAD947A);
			this.font.drawString(new TranslationTextComponent("quest.done", cap.countFinishedQuests()).getFormattedText(), this.guiLeft + 25, this.guiTop + 46, 0xAD947A);
		});
		
		Minecraft.getInstance().getTextureManager().bindTexture(QuestScreen.QUEST_TEXTURE);
		
		if(!this.activeTab)
		{
			Screen.blit(this.guiLeft + 23, this.guiTop + 67, this.getBlitOffset(), 41.f, 291.f, 164, 16, 512, 512);
			this.font.drawString(new TranslationTextComponent("quest.active.label").getFormattedText(), this.guiLeft + 31, this.guiTop + 72, 0x725B43);
			this.font.drawString(new TranslationTextComponent("quest.done.label").getFormattedText(), this.guiLeft + 112, this.guiTop + 73, 0xA5896C);
		}
		else
		{
			Screen.blit(this.guiLeft + 23, this.guiTop + 67, this.getBlitOffset(), 41.f, 321.f, 164, 16, 512, 512);
			this.font.drawString(new TranslationTextComponent("quest.active.label").getFormattedText(), this.guiLeft + 31, this.guiTop + 73, 0xA5896C);
			this.font.drawString(new TranslationTextComponent("quest.done.label").getFormattedText(), this.guiLeft + 112, this.guiTop + 72, 0x725B43);
		}
		
		if(this.questIn != null)
		{
			if(this.page == 0)
			{
				this.font.drawString("§l" + this.questIn.getName(), this.guiLeft + 214, this.guiTop + 24, 0x5E472F);
				
				AtomicInteger i = new AtomicInteger(0);
				FontRendererStringUtil.splitStringMultiline(165, this.questIn.getDescription()).forEach(e -> {
					this.font.drawString(e, this.guiLeft + 214, this.guiTop + 39 + (i.get() * 10), 0x725B43);
					i.set(i.get() + 1);
				});
			}
			else
			{
				QuestPart part = this.questIn.getParts().get(this.page - 1);
				
				this.font.drawString("§l" + this.questIn.getName(), this.guiLeft + 214, this.guiTop + 24, 0x5E472F);
				this.font.drawString(new TranslationTextComponent("quest.objectives").getFormattedText(), this.guiLeft + 218, this.guiTop + 36, 0x5E472F);
				
				if(part.getId() >= this.questIn.currentPart())
				{
					this.font.drawString("???", this.guiLeft + 218, this.guiTop + 48, 0x725B43);
					this.font.drawString(new TranslationTextComponent("quest.rewards").getFormattedText(), this.guiLeft + 218, this.guiTop + 60, 0x5E472F);
					this.font.drawString("???", this.guiLeft + 218, this.guiTop + 72, 0x725B43);
				}
				else
				{
					int i = 0;
					for(QuestTask task : part.getTasks())
					{						
						List<String> l = FontRendererStringUtil.splitStringMultiline(160, task.formatedText().getFormattedText());
						
						for(String s : l)
						{
							this.font.drawString(s, this.guiLeft + 218, this.guiTop + 48 + (i * 12), 0x725B43);
							i++;
						}
						
						if(task.isDone())
						{
							float scale = .75f, inverse = 1f/scale;
							RenderSystem.scalef(scale, scale, scale);
							Minecraft.getInstance().getTextureManager().bindTexture(QuestScreen.QUEST_TEXTURE);
							Screen.blit((int)(inverse * (this.guiLeft + 208)), (int)(inverse * (this.guiTop + 50 + (i * 10))), this.getBlitOffset(), 3f, 287f, 9, 10, 512, 512);
							RenderSystem.scalef(inverse, inverse, inverse);
						}
					}
					
					this.font.drawString(new TranslationTextComponent("quest.rewards").getFormattedText(), this.guiLeft + 218, this.guiTop + 58 + (i * 12), 0x5E472F);
					
					if(part.getXpReward() > 0)
						this.font.drawString(new TranslationTextComponent("quest.reward.exp", part.xpReward).getFormattedText(), this.guiLeft + 218, this.guiTop + 58 + ((i + 1) * 12), 0x725B43);
					
					if(part.getMoneyReward() > 0)
						this.font.drawString(new TranslationTextComponent("quest.reward.money", part.moneyReward).getFormattedText(), this.guiLeft + 218, this.guiTop + 58 + ((i + 2) * 12), 0x725B43);
				}
			}
		}
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
	
	protected void init() 
	{
		super.init();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - ySize) / 2;
		this.questSelector(0, this.activeTab);
		
		this.addButton(prev = new ChangePageButton(this.guiLeft + 210, this.guiTop + this.ySize - 35, false, button -> changePage(false), true));
		this.addButton(next = new ChangePageButton(this.guiLeft + this.xSize - 48, this.guiTop + this.ySize - 35, true, button -> changePage(true), true));
	}
	
	public void changePage(boolean flag)
	{
		if(flag)
		{
			this.page++;
		}
		else
		{
			this.page--;
			
			if(page < 0)
				page = 0;
		}
	}
	
	@SuppressWarnings("resource")
	public void questSelector(int start, boolean flag)
	{
		Minecraft.getInstance().player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> 
		{			
			if(flag)
			{
				if(start > cap.getFinishedQuests().size())
				{
					return;
				}
				
				for(int i = start ; i < Math.min(cap.getFinishedQuests().size(), i + 6) ; i++)
				{
					this.addButton(new QuestSelector(this.guiLeft + 27, this.guiTop + 86 + ((i - start) * 30), cap.getFinishedQuests().get(i), this));
				}
			}
			else
			{
				if(start > cap.getActiveQuests().size())
				{
					return;
				}
				
				for(int i = start ; i < Math.min(cap.getActiveQuests().size(), i + 6) ; i++)
				{
					this.addButton(new QuestSelector(this.guiLeft + 27, this.guiTop + 86 + ((i - start) * 30), cap.getActiveQuests().get(i), this));
				}
			}
		});
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int click) 
	{
		if(super.mouseClicked(mouseX, mouseY, click))
		{
			return true;
		}
		
		if(mouseX >= this.guiLeft + 23 && mouseX <= this.guiLeft + 105 && this.activeTab && mouseY >= this.guiTop + 67 && mouseY <= this.guiTop + 81)
		{
			this.activeTab = false;
			Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			this.scrollValue = .0f;
			this.removeQuestSelectors();
			this.questSelector(0, this.activeTab);
			this.questIn = null;
			this.page = 0;
			return true;
		}
		
		if(mouseX >= this.guiLeft + 105 && mouseX <= this.guiLeft + 187 && !this.activeTab && mouseY >= this.guiTop + 67 && mouseY <= this.guiTop + 81)
		{
			this.activeTab = true;
			Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			this.scrollValue = .0f;
			this.removeQuestSelectors();
			this.questSelector(0, this.activeTab);
			this.questIn = null;
			this.page = 0;
			return true;
		}
		
		return false;
	}
	
	public void removeQuestSelectors() 
	{
		for(Iterator<Widget> it = this.buttons.iterator() ; it.hasNext() ; )
		{
			Widget w = it.next();
			
			if(w instanceof QuestSelector)
			{
				it.remove();
			}
		}
		
		for(Iterator<IGuiEventListener> it = this.children.iterator() ; it.hasNext() ; )
		{
			IGuiEventListener w = it.next();
			
			if(w instanceof QuestSelector)
			{
				it.remove();
			}
		}
	}
	
	public void unselectQuest()
	{
		for(Iterator<Widget> it = this.buttons.iterator() ; it.hasNext() ; )
		{
			Widget w = it.next();
			
			if(w instanceof QuestSelector)
			{
				((QuestSelector) w).selected = false;
			}
		}
		
		for(Iterator<IGuiEventListener> it = this.children.iterator() ; it.hasNext() ; )
		{
			IGuiEventListener w = it.next();
			
			if(w instanceof QuestSelector)
			{
				((QuestSelector) w).selected = false;
			}
		}
	}
	
	public class QuestSelector extends Widget
	{
		Quest quest;
		QuestScreen screen;
		boolean selected = false;
		
		public QuestSelector(int xIn, int yIn, Quest quest, QuestScreen screen) 
		{
			super(xIn, yIn, 156, 26, quest.getName());
			this.quest = quest;
			this.screen = screen;
		}
		
		public void renderButton(int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) 
		{
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontrenderer = minecraft.fontRenderer;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
		    RenderSystem.defaultBlendFunc();
		    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			fontrenderer.drawString(this.getMessage(), this.x + 4, this.y + 4, 0x7F6952);
			fontrenderer.drawString(new TranslationTextComponent("quest.part", quest.currentPart()).getFormattedText(), this.x + 4, this.y + 14, 0xB49C84);
			
			Minecraft.getInstance().getTextureManager().bindTexture(QuestScreen.QUEST_TEXTURE);
			
			if(this.selected)
			{
				Screen.blit(this.x, this.y, this.getBlitOffset(), 212f, 302f, this.width, this.height, 512, 512);
			}
		}
		
		public void onClick(double p_onClick_1_, double p_onClick_3_) 
		{
			super.onClick(p_onClick_1_, p_onClick_3_);
			this.screen.questIn = this.quest;
			this.screen.unselectQuest();
			this.screen.page = 0;
			this.selected = true;
		}
	}
}
