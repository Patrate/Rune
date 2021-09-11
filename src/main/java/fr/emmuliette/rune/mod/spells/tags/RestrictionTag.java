package fr.emmuliette.rune.mod.spells.tags;

import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.variable.VariableProperty;

public abstract class RestrictionTag extends OtherTag {
	public static enum Context {
		BUILD, CAST;
	}

	public static final RestrictionTag VARIABLE = new RestrictionTag() {
		@Override
		public boolean isValid(AbstractSpellComponent component, Context context) {
			if (context != Context.CAST)
				return true;
			for (Property<?> prop : component.getProperties().getProperties()) {
				if (prop instanceof VariableProperty && ((VariableProperty<?>) prop).isVariable()
						&& prop.getValue() == null) {
					return false;
				}
			}
			return true;
		}
	};
	// public static final RestrictionTag NO_AGGRO = new RestrictionTag();
	// public static final RestrictionTag NO_PARCHMENT = new RestrictionTag();
	// public static final RestrictionTag NO_IA = new RestrictionTag();

	public RestrictionTag() {
	}

	public abstract boolean isValid(AbstractSpellComponent component, Context context);
}
