package fr.emmuliette.rune.mod.spells.entities.blockEffect;

import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockEffectEntity extends Entity {
	private int duration;
	protected BlockGrid blocks;
	protected BlockPos center;

	public BlockEffectEntity(EntityType<? extends BlockEffectEntity> blockEffectEntity, World world) {
		super(blockEffectEntity, world);
	}

	public BlockEffectEntity(EntityType<? extends BlockEffectEntity> blockEffectEntity, World world, BlockPos center,
			BlockGrid blocks, int duration) {
		this(blockEffectEntity, world);
		if(center != null)
			this.setPos(center.getX(), center.getY(), center.getZ());
		this.center = center;
		this.blocks = blocks;
		this.duration = duration;
		this.noPhysics = true;
	}

	protected void defineSynchedData() {
	}

	public int getDuration() {
		return this.duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide) {
			if (this.tickCount >= this.duration) {
				this.remove();
				return;
			}
		}
	}

	protected abstract void onRemove();

	@Override
	public void remove() {
		onRemove();
		super.remove();
	}

	protected void readAdditionalSaveData(CompoundNBT nbt) {
		this.tickCount = nbt.getInt("Age");
		this.duration = nbt.getInt("Duration");
		this.blocks = BlockGrid.fromNBT(nbt.get("grid"));
		this.center = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
	}

	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putInt("Age", this.tickCount);
		nbt.putInt("Duration", this.duration);
		nbt.put("grid", this.blocks.toNBT());
		nbt.putInt("x", center.getX());
		nbt.putInt("y", center.getY());
		nbt.putInt("z", center.getZ());
	}

	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	public IPacket<?> getAddEntityPacket() {
		return new SSpawnObjectPacket(this);
	}
}