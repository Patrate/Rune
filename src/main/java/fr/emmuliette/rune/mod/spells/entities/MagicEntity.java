package fr.emmuliette.rune.mod.spells.entities;

import java.util.UUID;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.AI.castAI.CastAI;
import fr.emmuliette.rune.mod.spells.AI.moveAI.MoveAI;
import fr.emmuliette.rune.mod.spells.AI.renderAI.RenderAI;
import fr.emmuliette.rune.mod.spells.AI.targetAI.ClosestEntityAI;
import fr.emmuliette.rune.mod.spells.AI.targetAI.TargetAI;
import fr.emmuliette.rune.mod.spells.component.structureComponent.MagicEntityComponent;
import fr.emmuliette.rune.mod.spells.component.structureComponent.AI.AbstractAIComponent;
import net.minecraft.block.material.PushReaction;
import net.minecraft.command.arguments.EntityAnchorArgument.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MagicEntity extends Entity {
	private UUID ownerUUID;

	private SpellContext context;
	private MagicEntityComponent<?> component;

	private BlockPos startingPosition;

	private MoveAI moveAI = MoveAI.DEFAULT.get();
	private TargetAI targetAI = new ClosestEntityAI(false);// TargetAI.DEFAULT;;
	private CastAI castAI = CastAI.DEFAULT.get();
	private RenderAI renderAI = RenderAI.DEFAULT.get();
	// private IA brain;
	// Used to define comportment, aka where yo go, when to cast spell, how long
	// survive
	// Where to go: don't move, toward/backward closest entity (ally/ennemy),
	// to/back from caster, move in circle around caster ?
	// When to cast spell: Contact ? Every interval of time ? Continuously ?
	// how long to survive: amount of spell detemined ? amount of time ?

	public MagicEntity(EntityType<? extends MagicEntity> areaEffectCloudEntity, World world) {
		super(areaEffectCloudEntity, world);
		this.noPhysics = true;
		this.setInvisible(false);
	}

	public MagicEntity(SpellContext context, MagicEntityComponent<?> component, World world, BlockPos position) {
		this(ModEntities.MAGIC_ENTITY.get(), world);
		this.setPos(position.getX() + 0.5, position.getY() + 1., position.getZ() + 0.5);
		this.context = context;
		this.startingPosition = position;
		this.lookAt(Type.FEET, context.getCaster().getPosition(0));
		context.setCurrentCaster(this);
		context.setBlock(null);
		context.setTarget(null);
		this.component = component;
		for (AbstractAIComponent aiComp : component.getAIChildren()) {
			if (aiComp.getAI(context) instanceof MoveAI) {
				moveAI = (MoveAI) aiComp.getAI(context);
				continue;
			}
			if (aiComp.getAI(context) instanceof TargetAI) {
				targetAI = (TargetAI) aiComp.getAI(context);
				continue;
			}
			if (aiComp.getAI(context) instanceof CastAI) {
				castAI = (CastAI) aiComp.getAI(context);
				continue;
			}
			if (aiComp.getAI(context) instanceof RenderAI) {
				renderAI = (RenderAI) aiComp.getAI(context);
				continue;
			}
		}
	}

	public void setTarget(LivingEntity target) {
		context.setTarget(target);
	}

	@Override
	protected void defineSynchedData() {
		/*
		 * this.getEntityData().define(DATA_COLOR, 0);
		 * this.getEntityData().define(DATA_RADIUS, 0.5F);
		 * this.getEntityData().define(DATA_WAITING, false);
		 * this.getEntityData().define(DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
		 */
	}

	@Override
	public void refreshDimensions() {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		super.refreshDimensions();
		this.setPos(d0, d1, d2);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level.isClientSide) {
			renderAI.render(this, this.random);
		} else {
			if (getContext() == null || !castAI.isAlive(getContext(), this)) {
				this.remove();
				return;
			}
			moveAI.move(this);
			LivingEntity target = targetAI.getEntityTarget(this);
			BlockPos block = targetAI.getBlockTarget(this);
			getContext().setTarget(target);
			if (target != null) {
				this.lookAt(Type.EYES, target.getPosition(0));
				this.lookAt(Type.FEET, target.getPosition(0));
				System.out.println("TARGET IS " + target.getName());
			}
			getContext().setBlock(block);
			if (block != null) {
				this.lookAt(Type.EYES, new Vector3d(block.getX(), block.getY(), block.getZ()));
				this.lookAt(Type.FEET, new Vector3d(block.getX(), block.getY(), block.getZ()));
			}
			// TODO updateContext here
			if (castAI.canCast(getContext(), this)) {
				component.castChildren(getContext());
				castAI.cast();
			}
		}
	}

