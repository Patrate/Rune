package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.Spell;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractCastComponent extends AbstractSpellComponent implements ComponentContainer<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;
	
	public AbstractCastComponent(RuneProperties properties) throws RunePropertiesException {
		super(properties);
		children = new ArrayList<AbstractEffectComponent>();
		//this.nextComponent = (AbstractEffectComponent) properties.getProperty(RuneProperties.Property.PREVIOUS);
	}

	protected List<AbstractEffectComponent> getNextComponents() {
		return children;
	}
	
	public abstract boolean cast(SpellContext context);
	public boolean canCast(SpellContext context) {
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
		return false;
	}
	
	protected boolean applyChildOnSelf(PlayerEntity player) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyEffect(player);
		}
		return result;
	}
	
	protected boolean applyChildOnEntity(LivingEntity target) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyEffect(target);
		}
		return result;
	}
	
	protected boolean applyChildOnBlock(World world, BlockPos blockPos) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyEffect(world, blockPos);
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
		for(int i = 0; i < children.size(); i++) {
			retour.put(Spell.NBT_CHILDREN + i, children.get(i).toNBT());
		}
		return retour;
	}
	

	
	public static AbstractCastComponent fromNBT(AbstractSpellComponent component, CompoundNBT data) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractCastComponent retour = (AbstractCastComponent) component;
		int i = 0;
		while(data.contains(Spell.NBT_CHILDREN + i)) {
			AbstractSpellComponent child = AbstractSpellComponent.fromNBT(data.getCompound(Spell.NBT_CHILDREN + i));
			if(retour.canAddChildren(child)) {
				retour.addChildren((AbstractEffectComponent) child);
			}
			i++;
		}
		return retour;
	}
}
