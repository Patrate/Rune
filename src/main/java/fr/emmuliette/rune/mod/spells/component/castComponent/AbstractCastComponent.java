package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public abstract class AbstractCastComponent {
	private AbstractEffectComponent nextComponent;
	
	public AbstractCastComponent(AbstractEffectComponent nextComponent) {
		this.nextComponent = nextComponent;
	}

	protected AbstractEffectComponent getNextComponent() {
		return nextComponent;
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
}
