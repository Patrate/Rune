package fr.emmuliette.rune.mod.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PhasedTileEntity extends TileEntity implements ITickableTileEntity {
	private int age;
	private int duration;
	private Integer oldBlockId;
	private CompoundNBT oldTE;

	public PhasedTileEntity() {
		super(ModTileEntity.PHASED_TE.get());
		this.oldBlockId = null;
		this.oldTE = null;
		this.age = 0;
		this.duration = 20;
	}

	public void setOldBlock(BlockState oldBlock, CompoundNBT oldTE) {
		this.oldBlockId = Block.getId(oldBlock);
		if (oldTE != null) {
			this.oldTE = oldTE;
		}
		setChanged();
	}

	public void setDuration(int duration) {
		this.duration = duration;
		setChanged();
	}

	public void addDuration(int duration) {
		this.duration += duration;
		setChanged();
	}

	private static final String AGE = "age", DURATION = "duration", OLD_BLOCK_ID = "oldBlockId",
			OLD_TILE_ENTITY = "oldTE";

	@Override
	public CompoundNBT save(CompoundNBT data) {
		data.putInt(AGE, age);
		data.putInt(DURATION, duration);
		if (this.oldBlockId != null) {
			data.putInt(OLD_BLOCK_ID, this.oldBlockId);
		}
		if (this.oldTE != null) {
			data.put(OLD_TILE_ENTITY, oldTE);
		}
		return super.save(data);
	}

	@Override
	public void load(BlockState state, CompoundNBT data) {
		this.age = data.getInt(AGE);
		this.duration = data.getInt(DURATION);
		if (data.contains(OLD_BLOCK_ID)) {
			this.oldBlockId = data.getInt(OLD_BLOCK_ID);
		}
		if (data.contains(OLD_TILE_ENTITY)) {
			this.oldTE = data.getCompound(OLD_TILE_ENTITY);
		}
		super.load(state, data);
	}

	@Override
	public void tick() {
		this.age++;
//		System.out.println("Phased: " + this.age + "/" + this.duration + ": id " + this.oldBlockId + "   "
//				+ ((this.oldTE == null) ? "null" : this.oldTE.toString()));
		if (this.age >= this.duration) {
			unPhase();
		}
		setChanged();
	}

	public void unPhase() {
		if (this.oldBlockId == null) {
			this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
		} else {
			BlockState oldBlockState = Block.stateById(oldBlockId);
//			System.out.println("[PHASE] Turning back into a id " + this.oldBlockId + " of " + oldBlockState.toString());
			this.level.setBlockAndUpdate(this.getBlockPos(), oldBlockState);
			if (this.oldTE != null)
				this.level.getBlockEntity(this.getBlockPos()).deserializeNBT(oldTE);
		}
	}

	public int getAge() {
		return age;
	}

	public Integer getOldBlockId() {
		return this.oldBlockId;
	}
}
