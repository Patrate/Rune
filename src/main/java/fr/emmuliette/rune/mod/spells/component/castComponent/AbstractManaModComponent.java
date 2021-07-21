package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public abstract class AbstractManaModComponent extends AbstractCastModComponent {

	public AbstractManaModComponent(PropertyFactory fact, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(fact, parent);
	}
	
	@Override
	public boolean cast(SpellContext context) {
		if(internalCast(context)) {
			return super.internalCast(context);
		}
		return false;
	}

	@Override
	protected Boolean checkManaCost(ICaster cap, SpellContext context) {
		// TODO this method will be changed sometime in the turfu
		return super.checkManaCost(cap, context);
	}

	@Override
	public float getManaCost() {
		// TODO this method will be changed sometime in the turfu
		return super.getManaCost();
	}

	@Override
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		// TODO this method will be changed sometime in the turfu
		super.payManaCost(cap, context);
	}
	
	protected float getBaseCost() {
		float totalCost = 0f;
		for (AbstractSpellComponent sc : getChildrens()) {
			totalCost += sc.getManaCost();
		}
		return totalCost;
	}

	@Override
	public float applyManaMod(float in) {
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}
}
