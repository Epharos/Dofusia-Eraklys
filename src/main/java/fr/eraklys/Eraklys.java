package fr.eraklys;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.eraklys.commands.ClearInventoryCommand;
import fr.eraklys.commands.GiveItemCommand;
import fr.eraklys.commands.GiveRandomCommand;
import fr.eraklys.commands.SpawnNpcCommand;
import fr.eraklys.economy.bank.ContainerBank;
import fr.eraklys.economy.bank.ScreenBank;
import fr.eraklys.economy.bank.capability.IBank;
import fr.eraklys.economy.bank.capability.InventoryBankWrapper;
import fr.eraklys.economy.bank.capability.ServerInventoryBankHolder;
import fr.eraklys.entities.EntitiesRegister;
import fr.eraklys.entities.RenderRegister;
import fr.eraklys.init.ModBlocks;
import fr.eraklys.inventory.ComparableItemStack;
import fr.eraklys.inventory.ItemWeight;
import fr.eraklys.jobs.capability.IPlayerJobs;
import fr.eraklys.jobs.capability.PlayerJobsWrapper;
import fr.eraklys.jobs.capability.ServerJobPlayerHolder;
import fr.eraklys.player.inventory.ContainerInventory;
import fr.eraklys.player.inventory.PacketUpdateMoney;
import fr.eraklys.player.inventory.ScreenInventory;
import fr.eraklys.player.inventory.capability.IInventoryPlayer;
import fr.eraklys.player.inventory.capability.InventoryPlayerWrapper;
import fr.eraklys.player.inventory.capability.ServerInventoryPlayerHolder;
import fr.eraklys.proxy.ClientProxy;
import fr.eraklys.proxy.Proxy;
import fr.eraklys.proxy.ServerProxy;
import fr.eraklys.quests.KillMonsterTask;
import fr.eraklys.quests.PacketUpdateQuest;
import fr.eraklys.quests.Quest;
import fr.eraklys.quests.QuestLoader;
import fr.eraklys.quests.QuestPart;
import fr.eraklys.quests.QuestScreen;
import fr.eraklys.quests.QuestTask;
import fr.eraklys.quests.capability.IQuest;
import fr.eraklys.quests.capability.QuestWrapper;
import fr.eraklys.quests.capability.ServerQuestHolder;
import fr.eraklys.speech.Speech;
import fr.eraklys.translation.TranslationLoader;
import fr.eraklys.utils.CapabilitiesRegisterer;
import fr.eraklys.utils.ItemStackUtil;
import fr.eraklys.utils.PacketRegisterer;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * This is the main class of the mod used on Eraklys
 * 
 * @author Aurelien "Epharos" REY
 */
