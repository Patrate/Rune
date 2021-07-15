package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.lang.reflect.InvocationTargetException;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.exception.RunePropertiesException;
import fr.emmuliette.rune.mod.player.capability.CasterCapability;
import fr.emmuliette.rune.mod.player.capability.ICaster;
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

// TODO: besoin de réfactorer
public abstract class AbstractCastModifierComponent extends AbstractSpellComponent implements ComponentContainer<AbstractCastComponent>{
	private AbstractCastComponent children;
	
	public AbstractCastModifierComponent() throws RunePropertiesException {
		super();
		children = null;
	}

	protected AbstractCastComponent getNextComponents() {
		return children;
	}
	
	public boolean specialCast(SpellContext context) {
		return children.specialCast(context);
	}
	
	protected abstract boolean internalCast(SpellContext context);
	public boolean cast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY).orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			cap.delMana(this.getManaCost());
			cap.setCooldown(Math.max(AbstractSpellComponent.DEFAULT_COOLDOWN, this.getCooldown()));
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
		return children.applyChildOnSelf(caster, context);
	}
	
	protected boolean applyChildOnEntity(LivingEntity target, SpellContext context) {
		return children.applyChildOnEntity(target, context);
	}
	
	protected boolean applyChildOnBlock(World world, BlockPos blockPos, SpellContext context) {
		return children.applyChildOnBlock(world, blockPos, context);
	}
	
	@Override
	public void _addChildren(AbstractCastComponent newCast) {
		children = newCast;
	}

	@Override
	public int getMaxSize() {
		return 1;
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return ((children instanceof AbstractCastComponent) && children == null);
	}
		
	public static AbstractCastModifierComponent fromNBT(AbstractSpellComponent component, CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractCastModifierComponent retour = (AbstractCastModifierComponent) component;
		if(data.contains(Spell.NBT_CHILDREN)) {
			ListNBT childrenNBT = (ListNBT) data.get(Spell.NBT_CHILDREN);
			for(INBT childNBT:childrenNBT) {
				AbstractSpellComponent child = AbstractSpellComponent.fromNBT((CompoundNBT) childNBT);
				if(retour.canAddChildren(child)) {
					retour.addChildren((AbstractCastComponent) child);
				}
			}
		}
		return retour;
	}

}
