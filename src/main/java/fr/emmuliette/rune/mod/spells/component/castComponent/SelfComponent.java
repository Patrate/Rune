package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public class SelfComponent extends AbstractCastComponent implements TargetAir {

	public SelfComponent(AbstractEffectComponent nextComponent) {
		super(nextComponent);
	}

	@Override
	public boolean cast(SpellContext context) {
		return getNextComponent().applyEffect(context.getPlayer());
	}

}
