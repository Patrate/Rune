package fr.emmuliette.rune.mod.spells.component.castComponent;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.cost.Cost;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public abstract class AbstractManaModComponent extends AbstractCastModComponent {

	public AbstractManaModComponent(PropertyFactory fact, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(fact, parent);
	}

	@Override
	public boolean cast(SpellContext context) {
		if (internalCast(context)) {
			return super.internalCast(context);
		}
		return false;
	}

	@Override
	protected Boolean checkCost(ICaster cap, SpellContext context) {
		// TODO this method will be changed sometime in the turfu
		return super.checkCost(cap, context);
	}

	@Override
	public Cost<?> getCost() {
		// TODO this method will be changed sometime in the turfu
		return super.getCost();
	}

	@Override
	protected void payCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		// TODO this method will be changed sometime in the turfu
		super.payCost(cap, context);
	}

	protected Cost<?> getBaseCost() {
		Cost<?> totalCost = Cost.getZeroCost();
		for (AbstractSpellComponent sc : getChildrens()) {
			totalCost.add(sc.getCost());
		}
		return totalCost;
	}

	@Override
	public Cost<?> applyCostMod(Cost<?> in) {
		return in;
	}

	@Override
	public int applyCDMod(int in) {
		return in;
	}

	protected float getCasterMana(SpellContext context) throws CasterCapabilityException {
		ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
				.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
		return cap.getMana();
	}

	protected void delCasterMana(SpellContext context, float amount)
			throws CasterCapabilityException, NotEnoughManaException {
		ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
				.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
		cap.delMana(amount);
	}
}
