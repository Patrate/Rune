package fr.emmuliette.rune.mod.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class IllusionTileEntity extends TileEntity implements ITickableTileEntity {
	private int age;
	private int duration;
	private Integer blockId;
	private CompoundNBT tileEntity;

	public IllusionTileEntity() {
		super(ModTileEntity.ILLUSION_TE.get());
		this.blockId = null;
		this.tileEntity = null;
		this.age = 0;
		this.duration = 20;
	}

	public void setBlock(BlockState block, CompoundNBT tileEntity) {
		this.blockId = Block.getId(block);
		if (tileEntity != null) {
			this.tileEntity = tileEntity;
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
		if (this.blockId != null) {
			data.putInt(OLD_BLOCK_ID, this.blockId);
		}
		if (this.tileEntity != null) {
			data.put(OLD_TILE_ENTITY, tileEntity);
		}
		return super.save(data);
	}

	@Override
	public void load(BlockState state, CompoundNBT data) {
		this.age = data.getInt(AGE);
		this.duration = data.getInt(DURATION);
		if (data.contains(OLD_BLOCK_ID)) {
			this.blockId = data.getInt(OLD_BLOCK_ID);
		}
		if (data.contains(OLD_TILE_ENTITY)) {
			this.tileEntity = data.getCompound(OLD_TILE_ENTITY);
		}
		super.load(state, data);
	}

	@Override
	public void tick() {
		this.age++;
//		System.out.println("Phased: " + this.age + "/" + this.duration + ": id " + this.oldBlockId + "   "
//				+ ((this.oldTE == null) ? "null" : this.oldTE.toString()));
		if (this.age >= this.duration) {
			this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
		}
		setChanged();
	}

	public int getAge() {
		return age;
	}

	public Integer getBlockId() {
		return this.blockId;
	}
}
