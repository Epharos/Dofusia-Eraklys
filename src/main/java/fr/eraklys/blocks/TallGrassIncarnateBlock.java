package fr.eraklys.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class TallGrassIncarnateBlock extends TallGrassBlock
{
	public TallGrassIncarnateBlock(Properties properties) 
	{
		super(properties);
	}

	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
		return 1;
	}
}
