package fr.emmuliette.rune.mod.blocks;

import fr.emmuliette.rune.mod.tileEntity.IllusionTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class IllusionBlock extends Block {

	public static void createIllusionBlock(World world, BlockPos position, int duration, BlockState block) {
		TileEntity oldTE = world.getBlockEntity(position);
		if (world.getBlockState(position).getBlockState() == Blocks.AIR.defaultBlockState()
				|| oldTE instanceof IllusionTileEntity) {
			world.setBlockAndUpdate(position, ModBlocks.ILLUSION_BLOCK.get().defaultBlockState());
			IllusionTileEntity pte = (IllusionTileEntity) world.getBlockEntity(position);
			if (pte != null) {
				pte.setBlock(block, null);
				pte.setDuration(duration);
			}
		}
	}

	public IllusionBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new IllusionTileEntity();
	}
}
