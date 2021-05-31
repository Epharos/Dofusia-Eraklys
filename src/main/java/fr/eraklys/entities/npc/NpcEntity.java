package fr.eraklys.entities.npc;

import java.util.ArrayList;
import java.util.List;

import fr.eraklys.Eraklys;
import fr.eraklys.entities.npc.screen.TalkingScreen;
import fr.eraklys.speech.PacketSendSpeechToClient;
import fr.eraklys.speech.Speech;
import fr.eraklys.speech.SpeechNode;
import fr.eraklys.speech.SpeechResponse;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class NpcEntity extends CreatureEntity
{
	Vec3d theoricalPosition = null;
	protected static final DataParameter<String> TEXTURE = EntityDataManager.createKey(NpcEntity.class, DataSerializers.STRING);
	private int speechID = -1;
	
	public NpcEntity(EntityType<? extends NpcEntity> type, World worldIn) 
	{
		super(type, worldIn);
		this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8f));
		this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
	}
	
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(TEXTURE, "default");
	}
	
	public NpcEntity setTexture(String s)
	{
		if(NpcSkinRegistry.skins.get(s) != null)
		{
			this.dataManager.set(TEXTURE, s);
		}
		
		return this;
	}
	
	public String getTexture()
	{
		return this.dataManager.get(TEXTURE);
	}
	
	public NpcEntity setName(String name)
	{
		this.setCustomName(new StringTextComponent(name));
		return this;
	}
	
	public NpcEntity setSpeech(int id)
	{
		this.speechID = id;
		return this;
	}
	
	public void setTheoricalPosition(Vec3d bp)
	{
		this.theoricalPosition = bp;
	}
	
	public boolean canBePushed() 
	{
		return false;
	}
	
	public boolean canBeCollidedWith()
	{
		return true;
	}
	
	public void tick() 
	{
		super.tick();
		
		if(this.theoricalPosition != null)
		{
			this.setPosition(this.theoricalPosition.getX(), this.theoricalPosition.getY(), this.theoricalPosition.getZ());
		}
		
//		Eraklys.LOGGER.debug(this.getCustomName().getString() + ":" + this.textureID);
	}
	
	public void writeAdditional(CompoundNBT compound) 
	{
		super.writeAdditional(compound);
		compound.putDouble("TX", this.theoricalPosition.x);
		compound.putDouble("TY", this.theoricalPosition.y);
		compound.putDouble("TZ", this.theoricalPosition.z);
		compound.putString("texture", this.getTexture());
		compound.putInt("speech", this.speechID);
	}
	
	public void readAdditional(CompoundNBT compound) 
	{
		super.readAdditional(compound);
		double x = compound.getDouble("TX");
		double y = compound.getDouble("TY");
		double z = compound.getDouble("TZ");
		this.theoricalPosition = new Vec3d(x, y, z);
		this.setTexture(compound.getString("texture"));
		this.setSpeech(compound.getInt("speech"));
	}
	
	public boolean attackEntityFrom(DamageSource source, float amount) 
	{
		if(source == DamageSource.LAVA)
		{
			return super.attackEntityFrom(source, amount);
		}
		
		return false;
	}
	
	public boolean canDespawn(double distanceToClosestPlayer) 
	{
		return false;
	}
	
	public boolean processInteract(PlayerEntity player, Hand hand) 
	{
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> 
		{
			this.handleRightClickClient(player);
		});
		
		DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
		{
			this.handleRightClickServer((ServerPlayerEntity) player);
		});
		
		return true;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean handleRightClickClient(PlayerEntity player)
	{
		Minecraft.getInstance().displayGuiScreen(new TalkingScreen(this.getCustomName()));
		return true;
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	public boolean handleRightClickServer(ServerPlayerEntity player)
	{		
		SpeechNode node = Eraklys.SPEECH.speechNodesLoader.getSpeechNode(this.speechID);
		String content = Eraklys.TRANSLATION.getTranslation(player, node.getContent());
		List<PacketSendSpeechToClient.PSpeechResponse> srl = new ArrayList<>();
		
		for(SpeechResponse sr : node.getResponses())
		{
			if(sr.trigger(player))
			{
				srl.add(new PacketSendSpeechToClient.PSpeechResponse(sr.getId(), Eraklys.TRANSLATION.getTranslation(player, sr.getResponse())));
			}
		}
		
		Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new PacketSendSpeechToClient(new StringTextComponent(content), srl));
		Speech.playerTchating.put(player, node);
		return true;
	}
}
