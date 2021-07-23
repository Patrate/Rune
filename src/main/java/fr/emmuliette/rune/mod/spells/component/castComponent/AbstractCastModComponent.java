package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public abstract class AbstractCastModComponent extends AbstractCastModContainerComponent {

	public AbstractCastModComponent(PropertyFactory propFactory, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(propFactory, parent);
	}

	@Override
	public boolean cast(SpellContext context) {
		if (this instanceof CallbackMod) {
			Callback cb = ((CallbackMod) this).castCallback(context);

			if (cb != null) {
				try {
					payCost(context);
					cb.begin();
					CallbackManager.register(cb);
					return true;
				} catch (NotEnoughManaException | CasterCapabilityException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		return super.cast(context);
	}

	@Override
	public Cost<?> getCost() {
		Cost<?> retour = applyCostMod(super.getCost());
		return retour;
	}

	@Override
	public int getCooldown() {
		int retour = this.applyCDMod(super.getCooldown());
		return Math.max(0, retour);
	}

	public abstract Cost<?> applyCostMod(Cost<?> in);

	public abstract int applyCDMod(int in);
}
