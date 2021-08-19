package fr.emmuliette.rune.mod.blocks;

import fr.emmuliette.rune.mod.tileEntity.AnchoredTileEntity;
import fr.emmuliette.rune.mod.tileEntity.PhasedTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class PhasedBlock extends Block {

	public static void phaseBlock(World world, BlockPos position, int duration) {
		TileEntity oldTE = world.getBlockEntity(position);
		if (oldTE instanceof PhasedTileEntity) {
//			PhasedTileEntity pte = (PhasedTileEntity) world.getBlockEntity(position);
//			pte.addDuration(duration);
		} else if (oldTE instanceof AnchoredTileEntity) {
			// TODO faire apparaître des paillettes et désancrer !
			((AnchoredTileEntity) oldTE).unAnchor();
		} else {
			BlockState oldBlock = world.getBlockState(position);
			CompoundNBT oldTeNBT = null;
			if (oldTE != null)
				oldTeNBT = oldTE.serializeNBT();
			world.removeBlock(position, false);
			world.setBlockAndUpdate(position, ModBlocks.PHASED_BLOCK.getBlock().defaultBlockState());
			PhasedTileEntity pte = (PhasedTileEntity) world.getBlockEntity(position);
			if (pte != null) {
				pte.setOldBlock(oldBlock, oldTeNBT);
				pte.setDuration(duration);
			}
		}
	}

	public PhasedBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PhasedTileEntity();
	}
}
