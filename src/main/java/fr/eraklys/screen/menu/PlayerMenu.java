package fr.eraklys.screen.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

public class PlayerMenu extends Menu 
{
	public AbstractClientPlayerEntity target;
	
	@SuppressWarnings("resource")
	public PlayerMenu(AbstractClientPlayerEntity e)
	{
		this.setTarget(e);
		
		if(this.target != Minecraft.getInstance().player)
		{
//			this.addEntry(new PrivateMessageEntry(this, 
//					ent -> { 
//						Minecraft.getInstance().displayGuiScreen(new ChatScreen("/mp " + target.getName().getString() + " "));
//					}));
//			
//			this.addEntry(new PlayerTradeEntry(this,
//					ent -> {
//						//TODO
//					}));
//			
//			this.addEntry(new SeparatorEntry(this));
//			
////			if(ClientGroup.isInGroup(Minecraft.getInstance().player) && ClientGroup.isInGroup(target))
////			{
////				if(ClientGroup.getOwnerID() == Minecraft.getInstance().player.getEntityId())
////				{
////					this.addEntry(new KickPlayerGroupEntry(this, 
////							ent -> { 
////								Eraklys.CHANNEL.sendToServer(new PacketKickGroupPlayer(target.getEntityId()));
////							}));
////					this.addEntry(new GroupOwnerEntry(this,
////							ent -> {
////								Eraklys.CHANNEL.sendToServer(new PacketUpdateGroup(target.getEntityId(), 10));
////							}));
////					this.addEntry(new SeparatorEntry(this));
////				}
////			}
////			
////			if(!ClientGroup.isInGroup(e) && ClientGroup.groupSize() < 6)
////				this.addEntry(new GroupInviteEntry(this, ent -> ClientPlayerUtil.groupInvite(target.getEntityId())));
//			this.addEntry(new GuildInviteEntry(this,
//					ent -> {
//						//TODO
//					}));
//			this.addEntry(new SeparatorEntry(this));
//			
//			this.addEntry(new FriendInviteEntry(this,
//					ent -> {
//						//TODO
//					}));
		}
		else
		{
//			if(ClientGroup.isInGroup(Minecraft.getInstance().player))
//			{
//				this.addEntry(new QuitGroupEntry(this, 
//						ent -> {
//							Eraklys.CHANNEL.sendToServer(new PacketUpdateGroup(target.getEntityId(), 1));
//						}));
//			}
		}
	}
	
	public PlayerMenu setTarget(AbstractClientPlayerEntity e)
	{
		this.target = e;
		return this;
	}
}
