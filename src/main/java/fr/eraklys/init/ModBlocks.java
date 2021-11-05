package fr.eraklys.init;

import java.util.function.Supplier;

import fr.eraklys.Eraklys;
import fr.eraklys.blocks.TallGrassIncarnateBlock;
import fr.eraklys.blocks.BlockLeavesCarpet;
import fr.eraklys.blocks.BlockIncarnateLeaves;
import fr.eraklys.blocks.BlockIncarnateLog;
import fr.eraklys.blocks.BlockPathIncarnate;
import fr.eraklys.blocks.BlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks 
{
	private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Eraklys.MODID);
	private static final DeferredRegister<Item> ITEM_BLOCKS = new DeferredRegister<>(ForgeRegistries.ITEMS, Eraklys.MODID);

	public static final RegistryObject<Block> STONE_INCARNATE = registerBlockWithBasicItem("stone_incarnate", BlockProperties.STONE_INCARNATE);
	public static final RegistryObject<Block> PATH_INCARNATE_DEFAULT = registerBlockWithBasicItem("path_incarnate_default", BlockProperties.PATH_INCARNATE_DEFAULT);
	public static final RegistryObject<Block> PATH_INCARNATE_A = registerBlockWithBasicItem("path_incarnate_a", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_A));
	public static final RegistryObject<Block> PATH_INCARNATE_B = registerBlockWithBasicItem("path_incarnate_b", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_B));
	public static final RegistryObject<Block> PATH_INCARNATE_C = registerBlockWithBasicItem("path_incarnate_c", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_C));
	public static final RegistryObject<Block> PATH_INCARNATE_D = registerBlockWithBasicItem("path_incarnate_d", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_D));
	public static final RegistryObject<Block> PATH_INCARNATE_E = registerBlockWithBasicItem("path_incarnate_e", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_E));
	public static final RegistryObject<Block> PATH_INCARNATE_F = registerBlockWithBasicItem("path_incarnate_f", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_F));
	public static final RegistryObject<Block> PATH_INCARNATE_G = registerBlockWithBasicItem("path_incarnate_g", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_G));
	public static final RegistryObject<Block> PATH_INCARNATE_H = registerBlockWithBasicItem("path_incarnate_h", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_H));
	public static final RegistryObject<Block> PATH_INCARNATE_I = registerBlockWithBasicItem("path_incarnate_i", () -> new BlockPathIncarnate(BlockProperties.PATH_INCARNATE_I));
	public static final RegistryObject<Block> STONE_INCARNATE_STAIRS = registerBlockWithBasicItem("stone_incarnate_stairs", () -> new StairsBlock(() -> STONE_INCARNATE.get().getDefaultState(), BlockProperties.STONE_INCARNATE_STAIRS));
	public static final RegistryObject<Block> STONE_INCARNATE_SLAB = registerBlockWithBasicItem("stone_incarnate_slab", () -> new SlabBlock(BlockProperties.STONE_INCARNATE_SLAB));
	public static final RegistryObject<Block> STONE_INCARNATE_WALL = registerBlockWithBasicItem("stone_incarnate_wall", () -> new WallBlock(BlockProperties.STONE_INCARNATE_WALL));
	public static final RegistryObject<Block> PATH_INCARNATE_STAIRS = registerBlockWithBasicItem("path_incarnate_stairs", () -> new StairsBlock(() -> PATH_INCARNATE_DEFAULT.get().getDefaultState(), BlockProperties.PATH_INCARNATE_STAIRS));
	public static final RegistryObject<Block> PATH_INCARNATE_SLAB = registerBlockWithBasicItem("path_incarnate_slab", () -> new SlabBlock(BlockProperties.PATH_INCARNATE_SLAB));
	public static final RegistryObject<Block> PATH_INCARNATE_WALL = registerBlockWithBasicItem("path_incarnate_wall", () -> new WallBlock(BlockProperties.PATH_INCARNATE_WALL));
	public static final RegistryObject<Block> GRASS_INCARNATE_BLOCK = registerBlockWithBasicItem("grass_incarnate_block", () -> new GrassBlock(BlockProperties.GRASS_INCARNATE_BLOCK));
	public static final RegistryObject<Block> DIRT_INCARNATE = registerBlockWithBasicItem("dirt_incarnate", BlockProperties.DIRT_INCARNATE);
	public static final RegistryObject<Block> GRASS_INCARNATE = registerBlockWithBasicItem("grass_incarnate", () -> new TallGrassIncarnateBlock(BlockProperties.GRASS_INCARNATE));
	public static final RegistryObject<Block> INCARNATE_LOG = registerBlockWithBasicItem("incarnate_log", () -> new BlockIncarnateLog(MaterialColor.WOOD, BlockProperties.INCARNATE_LOG));
	public static final RegistryObject<Block> INCARNATE_LOG_A = registerBlockWithBasicItem("incarnate_log_a", () -> new BlockIncarnateLog(MaterialColor.WOOD, BlockProperties.INCARNATE_LOG));
	public static final RegistryObject<Block> INCARNATE_LOG_B = registerBlockWithBasicItem("incarnate_log_b", () -> new BlockIncarnateLog(MaterialColor.WOOD, BlockProperties.INCARNATE_LOG));
	public static final RegistryObject<Block> INCARNATE_LEAVES = registerBlockWithBasicItem("incarnate_leaves", () -> new BlockIncarnateLeaves(BlockProperties.INCARNATE_LEAVES));
	public static final RegistryObject<Block> INCARNATE_B_LOG = registerBlockWithBasicItem("incarnate_b_log", () -> new BlockIncarnateLog(MaterialColor.WOOD, BlockProperties.INCARNATE_LOG));
	public static final RegistryObject<Block> INCARNATE_B_LEAVES = registerBlockWithBasicItem("incarnate_b_leaves", () -> new BlockIncarnateLeaves(BlockProperties.INCARNATE_LEAVES));
	public static final RegistryObject<Block> INCARNATE_LEAVES_CARPET_A = registerBlockWithBasicItem("incarnate_leaves_carpet_a", () -> new BlockLeavesCarpet(BlockProperties.INCARNATE_LEAVES_CARPET_A));
	
	public static void register(IEventBus modEventBus)
    {
        BLOCKS.register(modEventBus);
        ITEM_BLOCKS.register(modEventBus);
    }

	private static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<B> supplier)
    {
        return BLOCKS.register(name, supplier);
    }

    private static RegistryObject<Block> registerBlockWithBasicItem(String name, Block.Properties properties, ItemGroup group)
    {
        RegistryObject<Block> registryObject = registerBlock(name, () -> new Block(properties));
        ITEM_BLOCKS.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties().group(group)));
        return registryObject;
    }

    private static RegistryObject<Block> registerBlockWithBasicItem(String name, Block.Properties properties)
    {
        return registerBlockWithBasicItem(name, properties, Eraklys.ITEM_GROUP);
    }

    private static <B extends Block> RegistryObject<B> registerBlockWithBasicItem(String name, Supplier<B> supplier, ItemGroup group)
    {
        RegistryObject<B> registryObject = registerBlock(name, supplier);
        ITEM_BLOCKS.register(name, () -> new BlockItem(registryObject.get(), new Item.Properties().group(group)));
        return registryObject;
    }

    @SuppressWarnings("unused")
	private static <B extends Block> RegistryObject<B> registerBlockWithBasicItem(String name, Supplier<B> supplier)
    {
        return registerBlockWithBasicItem(name, supplier, Eraklys.ITEM_GROUP);
    }
}
