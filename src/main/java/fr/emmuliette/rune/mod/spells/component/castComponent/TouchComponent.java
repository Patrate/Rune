package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;

public class TouchComponent extends AbstractCastComponent implements TargetBlock, TargetLivingEntity {

	public TouchComponent() throws RunePropertiesException {
		super();
	}

	@Override
	public boolean internalCast(SpellContext context) {
		if(context.getTargetType() == SpellContext.TargetType.BLOCK) {
			return applyChildOnBlock(context.getWorld(), context.getItemUseContext().getClickedPos(), context);
		} else if(context.getTargetType() == SpellContext.TargetType.ENTITY) {
			return applyChildOnEntity(context.getTarget(), context);
		} else {
			return false;
		}
	}
	@Override
	public SpellProperties getDefaultProperties() {
		SpellProperties retour = new SpellProperties();
		return retour;
	}

}
