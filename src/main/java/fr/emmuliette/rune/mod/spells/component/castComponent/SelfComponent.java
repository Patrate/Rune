package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;

public class SelfComponent extends AbstractCastComponent implements TargetAir {

	public SelfComponent() throws RunePropertiesException {
		super();
	}

	@Override
	public boolean internalCast(SpellContext context) {
		return applyChildOnSelf(context.getPlayer(), context);
	}
	@Override
	public SpellProperties getDefaultProperties() {
		SpellProperties retour = new SpellProperties();
		return retour;
	}
}