@Mod(Eraklys.MODID)
public class Eraklys 
{
	public static final String MODID = "eraklys";
	public static final String VERSION = "0.0.1 (alpha)";
	public static final Logger LOGGER = LogManager.getLogger(Eraklys.MODID);
	public static Proxy proxy;
	public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID)
    {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon()
        {
            return new ItemStack(Blocks.COBBLESTONE);
        }
    };
	
	public static final Speech SPEECH = new Speech();
	public static final TranslationLoader TRANSLATION = new TranslationLoader();
	public static final QuestLoader QUEST = new QuestLoader();
	
	//--- CHANNELS ---
	
	public static final String PROTOCOL_VERSION = String.valueOf(1);
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation(Eraklys.MODID, Eraklys.PROTOCOL_VERSION))
			.networkProtocolVersion(() -> Eraklys.PROTOCOL_VERSION)
			.clientAcceptedVersions(Eraklys.PROTOCOL_VERSION::equals)
			.serverAcceptedVersions(Eraklys.PROTOCOL_VERSION::equals)
			.simpleChannel();
	
	//--- CONTAINERS ---
	
	public static ContainerType<?> playerInventoryContainer;
	public static ContainerType<?> bankInventoryContainer;
	
	//--- CAPABILITIES ---
	
	@CapabilityInject(IInventoryPlayer.class)
	public static final Capability<IInventoryPlayer> INVENTORY_CAPABILITY = null;
	public static final ResourceLocation INVENTORY_KEY = new ResourceLocation(Eraklys.MODID, "player_inventory");
	private static final Map<PlayerEntity, IInventoryPlayer> INVALIDATED_INVENTORY = new WeakHashMap<>();
	
	@CapabilityInject(IQuest.class)
	public static final Capability<IQuest> QUEST_CAPABILITY = null;
	public static final ResourceLocation QUEST_KEY = new ResourceLocation(Eraklys.MODID, "quest");
	private static final Map<PlayerEntity, IQuest> INVALIDATED_QUEST = new WeakHashMap<>();
	
	@CapabilityInject(IBank.class)
	public static final Capability<IBank> BANK_CAPABILITY = null;
	public static final ResourceLocation BANK_KEY = new ResourceLocation(Eraklys.MODID, "player_bank");
	private static final Map<PlayerEntity, IBank> INVALIDATED_BANK = new WeakHashMap<>();
	
	@CapabilityInject(IPlayerJobs.class)
	public static final Capability<IPlayerJobs> PLAYER_JOBS_CAPABILITY = null;
	public static final ResourceLocation JOB_KEY = new ResourceLocation(Eraklys.MODID, "player_job");
	private static final Map<PlayerEntity, IPlayerJobs> INVALIDATED_JOB = new WeakHashMap<>();
	
	/**
	 * Constructor used by Forge to create and load the mod
	 */
	public Eraklys()
	{	
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
		MinecraftForge.EVENT_BUS.register(this);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.register(modEventBus);
	}
	
	/**
	 * This method sets up things for client and server side
	 * @param event
	 */
	public void commonSetup(final FMLCommonSetupEvent event)
	{
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		
		PacketRegisterer.registerPackets(CHANNEL);
		CapabilitiesRegisterer.registerCapabilities();
	}
	
	public void clientSetup(final FMLClientSetupEvent event)
	{
		RenderRegister.registryEntityRenders();
	}
	
	public void serverSetup(final FMLDedicatedServerSetupEvent event)
	{
		SharedConstants.developmentMode = true;
	}
	
	/**
	 * Method called when the server is starting
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void serverStarting(final FMLServerStartingEvent event)
	{
		GiveItemCommand.register(event.getCommandDispatcher());
		ClearInventoryCommand.register(event.getCommandDispatcher());
		SpawnNpcCommand.register(event.getCommandDispatcher());
		GiveRandomCommand.register(event.getCommandDispatcher());
	}
	
	/**
	 * Attach all the capabilities to entities
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void attachToEntities(final AttachCapabilitiesEvent<Entity> event)
	{		
		if(event.getObject() instanceof PlayerEntity)
		{
			this.attachInventoryCapability(event);
			this.attachQuestCapability(event);
			this.attachBankCapability(event);
			this.attachJobCapability(event);
		}
	}
	
	/**
	 * Attach the inventory capability to a player, used in {@link #attachToEntities(AttachCapabilitiesEvent)}
	 * 
	 * @param event
	 */
	private void attachInventoryCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		InventoryPlayerWrapper wrapper;
		
		if(event.getObject() instanceof ServerPlayerEntity)
			wrapper = new InventoryPlayerWrapper(new ServerInventoryPlayerHolder((PlayerEntity) event.getObject()));
		else
			wrapper = new InventoryPlayerWrapper();
		
		event.addCapability(INVENTORY_KEY, wrapper);
		event.addListener(() -> wrapper.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> { 
			INVALIDATED_INVENTORY.put((PlayerEntity)event.getObject(), cap);
		}));
	}
	
	/**
	 * Attach the quest capability to a player, used in {@link #attachToEntities(AttachCapabilitiesEvent)}
	 * 
	 * @param event
	 */
	private void attachQuestCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		QuestWrapper wrapper;
		
		if(event.getObject() instanceof ServerPlayerEntity)
			wrapper = new QuestWrapper(new ServerQuestHolder((PlayerEntity) event.getObject()));
		else
			wrapper = new QuestWrapper();
		
		event.addCapability(QUEST_KEY, wrapper);
		event.addListener(() -> wrapper.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> { 
			INVALIDATED_QUEST.put((PlayerEntity)event.getObject(), cap);
		}));
	}
	
	private void attachBankCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		InventoryBankWrapper wrapper;
		
		if(event.getObject() instanceof ServerPlayerEntity)
			wrapper = new InventoryBankWrapper(new ServerInventoryBankHolder((PlayerEntity) event.getObject()));
		else
			wrapper = new InventoryBankWrapper();
		
		event.addCapability(BANK_KEY, wrapper);
		event.addListener(() -> wrapper.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
			INVALIDATED_BANK.put((PlayerEntity) event.getObject(), cap);
		}));
	}
	
	private void attachJobCapability(final AttachCapabilitiesEvent<Entity> event)
	{
		PlayerJobsWrapper wrapper;
		
		if(event.getObject() instanceof ServerPlayerEntity)
			wrapper = new PlayerJobsWrapper(new ServerJobPlayerHolder((PlayerEntity) event.getObject()));
		else
			wrapper = new PlayerJobsWrapper();
		
		event.addCapability(JOB_KEY, wrapper);
		event.addListener(() -> wrapper.getCapability(Eraklys.PLAYER_JOBS_CAPABILITY).ifPresent(cap -> { 
			INVALIDATED_JOB.put((PlayerEntity)event.getObject(), cap);
		}));
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void tooltipWeightAndQuantity(final ItemTooltipEvent event)
	{
		if(event.getPlayer() != null)
		{
			if(Minecraft.getInstance().currentScreen instanceof ScreenInventory)
			{
				event.getPlayer().getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
				{
					event.getToolTip().add(new TranslationTextComponent("item.tooltip.weight", ItemWeight.getWeight(event.getItemStack().getItem())));
					Integer count = cap.getInventory().getMapping().get(new ComparableItemStack(ItemStackUtil.getPrototype(event.getItemStack())));
					event.getToolTip().add(new TranslationTextComponent("item.tooltip.quantity", count == null ? "0" : count));
				});
			}
		}
	}
	
	@SubscribeEvent
	public void copyCapabilities(final PlayerEvent.Clone event)
	{
		if(event.isWasDeath())
		{
//			event.getEntity().getCapability(Eraklys.MONEY_CAPABILITY).ifPresent(copyCap -> {
//				if(INVALIDATED_MONEY.containsKey(event.getOriginal()))
//				{
//					INBT nbt = Eraklys.MONEY_CAPABILITY.writeNBT(INVALIDATED_MONEY.get(event.getOriginal()), null);
//					Eraklys.MONEY_CAPABILITY.readNBT(copyCap, null, nbt);
//				}
//			});
			
			event.getEntity().getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(copyCap -> {
				if(INVALIDATED_INVENTORY.containsKey(event.getOriginal()))
				{
					INBT nbt = Eraklys.INVENTORY_CAPABILITY.writeNBT(INVALIDATED_INVENTORY.get(event.getOriginal()), null);
					Eraklys.INVENTORY_CAPABILITY.readNBT(copyCap, null, nbt);
				}
			});
			
			event.getEntity().getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(copyCap -> {
				if(INVALIDATED_QUEST.containsKey(event.getOriginal()))
				{
					INBT nbt = Eraklys.QUEST_CAPABILITY.writeNBT(INVALIDATED_QUEST.get(event.getOriginal()), null);
					Eraklys.QUEST_CAPABILITY.readNBT(copyCap, null, nbt);
				}
			});
			
			event.getEntity().getCapability(Eraklys.BANK_CAPABILITY).ifPresent(copyCap -> {
				if(INVALIDATED_BANK.containsKey(event.getOriginal()))
				{
					INBT nbt = Eraklys.BANK_CAPABILITY.writeNBT(INVALIDATED_BANK.get(event.getOriginal()), null);
					Eraklys.BANK_CAPABILITY.readNBT(copyCap, null, nbt);
				}
			});
			
			event.getEntity().getCapability(Eraklys.PLAYER_JOBS_CAPABILITY).ifPresent(copyCap -> {
				if(INVALIDATED_JOB.containsKey(event.getOriginal()))
				{
					INBT nbt = Eraklys.PLAYER_JOBS_CAPABILITY.writeNBT(INVALIDATED_JOB.get(event.getOriginal()), null);
					Eraklys.PLAYER_JOBS_CAPABILITY.readNBT(copyCap, null, nbt);
				}
			});
		}
	}
	
	@OnlyIn(Dist.DEDICATED_SERVER)
	@SubscribeEvent
	public void onPlayerConnects(PlayerEvent.PlayerLoggedInEvent event)
	{
		PlayerEntity player = event.getPlayer();
		
		player.getCapability(Eraklys.INVENTORY_CAPABILITY).ifPresent(cap -> 
		{
			if(cap.getInventory().hasQueue())
			{
				cap.getInventory().syncQueue((ServerPlayerEntity) player);
			}
			
			if(cap.getInventory().hasHotbarQueue())
			{
				cap.getInventory().syncHotbarQueue((ServerPlayerEntity) player);
			}
			
			Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketUpdateMoney(cap.getMoney()));
		});
		
		player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap ->
		{
			if(cap.hasQueue())
			{
				cap.syncQueue();
			}
		});
		
