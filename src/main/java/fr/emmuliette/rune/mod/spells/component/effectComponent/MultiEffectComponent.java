package fr.emmuliette.rune.mod.spells.component.effectComponent;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.ComponentContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiEffectComponent extends AbstractEffectComponent implements ComponentContainer<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;
	
	public MultiEffectComponent(RuneProperties properties) {
		super(properties);
		children = new ArrayList<AbstractEffectComponent>();
	}
	
	public MultiEffectComponent(RuneProperties properties, List<AbstractEffectComponent> children) {
		super(properties);
		this.children = children;
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
	public boolean applyEffect(LivingEntity target) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyEffect(target);
		}
		return result;
	}
	@Override
	public boolean applyEffect(World world, BlockPos target) {
		boolean result = false;
		for(AbstractEffectComponent child:children) {
			result |= child.applyEffect(world, target);
		}
		return result;
	}
}
