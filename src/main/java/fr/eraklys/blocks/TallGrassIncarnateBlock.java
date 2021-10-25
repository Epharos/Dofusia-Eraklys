package fr.eraklys.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.GrassBlock;

public class TallGrassIncarnateBlock extends GrassBlock
{
	public TallGrassIncarnateBlock(Properties properties) 
	{
		super(properties);
	}

	public Block.OffsetType getOffsetType() 
	{
		return Block.OffsetType.XYZ;
	}
}