//		player.getCapability(Eraklys.MONEY_CAPABILITY).ifPresent(cap -> cap.sync());
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onClientTickEvent(final ClientTickEvent event)
	{		
		if(ClientProxy.showQuests.isPressed())
		{
			Minecraft.getInstance().displayGuiScreen(new QuestScreen());
		}
		
		if(ClientProxy.showBank.isPressed())
		{
			Minecraft.getInstance().player.getCapability(Eraklys.BANK_CAPABILITY).ifPresent(cap -> {
				cap.openBank();
			});
		}
	}
	
	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public void onInventoryOpen(final GuiOpenEvent event)
	{
		if(Minecraft.getInstance().player != null)
			if(Minecraft.getInstance().player.isCreative())
				return;
		
		if(event.getGui() instanceof InventoryScreen)
		{
			event.setGui(new ScreenInventory(Minecraft.getInstance().player));
		}
	}
	
	@SubscribeEvent
	public void onEntityDeath(final LivingDeathEvent event)
	{
		if(event.getSource().getTrueSource() instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)event.getSource().getTrueSource();
			
			player.getCapability(Eraklys.QUEST_CAPABILITY).ifPresent(cap -> {
				for(Quest quest : cap.getActiveQuests())
				{
					QuestPart part = quest.getParts().get(quest.currentPart() - 1);
					
					for(QuestTask task : part.getTasks())
					{
						if(task instanceof KillMonsterTask)
						{
							if(event.getEntity().getType() == ((KillMonsterTask)task).getMonsterType())
							{
								((KillMonsterTask)task).update(1);
								
								DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
									Eraklys.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketUpdateQuest(quest.getId(), part.getId(), task));
								});
								quest.checkIfFinished();
							}
						}
					}
				}
			});
		}
	}
	
	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents
	{		
		@SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event)
        {
        	event.getRegistry().registerAll(
        				Eraklys.playerInventoryContainer = IForgeContainerType.create(ContainerInventory::new).setRegistryName(new ResourceLocation(Eraklys.MODID, "container_inventory"))
        			);
        	
        	event.getRegistry().registerAll(
    				Eraklys.bankInventoryContainer = IForgeContainerType.create(ContainerBank::new).setRegistryName(new ResourceLocation(Eraklys.MODID, "container_bank"))
    			);
        }
		
		@SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event)
        {
        	event.getRegistry().registerAll(
        				EntitiesRegister.TALKING_NPC_ENTITY
        			);
        }
	}
}
