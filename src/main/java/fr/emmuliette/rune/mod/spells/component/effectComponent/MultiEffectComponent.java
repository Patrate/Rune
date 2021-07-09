package fr.emmuliette.rune.mod.spells.component.effectComponent;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiEffectComponent extends AbstractEffectComponent {
	private List<AbstractEffectComponent> children;
	
	public MultiEffectComponent() {
		children = new ArrayList<AbstractEffectComponent>();
	}
	
	public MultiEffectComponent(List<AbstractEffectComponent> children) {
		this.children = children;
	}
	
	public MultiEffectComponent addEffect(AbstractEffectComponent newEffect) {
		children.add(newEffect);
		return this;
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
