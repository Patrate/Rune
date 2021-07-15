package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.SpellContext;

public class TouchComponent extends AbstractCastComponent implements TargetBlock, TargetLivingEntity {

	public TouchComponent(RuneProperties properties) throws RunePropertiesException {
		super(properties);
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

}
