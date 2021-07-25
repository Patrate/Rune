package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCastComponent<T extends AbstractSpellComponent> extends AbstractSpellComponent
		implements ComponentContainer<T> {

	public static final int DEFAULT_COOLDOWN = 20;

	public AbstractCastComponent(PropertyFactory propFactory, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(propFactory, parent);
	}

	public boolean specialCast(SpellContext context) {
		return false;
	}

	protected abstract boolean internalCast(SpellContext context);

	protected final void payCost(SpellContext context) throws CasterCapabilityException, NotEnoughManaException {
		ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
				.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
		payCost(cap, context);
		setCooldown(cap, context);
	}	

	public boolean cast(SpellContext context) {
		try {
			payCost(context);
			return internalCast(context);
		} catch (NotEnoughManaException | CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void payCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		this.getCost().payCost(cap, context);
	}

	protected void setCooldown(ICaster cap, SpellContext context) {
		cap.setCooldown(Math.max(DEFAULT_COOLDOWN, this.getCooldown()));
	}

	public abstract Boolean canCast(SpellContext context);

	protected Boolean checkCooldown(ICaster cap, SpellContext context) {
		return (!cap.isCooldown());
	}

	protected Boolean checkCost(ICaster cap, SpellContext context) {
		return this.getCost().canPay(cap, context);
	}

	protected Boolean chechCastType(SpellContext context) {
		// target entity
		if (context.getTargetType() == SpellContext.TargetType.ENTITY && this instanceof TargetLivingEntity) {
			return true;
		}
		// target block
		if (context.getTargetType() == SpellContext.TargetType.BLOCK && this instanceof TargetBlock) {
			return true;
		}
		// target air
		if (context.getTargetType() == SpellContext.TargetType.AIR && this instanceof TargetAir) {
			return true;
		}
		return false;
	}

	protected Boolean checkChildrenCastType(SpellContext context) {
		// at least one valid target necessary
		for (T child : getChildrens()) {
			// target entity
			if (context.getTargetType() == SpellContext.TargetType.ENTITY && child instanceof TargetLivingEntity) {
				return true;
			}
			// target block
			if (context.getTargetType() == SpellContext.TargetType.BLOCK && child instanceof TargetBlock) {
				return true;
			}
			// target air
			if (context.getTargetType() == SpellContext.TargetType.AIR && child instanceof TargetAir) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean applyOnSelf(LivingEntity caster, SpellContext context) {
		boolean result = false;
		for (T child : getChildrens()) {
			result |= child.applyOnSelf(caster, context);
		}
		return result;
	}

	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		boolean result = false;
		for (T child : getChildrens()) {
			result |= child.applyOnTarget(target, context);
		}
		return result;
	}

	@Override
	public boolean applyOnPosition(World world, BlockPos blockPos, SpellContext context) {
		boolean result = false;
		for (T child : getChildrens()) {
			result |= child.applyOnPosition(world, blockPos, context);
		}
		return result;
	}

	public boolean addChildren(AbstractSpellComponent newEffect) {
		if (canAddChildren(newEffect)) {
			addChildrenInternal(newEffect);
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	protected void addChildrenInternal(AbstractSpellComponent newEffect) {
		getChildrens().add((T) newEffect);
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> cost = Cost.getZeroCost();
		for (AbstractSpellComponent sc : getChildrens()) {
			cost.add(sc.getCost());
		}
		return cost;
	}
	
	@Override
	public float getMaxPower() {
		float maxPower = 0f;
		for (AbstractSpellComponent sc : getChildrens()) {
			if(sc.getMaxPower() > maxPower)
				maxPower = sc.getMaxPower();
		}
		return maxPower;
	}


	@Override
	public int getCooldown() {
		int totalCD = 0;
		for (AbstractSpellComponent sc : getChildrens()) {
			totalCD += sc.getCooldown();
		}
		return totalCD;
	}

	/*
	 * @Override public CompoundNBT toNBT() { CompoundNBT retour = super.toNBT();
	 * ListNBT childrenNBT = new ListNBT(); for (T child : this.getChildrens()) {
	 * childrenNBT.add(child.toNBT()); } retour.put(Spell.NBT_CHILDREN,
	 * childrenNBT); return retour; }
	 */

	/*
	 * public static AbstractCastComponent<?> fromNBT(AbstractSpellComponent
	 * component, CompoundNBT data) throws ClassNotFoundException,
	 * InstantiationException, IllegalAccessException, IllegalArgumentException,
	 * InvocationTargetException, NoSuchMethodException, SecurityException {
	 * AbstractCastComponent<?> retour = (AbstractCastComponent<?>) component; if
	 * (data.contains(Spell.NBT_CHILDREN)) { ListNBT childrenNBT = (ListNBT)
	 * data.get(Spell.NBT_CHILDREN); for (INBT childNBT : childrenNBT) {
	 * AbstractSpellComponent child = AbstractSpellComponent.fromNBT((CompoundNBT)
	 * childNBT); retour.addChildren(child); } } return retour; }
	 */

	@Override
	public boolean addNextPart(AbstractSpellComponent other) {
		return addChildren(other);
	}
}
