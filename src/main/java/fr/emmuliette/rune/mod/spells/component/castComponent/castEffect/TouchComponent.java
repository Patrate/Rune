package fr.emmuliette.rune.mod.spells.component.castComponent.castEffect;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastEffectComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public class TouchComponent extends AbstractCastEffectComponent implements TargetBlock, TargetLivingEntity {

	public TouchComponent(AbstractSpellComponent parent) throws RunePropertiesException {
		super(PropertyFactory.EMPTY_FACTORY.get(), parent);
	}

	@Override
	public boolean internalCast(SpellContext context) {
		if(context.getTargetType() == SpellContext.TargetType.BLOCK) {
			return applyOnPosition(context.getWorld(), context.getItemUseContext().getClickedPos(), context);
		} else if(context.getTargetType() == SpellContext.TargetType.ENTITY) {
			return applyOnTarget(context.getTarget(), context);
		} else {
			return false;
		}
	}
}
