package fr.eraklys.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraftforge.common.ToolType;

public class BlockProperties 
{
	public static final Block.Properties STONE_INCARNATE = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_DEFAULT = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_A = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_B = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_C = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_D = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_E = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_F = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_G = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_H = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_I = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties STONE_INCARNATE_STAIRS = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties STONE_INCARNATE_SLAB = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties STONE_INCARNATE_WALL = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_STAIRS = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_SLAB = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties PATH_INCARNATE_WALL = createProperties(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties GRASS_INCARNATE_BLOCK = createProperties(Material.ORGANIC).sound(SoundType.PLANT).harvestTool(ToolType.SHOVEL).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties DIRT_INCARNATE = createProperties(Material.EARTH).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).harvestLevel(0).hardnessAndResistance(1.5F, 10.0F);
	public static final Block.Properties GRASS_INCARNATE = createProperties(Material.TALL_PLANTS).sound(SoundType.PLANT).doesNotBlockMovement().hardnessAndResistance(0F, 0F);
	public static final Block.Properties INCARNATE_LOG = createProperties(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0F, 5F);
	public static final Block.Properties INCARNATE_LEAVES = createProperties(Material.LEAVES).sound(SoundType.PLANT).notSolid().hardnessAndResistance(0.2F);
	public static final Block.Properties INCARNATE_LEAVES_CARPET_A = createProperties(Material.LEAVES).sound(SoundType.PLANT).notSolid().hardnessAndResistance(0.2F);
	
	private static Block.Properties createProperties(Material material)
    {
        return createProperties(material, material.getColor());
    }

    private static Block.Properties createProperties(Material material, MaterialColor color)
    {
        return Block.Properties.create(material, color);
    }

    private static Block.Properties createProperties(Material material, DyeColor color)
    {
        return Block.Properties.create(material, color);
    }
}
