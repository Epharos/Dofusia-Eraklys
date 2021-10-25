package fr.eraklys.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.TallGrassBlock;

public class TallGrassIncarnateBlock extends TallGrassBlock
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
