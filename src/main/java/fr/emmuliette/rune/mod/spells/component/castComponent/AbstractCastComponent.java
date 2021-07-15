package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.player.capability.CasterCapability;
import fr.emmuliette.rune.mod.player.capability.ICaster;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCastComponent extends AbstractSpellComponent implements ComponentContainer<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;
	private int DEFAULT_COOLDOWN = 20;
	
	public AbstractCastComponent() throws RunePropertiesException {
		super();
		children = new ArrayList<AbstractEffectComponent>();
	}

	protected List<AbstractEffectComponent> getNextComponents() {
		return children;
	}
	
	public boolean specialCast(SpellContext context) {
		return false;
	}
	
	protected abstract boolean internalCast(SpellContext context);
	public boolean cast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY).orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			cap.delMana(this.getManaCost());
			cap.setCooldown(Math.max(DEFAULT_COOLDOWN, this.getCooldown()));
			internalCast(context);
		} catch(NotEnoughManaException | CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY).orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			
			if(cap.isCooldown()) {
				return false;
			}
			
			if(this.getManaCost() > cap.getMana()) {
				return false;
			}
			// target entity
			if(context.getTargetType() == SpellContext.TargetType.ENTITY && this instanceof TargetLivingEntity) {
				return true;
			}
			// target block
			if(context.getTargetType() == SpellContext.TargetType.BLOCK && this instanceof TargetBlock) {
				return true;
			}
			// target air
			if(context.getTargetType() == SpellContext.TargetType.AIR && this instanceof TargetAir) {
				return true;
			}
		} catch(CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean applyChildOnSelf(LivingEntity caster, SpellContext context) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyOnSelf(caster, context);
		}
		return result;
	}
	
	protected boolean applyChildOnEntity(LivingEntity target, SpellContext context) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyOnTarget(target, context);
		}
		return result;
	}
	
	protected boolean applyChildOnBlock(World world, BlockPos blockPos, SpellContext context) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyOnPosition(world, blockPos, context);
		}
		return result;
	}
	
	@Override
	public void addChildren(AbstractEffectComponent newEffect) {
		children.add(newEffect);
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return (children instanceof AbstractEffectComponent);
	}
	
	@Override
	public CompoundNBT toNBT() {
		CompoundNBT retour = super.toNBT();
		ListNBT childrenNBT = new ListNBT();
		for(AbstractSpellComponent child:children) {
			childrenNBT.add(child.toNBT());
		}
		retour.put(Spell.NBT_CHILDREN, childrenNBT);
		return retour;
	}
		
	public static AbstractCastComponent fromNBT(AbstractSpellComponent component, CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractCastComponent retour = (AbstractCastComponent) component;
		if(data.contains(Spell.NBT_CHILDREN)) {
			ListNBT childrenNBT = (ListNBT) data.get(Spell.NBT_CHILDREN);
			for(INBT childNBT:childrenNBT) {
				AbstractSpellComponent child = AbstractSpellComponent.fromNBT((CompoundNBT) childNBT);
				if(retour.canAddChildren(child)) {
					retour.addChildren((AbstractEffectComponent) child);
				}
			}
		}
		return retour;
	}
	
	@Override
	public float getManaCost() {
		float totalCost = 0f;
		for(AbstractSpellComponent sc: children) {
			totalCost += sc.getManaCost();
		}
		return totalCost;
	}
	
	protected float applyMultiplier(SpellContext context, float baseCost) {
		return baseCost * 1f; 
	}
	
	@Override
	public int getCooldown() {
		int totalCD = 0;
		for(AbstractSpellComponent sc: children) {
			totalCD += sc.getCooldown();
		}
		return totalCD;
	}
}
