package fr.emmuliette.rune.mod.spells.entities;

import java.util.Random;
import java.util.UUID;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullConsumer;
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
	private float mana;
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
		this.mana = 0;
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
	public ActionResultType interact(PlayerEntity player, Hand hand) {
		// TODO chargeable Magic Entity
		if(true) { // TODO changer par this.isChargeable()
			player.getCapability(CasterCapability.CASTER_CAPABILITY).ifPresent(new NonNullConsumer<ICaster>() {
				@Override
				public void accept(ICaster cap) {
					System.out.println("GIVING ONE MANA");
					if(cap.getMana() >= 1f) {
						try {
							cap.delMana(1f);
							addMana(1f);
							System.out.println("MANA GIVETH");
							player.startUsingItem(hand);
						} catch (NotEnoughManaException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
		return super.interact(player, hand);
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
			renderAI.render(this);
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

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		this.tickCount = nbt.getInt("Age");
		this.mana = nbt.getFloat("Mana");
		if (nbt.hasUUID("Owner")) {
			this.ownerUUID = nbt.getUUID("Owner");
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		nbt.putInt("Age", this.tickCount);
		nbt.putFloat("Mana", this.mana);
		if (this.ownerUUID != null) {
			nbt.putUUID("Owner", this.ownerUUID);
		}
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
	public EntitySize getDimensions(Pose pose) {
		return EntitySize.scalable(5.0F * 2.0F, 0.5F);
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
		this.refreshDimensions();
		super.onSyncedDataUpdated(p_184206_1_);
	}

	public RenderAI getRenderAI() {
		return renderAI;
	}

	public SpellContext getContext() {
		return context;
	}
	
	public BlockPos getStartingPosition() {
		return startingPosition;
	}
	
	public void addMana(float mana) {
		this.mana += mana;
	}
	
	public Random getRandom() {
		return this.random;
	}
}