package fr.emmuliette.rune.mod.circle;

import java.util.Random;

import fr.emmuliette.rune.mod.spells.entities.ModEntities;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class CircleEntity extends Entity {
	private Entity trackingEntity;
	private int duration;

	public CircleEntity(EntityType<? extends CircleEntity> circleEntity, World world) {
		super(circleEntity, world);
		this.noPhysics = true;
		this.setInvulnerable(true);
		this.duration = 200;
		this.trackingEntity = null;
	}

	public CircleEntity(Entity tracking, World world) {
		this(ModEntities.CIRCLE_ENTITY.get(), world);
		this.trackingEntity = tracking;
		this.setPos(tracking.getX(), tracking.getY() + 1.1, tracking.getZ());
	}

	public CircleEntity(BlockPos position, World world) {
		this(ModEntities.CIRCLE_ENTITY.get(), world);
		this.setPos(position.getX(), position.getY() + 1.1, position.getZ());
	}

	@Override
	public void tick() {
		this.setYBodyRot(((float) duration) * 0.17f);
		if (this.trackingEntity != null)
			this.teleportTo(this.trackingEntity.getX(), this.trackingEntity.getY(), this.trackingEntity.getZ());
		if (duration <= 0)
			this.remove();
		--duration;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	public PushReaction getPistonPushReaction() {
		return PushReaction.IGNORE;
	}

	@Override
	public IPacket<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
		super.onSyncedDataUpdated(p_184206_1_);
	}

	public Random getRandom() {
		return this.random;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {}

	@Override
	protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {}
}