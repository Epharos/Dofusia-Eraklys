package fr.eraklys.entities.npc.screen;

import java.util.Iterator;
import java.util.List;

import fr.eraklys.Eraklys;
import fr.eraklys.screen.widget.ClickableText;
import fr.eraklys.speech.PacketResponseFromClient;
import fr.eraklys.speech.PacketSendSpeechToClient;
import fr.eraklys.utils.FontRendererStringUtil;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TalkingScreen extends Screen
{
	int xSize = 250, ySize = 150;
	int guiLeft, guiTop;
	private ITextComponent speechContent = new TranslationTextComponent("speech.loading");
	
	public TalkingScreen(ITextComponent titleIn) 
	{
		super(titleIn);
	}
	
	public void render(int p_render_1_, int p_render_2_, float p_render_3_)
	{
		this.fillGradient(this.guiLeft, this.guiTop, this.guiLeft + this.xSize, this.guiTop + this.ySize, 0xee777777, 0xee333333);
		this.font.drawString("§l" + this.getTitle().getFormattedText(), this.guiLeft + 5, this.guiTop + 6, 0xffffff);
		
		int i = 0;
		for(String s : FontRendererStringUtil.splitStringMultiline(this.xSize - 10, this.getSpeechContent().getFormattedText()))
		{
			this.font.drawString(s, this.guiLeft + 5, this.guiTop + 18 + (i * 10), 0xffffff);
			i++;
		}
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
	
	protected void init() 
	{
		super.init();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - ySize) / 2;
		this.resize();
	}
	
	public void clearResponses()
	{
		for(Iterator<Widget> it = this.buttons.iterator() ; it.hasNext() ; )
		{
			Widget w = it.next();
			
			if(w instanceof ClickableText)
			{
				it.remove();
			}
		}
		
		for(Iterator<IGuiEventListener> it = this.children.iterator() ; it.hasNext() ; )
		{
			IGuiEventListener w = it.next();
			
			if(w instanceof ClickableText)
			{
				it.remove();
			}
		}
	}
	
	public void setResponses(List<PacketSendSpeechToClient.PSpeechResponse> rl)
	{
		int i = 0;
		for(PacketSendSpeechToClient.PSpeechResponse response : rl)
		{
			this.addButton(new ClickableText(this.guiLeft + 10, this.guiTop + 25 + (FontRendererStringUtil.splitStringMultiline(this.xSize - 10, this.getSpeechContent().getFormattedText()).size() * 10) + (i * 12), "> " + response.content, ct -> {
				Eraklys.CHANNEL.sendToServer(new PacketResponseFromClient(response.id));
			}));
			i++;
		}
	}
	
	public void resize()
	{
		ySize = FontRendererStringUtil.splitStringMultiline(this.xSize - 10, this.getSpeechContent().getFormattedText()).size() * 10 + this.buttons.size() * 12 + 26;
	}

	public ITextComponent getSpeechContent() 
	{
		return speechContent;
	}

	public void setSpeechContent(ITextComponent speechContent) 
	{
		this.speechContent = speechContent;
	}
	
	public void onClose() 
	{
		super.onClose();
	}
	
	public boolean shouldCloseOnEsc() 
	{
	    return true;
	}
}
