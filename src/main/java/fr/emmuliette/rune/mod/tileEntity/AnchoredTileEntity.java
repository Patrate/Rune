package fr.emmuliette.rune.mod.tileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.BlockFlags;

public class AnchoredTileEntity extends TileEntity implements ITickableTileEntity {
	private int age;
	private int duration;
	private Integer oldBlockId;
	private CompoundNBT oldTE;

	public AnchoredTileEntity() {
		super(ModTileEntity.ANCHORED_TE.get());
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
		this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(),
				BlockFlags.BLOCK_UPDATE);
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
		if (this.getOldBlockId() != null) {
			data.putInt(OLD_BLOCK_ID, this.getOldBlockId());
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
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(),
					BlockFlags.BLOCK_UPDATE);
		}
		super.load(state, data);
	}

	@Override
	public void tick() {
		if (!this.level.isClientSide) {
			this.age++;
//		System.out.println("Anchored: " + this.age + "/" + this.duration + ": id " + this.getOldBlockId() + "   "
//				+ ((this.oldTE == null) ? "null" : this.oldTE.toString()));
			if (this.age >= this.duration) {
				unAnchor();
			}
			setChanged();
		}
	}

	public void unAnchor() {
		if (this.getOldBlockId() == null) {
			this.level.setBlockAndUpdate(this.getBlockPos(), Blocks.AIR.defaultBlockState());
		} else {
			BlockState oldBlockState = Block.stateById(getOldBlockId());
//			System.out.println("[ANCHOR] Turning back into a id " + this.getOldBlockId() + " of " + oldBlockState.toString());
			this.level.setBlockAndUpdate(this.getBlockPos(), oldBlockState);
			if (this.oldTE != null)
				this.level.getBlockEntity(this.getBlockPos()).deserializeNBT(oldTE);
		}
	}

	public int getAge() {
		return age;
	}

	public Integer getOldBlockId() {
		return oldBlockId;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT data = super.getUpdateTag();
		if (this.getOldBlockId() != null) {
			data.putInt(OLD_BLOCK_ID, this.getOldBlockId());
		}
		return data;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT data) {
		if (data.contains(OLD_BLOCK_ID)) {
			this.oldBlockId = data.getInt(OLD_BLOCK_ID);
		}
		super.handleUpdateTag(state, data);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		if (this.getOldBlockId() != null) {
			nbtTag.putInt(OLD_BLOCK_ID, this.getOldBlockId());
		}
		return new SUpdateTileEntityPacket(getBlockPos(), -1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT tag = pkt.getTag();
		if (tag.contains(OLD_BLOCK_ID)) {
			this.oldBlockId = tag.getInt(OLD_BLOCK_ID);
		}
	}
}
