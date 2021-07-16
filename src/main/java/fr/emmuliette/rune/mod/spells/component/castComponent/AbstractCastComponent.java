package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCastComponent<T extends AbstractSpellComponent> extends AbstractSpellComponent implements ComponentContainer<T> {
	public static final int DEFAULT_COOLDOWN = 20;
	
	public AbstractCastComponent() throws RunePropertiesException {
		super();
	}
	
	public boolean specialCast(SpellContext context) {
		return false;
	}
	
	protected abstract boolean internalCast(SpellContext context);
	public boolean cast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY).orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			payManaCost(cap, context);
			setCooldown(cap, context);
			return internalCast(context);
		} catch(NotEnoughManaException | CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		cap.delMana(this.getManaCost());
	}
	
	protected void setCooldown(ICaster cap, SpellContext context) {
		cap.setCooldown(Math.max(DEFAULT_COOLDOWN, this.getCooldown()));
	}
	
	public abstract boolean canCast(SpellContext context);
	
	@Override
	public boolean applyOnSelf(LivingEntity caster, SpellContext context) {
		boolean result = false;
		for(T child:getChildrens()) {
			result |= child.applyOnSelf(caster, context);
		}
		return result;
	}
	
	@Override
	public boolean applyOnTarget(LivingEntity target, SpellContext context) {
		boolean result = false;
		for(T child:getChildrens()) {
			result |= child.applyOnTarget(target, context);
		}
		return result;
	}
	
	@Override
	public boolean applyOnPosition(World world, BlockPos blockPos, SpellContext context) {
		boolean result = false;
		for(T child:getChildrens()) {
			result |= child.applyOnPosition(world, blockPos, context);
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public void addChildren(AbstractSpellComponent newEffect) {
		if(canAddChildren(newEffect)) {
			getChildrens().add((T) newEffect);
		}
	}

	@Override
	public float getManaCost() {
		float totalCost = 0f;
		for(AbstractSpellComponent sc: getChildrens()) {
			totalCost += sc.getManaCost();
		}
		return totalCost;
	}
		
	@Override
	public int getCooldown() {
		int totalCD = 0;
		for(AbstractSpellComponent sc: getChildrens()) {
			totalCD += sc.getCooldown();
		}
		return totalCD;
	}
	
	@Override
	public CompoundNBT toNBT() {
		CompoundNBT retour = super.toNBT();
		ListNBT childrenNBT = new ListNBT();
		for(T child:this.getChildrens()) {
			childrenNBT.add(child.toNBT());
		}
		retour.put(Spell.NBT_CHILDREN, childrenNBT);
		return retour;
	}
		
	public static AbstractCastComponent<?> fromNBT(AbstractSpellComponent component, CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractCastComponent<?> retour = (AbstractCastComponent<?>) component;
		if(data.contains(Spell.NBT_CHILDREN)) {
			ListNBT childrenNBT = (ListNBT) data.get(Spell.NBT_CHILDREN);
			for(INBT childNBT:childrenNBT) {
				AbstractSpellComponent child = AbstractSpellComponent.fromNBT((CompoundNBT) childNBT);
				retour.addChildren(child);
			}
		}
		return retour;
	}
}
