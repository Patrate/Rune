package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.capabilities.caster.CasterCapability;
import fr.emmuliette.rune.mod.capabilities.caster.ICaster;
import fr.emmuliette.rune.mod.capabilities.socket.ISocket;
import fr.emmuliette.rune.mod.capabilities.socket.SocketCapability;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;
import fr.emmuliette.rune.mod.spells.tags.BuildTag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCastComponent<T extends AbstractSpellComponent> extends AbstractSpellComponent
		implements ComponentContainer<T> {

	public static final int DEFAULT_COOLDOWN = 20;

	public AbstractCastComponent(PropertyFactory propFactory, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(propFactory, parent);
		this.addTag(BuildTag.CAST);
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

	public final boolean cast(SpellContext context, boolean payCost) {
		try {
			if (payCost)
				payCost(context);
			return internalCast(context);
		} catch (NotEnoughManaException | CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean cast(SpellContext context) {
		return cast(context, true);
	}

	protected void payCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		this.getCost().payCost(cap, context);
	}

	protected void setCooldown(ICaster cap, SpellContext context) {
		if (context.getSocketItem() != null) {
			ISocket socket = context.getSocketItem().getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socket == null) {
				// TODO throw exception
				return;
			}
			// TODO add socket cooldown
//			socket.setCooldown(Math.max(DEFAULT_COOLDOWN, this.getCooldown()));
		} else {
			// TODO add spell cooldown
			cap.setCooldown(Math.max(DEFAULT_COOLDOWN, this.getCooldown()));
		}
	}

	public Boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			Boolean checkCd = checkCooldown(cap, context);
			if (checkCd == null || !checkCd) {
				return checkCd;
			}

			Boolean checkManaCost = checkCost(cap, context);
			if (checkManaCost == null || !checkManaCost) {
				return checkManaCost;
			}

			Boolean checkCastType;
			if (this instanceof AbstractCastModComponent)
				checkCastType = checkChildrenCastType(context);
			else
				checkCastType = chechCastType(context);

			if (checkCastType == null || !checkCastType) {
				return checkCastType;
			}

			return true;
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected Boolean checkCooldown(ICaster cap, SpellContext context) {
		if (context.getSocketItem() != null) {
			ISocket socket = context.getSocketItem().getCapability(SocketCapability.SOCKET_CAPABILITY).orElse(null);
			if (socket == null)
				return false;
			return (!socket.isCooldown());
		}
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
		System.out.println("Can't add children");
		return false;
	}

	@SuppressWarnings("unchecked")
	protected void addChildrenInternal(AbstractSpellComponent newEffect) {
		getChildrens().add((T) newEffect);
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> cost = super.getCost();
		for (AbstractSpellComponent sc : getChildrens()) {
			cost.add(sc.getCost());
		}
		return cost;
	}

	@Override
	public float getMaxPower() {
		float maxPower = super.getMaxPower();
		for (AbstractSpellComponent sc : getChildrens()) {
			if (sc.getMaxPower() > maxPower)
				maxPower = sc.getMaxPower();
		}
		return maxPower;
	}

	@Override
	public Cost<?> getBoostCost() {
		Cost<?> boostCost = super.getBoostCost();
		if (boostCost == null)
			boostCost = Cost.ZERO_COST.get();
		for (AbstractSpellComponent sc : getChildrens()) {
			Cost<?> childCost = sc.getBoostCost();
			if (childCost != null)
				boostCost.add(childCost);
		}
		return boostCost;
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

	@Override
	public void clear() {
		clearChildrens();
	}
}