//	@Nullable
//	public LivingEntity getOwner() {
//		if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerWorld) {
//			Entity entity = ((ServerWorld) this.level).getEntity(this.ownerUUID);
//			if (entity instanceof LivingEntity) {
//				this.owner = (LivingEntity) entity;
//			}
//		}
//
//		return this.owner;
//	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		this.tickCount = nbt.getInt("Age");
		/*
		 * this.duration = nbt.getInt("Duration"); this.waitTime =
		 * nbt.getInt("WaitTime"); this.reapplicationDelay =
		 * nbt.getInt("ReapplicationDelay"); this.durationOnUse =
		 * nbt.getInt("DurationOnUse"); this.radiusOnUse = nbt.getFloat("RadiusOnUse");
		 * this.radiusPerTick = nbt.getFloat("RadiusPerTick");
		 * this.setRadius(nbt.getFloat("Radius"));
		 */
		if (nbt.hasUUID("Owner")) {
			this.ownerUUID = nbt.getUUID("Owner");
		}

		/*
		 * if (nbt.contains("Particle", 8)) { try {
		 * this.setParticle(ParticleArgument.readParticle(new
		 * StringReader(nbt.getString("Particle")))); } catch (CommandSyntaxException
		 * commandsyntaxexception) { LOGGER.warn("Couldn't load custom particle {}",
		 * nbt.getString("Particle"), commandsyntaxexception); } }
		 * 
		 * if (nbt.contains("Color", 99)) { this.setFixedColor(nbt.getInt("Color")); }
		 */

		/*
		 * if (p_70037_1_.contains("Potion", 8)) {
		 * this.setPotion(PotionUtils.getPotion(p_70037_1_)); }
		 */

		/*
		 * if (nbt.contains("Effects", 9)) { ListNBT listnbt = nbt.getList("Effects",
		 * 10); this.effects.clear();
		 * 
		 * for (int i = 0; i < listnbt.size(); ++i) { EffectInstance effectinstance =
		 * EffectInstance.load(listnbt.getCompound(i)); if (effectinstance != null) {
		 * this.addEffect(effectinstance); } } }
		 */

	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putInt("Age", this.tickCount);
		/*
		 * nbt.putInt("Duration", this.duration); nbt.putInt("WaitTime", this.waitTime);
		 * nbt.putInt("ReapplicationDelay", this.reapplicationDelay);
		 * nbt.putInt("DurationOnUse", this.durationOnUse); nbt.putFloat("RadiusOnUse",
		 * this.radiusOnUse); nbt.putFloat("RadiusPerTick", this.radiusPerTick);
		 * nbt.putFloat("Radius", this.getRadius()); nbt.putString("Particle",
		 * this.getParticle().writeToString());
		 */
		if (this.ownerUUID != null) {
			nbt.putUUID("Owner", this.ownerUUID);
		}

//		if (this.fixedColor) {
//			nbt.putInt("Color", this.getColor());
//		}

//		if (!this.effects.isEmpty()) {
//			ListNBT listnbt = new ListNBT();
//
//			for (EffectInstance effectinstance : this.effects) {
//				listnbt.add(effectinstance.save(new CompoundNBT()));
//			}
//
//			nbt.put("Effects", listnbt);
//		}
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
		// return new SSpawnObjectPacket(this);
	}

	@Override
	public EntitySize getDimensions(Pose pose) {
		return EntitySize.scalable(5.0F * 2.0F, 0.5F);
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
		this.refreshDimensions();
		super.onSyncedDataUpdated(p_184206_1_);
	}

	/*public Entity getCaster() {
		return caster;
	}*/

	public RenderAI getRenderAI() {
		return renderAI;
	}

	public SpellContext getContext() {
		return context;
	}
	
	public BlockPos getStartingPosition() {
		return startingPosition;
	}
}