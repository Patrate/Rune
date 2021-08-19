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

public class AnchoredBlock extends Block {

	public static void anchorBlock(World world, BlockPos position, int duration) {
		TileEntity oldTE = world.getBlockEntity(position);
		if (oldTE instanceof AnchoredTileEntity) {
//			AnchoredTileEntity pte = (AnchoredTileEntity) oldTE;
//			pte.addDuration(duration);
		} else if (oldTE instanceof PhasedTileEntity) {
			// TODO faire apparaître des paillettes et déphaser !
			((PhasedTileEntity) oldTE).unPhase();
		} else {
			BlockState oldBlock = world.getBlockState(position);
			CompoundNBT oldTeNBT = null;
			if (oldTE != null)
				oldTeNBT = oldTE.serializeNBT();
			world.removeBlock(position, false);
			world.setBlockAndUpdate(position, ModBlocks.ANCHORED_BLOCK.getBlock().defaultBlockState());
			AnchoredTileEntity pte = (AnchoredTileEntity) world.getBlockEntity(position);
			if (pte != null) {
				pte.setOldBlock(oldBlock, oldTeNBT);
				pte.setDuration(duration);
			} else {
				System.out.println("ANCHOR PTE IS NULL");
			}
		}
	}

	public AnchoredBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AnchoredTileEntity();
	}
}
