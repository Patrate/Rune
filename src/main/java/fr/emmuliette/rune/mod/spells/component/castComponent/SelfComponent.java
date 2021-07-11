package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.RuneProperties;
import fr.emmuliette.rune.mod.spells.SpellContext;

public class SelfComponent extends AbstractCastComponent implements TargetAir {

	public SelfComponent(RuneProperties properties) throws RunePropertiesException {
		super(properties);
	}

	@Override
	public boolean cast(SpellContext context) {
		return applyChildOnSelf(context.getPlayer());
	}

}
