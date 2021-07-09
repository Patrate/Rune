package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;

public class TouchComponent extends AbstractCastComponent implements TargetBlock, TargetLivingEntity {

	public TouchComponent(AbstractEffectComponent nextComponent) {
		super(nextComponent);
	}

	@Override
	public boolean cast(SpellContext context) {
		if(context.getTargetType() == SpellContext.TargetType.BLOCK) {
			return getNextComponent().applyEffect(context.getWorld(), context.getItemUseContext().getClickedPos());
		} else if(context.getTargetType() == SpellContext.TargetType.ENTITY) {
			return getNextComponent().applyEffect(context.getTarget());
		} else {
			return false;
		}
	}

}
