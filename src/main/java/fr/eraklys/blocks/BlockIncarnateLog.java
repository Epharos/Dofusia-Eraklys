package fr.eraklys.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockIncarnateLog extends LogBlock
{
	private final MaterialColor verticalColor;
	
	public BlockIncarnateLog(MaterialColor verticalColorIn, Properties properties) 
	{
		super(verticalColorIn, properties);
		this.verticalColor = verticalColorIn;
	}
	
	public MaterialColor getMaterialColor(BlockState state, IBlockReader worldIn, BlockPos pos) 
	{
	      return state.get(AXIS) == Direction.Axis.Y ? this.verticalColor : this.materialColor;
	   }
}
