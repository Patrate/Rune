package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.List;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.effectComponent.AbstractEffectComponent;
import fr.emmuliette.rune.mod.spells.properties.PropertyFactory;

public abstract class AbstractCastEffectComponent extends AbstractCastComponent<AbstractEffectComponent> {
	private List<AbstractEffectComponent> children;

	public AbstractCastEffectComponent(PropertyFactory propFactory, AbstractSpellComponent parent)
			throws RunePropertiesException {
		super(propFactory, parent);
		children = new ArrayList<AbstractEffectComponent>();
	}

	@Override
	public Boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			Boolean checkCd = checkCooldown(cap, context);
			if (checkCd == null || !checkCd)
				return checkCd;

			Boolean checkManaCost = checkManaCost(cap, context);
			if (checkManaCost == null || !checkManaCost)
				return checkManaCost;

			Boolean checkCastType = chechCastType(context);
			if (checkCastType == null || !checkCastType)
				return checkCastType;

			return true;
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public List<AbstractEffectComponent> getChildrens() {
		return children;
	}

	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return (children instanceof AbstractEffectComponent);
	}
}
